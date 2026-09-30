package co.edu.poli.sw2.aplicacion.puerto.entrada;

import co.edu.poli.sw2.dominio.modelo.Drone;

/**
 * Puerto de entrada (driving port) del hexagono: actualizar los datos de un dron.
 *
 * <p>Lo implementa el servicio de aplicacion y lo consume el adaptador de entrada (la interfaz JavaFX). Declara lo que la aplicacion ofrece.</p>
 */
public interface ActualizarDronUseCase {

    /**
     * Actualiza los datos de un dron existente.
     *
     * @param drone dron con los datos nuevos.
     * @return {@code true} si la operacion tuvo efecto.
     * @throws IllegalArgumentException si el dron no cumple las reglas de negocio.
     */
    boolean actualizar(Drone drone);
}
