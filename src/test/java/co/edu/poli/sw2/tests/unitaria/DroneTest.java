package co.edu.poli.sw2.tests.unitaria;

import co.edu.poli.sw2.dominio.modelo.Drone;
import co.edu.poli.sw2.dominio.modelo.Piloto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Pruebas unitarias de {@link Drone}.
 */
class DroneTest {

    /** Verifica que el constructor vacio deja el piloto en {@code null} y la lista de sensores vacia (no {@code null}). */
    @Test
    void constructorVacioInicializaSensoresYPilotoEnNulo() {
        Drone drone = new Drone();

        assertNull(drone.getPiloto());
        assertNotNull(drone.getSensores());
        assertTrue(drone.getSensores().isEmpty());
    }

    /** Verifica que el constructor con datos asigna todos los campos. */
    @Test
    void constructorConDatosAsignaTodosLosCampos() {
        Drone drone = new Drone("D1", "SER-001", "ModeloX", "FabricanteX", 2.5);

        assertEquals("D1", drone.getId());
        assertEquals("SER-001", drone.getSerial());
        assertEquals("ModeloX", drone.getModelo());
        assertEquals("FabricanteX", drone.getFabricante());
        assertEquals(2.5, drone.getPeso());
    }

    /** Verifica que se puede asignar un piloto al dron. */
    @Test
    void unDroneTieneUnSoloPilotoAsociado() {
        Drone drone = new Drone();
        Piloto piloto = new Piloto("P1", "Juan Perez", "LIC-001", "3000000000");

        drone.setPiloto(piloto);

        assertEquals(piloto, drone.getPiloto());
    }

    /** Verifica que asignar un segundo piloto reemplaza al primero (relacion 1 a 1). */
    @Test
    void reemplazarElPilotoDescartaElAnterior() {
        Drone drone = new Drone();
        Piloto primero = new Piloto("P1", "Juan Perez", "LIC-001", "3000000000");
        Piloto segundo = new Piloto("P2", "Maria Lopez", "LIC-002", "3000000001");

        drone.setPiloto(primero);
        drone.setPiloto(segundo);

        assertEquals(segundo, drone.getPiloto());
    }

    /** Verifica que {@code toString} incluye los datos del piloto. */
    @Test
    void toStringIncluyeElPiloto() {
        Drone drone = new Drone("D1", "SER-001", "ModeloX", "FabricanteX", 2.5);
        drone.setPiloto(new Piloto("P1", "Juan Perez", "LIC-001", "3000000000"));

        assertTrue(drone.toString().contains("Juan Perez"));
    }
}
