package co.edu.poli.sw2.domain.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MisionTest {

    private static final String FECHA = "2026-08-12";

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

    @Test
    void toStringIncluyeLosDrones() {
        Mision mision = new Mision("M1", "Inspeccion", "Bogota", FECHA);
        mision.getDrones().add(new Drone("D1", "SER-001", "ModeloX", "FabricanteX", 2.5));

        assertTrue(mision.toString().contains("SER-001"));
    }
}
