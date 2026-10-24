package co.edu.poli.sw2.tests.unitaria;

import co.edu.poli.sw2.aplicacion.servicio.EvaluarViabilidadVueloServicio;
import co.edu.poli.sw2.dominio.weather.Weather;
import co.edu.poli.sw2.dominio.weather.chain.ViabilityResult;
import co.edu.poli.sw2.dominio.weather.chain.WeatherHandler;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class EvaluarViabilidadVueloServicioTest {

    @Test
    void evaluarClimaAptoRetornaResultadoAprobado() {
        WeatherHandler handler = new WeatherHandler() {
            @Override
            protected boolean cumple(Weather weather) {
                return true;
            }

            @Override
            protected String motivo(Weather weather) {
                return "";
            }
        };
        EvaluarViabilidadVueloServicio servicio = new EvaluarViabilidadVueloServicio(handler);
        Weather weather = new Weather(25.0, 50.0, 10.0, 1013.0, 10.0);

        ViabilityResult result = servicio.evaluar(weather);

        assertTrue(result.isApto());
        assertEquals("CLIMA APTO PARA OPERACIÓN DE DRONES", result.message());
    }

    @Test
    void evaluarClimaNoAptoRetornaResultadoRechazado() {
        WeatherHandler handler = new WeatherHandler() {
            @Override
            protected boolean cumple(Weather weather) {
                return false;
            }

            @Override
            protected String motivo(Weather weather) {
                return "Viento excesivo: 55 km/h";
            }
        };
        EvaluarViabilidadVueloServicio servicio = new EvaluarViabilidadVueloServicio(handler);
        Weather weather = new Weather(25.0, 50.0, 55.0, 1013.0, 10.0);

        ViabilityResult result = servicio.evaluar(weather);

        assertFalse(result.isApto());
        assertEquals("Viento excesivo: 55 km/h", result.getReason());
        assertEquals("CLIMA NO APTO PARA OPERACIÓN DE DRONES: Viento excesivo: 55 km/h", result.message());
    }

    @Test
    void evaluarWeatherNullLanzaIllegalArgumentException() {
        WeatherHandler handler = new WeatherHandler() {
            @Override
            protected boolean cumple(Weather weather) {
                return true;
            }

            @Override
            protected String motivo(Weather weather) {
                return "";
            }
        };
        EvaluarViabilidadVueloServicio servicio = new EvaluarViabilidadVueloServicio(handler);

        assertThrows(IllegalArgumentException.class, () -> servicio.evaluar(null));
    }
}
