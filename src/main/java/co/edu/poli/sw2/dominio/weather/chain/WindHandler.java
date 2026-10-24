package co.edu.poli.sw2.dominio.weather.chain;

import co.edu.poli.sw2.dominio.weather.Weather;

/**
 * Validador de velocidad del viento permitida para el vuelo de drones (menor o igual a 40 km/h).
 */
public class WindHandler extends WeatherHandler {

    /**
     * Crea una nueva instancia del validador de velocidad del viento.
     */
    public WindHandler() {
    }

    /** {@inheritDoc} */
    @Override
    protected boolean cumple(Weather weather) {
        return weather.windSpeed <= 40;
    }

    /** {@inheritDoc} */
    @Override
    protected String motivo(Weather weather) {
        return "Velocidad del viento fuera de rango (<= 40 km/h): " + weather.windSpeed + " km/h";
    }
}
