package co.edu.poli.sw2.domain.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;

class VigilanciaTest {

    @Test
    void esUnaEspecializacionDeDrone() {
        Vigilancia vigilancia = new Vigilancia("V1", "SER-001", "ModeloX", "FabricanteX", 2.5, true);

        assertInstanceOf(Drone.class, vigilancia);
        assertEquals("V1", vigilancia.getId());
        assertEquals("SER-001", vigilancia.getSerial());
        assertTrue(vigilancia.isDeteccionTermica());
    }

    @Test
    void laDeteccionTermicaEsModificable() {
        Vigilancia vigilancia = new Vigilancia();

        vigilancia.setDeteccionTermica(false);

        assertFalse(vigilancia.isDeteccionTermica());
    }
}
