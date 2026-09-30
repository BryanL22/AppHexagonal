package co.edu.poli.sw2.infraestructura.persistencia;

import co.edu.poli.sw2.dominio.modelo.Drone;
import co.edu.poli.sw2.dominio.puerto.salida.ActualizarDronPort;
import co.edu.poli.sw2.dominio.puerto.salida.BuscarDronPort;
import co.edu.poli.sw2.dominio.puerto.salida.BuscarDronesPort;
import co.edu.poli.sw2.dominio.puerto.salida.EliminarDronPort;
import co.edu.poli.sw2.dominio.puerto.salida.GuardarDronPort;
import co.edu.poli.sw2.dominio.puerto.salida.PersistenciaException;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * Adaptador de salida: implementa los cinco puertos de salida contra MySQL.
 *
 * <p>Es la pieza que traduce entre el hexagono y la tecnologia. Delega el
 * trabajo en {@link DroneDAO}, que es quien conoce el SQL, y convierte las
 * excepciones tecnicas ({@link SQLException}, {@link IOException}) en una
 * {@link PersistenciaException} para que no crucen hacia el dominio.</p>
 *
 * <p>La conexion la entrega el Singleton {@link Conexion}. Si algun dia se
 * cambia MySQL por otro almacenamiento basta con escribir otro adaptador que
 * implemente los mismos puertos: ni el dominio ni la aplicacion se enteran.</p>
 */
public class DronRepository implements GuardarDronPort, BuscarDronPort, BuscarDronesPort,
        ActualizarDronPort, EliminarDronPort {

    private final DroneDAO droneDAO = new DroneDAO();

    @Override
    public boolean guardar(Drone drone) {
        try {
            return droneDAO.crear(drone);
        } catch (SQLException | IOException e) {
            throw new PersistenciaException("No se pudo guardar el drone.", e);
        }
    }

    @Override
    public Drone buscarPorId(String id) {
        try {
            return droneDAO.obtenerPorId(id);
        } catch (SQLException | IOException e) {
            throw new PersistenciaException("No se pudo consultar el drone.", e);
        }
    }

    @Override
    public List<Drone> buscarTodos() {
        try {
            return droneDAO.obtenerTodos();
        } catch (SQLException | IOException e) {
            throw new PersistenciaException("No se pudieron consultar los drones.", e);
        }
    }

    @Override
    public boolean actualizar(Drone drone) {
        try {
            return droneDAO.actualizar(drone);
        } catch (SQLException | IOException e) {
            throw new PersistenciaException("No se pudo actualizar el drone.", e);
        }
    }

    @Override
    public boolean eliminar(String id) {
        try {
            return droneDAO.eliminar(id);
        } catch (SQLException | IOException e) {
            throw new PersistenciaException("No se pudo eliminar el drone.", e);
        }
    }
}
