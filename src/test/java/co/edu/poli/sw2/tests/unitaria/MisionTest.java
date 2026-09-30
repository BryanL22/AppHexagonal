package co.edu.poli.sw2.tests.unitaria;

import co.edu.poli.sw2.dominio.modelo.Drone;
import co.edu.poli.sw2.dominio.modelo.Mision;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Pruebas unitarias de {@link Mision}.
 */
class MisionTest {

    /** Fecha de ejemplo usada en las pruebas. */
    private static final String FECHA = "2026-08-12";

    /** Verifica que el constructor asigna todos los campos y deja la lista de drones vacia. */
    @Test
    void constructorAsignaTodosLosCamposYDronesInicializaVacio() {
        Mision mision = new Mision("M1", "Inspeccion", "Bogota", FECHA);

        assertEquals("M1", mision.getId());
        assertEquals("Inspeccion", mision.getNombre());
        assertEquals("Bogota", mision.getUbicacion());
        assertEquals(FECHA, mision.getFecha());
        assertNotNull(mision.getDrones());
        assertTrue(mision.getDrones().isEmpty());
    }

    /** Verifica que una mision puede agrupar varios drones. */
    @Test
    void unaMisionAgrupaVariosDrones() {
        Mision mision = new Mision("M1", "Inspeccion", "Bogota", FECHA);
        Drone drone1 = new Drone("D1", "SER-001", "ModeloX", "FabricanteX", 2.5);
        Drone drone2 = new Drone("D2", "SER-002", "ModeloY", "FabricanteY", 3.1);

        mision.getDrones().add(drone1);
        mision.getDrones().add(drone2);

        assertEquals(2, mision.getDrones().size());
        assertTrue(mision.getDrones().containsAll(java.util.List.of(drone1, drone2)));
    }

    /** Verifica que {@code toString} incluye los drones de la mision. */
    @Test
    void toStringIncluyeLosDrones() {
        Mision mision = new Mision("M1", "Inspeccion", "Bogota", FECHA);
        mision.getDrones().add(new Drone("D1", "SER-001", "ModeloX", "FabricanteX", 2.5));

        assertTrue(mision.toString().contains("SER-001"));
    }
}
