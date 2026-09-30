package co.edu.poli.sw2.dominio.modelo;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PilotoTest {

    @Test
    void constructorConDatosAsignaTodosLosCampos() {
        Piloto piloto = new Piloto("P1", "Juan Perez", "LIC-001", "3000000000");

        assertEquals("P1", piloto.getId());
        assertEquals("Juan Perez", piloto.getNombre());
        assertEquals("LIC-001", piloto.getLicencia());
        assertEquals("3000000000", piloto.getTelefono());
    }

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

    @Test
    void toStringIncluyeElNombre() {
        Piloto piloto = new Piloto("P1", "Juan Perez", "LIC-001", "3000000000");

        assertTrue(piloto.toString().contains("Juan Perez"));
    }
}
