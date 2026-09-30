package co.edu.poli.sw2.domain.port.out;

/**
 * Error de persistencia que puede propagar {@link DroneRepositoryPort}.
 *
 * <p>Forma parte del contrato del puerto: el adaptador envuelve aqui sus
 * excepciones tecnicas (JDBC, lectura de configuracion) para que no crucen
 * hacia el dominio. Es unchecked a proposito, de modo que la firma del puerto
 * no tenga que mencionar tecnologia alguna.</p>
 */
public class PersistenciaException extends RuntimeException {

    /**
     * Crea la excepcion con el mensaje y la causa tecnica original.
     *
     * @param mensaje descripcion de la operacion que fallo.
     * @param causa excepcion tecnica que la origino.
     */
    public PersistenciaException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
