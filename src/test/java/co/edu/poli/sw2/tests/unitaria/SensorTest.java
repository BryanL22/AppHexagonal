package co.edu.poli.sw2.tests.unitaria;

import co.edu.poli.sw2.dominio.modelo.Sensor;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Pruebas unitarias de {@link Sensor}.
 */
class SensorTest {

    /** Verifica que el constructor con datos asigna todos los campos. */
    @Test
    void constructorConDatosAsignaTodosLosCampos() {
        Sensor sensor = new Sensor("S1", "Camara", "FabricanteX");

        assertEquals("S1", sensor.getId());
        assertEquals("Camara", sensor.getTipo());
        assertEquals("FabricanteX", sensor.getFabricante());
    }

    /** Verifica que todas las propiedades se pueden cambiar con sus setters. */
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

    /** Verifica que {@code toString} incluye el tipo de sensor. */
    @Test
    void toStringIncluyeElTipo() {
        Sensor sensor = new Sensor("S1", "Camara", "FabricanteX");

        assertTrue(sensor.toString().contains("Camara"));
    }
}
