package co.edu.poli.sw2.aplicacion.mediator.components;

import co.edu.poli.sw2.aplicacion.mediator.DroneMediator;

/**
 * Componente del patron Mediator: sistema de comunicacion, establece el
 * enlace con el dron. Al terminar avisa al mediador con
 * {@code "ENLACE_LISTO"}.
 */
public class CommunicationSystem {

    /** Mediador al que se avisa; el unico colaborador que conoce. */
    private DroneMediator mediador;

    /**
     * Crea el sistema de comunicacion; el mediador se asigna despues.
     */
    public CommunicationSystem() {
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
     * Establece el enlace y avisa al mediador.
     */
    public void establecerEnlace() {
        mediador.notificar(this, "ENLACE_LISTO");
    }
}
