package co.edu.poli.sw2.aplicacion.mediator.components;

import co.edu.poli.sw2.aplicacion.mediator.DroneMediator;

/**
 * Componente del patron Mediator: sistema de bateria, verifica la
 * disponibilidad energetica. Avisa al mediador con {@code "BATERIA_OK"} si
 * la carga alcanza el minimo o con {@code "BATERIA_BAJA"} si no.
 */
public class BatterySystem {

    /** Porcentaje minimo de carga para iniciar una mision. */
    public static final int NIVEL_MINIMO = 30;

    /** Mediador al que se avisa; el unico colaborador que conoce. */
    private DroneMediator mediador;

    /**
     * Crea el sistema de bateria; el mediador se asigna despues.
     */
    public BatterySystem() {
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
     * Compara la carga con {@link #NIVEL_MINIMO} y avisa el resultado al
     * mediador.
     *
     * @param nivelBateria porcentaje de carga del dron, de 0 a 100.
     */
    public void verificarDisponibilidad(int nivelBateria) {
        mediador.notificar(this, nivelBateria >= NIVEL_MINIMO ? "BATERIA_OK" : "BATERIA_BAJA");
    }
}
