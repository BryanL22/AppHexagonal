package co.edu.poli.sw2.aplicacion.servicio;

import co.edu.poli.sw2.aplicacion.puerto.entrada.EvaluarViabilidadVueloUseCase;
import co.edu.poli.sw2.dominio.weather.Weather;
import co.edu.poli.sw2.dominio.weather.chain.ViabilityResult;
import co.edu.poli.sw2.dominio.weather.chain.WeatherHandler;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Servicio de aplicacion que evalua la viabilidad de vuelo a traves de una cadena de responsabilidad.
 */
public class EvaluarViabilidadVueloServicio implements EvaluarViabilidadVueloUseCase {

    /** Registrador de eventos para advertencias de no viabilidad. */
    private static final Logger LOGGER = Logger.getLogger(EvaluarViabilidadVueloServicio.class.getName());

    /** Primer evaluador de la cadena de responsabilidad. */
    private final WeatherHandler cadena;

    /**
     * Construye el servicio inyectando el primer evaluador de la cadena.
     *
     * @param cadena primer evaluador de la cadena de responsabilidad.
     */
    public EvaluarViabilidadVueloServicio(WeatherHandler cadena) {
        this.cadena = cadena;
    }

    /** {@inheritDoc} */
    @Override
    public ViabilityResult evaluar(Weather weather) {
        if (weather == null) {
            throw new IllegalArgumentException("Los datos meteorologicos son obligatorios.");
        }
        ViabilityResult resultado = cadena.handle(weather);
        if (!resultado.isApto()) {
            LOGGER.log(Level.WARNING, "Clima no apto para operacion de vuelo: {0}", resultado.getReason());
        }
        return resultado;
    }
}
