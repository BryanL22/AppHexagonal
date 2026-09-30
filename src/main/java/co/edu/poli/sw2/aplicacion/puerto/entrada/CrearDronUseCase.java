package co.edu.poli.sw2.aplicacion.puerto.entrada;

import co.edu.poli.sw2.dominio.modelo.Drone;

/**
 * Puerto de entrada (driving port) del hexagono: registrar un dron nuevo.
 *
 * <p>Lo implementa el servicio de aplicacion y lo consume el adaptador de entrada (la interfaz JavaFX). Declara lo que la aplicacion ofrece.</p>
 */
public interface CrearDronUseCase {

    /**
     * Registra un dron nuevo, siempre que sea valido y su identificador no
     * este ocupado.
     *
     * @param drone dron a registrar.
     * @return {@code true} si se guardo; {@code false} si el identificador ya existia.
     * @throws IllegalArgumentException si el dron no cumple las reglas de negocio.
     */
    boolean crear(Drone drone);
}
