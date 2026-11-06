package co.edu.poli.sw2.dominio.state;

/**
 * Estado APAGADO: es el estado inicial de todo dron. Solo permite encender,
 * que lo lleva a {@link ReadyState}.
 */
public class OffState implements DroneState {

    /**
     * Crea el estado APAGADO.
     */
    public OffState() {
    }

    /**
     * Enciende el dron: pasa a EN ESPERA.
     *
     * @param contexto contexto del dron sobre el que se aplica la transicion.
     */
    @Override
    public void encender(DroneContext contexto) {
        contexto.setEstado(new ReadyState());
    }

    /**
     * Devuelve el nombre del estado.
     *
     * @return "APAGADO".
     */
    @Override
    public String getNombre() {
        return "APAGADO";
    }
}
