package co.edu.poli.sw2.domain.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SensorTest {

    @Test
    void constructorConDatosAsignaTodosLosCampos() {
        Sensor sensor = new Sensor("S1", "Camara", "FabricanteX");

        assertEquals("S1", sensor.getId());
        assertEquals("Camara", sensor.getTipo());
        assertEquals("FabricanteX", sensor.getFabricante());
    }

    @Test
    void lasPropiedadesSonModificablesMedianteSetters() {
        Sensor sensor = new Sensor();

        sensor.setId("S2");
        sensor.setTipo("GPS");
        sensor.setFabricante("FabricanteY");

        assertEquals("S2", sensor.getId());
        assertEquals("GPS", sensor.getTipo());
        assertEquals("FabricanteY", sensor.getFabricante());
    }

    @Test
    void toStringIncluyeElTipo() {
        Sensor sensor = new Sensor("S1", "Camara", "FabricanteX");

        assertTrue(sensor.toString().contains("Camara"));
    }
}
