package co.edu.poli.sw2.tests.unitaria;

import co.edu.poli.sw2.dominio.weather.Weather;
import co.edu.poli.sw2.dominio.weather.chain.HumidityHandler;
import co.edu.poli.sw2.dominio.weather.chain.PressureHandler;
import co.edu.poli.sw2.dominio.weather.chain.TemperatureHandler;
import co.edu.poli.sw2.dominio.weather.chain.ViabilityResult;
import co.edu.poli.sw2.dominio.weather.chain.VisibilityHandler;
import co.edu.poli.sw2.dominio.weather.chain.WeatherHandler;
import co.edu.poli.sw2.dominio.weather.chain.WindHandler;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class WeatherChainTest {

    @Test
    void todoValidoDevuelveMensajeAptoExacto() {
        WeatherHandler cadena = new TemperatureHandler();
        cadena.setNext(new HumidityHandler())
              .setNext(new WindHandler())
              .setNext(new PressureHandler())
              .setNext(new VisibilityHandler());

        Weather climaApto = new Weather(24.0, 65.0, 18.0, 1012.0, 10.0);
        ViabilityResult result = cadena.handle(climaApto);

        assertTrue(result.isApto());
        assertEquals("CLIMA APTO PARA OPERACIÓN DE DRONES", result.message());
    }

    @Test
    void dosReglasIncumplidasDevuelveMotivoDelPrimerHandlerYNoEvaluaSiguientes() {
        WeatherHandler cadena = new TemperatureHandler();
        HumidityHandler humidityHandler = new HumidityHandler();
        EspiaWindHandler espiaWind = new EspiaWindHandler();

        cadena.setNext(humidityHandler).setNext(espiaWind);

        // Temperatura valida (25), humedad invalida (90) y viento invalido (60)
        Weather clima = new Weather(25.0, 90.0, 60.0, 1013.0, 10.0);
        ViabilityResult result = cadena.handle(clima);

        assertFalse(result.isApto());
        assertTrue(result.getReason().contains("Humedad fuera de rango (<= 85 %)"));
        assertEquals("CLIMA NO APTO PARA OPERACIÓN DE DRONES: " + result.getReason(), result.message());
        assertFalse(espiaWind.fueEvaluado, "El siguiente handler no debio ser evaluado");
    }

    @Test
    void cadenaDeUnSoloHandler() {
        WeatherHandler handlerUnico = new TemperatureHandler();

        Weather valido = new Weather(20.0, 50.0, 10.0, 1013.0, 10.0);
        ViabilityResult resValido = handlerUnico.handle(valido);
        assertTrue(resValido.isApto());
        assertEquals("CLIMA APTO PARA OPERACIÓN DE DRONES", resValido.message());

        Weather invalido = new Weather(45.0, 50.0, 10.0, 1013.0, 10.0);
        ViabilityResult resInvalido = handlerUnico.handle(invalido);
        assertFalse(resInvalido.isApto());
        assertEquals("CLIMA NO APTO PARA OPERACIÓN DE DRONES: " + resInvalido.getReason(), resInvalido.message());
    }

    private static class EspiaWindHandler extends WindHandler {
        boolean fueEvaluado = false;

        @Override
        public ViabilityResult handle(Weather weather) {
            fueEvaluado = true;
            return super.handle(weather);
        }

        @Override
        protected boolean cumple(Weather weather) {
            fueEvaluado = true;
            return super.cumple(weather);
        }
    }
}
