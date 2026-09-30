package co.edu.poli.sw2.domain.port.out;

import co.edu.poli.sw2.domain.model.Drone;

import java.util.List;

/**
 * Puerto de salida (driven port) del hexagono.
 *
 * <p>El dominio declara aqui lo que <em>necesita</em> para persistir drones,
 * sin decir como se hace. Quien lo implementa es un adaptador de
 * infraestructura; si manana se cambia MySQL por otro almacenamiento, solo
 * cambia el adaptador y el dominio queda intacto.</p>
 *
 * <p>Notese que esta interfaz no menciona JDBC, SQL ni ninguna tecnologia:
 * esa es la regla que mantiene limpio el hexagono.</p>
 */
public interface DroneRepositoryPort {

    /**
     * Guarda un dron nuevo.
     *
     * @param drone dron a guardar.
     * @return {@code true} si se guardo.
     */
    boolean guardar(Drone drone);

    /**
     * Busca un dron por su identificador.
     *
     * @param id identificador del dron.
     * @return el dron encontrado, o {@code null} si no existe.
     */
    Drone buscarPorId(String id);

    /**
     * Devuelve todos los drones registrados.
     *
     * @return la lista de drones.
     */
    List<Drone> buscarTodos();

    /**
     * Actualiza los datos de un dron existente.
     *
     * @param drone dron con los datos nuevos.
     * @return {@code true} si se actualizo.
     */
    boolean actualizar(Drone drone);

    /**
     * Elimina el dron indicado.
     *
     * @param id identificador del dron a eliminar.
     * @return {@code true} si se elimino.
     */
    boolean eliminar(String id);
}
