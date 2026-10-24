package co.edu.poli.sw2.dominio.weather.chain;

import co.edu.poli.sw2.dominio.weather.Weather;

/**
 * Validador de presion atmosferica permitida para el vuelo de drones (950 a 1050 hPa inclusive).
 */
public class PressureHandler extends WeatherHandler {

    /**
     * Crea una nueva instancia del validador de presion atmosferica.
     */
    public PressureHandler() {
    }

    /** {@inheritDoc} */
    @Override
    protected boolean cumple(Weather weather) {
        return weather.pressure >= 950 && weather.pressure <= 1050;
    }

    /** {@inheritDoc} */
    @Override
    protected String motivo(Weather weather) {
        return "Presion fuera de rango (950 a 1050 hPa): " + weather.pressure + " hPa";
    }
}
