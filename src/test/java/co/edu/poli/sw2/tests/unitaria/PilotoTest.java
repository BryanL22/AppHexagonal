package co.edu.poli.sw2.tests.unitaria;

import co.edu.poli.sw2.dominio.modelo.Piloto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Pruebas unitarias de {@link Piloto}.
 */
class PilotoTest {

    /** Verifica que el constructor con datos asigna todos los campos. */
    @Test
    void constructorConDatosAsignaTodosLosCampos() {
        Piloto piloto = new Piloto("P1", "Juan Perez", "LIC-001", "3000000000");

        assertEquals("P1", piloto.getId());
        assertEquals("Juan Perez", piloto.getNombre());
        assertEquals("LIC-001", piloto.getLicencia());
        assertEquals("3000000000", piloto.getTelefono());
    }

    /** Verifica que todas las propiedades se pueden cambiar con sus setters. */
    @Test
    void lasPropiedadesSonModificablesMedianteSetters() {
        Piloto piloto = new Piloto();

        piloto.setId("P2");
        piloto.setNombre("Maria Lopez");
        piloto.setLicencia("LIC-002");
        piloto.setTelefono("3000000001");

        assertEquals("P2", piloto.getId());
        assertEquals("Maria Lopez", piloto.getNombre());
        assertEquals("LIC-002", piloto.getLicencia());
        assertEquals("3000000001", piloto.getTelefono());
    }

    /** Verifica que {@code toString} incluye el nombre del piloto. */
    @Test
    void toStringIncluyeElNombre() {
        Piloto piloto = new Piloto("P1", "Juan Perez", "LIC-001", "3000000000");

        assertTrue(piloto.toString().contains("Juan Perez"));
    }
}
