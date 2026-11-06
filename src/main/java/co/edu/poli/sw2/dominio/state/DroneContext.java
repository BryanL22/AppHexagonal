package co.edu.poli.sw2.dominio.state;

import co.edu.poli.sw2.dominio.modelo.Drone;

/**
 * Contexto del patron State para el ciclo de vida de un dron.
 *
 * <p>Envuelve a la entidad {@link Drone} sin modificarla: la entidad
 * conserva solo sus datos, y este contexto mantiene la referencia al estado
 * actual ({@link DroneState}) y delega en el las operaciones del ciclo de
 * vida. Los estados hacen las transiciones llamando a
 * {@link #setEstado(DroneState)}.</p>
 */
public class DroneContext {

    /** Dron cuyo ciclo de vida se maneja. */
    private final Drone drone;

    /** Estado actual del ciclo de vida. */
    private DroneState estado;

    /**
     * Crea el contexto de un dron con su estado actual.
     *
     * @param drone dron cuyo ciclo de vida se maneja.
     * @param estado estado actual del dron.
     */
    public DroneContext(Drone drone, DroneState estado) {
        this.drone = drone;
        this.estado = estado;
    }

    /**
     * Enciende el dron. Delega en su estado actual.
     *
     * @throws IllegalStateException si el estado actual no permite encender.
     */
    public void encender() {
        estado.encender(this);
    }

    /**
     * Despega el dron. Delega en su estado actual.
     *
     * @throws IllegalStateException si el estado actual no permite despegar.
     */
    public void despegar() {
        estado.despegar(this);
    }

    /**
     * Inicia el aterrizaje del dron. Delega en su estado actual.
     *
     * @throws IllegalStateException si el estado actual no permite aterrizar.
     */
    public void aterrizar() {
        estado.aterrizar(this);
    }

    /**
     * Termina el aterrizaje del dron. Delega en su estado actual.
     *
     * @throws IllegalStateException si el estado actual no permite finalizar el aterrizaje.
     */
    public void finalizarAterrizaje() {
        estado.finalizarAterrizaje(this);
    }

    /**
     * Apaga el dron. Delega en su estado actual.
     *
     * @throws IllegalStateException si el estado actual no permite apagar.
     */
    public void apagar() {
        estado.apagar(this);
    }

    /**
     * Devuelve el dron cuyo ciclo de vida se maneja.
     *
     * @return el dron.
     */
    public Drone getDrone() {
        return drone;
    }

    /**
     * Devuelve el estado actual del ciclo de vida.
     *
     * @return el estado actual.
     */
    public DroneState getEstado() {
        return estado;
    }

    /**
     * Cambia el estado actual. Lo usan los estados para hacer una transicion.
     *
     * @param estado nuevo estado del dron.
     */
    public void setEstado(DroneState estado) {
        this.estado = estado;
    }
}
