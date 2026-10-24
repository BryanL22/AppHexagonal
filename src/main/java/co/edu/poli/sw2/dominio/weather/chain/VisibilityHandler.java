package co.edu.poli.sw2.dominio.weather.chain;

import co.edu.poli.sw2.dominio.weather.Weather;

/**
 * Validador de visibilidad minima permitida para el vuelo de drones (mayor o igual a 5 km).
 */
public class VisibilityHandler extends WeatherHandler {

    /**
     * Crea una nueva instancia del validador de visibilidad.
     */
    public VisibilityHandler() {
    }

    /** {@inheritDoc} */
    @Override
    protected boolean cumple(Weather weather) {
        return weather.visibility >= 5;
    }

    /** {@inheritDoc} */
    @Override
    protected String motivo(Weather weather) {
        return "Visibilidad fuera de rango (>= 5 km): " + weather.visibility + " km";
    }
}
