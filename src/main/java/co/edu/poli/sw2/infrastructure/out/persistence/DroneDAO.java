package co.edu.poli.sw2.infrastructure.out.persistence;

import co.edu.poli.sw2.domain.model.Agricultura;
import co.edu.poli.sw2.domain.model.Drone;
import co.edu.poli.sw2.domain.model.Vigilancia;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementacion del contrato {@code DroneRepositoryPort} para la entidad {@link Drone} y
 * sus especializaciones {@link Agricultura} y {@link Vigilancia}.
 *
 * <p>Sigue el patron de herencia por tabla: los campos comunes viven en
 * {@code drone} y los propios de cada especializacion en {@code agricultura}
 * / {@code vigilancia}, relacionadas mediante {@code id_drone}
 * (con {@code ON DELETE CASCADE}). En lugar de tener un DAO por cada
 * subclase, esta unica clase decide con {@code instanceof} que tabla
 * adicional leer, escribir o actualizar segun el tipo real del objeto.</p>
 *
 * <p>Esta clase tambien garantiza la existencia de las tablas relacionadas
 * del diagrama de clases que aun no tienen su propio DAO:</p>
 * <ul>
 *     <li>{@code piloto}: independiente; {@code drone} la referencia de
 *     forma opcional mediante {@code id_piloto} (relacion 1 a 1,
 *     {@code ON DELETE SET NULL} para no borrar el dron si se borra su
 *     piloto).</li>
 *     <li>{@code sensor}: cada fila pertenece a un unico dron mediante
 *     {@code id_drone} (relacion de 1 dron a muchos sensores,
 *     {@code ON DELETE CASCADE}).</li>
 *     <li>{@code mision}: independiente. Se relaciona con {@code drone} en
 *     una relacion muchos-a-muchos (una mision usa varios drones y un mismo
 *     dron puede participar en varias misiones), asi que no existe una unica
 *     columna de llave foranea posible; en su lugar se usa la tabla
 *     intermedia {@code mision_drone}, con una llave foranea hacia cada lado
 *     ({@code id_mision}, {@code id_drone}) y llave primaria compuesta por
 *     ambas ({@code ON DELETE CASCADE} en las dos, para no dejar asignaciones
 *     huerfanas si se borra la mision o el dron).</li>
 * </ul>
 * <p>Por ahora solo se crean sus tablas (a nivel de base de datos); todavia
 * no hay operaciones CRUD en Java para {@link co.edu.poli.sw2.domain.model.Piloto},
 * {@link co.edu.poli.sw2.domain.model.Sensor} ni {@link co.edu.poli.sw2.domain.model.Mision}.</p>
 *
 * <p>El identificador de cada dron lo asigna el usuario manualmente (no se
 * genera de forma automatica) y es la llave primaria de {@code drone}.</p>
 *
 * <p>La conexion JDBC se obtiene del servicio Singleton {@link Conexion} y
 * se comparte entre todas las operaciones (no se cierra al terminar cada
 * una): cada metodo solo cierra sus propios {@link Statement}/
 * {@link ResultSet}.</p>
 */
public class DroneDAO {

    private static final String SQL_CREAR_TABLA_PILOTO = "CREATE TABLE IF NOT EXISTS piloto (" +
            "id VARCHAR(100) PRIMARY KEY, " +
            "nombre VARCHAR(100) NOT NULL, " +
            "licencia VARCHAR(100) NOT NULL, " +
            "telefono VARCHAR(100) NOT NULL)";

    private static final String SQL_CREAR_TABLA_DRONE = "CREATE TABLE IF NOT EXISTS drone (" +
            "id VARCHAR(100) PRIMARY KEY, " +
            "`serial` VARCHAR(100) NOT NULL, " +
            "modelo VARCHAR(100) NOT NULL, " +
            "fabricante VARCHAR(100) NOT NULL, " +
            "peso DOUBLE NOT NULL, " +
            "id_piloto VARCHAR(100), " +
            "FOREIGN KEY (id_piloto) REFERENCES piloto(id) ON DELETE SET NULL)";

    private static final String SQL_CREAR_TABLA_AGRICULTURA = "CREATE TABLE IF NOT EXISTS agricultura (" +
            "id_drone VARCHAR(100) PRIMARY KEY, " +
            "capacidad_tanque DOUBLE NOT NULL, " +
            "FOREIGN KEY (id_drone) REFERENCES drone(id) ON DELETE CASCADE)";

    private static final String SQL_CREAR_TABLA_VIGILANCIA = "CREATE TABLE IF NOT EXISTS vigilancia (" +
            "id_drone VARCHAR(100) PRIMARY KEY, " +
            "deteccion_termica BOOLEAN NOT NULL, " +
            "FOREIGN KEY (id_drone) REFERENCES drone(id) ON DELETE CASCADE)";

    private static final String SQL_CREAR_TABLA_SENSOR = "CREATE TABLE IF NOT EXISTS sensor (" +
            "id VARCHAR(100) PRIMARY KEY, " +
            "tipo VARCHAR(100) NOT NULL, " +
            "fabricante VARCHAR(100) NOT NULL, " +
            "id_drone VARCHAR(100) NOT NULL, " +
            "FOREIGN KEY (id_drone) REFERENCES drone(id) ON DELETE CASCADE)";

    private static final String SQL_CREAR_TABLA_MISION = "CREATE TABLE IF NOT EXISTS mision (" +
            "id VARCHAR(100) PRIMARY KEY, " +
            "nombre VARCHAR(100) NOT NULL, " +
            "ubicacion VARCHAR(100) NOT NULL, " +
            "fecha VARCHAR(100) NOT NULL)";

    // Tabla intermedia (resuelve la relacion muchos-a-muchos entre mision y
    // drone: una mision usa varios drones y un mismo drone puede participar
    // en varias misiones). La llave primaria compuesta evita ademas que un
    // mismo drone quede asignado dos veces a la misma mision.
    private static final String SQL_CREAR_TABLA_MISION_DRONE = "CREATE TABLE IF NOT EXISTS mision_drone (" +
            "id_mision VARCHAR(100) NOT NULL, " +
            "id_drone VARCHAR(100) NOT NULL, " +
            "PRIMARY KEY (id_mision, id_drone), " +
            "FOREIGN KEY (id_mision) REFERENCES mision(id) ON DELETE CASCADE, " +
            "FOREIGN KEY (id_drone) REFERENCES drone(id) ON DELETE CASCADE)";

    private static final String SQL_VERIFICAR_COLUMNA_ID_PILOTO =
            "SELECT COUNT(*) FROM information_schema.columns " +
                    "WHERE table_schema = DATABASE() AND table_name = 'drone' AND column_name = 'id_piloto'";

    private static final String SQL_AGREGAR_COLUMNA_ID_PILOTO =
            "ALTER TABLE drone ADD COLUMN id_piloto VARCHAR(100), " +
                    "ADD FOREIGN KEY (id_piloto) REFERENCES piloto(id) ON DELETE SET NULL";

    private static final String SQL_SELECT_BASE =
            "SELECT d.id, d.`serial`, d.modelo, d.fabricante, d.peso, " +
                    "a.capacidad_tanque, v.deteccion_termica " +
                    "FROM drone d " +
                    "LEFT JOIN agricultura a ON d.id = a.id_drone " +
                    "LEFT JOIN vigilancia v ON d.id = v.id_drone";

    /**
     * Crea el DAO. No recibe dependencias: la conexion se obtiene del
     * servicio Singleton {@link Conexion} en cada operacion.
     */
    public DroneDAO() {
    }

    /**
     * Obtiene la conexion compartida (Singleton) y garantiza que todas las
     * tablas existan, en un orden que respeta sus llaves foraneas: primero
     * {@code piloto} (de la que depende {@code drone}), luego {@code drone},
     * despues {@code agricultura}/{@code vigilancia}/{@code sensor} (que
     * dependen de {@code drone}) y {@code mision}, y por ultimo la tabla
     * intermedia {@code mision_drone} (que depende de {@code drone} y de
     * {@code mision}). La conexion no se cierra aqui: la administra
     * {@link Conexion} durante toda la vida de la aplicacion.
     *
     * @return conexion JDBC lista para usar.
     * @throws SQLException si falla la conexion o la creacion de alguna tabla.
     * @throws IOException si no se pudo leer la configuracion de conexion.
     */
    private Connection obtenerConexion() throws SQLException, IOException {
        Connection connection = Conexion.obtenerInstancia().getConnection();
        crearTabla(connection, SQL_CREAR_TABLA_PILOTO);
        crearTabla(connection, SQL_CREAR_TABLA_DRONE);
        agregarColumnaIdPilotoSiFalta(connection);
        crearTabla(connection, SQL_CREAR_TABLA_AGRICULTURA);
        crearTabla(connection, SQL_CREAR_TABLA_VIGILANCIA);
        crearTabla(connection, SQL_CREAR_TABLA_SENSOR);
        crearTabla(connection, SQL_CREAR_TABLA_MISION);
        crearTabla(connection, SQL_CREAR_TABLA_MISION_DRONE);
        eliminarColumnaTipoControlSiExiste(connection);
        return connection;
    }

    /**
     * Agrega la columna {@code id_piloto} (con su llave foranea hacia
     * {@code piloto}) a una tabla {@code drone} que ya existia antes de que
     * esa relacion se introdujera. {@code CREATE TABLE IF NOT EXISTS} no
     * modifica una tabla que ya existe, asi que sin esta migracion quienes
     * ya tuvieran la base de datos creada se quedarian con el esquema viejo.
     *
     * @param connection conexion JDBC activa sobre la cual verificar y, si
     *                   hace falta, alterar la tabla.
     * @throws SQLException si ocurre un error al consultar o alterar el esquema.
     */
    private void agregarColumnaIdPilotoSiFalta(Connection connection) throws SQLException {
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(SQL_VERIFICAR_COLUMNA_ID_PILOTO)) {
            resultSet.next();
            if (resultSet.getInt(1) == 0) {
                try (Statement alterStatement = connection.createStatement()) {
                    alterStatement.executeUpdate(SQL_AGREGAR_COLUMNA_ID_PILOTO);
                }
            }
        }
    }

    /**
     * Elimina la columna {@code tipo_control} de la tabla {@code drone} si
     * quedo de una version anterior del esquema. El tipo de control ya no es
     * un atributo de {@link Drone} (esa responsabilidad es del patron
     * Bridge, ver {@link co.edu.poli.sw2.services.ControlVuelo}), por lo que
     * no debe persistirse en base de datos.
     */
    private void eliminarColumnaTipoControlSiExiste(Connection connection) {
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate("ALTER TABLE drone DROP COLUMN tipo_control");
        } catch (SQLException ignored) {
            // La columna ya no existe (o nunca existio) en la base de datos.
        }
    }

    /**
     * Ejecuta una sentencia de creacion de tabla.
     *
     * @param connection conexion activa.
     * @param sentenciaCreacion sentencia {@code CREATE TABLE IF NOT EXISTS}.
     * @throws SQLException si la sentencia falla.
     */
    private void crearTabla(Connection connection, String sentenciaCreacion) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(sentenciaCreacion);
        }
    }

    public boolean crear(Drone obj) throws SQLException, IOException {
        String sql = "INSERT INTO drone (id, `serial`, modelo, fabricante, peso) VALUES (?, ?, ?, ?, ?)";

        Connection connection = obtenerConexion();
        connection.setAutoCommit(false);

        boolean exito = false;
        try {
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, obj.getId());
                statement.setString(2, obj.getSerial());
                statement.setString(3, obj.getModelo());
                statement.setString(4, obj.getFabricante());
                statement.setDouble(5, obj.getPeso());
                statement.executeUpdate();

                insertarEspecializacion(connection, obj);
            }

            connection.commit();
            exito = true;
            return true;
        } finally {
            if (!exito) {
                connection.rollback();
            }
            connection.setAutoCommit(true);
        }
    }

    private void insertarEspecializacion(Connection connection, Drone obj) throws SQLException {
        if (obj instanceof Agricultura agricultura) {
            try (PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO agricultura (id_drone, capacidad_tanque) VALUES (?, ?)")) {
                statement.setString(1, obj.getId());
                statement.setDouble(2, agricultura.getCapacidadTanque());
                statement.executeUpdate();
            }
        } else if (obj instanceof Vigilancia vigilancia) {
            try (PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO vigilancia (id_drone, deteccion_termica) VALUES (?, ?)")) {
                statement.setString(1, obj.getId());
                statement.setBoolean(2, vigilancia.isDeteccionTermica());
                statement.executeUpdate();
            }
        }
    }

    public List<Drone> obtenerTodos() throws SQLException, IOException {
        List<Drone> drones = new ArrayList<>();

        Connection connection = obtenerConexion();

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(SQL_SELECT_BASE)) {

            while (resultSet.next()) {
                drones.add(mapearDrone(resultSet));
            }
        }

        return drones;
    }

    public Drone obtenerPorId(String id) throws SQLException, IOException {
        String sql = SQL_SELECT_BASE + " WHERE d.id = ?";

        Connection connection = obtenerConexion();

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapearDrone(resultSet);
                }
            }
        }

        return null;
    }

    public boolean actualizar(Drone obj) throws SQLException, IOException {
        String sql = "UPDATE drone SET `serial` = ?, modelo = ?, fabricante = ?, peso = ? WHERE id = ?";

        Connection connection = obtenerConexion();
        connection.setAutoCommit(false);

        boolean exito = false;
        try {
            int filasDrone;
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, obj.getSerial());
                statement.setString(2, obj.getModelo());
                statement.setString(3, obj.getFabricante());
                statement.setDouble(4, obj.getPeso());
                statement.setString(5, obj.getId());
                filasDrone = statement.executeUpdate();
            }

            int filasEspecializacion = actualizarEspecializacion(connection, obj);

            connection.commit();
            exito = true;
            return filasDrone > 0 && filasEspecializacion > 0;
        } finally {
            if (!exito) {
                connection.rollback();
            }
            connection.setAutoCommit(true);
        }
    }

    private int actualizarEspecializacion(Connection connection, Drone obj) throws SQLException {
        if (obj instanceof Agricultura agricultura) {
            try (PreparedStatement statement = connection.prepareStatement(
                    "UPDATE agricultura SET capacidad_tanque = ? WHERE id_drone = ?")) {
                statement.setDouble(1, agricultura.getCapacidadTanque());
                statement.setString(2, obj.getId());
                return statement.executeUpdate();
            }
        }
        if (obj instanceof Vigilancia vigilancia) {
            try (PreparedStatement statement = connection.prepareStatement(
                    "UPDATE vigilancia SET deteccion_termica = ? WHERE id_drone = ?")) {
                statement.setBoolean(1, vigilancia.isDeteccionTermica());
                statement.setString(2, obj.getId());
                return statement.executeUpdate();
            }
        }
        // El dron no tiene especializacion: no hay una segunda tabla que actualizar.
        return 1;
    }

    public boolean eliminar(String id) throws SQLException, IOException {
        // Las filas de "agricultura"/"vigilancia" se eliminan en cascada (ON DELETE CASCADE).
        String sql = "DELETE FROM drone WHERE id = ?";

        Connection connection = obtenerConexion();

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, id);
            return statement.executeUpdate() > 0;
        }
    }

    private Drone mapearDrone(ResultSet resultSet) throws SQLException {
        String id = resultSet.getString("id");
        String serial = resultSet.getString("serial");
        String modelo = resultSet.getString("modelo");
        String fabricante = resultSet.getString("fabricante");
        double peso = resultSet.getDouble("peso");

        Drone drone;
        double capacidadTanque = resultSet.getDouble("capacidad_tanque");
        if (!resultSet.wasNull()) {
            drone = new Agricultura(id, serial, modelo, fabricante, peso, capacidadTanque);
        } else {
            boolean deteccionTermica = resultSet.getBoolean("deteccion_termica");
            if (!resultSet.wasNull()) {
                drone = new Vigilancia(id, serial, modelo, fabricante, peso, deteccionTermica);
            } else {
                drone = new Drone(id, serial, modelo, fabricante, peso);
            }
        }

        return drone;
    }
}
