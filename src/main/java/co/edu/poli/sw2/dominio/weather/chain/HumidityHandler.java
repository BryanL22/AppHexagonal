package co.edu.poli.sw2.dominio.weather.chain;

import co.edu.poli.sw2.dominio.weather.Weather;

/**
 * Validador de humedad relativa permitida para el vuelo de drones (menor o igual a 85 %).
 */
public class HumidityHandler extends WeatherHandler {

    /**
     * Crea una nueva instancia del validador de humedad.
     */
    public HumidityHandler() {
    }

    /** {@inheritDoc} */
    @Override
    protected boolean cumple(Weather weather) {
        return weather.humidity <= 85;
    }

    /** {@inheritDoc} */
    @Override
    protected String motivo(Weather weather) {
        return "Humedad fuera de rango (<= 85 %): " + weather.humidity + " %";
    }
}
