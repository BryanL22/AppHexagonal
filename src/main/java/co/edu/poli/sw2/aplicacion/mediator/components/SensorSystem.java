package co.edu.poli.sw2.aplicacion.mediator.components;

import co.edu.poli.sw2.aplicacion.mediator.DroneMediator;

/**
 * Componente del patron Mediator: sistema de sensores, activa los sensores
 * de vigilancia. Al terminar avisa al mediador con {@code "SENSORES_ACTIVOS"}.
 */
public class SensorSystem {

    /** Mediador al que se avisa; el unico colaborador que conoce. */
    private DroneMediator mediador;

    /**
     * Crea el sistema de sensores; el mediador se asigna despues.
     */
    public SensorSystem() {
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
     * Activa los sensores y avisa al mediador.
     */
    public void activarSensores() {
        mediador.notificar(this, "SENSORES_ACTIVOS");
    }
}
