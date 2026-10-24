package co.edu.poli.sw2.aplicacion.puerto.salida;

/** Error de consulta meteorologica independiente del proveedor utilizado. */
public class WeatherQueryException extends RuntimeException {

    /**
     * Construye un error con un mensaje comprensible.
     *
     * @param message descripcion del error.
     */
    public WeatherQueryException(String message) {
        super(message);
    }

    /**
     * Construye un error con su causa tecnica.
     *
     * @param message descripcion del error.
     * @param cause causa original.
     */
    public WeatherQueryException(String message, Throwable cause) {
        super(message, cause);
    }
}