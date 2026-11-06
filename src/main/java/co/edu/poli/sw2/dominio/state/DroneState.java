package co.edu.poli.sw2.dominio.state;

/**
 * Estado del ciclo de vida de un dron (rol State del patron State).
 *
 * <p>Cada transicion es un metodo {@code default} que lanza
 * {@link IllegalStateException}: asi cada estado concreto sobrescribe solo
 * las transiciones que permite y no hacen falta condicionales. Para hacer
 * una transicion, el estado llama a
 * {@link DroneContext#setEstado(DroneState)} con el estado siguiente.</p>
 */
public interface DroneState {

    /**
     * Enciende el dron.
     *
     * @param contexto contexto del dron sobre el que se aplica la transicion.
     * @throws IllegalStateException si este estado no permite encender.
     */
    default void encender(DroneContext contexto) {
        throw transicionInvalida("encender");
    }

    /**
     * Despega el dron.
     *
     * @param contexto contexto del dron sobre el que se aplica la transicion.
     * @throws IllegalStateException si este estado no permite despegar.
     */
    default void despegar(DroneContext contexto) {
        throw transicionInvalida("despegar");
    }

    /**
     * Inicia el aterrizaje del dron.
     *
     * @param contexto contexto del dron sobre el que se aplica la transicion.
     * @throws IllegalStateException si este estado no permite aterrizar.
     */
    default void aterrizar(DroneContext contexto) {
        throw transicionInvalida("aterrizar");
    }

    /**
     * Termina el aterrizaje del dron.
     *
     * @param contexto contexto del dron sobre el que se aplica la transicion.
     * @throws IllegalStateException si este estado no permite finalizar el aterrizaje.
     */
    default void finalizarAterrizaje(DroneContext contexto) {
        throw transicionInvalida("finalizar el aterrizaje");
    }

    /**
     * Apaga el dron.
     *
     * @param contexto contexto del dron sobre el que se aplica la transicion.
     * @throws IllegalStateException si este estado no permite apagar.
     */
    default void apagar(DroneContext contexto) {
        throw transicionInvalida("apagar");
    }

    /**
     * Devuelve el nombre del estado tal como se muestra al usuario.
     *
     * @return el nombre del estado (por ejemplo, "APAGADO").
     */
    String getNombre();

    /**
     * Construye el error de una transicion que este estado no permite.
     *
     * @param accion accion que se intento (por ejemplo, "despegar").
     * @return la excepcion con un mensaje que indica la accion y el estado actual.
     */
    private IllegalStateException transicionInvalida(String accion) {
        return new IllegalStateException("No se puede " + accion + " en estado " + getNombre() + ".");
    }
}
