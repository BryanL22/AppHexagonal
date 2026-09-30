package co.edu.poli.sw2.dominio.puerto.entrada;

/**
 * Puerto de entrada (driving port) del hexagono: eliminar un dron.
 *
 * <p>Lo implementa el servicio de aplicacion y lo consume el adaptador de entrada (la interfaz JavaFX). Declara lo que la aplicacion ofrece.</p>
 */
public interface EliminarDronUseCase {

    /**
     * Elimina el dron indicado.
     *
     * @param id identificador del dron.
     * @return {@code true} si la operacion tuvo efecto.
     */
    boolean eliminar(String id);
}
