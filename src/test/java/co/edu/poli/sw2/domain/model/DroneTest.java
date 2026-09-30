package co.edu.poli.sw2.domain.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DroneTest {

    @Test
    void constructorVacioInicializaSensoresYPilotoEnNulo() {
        Drone drone = new Drone();

        assertNull(drone.getPiloto());
        assertNotNull(drone.getSensores());
        assertTrue(drone.getSensores().isEmpty());
    }

    @Test
    void constructorConDatosAsignaTodosLosCampos() {
        Drone drone = new Drone("D1", "SER-001", "ModeloX", "FabricanteX", 2.5);

        assertEquals("D1", drone.getId());
        assertEquals("SER-001", drone.getSerial());
        assertEquals("ModeloX", drone.getModelo());
        assertEquals("FabricanteX", drone.getFabricante());
        assertEquals(2.5, drone.getPeso());
    }

    @Test
    void unDroneTieneUnSoloPilotoAsociado() {
        Drone drone = new Drone();
        Piloto piloto = new Piloto("P1", "Juan Perez", "LIC-001", "3000000000");

        drone.setPiloto(piloto);

        assertEquals(piloto, drone.getPiloto());
    }

    @Test
    void reemplazarElPilotoDescartaElAnterior() {
        Drone drone = new Drone();
        Piloto primero = new Piloto("P1", "Juan Perez", "LIC-001", "3000000000");
        Piloto segundo = new Piloto("P2", "Maria Lopez", "LIC-002", "3000000001");

        drone.setPiloto(primero);
        drone.setPiloto(segundo);

        assertEquals(segundo, drone.getPiloto());
    }

    @Test
    void toStringIncluyeElPiloto() {
        Drone drone = new Drone("D1", "SER-001", "ModeloX", "FabricanteX", 2.5);
        drone.setPiloto(new Piloto("P1", "Juan Perez", "LIC-001", "3000000000"));

        assertTrue(drone.toString().contains("Juan Perez"));
    }
}
