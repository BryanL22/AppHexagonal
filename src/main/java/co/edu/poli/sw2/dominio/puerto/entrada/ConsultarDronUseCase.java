package co.edu.poli.sw2.dominio.puerto.entrada;

import co.edu.poli.sw2.dominio.modelo.Drone;

/**
 * Puerto de entrada (driving port) del hexagono: consultar un dron por su identificador.
 *
 * <p>Lo implementa el servicio de aplicacion y lo consume el adaptador de entrada (la interfaz JavaFX). Declara lo que la aplicacion ofrece.</p>
 */
public interface ConsultarDronUseCase {

    /**
     * Consulta un dron por su identificador.
     *
     * @param id identificador del dron.
     * @return el dron encontrado, o {@code null} si no existe.
     */
    Drone consultar(String id);
}
