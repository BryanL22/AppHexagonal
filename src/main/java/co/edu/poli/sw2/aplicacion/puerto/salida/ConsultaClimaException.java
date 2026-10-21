package co.edu.poli.sw2.aplicacion.puerto.salida;

/**
 * Error que puede propagar {@link WeatherPort} al consultar el clima.
 *
 * <p>Cumple el mismo papel que {@link PersistenciaException}: el adaptador
 * envuelve aqui sus excepciones tecnicas (HTTP, JSON, red) para que no
 * crucen hacia la aplicacion. Es unchecked para que la firma del puerto no
 * mencione ninguna tecnologia.</p>
 */
public class ConsultaClimaException extends RuntimeException {

    /**
     * Crea la excepcion solo con un mensaje, para errores sin causa tecnica
     * (por ejemplo, una ciudad que no existe).
     *
     * @param mensaje descripcion del problema.
     */
    public ConsultaClimaException(String mensaje) {
        super(mensaje);
    }

    /**
     * Crea la excepcion con el mensaje y la causa tecnica original.
     *
     * @param mensaje descripcion de la operacion que fallo.
     * @param causa excepcion tecnica que la origino.
     */
    public ConsultaClimaException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
