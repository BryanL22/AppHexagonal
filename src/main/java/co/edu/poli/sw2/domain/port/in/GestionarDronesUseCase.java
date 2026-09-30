package co.edu.poli.sw2.domain.port.in;

import co.edu.poli.sw2.domain.model.Drone;

import java.util.List;

/**
 * Puerto de entrada (driving port) del hexagono.
 *
 * <p>Declara lo que la aplicacion <em>ofrece</em> al mundo exterior: las
 * operaciones de gestion de drones. Lo implementa un servicio de aplicacion y
 * lo consume un adaptador de entrada, hoy la interfaz JavaFX.</p>
 *
 * <p>Gracias a este puerto la vista no conoce la base de datos: solo pide
 * operaciones de negocio.</p>
 */
public interface GestionarDronesUseCase {

    /**
     * Registra un dron nuevo. Falla si ya existe otro con el mismo
     * identificador.
     *
     * @param drone dron a registrar.
     * @return {@code true} si quedo registrado.
     */
    boolean registrarDrone(Drone drone);

    /**
     * Consulta un dron por su identificador.
     *
     * @param id identificador del dron.
     * @return el dron encontrado, o {@code null} si no existe.
     */
    Drone consultarDrone(String id);

    /**
     * Consulta todos los drones registrados.
     *
     * @return la lista de drones.
     */
    List<Drone> consultarDrones();

    /**
     * Actualiza los datos de un dron existente.
     *
     * @param drone dron con los datos nuevos.
     * @return {@code true} si se actualizo.
     */
    boolean actualizarDrone(Drone drone);

    /**
     * Elimina el dron indicado.
     *
     * @param id identificador del dron a eliminar.
     * @return {@code true} si se elimino.
     */
    boolean eliminarDrone(String id);
}
