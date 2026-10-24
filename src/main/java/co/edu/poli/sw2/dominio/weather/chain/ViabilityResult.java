package co.edu.poli.sw2.dominio.weather.chain;

/**
 * Resultado de la evaluacion de viabilidad para la operacion de drones segun el clima.
 */
public class ViabilityResult {

    /** Indica si las condiciones permiten la operacion. */
    private final boolean apto;
    /** Descripcion del motivo de rechazo o null si es apto. */
    private final String reason;

    /**
     * Construye un resultado con el estado y motivo de viabilidad.
     *
     * @param apto true si las condiciones son aptas para el vuelo, false en caso contrario.
     * @param reason descripcion de la regla incumplida o null si es apto.
     */
    private ViabilityResult(boolean apto, String reason) {
        this.apto = apto;
        this.reason = reason;
    }

    /**
     * Fabrica para un resultado aprobado.
     *
     * @return resultado favorable para operacion de vuelo.
     */
    public static ViabilityResult approved() {
        return new ViabilityResult(true, null);
    }

    /**
     * Fabrica para un resultado rechazado.
     *
     * @param reason descripcion de la condicion que no se cumplio.
     * @return resultado no apto para operacion de vuelo.
     */
    public static ViabilityResult rejected(String reason) {
        return new ViabilityResult(false, reason);
    }

    /**
     * Indica si las condiciones meteorologicas son aptas para el vuelo.
     *
     * @return true si es apto, false de lo contrario.
     */
    public boolean isApto() {
        return apto;
    }

    /**
     * Retorna el motivo del rechazo, o null si fue aprobado.
     *
     * @return motivo del rechazo.
     */
    public String getReason() {
        return reason;
    }

    /**
     * Genera el mensaje formateado para la interfaz y registros.
     *
     * @return mensaje exacto del estado de viabilidad.
     */
    public String message() {
        if (apto) {
            return "CLIMA APTO PARA OPERACIÓN DE DRONES";
        }
        return "CLIMA NO APTO PARA OPERACIÓN DE DRONES: " + (reason != null ? reason : "");
    }
}
