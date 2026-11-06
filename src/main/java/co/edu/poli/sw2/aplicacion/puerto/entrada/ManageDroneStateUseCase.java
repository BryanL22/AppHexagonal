package co.edu.poli.sw2.aplicacion.puerto.entrada;

/**
 * Puerto de entrada (driving port) del hexagono: manejar el ciclo de vida
 * de un dron (encender, despegar, aterrizar, finalizar aterrizaje y apagar).
 *
 * <p>Lo implementa el servicio de aplicacion y lo consume el adaptador de
 * entrada (la interfaz JavaFX). Cada operacion devuelve el nombre del estado
 * en que queda el dron.</p>
 */
public interface ManageDroneStateUseCase {

    /**
     * Enciende el dron.
     *
     * @param droneId identificador del dron.
     * @return el nombre del nuevo estado.
     * @throws IllegalArgumentException si el identificador esta vacio o el dron no existe.
     * @throws IllegalStateException si el estado actual no permite la transicion.
     */
    String encender(String droneId);

    /**
     * Despega el dron.
     *
     * @param droneId identificador del dron.
     * @return el nombre del nuevo estado.
     * @throws IllegalArgumentException si el identificador esta vacio o el dron no existe.
     * @throws IllegalStateException si el estado actual no permite la transicion.
     */
    String despegar(String droneId);

    /**
     * Inicia el aterrizaje del dron.
     *
     * @param droneId identificador del dron.
     * @return el nombre del nuevo estado.
     * @throws IllegalArgumentException si el identificador esta vacio o el dron no existe.
     * @throws IllegalStateException si el estado actual no permite la transicion.
     */
    String aterrizar(String droneId);

    /**
     * Termina el aterrizaje del dron.
     *
     * @param droneId identificador del dron.
     * @return el nombre del nuevo estado.
     * @throws IllegalArgumentException si el identificador esta vacio o el dron no existe.
     * @throws IllegalStateException si el estado actual no permite la transicion.
     */
    String finalizarAterrizaje(String droneId);

    /**
     * Apaga el dron.
     *
     * @param droneId identificador del dron.
     * @return el nombre del nuevo estado.
     * @throws IllegalArgumentException si el identificador esta vacio o el dron no existe.
     * @throws IllegalStateException si el estado actual no permite la transicion.
     */
    String apagar(String droneId);

    /**
     * Consulta el estado actual del dron.
     *
     * @param droneId identificador del dron.
     * @return el nombre del estado actual.
     * @throws IllegalArgumentException si el identificador esta vacio o el dron no existe.
     */
    String consultarEstado(String droneId);
}
