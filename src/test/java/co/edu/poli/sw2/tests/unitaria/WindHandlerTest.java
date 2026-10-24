package co.edu.poli.sw2.tests.unitaria;

import co.edu.poli.sw2.dominio.weather.Weather;
import co.edu.poli.sw2.dominio.weather.chain.ViabilityResult;
import co.edu.poli.sw2.dominio.weather.chain.WindHandler;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class WindHandlerTest {

    @Test
    void vientoValidoDentroDeRango() {
        WindHandler handler = new WindHandler();
        Weather weather = new Weather(20, 50, 25.0, 1013, 10);

        ViabilityResult result = handler.handle(weather);

        assertTrue(result.isApto());
        assertNull(result.getReason());
    }

    @Test
    void vientoInvalidoSuperiorAlLimite() {
        WindHandler handler = new WindHandler();
        Weather weather = new Weather(20, 50, 48.2, 1013, 10);

        ViabilityResult result = handler.handle(weather);

        assertFalse(result.isApto());
        assertTrue(result.getReason().contains("Velocidad del viento fuera de rango (<= 40 km/h)"));
        assertTrue(result.getReason().contains("48.2"));
    }

    @Test
    void limitesViento() {
        WindHandler handler = new WindHandler();

        assertTrue(handler.handle(new Weather(20, 50, 40, 1013, 10)).isApto());

        ViabilityResult alto = handler.handle(new Weather(20, 50, 40.1, 1013, 10));
        assertFalse(alto.isApto());
        assertTrue(alto.getReason().contains("Velocidad del viento fuera de rango (<= 40 km/h)"));
    }
}
