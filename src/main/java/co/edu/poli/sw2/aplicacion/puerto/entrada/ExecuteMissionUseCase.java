package co.edu.poli.sw2.aplicacion.puerto.entrada;

import java.util.List;

/**
 * Puerto de entrada (driving port) del hexagono: ejecutar una operacion de
 * vigilancia coordinada por la torre de control (patron Mediator).
 *
 * <p>Lo implementa el servicio de aplicacion y lo consume el adaptador de
 * entrada (la interfaz JavaFX). No usa las operaciones CRUD del dron.</p>
 */
public interface ExecuteMissionUseCase {

    /**
     * Ejecuta la operacion de vigilancia de un dron.
     *
     * @param droneId identificador del dron.
     * @param nivelBateria porcentaje de bateria del dron, de 0 a 100.
     * @return la bitacora de la mision, con cada paso en orden.
     * @throws IllegalArgumentException si el identificador esta vacio o el
     *         nivel de bateria no esta entre 0 y 100.
     */
    List<String> ejecutarMision(String droneId, int nivelBateria);
}
