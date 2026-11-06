package co.edu.poli.sw2.aplicacion.mediator.components;

import co.edu.poli.sw2.aplicacion.mediator.DroneMediator;

/**
 * Componente del patron Mediator: sistema de navegacion, prepara la ruta del
 * dron. Al terminar avisa al mediador con {@code "RUTA_LISTA"}.
 */
public class NavigationSystem {

    /** Mediador al que se avisa; el unico colaborador que conoce. */
    private DroneMediator mediador;

    /**
     * Crea el sistema de navegacion; el mediador se asigna despues.
     */
    public NavigationSystem() {
    }

    /**
     * Asigna el mediador al que este sistema avisa.
     *
     * @param mediador mediador de la operacion.
     */
    public void setMediador(DroneMediator mediador) {
        this.mediador = mediador;
    }

    /**
     * Prepara la ruta del dron y avisa al mediador.
     *
     * @param droneId identificador del dron.
     */
    public void prepararRuta(String droneId) {
        mediador.notificar(this, "RUTA_LISTA");
    }
}
