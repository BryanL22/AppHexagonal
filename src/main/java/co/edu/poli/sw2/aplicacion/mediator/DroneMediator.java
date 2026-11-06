package co.edu.poli.sw2.aplicacion.mediator;

/**
 * Mediator del patron Mediator: canal unico por el que los sistemas de una
 * operacion de vigilancia avisan que terminaron su paso.
 *
 * <p>Los sistemas no se conocen entre si; solo guardan una referencia a esta
 * interfaz. Quien la implementa decide que sistema actua a continuacion.</p>
 */
public interface DroneMediator {

    /**
     * Recibe el aviso de un sistema.
     *
     * @param emisor sistema que envia el aviso.
     * @param evento lo que ocurrio, por ejemplo {@code "RUTA_LISTA"}.
     */
    void notificar(Object emisor, String evento);
}
