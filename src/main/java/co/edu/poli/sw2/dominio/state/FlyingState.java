package co.edu.poli.sw2.dominio.state;

/**
 * Estado VOLANDO: el dron esta en el aire. Solo permite aterrizar, que lo
 * lleva a {@link LandingState}.
 */
public class FlyingState implements DroneState {

    /**
     * Crea el estado VOLANDO.
     */
    public FlyingState() {
    }

    /**
     * Inicia el aterrizaje: pasa a ATERRIZANDO.
     *
     * @param contexto contexto del dron sobre el que se aplica la transicion.
     */
    @Override
    public void aterrizar(DroneContext contexto) {
        contexto.setEstado(new LandingState());
    }

    /**
     * Devuelve el nombre del estado.
     *
     * @return "VOLANDO".
     */
    @Override
    public String getNombre() {
        return "VOLANDO";
    }
}
