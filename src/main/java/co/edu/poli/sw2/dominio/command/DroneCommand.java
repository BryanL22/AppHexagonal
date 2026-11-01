package co.edu.poli.sw2.dominio.command;

/**
 * Interfaz que define el contrato para todos los comandos de operacion
 * de un dron, segun el patron de diseno Command.
 *
 * <p>Cada implementacion encapsula una operacion concreta y delega
 * su ejecucion al {@code DroneOperacionService} (Receiver/Wrapper),
 * devolviendo el resultado como texto.</p>
 */
public interface DroneCommand {

    /**
     * Ejecuta la operacion encapsulada por este comando.
     *
     * @return un {@code String} con el resultado de la operacion.
     */
    String ejecutar();
}
