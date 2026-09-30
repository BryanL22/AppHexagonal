package co.edu.poli.sw2.dominio.modelo;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

class AgriculturaTest {

    @Test
    void esUnaEspecializacionDeDrone() {
        Agricultura agricultura = new Agricultura("A1", "SER-001", "ModeloX", "FabricanteX", 2.5, 10.0);

        assertInstanceOf(Drone.class, agricultura);
        assertEquals("A1", agricultura.getId());
        assertEquals("SER-001", agricultura.getSerial());
        assertEquals(10.0, agricultura.getCapacidadTanque());
    }

    @Test
    void laCapacidadDelTanqueEsModificable() {
        Agricultura agricultura = new Agricultura();

        agricultura.setCapacidadTanque(15.5);

        assertEquals(15.5, agricultura.getCapacidadTanque());
    }
}
