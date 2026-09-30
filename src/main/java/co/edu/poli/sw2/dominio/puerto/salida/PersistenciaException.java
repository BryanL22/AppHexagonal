package co.edu.poli.sw2.dominio.puerto.salida;

/**
 * Error de persistencia que pueden propagar los puertos de salida.
 *
 * <p>Forma parte del contrato de los puertos: el adaptador envuelve aqui sus
 * excepciones tecnicas (JDBC, lectura de configuracion) para que no crucen
 * hacia el dominio. Es unchecked a proposito, de modo que la firma de los
 * puertos no tenga que mencionar tecnologia alguna.</p>
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
