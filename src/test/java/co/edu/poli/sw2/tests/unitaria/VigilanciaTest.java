package co.edu.poli.sw2.tests.unitaria;

import co.edu.poli.sw2.dominio.modelo.Drone;
import co.edu.poli.sw2.dominio.modelo.Vigilancia;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Pruebas unitarias de {@link Vigilancia}.
 */
class VigilanciaTest {

    /** Verifica que hereda de {@link Drone} y que el constructor asigna los datos comunes y el propio. */
    @Test
    void esUnaEspecializacionDeDrone() {
        Vigilancia vigilancia = new Vigilancia("V1", "SER-001", "ModeloX", "FabricanteX", 2.5, true);

        assertInstanceOf(Drone.class, vigilancia);
        assertEquals("V1", vigilancia.getId());
        assertEquals("SER-001", vigilancia.getSerial());
        assertTrue(vigilancia.isDeteccionTermica());
    }

    /** Verifica que la deteccion termica se puede cambiar con su setter. */
    @Test
    void laDeteccionTermicaEsModificable() {
        Vigilancia vigilancia = new Vigilancia();

        vigilancia.setDeteccionTermica(false);

        assertFalse(vigilancia.isDeteccionTermica());
    }
}
