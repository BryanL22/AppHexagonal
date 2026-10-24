package co.edu.poli.sw2.tests.unitaria;

import co.edu.poli.sw2.dominio.weather.Weather;
import co.edu.poli.sw2.dominio.weather.chain.ViabilityResult;
import co.edu.poli.sw2.dominio.weather.chain.VisibilityHandler;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class VisibilityHandlerTest {

    @Test
    void visibilidadValidaDentroDeRango() {
        VisibilityHandler handler = new VisibilityHandler();
        Weather weather = new Weather(20, 50, 10, 1013, 12.5);

        ViabilityResult result = handler.handle(weather);

        assertTrue(result.isApto());
        assertNull(result.getReason());
    }

    @Test
    void visibilidadInvalidaInferiorAlLimite() {
        VisibilityHandler handler = new VisibilityHandler();
        Weather weather = new Weather(20, 50, 10, 1013, 3.2);

        ViabilityResult result = handler.handle(weather);

        assertFalse(result.isApto());
        assertTrue(result.getReason().contains("Visibilidad fuera de rango (>= 5 km)"));
        assertTrue(result.getReason().contains("3.2"));
    }

    @Test
    void limitesVisibilidad() {
        VisibilityHandler handler = new VisibilityHandler();

        assertTrue(handler.handle(new Weather(20, 50, 10, 1013, 5)).isApto());

        ViabilityResult bajo = handler.handle(new Weather(20, 50, 10, 1013, 4.9));
        assertFalse(bajo.isApto());
        assertTrue(bajo.getReason().contains("Visibilidad fuera de rango (>= 5 km)"));
    }
}
