package co.edu.poli.sw2.aplicacion.puerto.salida;

/**
 * Puerto de salida (driven port) del hexagono: eliminar un dron del almacenamiento.
 *
 * <p>Lo implementa un adaptador de infraestructura. Declara lo que la aplicacion necesita, sin decir con que tecnologia se resuelve.</p>
 */
public interface EliminarDronPort {

    /**
     * Elimina del almacenamiento el dron indicado.
     *
     * @param id identificador del dron.
     * @return {@code true} si la operacion tuvo efecto.
     */
    boolean eliminar(String id);
}
