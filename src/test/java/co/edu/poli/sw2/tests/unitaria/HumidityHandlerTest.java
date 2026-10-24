package co.edu.poli.sw2.tests.unitaria;

import co.edu.poli.sw2.dominio.weather.Weather;
import co.edu.poli.sw2.dominio.weather.chain.HumidityHandler;
import co.edu.poli.sw2.dominio.weather.chain.ViabilityResult;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class HumidityHandlerTest {

    @Test
    void humedadValidaDentroDeRango() {
        HumidityHandler handler = new HumidityHandler();
        Weather weather = new Weather(20, 60, 10, 1013, 10);

        ViabilityResult result = handler.handle(weather);

        assertTrue(result.isApto());
        assertNull(result.getReason());
    }

    @Test
    void humedadInvalidaSuperiorAlLimite() {
        HumidityHandler handler = new HumidityHandler();
        Weather weather = new Weather(20, 92.5, 10, 1013, 10);

        ViabilityResult result = handler.handle(weather);

        assertFalse(result.isApto());
        assertTrue(result.getReason().contains("Humedad fuera de rango (<= 85 %)"));
        assertTrue(result.getReason().contains("92.5"));
    }

    @Test
    void limitesHumedad() {
        HumidityHandler handler = new HumidityHandler();

        assertTrue(handler.handle(new Weather(20, 85, 10, 1013, 10)).isApto());

        ViabilityResult alto = handler.handle(new Weather(20, 85.1, 10, 1013, 10));
        assertFalse(alto.isApto());
        assertTrue(alto.getReason().contains("Humedad fuera de rango (<= 85 %)"));
    }
}
