package co.edu.poli.sw2.infrastructure.out.persistence;

import co.edu.poli.sw2.domain.model.Drone;
import co.edu.poli.sw2.domain.port.out.DroneRepositoryPort;
import co.edu.poli.sw2.domain.port.out.PersistenciaException;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * Adaptador de salida (driven adapter): conecta el puerto
 * {@link DroneRepositoryPort} con la persistencia real en MySQL.
 *
 * <p>Es la pieza que hace de traductor entre el hexagono y la tecnologia.
 * Delega el trabajo en {@link DroneDAO}, que es quien conoce el SQL, y
 * convierte las excepciones tecnicas ({@link SQLException},
 * {@link IOException}) en una {@link PersistenciaException} para que el
 * dominio no tenga que conocerlas.</p>
 *
 * <p>Si algun dia se cambia MySQL por otro almacenamiento, basta con escribir
 * otro adaptador que implemente el mismo puerto: ni el dominio ni la
 * aplicacion se enteran.</p>
 */
public class DroneJdbcAdapter implements DroneRepositoryPort {

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
