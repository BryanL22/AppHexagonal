package co.edu.poli.sw2.tests.unitaria;

import co.edu.poli.sw2.dominio.weather.Weather;
import co.edu.poli.sw2.dominio.weather.chain.TemperatureHandler;
import co.edu.poli.sw2.dominio.weather.chain.ViabilityResult;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TemperatureHandlerTest {

    @Test
    void temperaturaValidaDentroDeRango() {
        TemperatureHandler handler = new TemperatureHandler();
        Weather weather = new Weather(22.5, 50, 10, 1013, 10);

        ViabilityResult result = handler.handle(weather);

        assertTrue(result.isApto());
        assertNull(result.getReason());
    }

    @Test
    void temperaturaInvalidaFueraDeRango() {
        TemperatureHandler handler = new TemperatureHandler();
        Weather weather = new Weather(45.3, 50, 10, 1013, 10);

        ViabilityResult result = handler.handle(weather);

        assertFalse(result.isApto());
        assertTrue(result.getReason().contains("Temperatura fuera de rango (0 a 40 C)"));
        assertTrue(result.getReason().contains("45.3"));
    }

    @Test
    void limitesInclusivosTemperatura() {
        TemperatureHandler handler = new TemperatureHandler();

        assertTrue(handler.handle(new Weather(0, 50, 10, 1013, 10)).isApto());
        assertTrue(handler.handle(new Weather(40, 50, 10, 1013, 10)).isApto());

        ViabilityResult bajo = handler.handle(new Weather(-0.1, 50, 10, 1013, 10));
        assertFalse(bajo.isApto());
        assertTrue(bajo.getReason().contains("Temperatura fuera de rango (0 a 40 C)"));

        ViabilityResult alto = handler.handle(new Weather(40.1, 50, 10, 1013, 10));
        assertFalse(alto.isApto());
        assertTrue(alto.getReason().contains("Temperatura fuera de rango (0 a 40 C)"));
    }
}
