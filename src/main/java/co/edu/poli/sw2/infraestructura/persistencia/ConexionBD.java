package co.edu.poli.sw2.infraestructura.persistencia;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Servicio Singleton encargado de administrar la conexion JDBC hacia la
 * base de datos MySQL.
 *
 * <p>Al ser Singleton, existe una unica instancia de este servicio (y una
 * unica {@link Connection} JDBC) para toda la aplicacion: se obtiene con
 * {@link #obtenerInstancia()}, nunca con {@code new ConexionBD()} (el
 * constructor es privado). {@link #getConnection()} reutiliza la conexion
 * ya abierta mientras siga viva, y solo crea una nueva si nunca se abrio o
 * si la anterior se cerro.</p>
 *
 * <p>La URL, el usuario y la contrasena nunca se dejan escritos en el
 * codigo fuente: no hay ningun valor por defecto. Se resuelven en este
 * orden de prioridad:</p>
 * <ol>
 *     <li>Variables de entorno del sistema operativo ({@code DB_URL},
 *     {@code DB_USER}, {@code DB_PASSWORD}).</li>
 *     <li>Archivo {@code .env} en la raiz del proyecto (ver
 *     {@code .env.example}), pensado solo para desarrollo local.</li>
 * </ol>
 * <p>Si una variable no esta definida en ninguno de los dos origenes, la
 * clase falla al cargarse con un mensaje claro en vez de usar un valor
 * inventado. El archivo {@code .env} nunca debe subirse al repositorio:
 * esta excluido mediante {@code .gitignore}.</p>
 */
public class ConexionBD {

    /** Nombre del archivo de configuracion local, relativo al directorio de trabajo. */
    private static final String ENV_FILE = ".env";

    /** Unica instancia del Singleton; se crea la primera vez que se pide. */
    private static ConexionBD instancia;

    /** URL JDBC de la base de datos ({@code DB_URL}). */
    private final String url;
    /** Usuario de la base de datos ({@code DB_USER}). */
    private final String usuario;
    /** Contrasena de la base de datos ({@code DB_PASSWORD}). */
    private final String password;

    /** Conexion JDBC compartida; se abre en el primer uso y se reabre si se cerro. */
    private Connection connection;

    /**
     * Carga la configuracion (variables de entorno y/o archivo {@code .env})
     * necesaria para abrir la conexion JDBC.
     *
     * @throws IOException si el archivo {@code .env} existe pero no se pudo leer.
     */
    private ConexionBD() throws IOException {
        Map<String, String> variablesEnv = cargarArchivoEnv();
        this.url = obtenerVariable(variablesEnv, "DB_URL");
        this.usuario = obtenerVariable(variablesEnv, "DB_USER");
        this.password = obtenerVariable(variablesEnv, "DB_PASSWORD");
    }

    /**
     * Punto de acceso unico al servicio de conexion (patron Singleton).
     * Crea la unica instancia la primera vez que se invoca; en llamadas
     * posteriores devuelve siempre esa misma instancia.
     *
     * @return la unica instancia de {@link ConexionBD} de la aplicacion.
     * @throws IOException si el archivo {@code .env} existe pero no se pudo leer.
     */
    public static synchronized ConexionBD obtenerInstancia() throws IOException {
        if (instancia == null) {
            instancia = new ConexionBD();
        }
        return instancia;
    }

    /**
     * Devuelve la conexion JDBC activa, reutilizandola si ya estaba abierta.
     *
     * @return una {@link Connection} abierta hacia la base de datos.
     * @throws SQLException si no fue posible abrir la conexion.
     */
    public Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(url, usuario, password);
        }
        return connection;
    }

    /**
     * Cierra la conexion compartida, si esta abierta. Debe llamarse al
     * finalizar la aplicacion (no despues de cada operacion), ya que la
     * misma conexion se reutiliza durante toda la ejecucion.
     *
     * @throws SQLException si ocurre un error al cerrar la conexion.
     */
    public void cerrar() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }

    /**
     * Resuelve el valor de una variable de configuracion obligatoria, dando
     * prioridad a las variables de entorno del sistema operativo y usando
     * el archivo {@code .env} como respaldo.
     *
     * @param variablesEnv variables leidas del archivo {@code .env}.
     * @param clave nombre de la variable (p. ej. {@code DB_USER}).
     * @return el valor resuelto para la variable.
     * @throws IllegalStateException si la variable no esta definida en ningun origen.
     */
    private static String obtenerVariable(Map<String, String> variablesEnv, String clave) {
        String valor = System.getenv(clave);
        if (valor == null) {
            valor = variablesEnv.get(clave);
        }

        if (valor == null) {
            throw new IllegalStateException("Falta la variable '" + clave + "'. Definela como variable de "
                    + "entorno o agregala al archivo .env en la raiz del proyecto (ver .env.example).");
        }

        return valor;
    }

    /**
     * Carga las variables definidas en el archivo {@code .env} de la raiz
     * del proyecto, si existe. El formato esperado es {@code CLAVE=valor},
     * una entrada por linea, admitiendo lineas en blanco y comentarios que
     * inicien con {@code #}.
     *
     * @return mapa con las variables encontradas; vacio si el archivo no existe.
     * @throws IOException si el archivo existe pero no se pudo leer.
     */
    private static Map<String, String> cargarArchivoEnv() throws IOException {
        Path ruta = Path.of(ENV_FILE);

        if (!Files.exists(ruta)) {
            return new HashMap<>();
        }

        return parsearEnv(Files.readAllLines(ruta));
    }

    /**
     * Interpreta el contenido de un archivo {@code .env} ya leido en
     * memoria. Metodo de paquete (sin modificador de acceso) para poder
     * probarlo directamente desde las pruebas unitarias, sin depender del
     * sistema de archivos.
     *
     * @param lineas lineas del archivo {@code .env}.
     * @return mapa con las variables encontradas.
     */
    static Map<String, String> parsearEnv(List<String> lineas) {
        Map<String, String> variables = new HashMap<>();

        for (String linea : lineas) {
            String texto = linea.trim();
            if (texto.isEmpty() || texto.startsWith("#") || !texto.contains("=")) {
                continue;
            }
            int separador = texto.indexOf('=');
            String clave = texto.substring(0, separador).trim();
            String valor = texto.substring(separador + 1).trim();
            variables.put(clave, valor);
        }

        return variables;
    }
}
