package co.edu.poli.sw2.dominio.puerto.entrada;

import co.edu.poli.sw2.dominio.modelo.Drone;

import java.util.List;

/**
 * Puerto de entrada (driving port) del hexagono: consultar todos los drones.
 *
 * <p>Lo implementa el servicio de aplicacion y lo consume el adaptador de entrada (la interfaz JavaFX). Declara lo que la aplicacion ofrece.</p>
 */
public interface ConsultarDronesUseCase {

    /**
     * Consulta todos los drones registrados.
     *
     * @return la lista de drones registrados.
     */
    List<Drone> consultarTodos();
}
