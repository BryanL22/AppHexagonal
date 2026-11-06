package co.edu.poli.sw2.dominio.state;

/**
 * Estado ATERRIZANDO: el dron esta descendiendo. Solo permite finalizar el
 * aterrizaje, que lo lleva a {@link ReadyState}.
 */
public class LandingState implements DroneState {

    /**
     * Crea el estado ATERRIZANDO.
     */
    public LandingState() {
    }

    /**
     * Termina el aterrizaje: pasa a EN ESPERA.
     *
     * @param contexto contexto del dron sobre el que se aplica la transicion.
     */
    @Override
    public void finalizarAterrizaje(DroneContext contexto) {
        contexto.setEstado(new ReadyState());
    }

    /**
     * Devuelve el nombre del estado.
     *
     * @return "ATERRIZANDO".
     */
    @Override
    public String getNombre() {
        return "ATERRIZANDO";
    }
}
