package co.edu.poli.sw2.dominio.state;

/**
 * Estado EN ESPERA: el dron esta encendido en tierra. Permite despegar, que
 * lo lleva a {@link FlyingState}, o apagar, que lo lleva a {@link OffState}.
 */
public class ReadyState implements DroneState {

    /**
     * Crea el estado EN ESPERA.
     */
    public ReadyState() {
    }

    /**
     * Despega el dron: pasa a VOLANDO.
     *
     * @param contexto contexto del dron sobre el que se aplica la transicion.
     */
    @Override
    public void despegar(DroneContext contexto) {
        contexto.setEstado(new FlyingState());
    }

    /**
     * Apaga el dron: pasa a APAGADO.
     *
     * @param contexto contexto del dron sobre el que se aplica la transicion.
     */
    @Override
    public void apagar(DroneContext contexto) {
        contexto.setEstado(new OffState());
    }

    /**
     * Devuelve el nombre del estado.
     *
     * @return "EN ESPERA".
     */
    @Override
    public String getNombre() {
        return "EN ESPERA";
    }
}
