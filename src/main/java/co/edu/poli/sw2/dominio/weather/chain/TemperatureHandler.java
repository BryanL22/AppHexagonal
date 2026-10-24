package co.edu.poli.sw2.dominio.weather.chain;

import co.edu.poli.sw2.dominio.weather.Weather;

/**
 * Validador de temperatura permitida para el vuelo de drones (0 a 40 C inclusive).
 */
public class TemperatureHandler extends WeatherHandler {

    /**
     * Crea una nueva instancia del validador de temperatura.
     */
    public TemperatureHandler() {
    }

    /** {@inheritDoc} */
    @Override
    protected boolean cumple(Weather weather) {
        return weather.temperature >= 0 && weather.temperature <= 40;
    }

    /** {@inheritDoc} */
    @Override
    protected String motivo(Weather weather) {
        return "Temperatura fuera de rango (0 a 40 C): " + weather.temperature + " C";
    }
}
