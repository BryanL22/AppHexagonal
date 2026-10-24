package co.edu.poli.sw2.dominio.weather.chain;

import co.edu.poli.sw2.dominio.weather.Weather;

/**
 * Clase abstracta que define la plantilla y el encadenamiento de la evaluacion meteorologica.
 */
public abstract class WeatherHandler {

    /** Siguiente eslabon en la cadena de responsabilidad. */
    protected WeatherHandler next;

    /**
     * Constructor base de los evaluadores de clima.
     */
    protected WeatherHandler() {
    }

    /**
     * Enlaza el siguiente evaluador de la cadena y lo retorna para encadenamiento fluido.
     *
     * @param next siguiente evaluador en la cadena.
     * @return el evaluador recibido como parametro.
     */
    public WeatherHandler setNext(WeatherHandler next) {
        this.next = next;
        return next;
    }

    /**
     * Metodo plantilla que ejecuta la evaluacion de este eslabon.
     * Si no cumple la condicion, detiene la cadena y devuelve el resultado rechazado.
     * Si la cumple, continua con el siguiente o aprueba si es el ultimo eslabon.
     *
     * @param weather datos meteorologicos a evaluar.
     * @return resultado de viabilidad de la evaluacion.
     */
    public ViabilityResult handle(Weather weather) {
        if (!cumple(weather)) {
            return ViabilityResult.rejected(motivo(weather));
        }
        if (next != null) {
            return next.handle(weather);
        }
        return ViabilityResult.approved();
    }

    /**
     * Evalua si los datos meteorologicos cumplen la regla especifica de este eslabon.
     *
     * @param weather datos meteorologicos.
     * @return true si cumple la regla, false si la incumple.
     */
    protected abstract boolean cumple(Weather weather);

    /**
     * Devuelve el motivo descriptivo del incumplimiento de la regla.
     *
     * @param weather datos meteorologicos.
     * @return motivo con la regla y el valor medido.
     */
    protected abstract String motivo(Weather weather);
}
