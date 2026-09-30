package co.edu.poli.sw2.tests.unitaria;

import co.edu.poli.sw2.dominio.modelo.Agricultura;
import co.edu.poli.sw2.dominio.modelo.Drone;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

/**
 * Pruebas unitarias de {@link Agricultura}.
 */
class AgriculturaTest {

    /** Verifica que hereda de {@link Drone} y que el constructor asigna los datos comunes y el propio. */
    @Test
    void esUnaEspecializacionDeDrone() {
        Agricultura agricultura = new Agricultura("A1", "SER-001", "ModeloX", "FabricanteX", 2.5, 10.0);

        assertInstanceOf(Drone.class, agricultura);
        assertEquals("A1", agricultura.getId());
        assertEquals("SER-001", agricultura.getSerial());
        assertEquals(10.0, agricultura.getCapacidadTanque());
    }

    /** Verifica que la capacidad del tanque se puede cambiar con su setter. */
    @Test
    void laCapacidadDelTanqueEsModificable() {
        Agricultura agricultura = new Agricultura();

        agricultura.setCapacidadTanque(15.5);

        assertEquals(15.5, agricultura.getCapacidadTanque());
    }
}
