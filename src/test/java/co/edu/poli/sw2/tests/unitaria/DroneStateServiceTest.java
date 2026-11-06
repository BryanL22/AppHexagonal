package co.edu.poli.sw2.tests.unitaria;

import co.edu.poli.sw2.aplicacion.puerto.salida.BuscarDronPort;
import co.edu.poli.sw2.aplicacion.servicio.DroneStateService;
import co.edu.poli.sw2.dominio.modelo.Drone;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Pruebas unitarias de {@link DroneStateService}, con un puerto de busqueda
 * falso que, como el repositorio real, crea un {@link Drone} nuevo en cada
 * consulta.
 */
class DroneStateServiceTest {

    /** Identificadores de los drones que "existen" en el almacenamiento falso. */
    private final Set<String> existentes = new HashSet<>(Set.of("D1", "D2"));

    private final BuscarDronPort buscarDron =
            id -> existentes.contains(id) ? new Drone(id, "SER", "Modelo", "Fabricante", 2.0) : null;

    private final DroneStateService servicio = new DroneStateService(buscarDron);

    @Test
    void unDronSinOperacionesEstaApagado() {
        assertEquals("APAGADO", servicio.consultarEstado("D1"));
    }

    @Test
    void recuerdaElEstadoAunqueElRepositorioDevuelvaUnDronNuevo() {
        assertEquals("EN ESPERA", servicio.encender("D1"));
        assertEquals("VOLANDO", servicio.despegar("D1"));
        assertEquals("VOLANDO", servicio.consultarEstado("D1"));
        assertEquals("ATERRIZANDO", servicio.aterrizar("D1"));
        assertEquals("EN ESPERA", servicio.finalizarAterrizaje("D1"));
        assertEquals("APAGADO", servicio.apagar("D1"));
    }

    @Test
    void cadaDronTieneSuPropioEstado() {
        servicio.encender("D1");

        assertEquals("EN ESPERA", servicio.consultarEstado("D1"));
        assertEquals("APAGADO", servicio.consultarEstado("D2"));
    }

    @Test
    void unaTransicionNoPermitidaNoCambiaElEstado() {
        servicio.encender("D1");

        assertThrows(IllegalStateException.class, () -> servicio.aterrizar("D1"));
        assertEquals("EN ESPERA", servicio.consultarEstado("D1"));
    }

    @Test
    void rechazaUnIdVacioOInexistente() {
        assertThrows(IllegalArgumentException.class, () -> servicio.encender("  "));
        assertThrows(IllegalArgumentException.class, () -> servicio.encender(null));
        assertThrows(IllegalArgumentException.class, () -> servicio.consultarEstado("X9"));
    }

    @Test
    void unDronEliminadoYaNoConservaSuEstado() {
        servicio.encender("D1");
        existentes.remove("D1");

        assertThrows(IllegalArgumentException.class, () -> servicio.consultarEstado("D1"));

        existentes.add("D1");
        assertEquals("APAGADO", servicio.consultarEstado("D1"));
    }
}
