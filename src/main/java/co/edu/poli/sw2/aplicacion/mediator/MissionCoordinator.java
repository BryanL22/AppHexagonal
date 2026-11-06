package co.edu.poli.sw2.aplicacion.mediator;

import co.edu.poli.sw2.aplicacion.mediator.components.BatterySystem;
import co.edu.poli.sw2.aplicacion.mediator.components.CommunicationSystem;
import co.edu.poli.sw2.aplicacion.mediator.components.NavigationSystem;
import co.edu.poli.sw2.aplicacion.mediator.components.SensorSystem;

import java.util.ArrayList;
import java.util.List;

/**
 * Mediador concreto del patron Mediator: el coordinador de mision, la torre
 * de control de la operacion de vigilancia.
 *
 * <p>Conoce a los cuatro sistemas y controla la secuencia: ruta, sensores,
 * bateria y enlace. Cada sistema le avisa cuando termina y el coordinador
 * decide el siguiente paso; si la bateria no alcanza, aborta la mision en
 * lugar de establecer el enlace. Cada paso queda anotado en la bitacora.</p>
 */
public class MissionCoordinator implements DroneMediator {

    /** Sistema que prepara la ruta. */
    private final NavigationSystem navegacion;
    /** Sistema que activa los sensores. */
    private final SensorSystem sensores;
    /** Sistema que verifica la energia disponible. */
    private final BatterySystem bateria;
    /** Sistema que establece el enlace. */
    private final CommunicationSystem comunicacion;
    /** Pasos de la mision en curso, en orden. */
    private final List<String> bitacora = new ArrayList<>();
    /** Porcentaje de bateria de la mision en curso. */
    private int nivelBateria;

    /**
     * Crea el coordinador y se registra como mediador de los cuatro sistemas.
     *
     * @param navegacion sistema de navegacion.
     * @param sensores sistema de sensores.
     * @param bateria sistema de bateria.
     * @param comunicacion sistema de comunicacion.
     */
    public MissionCoordinator(NavigationSystem navegacion, SensorSystem sensores,
            BatterySystem bateria, CommunicationSystem comunicacion) {
        this.navegacion = navegacion;
        this.sensores = sensores;
        this.bateria = bateria;
        this.comunicacion = comunicacion;
        navegacion.setMediador(this);
        sensores.setMediador(this);
        bateria.setMediador(this);
        comunicacion.setMediador(this);
    }

    /**
     * Inicia la operacion de vigilancia: pide la ruta al sistema de
     * navegacion y deja que los avisos de los sistemas encadenen el resto.
     *
     * @param droneId identificador del dron.
     * @param nivelBateria porcentaje de bateria del dron, de 0 a 100.
     * @return la bitacora de la mision, con cada paso en orden.
     */
    public List<String> iniciarMision(String droneId, int nivelBateria) {
        bitacora.clear();
        this.nivelBateria = nivelBateria;
        registrar("Coordinador: iniciando operacion de vigilancia del dron " + droneId + ".");
        registrar("Navegacion: preparando la ruta...");
        navegacion.prepararRuta(droneId);
        return List.copyOf(bitacora);
    }

    /**
     * Recibe el aviso de un sistema y decide el siguiente paso.
     *
     * @param emisor sistema que envia el aviso.
     * @param evento lo que ocurrio.
     * @throws IllegalArgumentException si el evento no es conocido.
     */
    @Override
    public void notificar(Object emisor, String evento) {
        switch (evento) {
            case "RUTA_LISTA" -> {
                registrar("Navegacion: ruta lista.");
                registrar("Sensores: iniciando los sensores...");
                sensores.activarSensores();
            }
            case "SENSORES_ACTIVOS" -> {
                registrar("Sensores: sensores activos.");
                registrar("Bateria: verificando disponibilidad energetica (" + nivelBateria + "%)...");
                bateria.verificarDisponibilidad(nivelBateria);
            }
            case "BATERIA_OK" -> {
                registrar("Bateria: energia suficiente.");
                registrar("Comunicacion: estableciendo enlace...");
                comunicacion.establecerEnlace();
            }
            case "BATERIA_BAJA" -> {
                registrar("Bateria: energia insuficiente (minimo " + BatterySystem.NIVEL_MINIMO + "%).");
                registrar("Coordinador: mision abortada.");
            }
            case "ENLACE_LISTO" -> {
                registrar("Comunicacion: enlace establecido.");
                registrar("Coordinador: mision iniciada.");
            }
            default -> throw new IllegalArgumentException("Evento desconocido: " + evento);
        }
    }

    /**
     * Anota un paso en la bitacora.
     *
     * @param mensaje descripcion del paso.
     */
    private void registrar(String mensaje) {
        bitacora.add(mensaje);
    }
}
