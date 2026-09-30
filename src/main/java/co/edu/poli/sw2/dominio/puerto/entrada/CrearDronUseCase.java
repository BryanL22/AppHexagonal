package co.edu.poli.sw2.dominio.puerto.entrada;

import co.edu.poli.sw2.dominio.modelo.Drone;

/**
 * Puerto de entrada (driving port) del hexagono: registrar un dron nuevo.
 *
 * <p>Lo implementa el servicio de aplicacion y lo consume el adaptador de entrada (la interfaz JavaFX). Declara lo que la aplicacion ofrece.</p>
 */
public interface CrearDronUseCase {

    /**
     * Registra un dron nuevo. Falla si su identificador ya esta ocupado.
     *
     * @param drone dron sobre el que se opera.
     * @return {@code true} si la operacion tuvo efecto.
     */
    boolean crear(Drone drone);
}
