package co.edu.poli.sw2.aplicacion.servicio;

import co.edu.poli.sw2.aplicacion.puerto.entrada.ConsultarDronesUseCase;
import co.edu.poli.sw2.aplicacion.puerto.salida.BuscarDronesPort;
import co.edu.poli.sw2.dominio.modelo.Drone;

import java.util.List;

/**
 * Servicio de aplicacion: implementa el caso de uso
 * {@link ConsultarDronesUseCase}. Delega directamente en el puerto de
 * salida, ya que no hay logica adicional.
 */
public class ConsultarDronesServicio implements ConsultarDronesUseCase {

    /** Puerto de salida para listar todos los drones. */
    private final BuscarDronesPort buscarDrones;

    /**
     * Crea el servicio con el puerto de salida que necesita.
     *
     * @param buscarDrones puerto para listar.
     */
    public ConsultarDronesServicio(BuscarDronesPort buscarDrones) {
        this.buscarDrones = buscarDrones;
    }

    /**
     * Consulta todos los drones registrados.
     *
     * @return lista de drones; vacia si no hay ninguno.
     * @throws co.edu.poli.sw2.aplicacion.puerto.salida.PersistenciaException
     *         si falla el almacenamiento.
     */
    @Override
    public List<Drone> consultarTodos() {
        return buscarDrones.buscarTodos();
    }
}
