package co.edu.poli.sw2.tests.unitaria;

import co.edu.poli.sw2.dominio.weather.Weather;
import co.edu.poli.sw2.dominio.weather.chain.PressureHandler;
import co.edu.poli.sw2.dominio.weather.chain.ViabilityResult;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PressureHandlerTest {

    @Test
    void presionValidaDentroDeRango() {
        PressureHandler handler = new PressureHandler();
        Weather weather = new Weather(20, 50, 10, 1013.2, 10);

        ViabilityResult result = handler.handle(weather);

        assertTrue(result.isApto());
        assertNull(result.getReason());
    }

    @Test
    void presionInvalidaFueraDeRango() {
        PressureHandler handler = new PressureHandler();
        Weather weather = new Weather(20, 50, 10, 930.0, 10);

        ViabilityResult result = handler.handle(weather);

        assertFalse(result.isApto());
        assertTrue(result.getReason().contains("Presion fuera de rango (950 a 1050 hPa)"));
        assertTrue(result.getReason().contains("930.0"));
    }

    @Test
    void limitesPresion() {
        PressureHandler handler = new PressureHandler();

        assertTrue(handler.handle(new Weather(20, 50, 10, 950, 10)).isApto());
        assertTrue(handler.handle(new Weather(20, 50, 10, 1050, 10)).isApto());

        ViabilityResult bajo = handler.handle(new Weather(20, 50, 10, 949.9, 10));
        assertFalse(bajo.isApto());
        assertTrue(bajo.getReason().contains("Presion fuera de rango (950 a 1050 hPa)"));

        ViabilityResult alto = handler.handle(new Weather(20, 50, 10, 1050.1, 10));
        assertFalse(alto.isApto());
        assertTrue(alto.getReason().contains("Presion fuera de rango (950 a 1050 hPa)"));
    }
}
