package co.edu.poli.sw2.aplicacion.servicio;

/**
 * Servicio wrapper (Receiver) que implementa las operaciones de un dron
 * sin modificar la clase {@code Drone} ni el modelo de dominio existente.
 *
 * <p>Cada metodo representa una operacion concreta que los comandos
 * ({@code DroneCommand}) delegan a este servicio. Por ahora, cada
 * metodo retorna un {@code String} descriptivo del resultado.</p>
 */
public class DroneOperacionService {

    /**
     * Ejecuta la operacion de tomar una fotografia.
     *
     * @return un {@code String} con el resultado de la operacion.
     */
    public String tomarFotografia() {
        return "Fotografia tomada exitosamente.";
    }

    /**
     * Ejecuta la operacion de escanear una zona.
     *
     * @return un {@code String} con el resultado de la operacion.
     */
    public String escanearZona() {
        return "Zona escaneada exitosamente.";
    }

    /**
     * Ejecuta la operacion de medir la temperatura.
     *
     * @return un {@code String} con el resultado de la operacion.
     */
    public String medirTemperatura() {
        return "Temperatura medida exitosamente.";
    }
}
