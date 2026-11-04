package co.edu.poli.sw2.tests.unitaria;

import co.edu.poli.sw2.dominio.modelo.Drone;
import co.edu.poli.sw2.dominio.state.DroneContext;
import co.edu.poli.sw2.dominio.state.FlyingState;
import co.edu.poli.sw2.dominio.state.LandingState;
import co.edu.poli.sw2.dominio.state.OffState;
import co.edu.poli.sw2.dominio.state.ReadyState;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Pruebas unitarias del patron State: {@link DroneContext} como Contexto y
 * los estados APAGADO, EN ESPERA, VOLANDO y ATERRIZANDO.
 */
class DroneStateTest {

    private final Drone drone = new Drone("D1", "SER-001", "ModeloX", "FabricanteX", 2.5);

    /** Contexto en estado APAGADO, como lo arma el servicio de aplicacion. */
    private DroneContext nuevoContexto() {
        return new DroneContext(drone, new OffState());
    }

    @Test
    void laEntidadDroneNoTieneEstado() {
        boolean tieneEstado = Arrays.stream(Drone.class.getDeclaredFields())
                .anyMatch(campo -> campo.getType().getPackageName().endsWith("dominio.state"));

        assertFalse(tieneEstado);
    }

    @Test
    void elContextoGuardaElDronYSuEstado() {
        DroneContext contexto = nuevoContexto();

        assertSame(drone, contexto.getDrone());
        assertInstanceOf(OffState.class, contexto.getEstado());
        assertEquals("APAGADO", contexto.getEstado().getNombre());
    }

    @Test
    void recorreElCicloCompletoDeTransicionesPermitidas() {
        DroneContext contexto = nuevoContexto();

        contexto.encender();
        assertInstanceOf(ReadyState.class, contexto.getEstado());
        assertEquals("EN ESPERA", contexto.getEstado().getNombre());

        contexto.despegar();
        assertInstanceOf(FlyingState.class, contexto.getEstado());
        assertEquals("VOLANDO", contexto.getEstado().getNombre());

        contexto.aterrizar();
        assertInstanceOf(LandingState.class, contexto.getEstado());
        assertEquals("ATERRIZANDO", contexto.getEstado().getNombre());

        contexto.finalizarAterrizaje();
        assertInstanceOf(ReadyState.class, contexto.getEstado());

        contexto.apagar();
        assertInstanceOf(OffState.class, contexto.getEstado());
    }

    @Test
    void apagadoSoloPermiteEncender() {
        DroneContext contexto = nuevoContexto();

        assertThrows(IllegalStateException.class, contexto::despegar);
        assertThrows(IllegalStateException.class, contexto::aterrizar);
        assertThrows(IllegalStateException.class, contexto::finalizarAterrizaje);
        assertThrows(IllegalStateException.class, contexto::apagar);
        assertInstanceOf(OffState.class, contexto.getEstado());
    }

    @Test
    void enEsperaNoPermiteEncenderAterrizarNiFinalizar() {
        DroneContext contexto = nuevoContexto();
        contexto.encender();

        assertThrows(IllegalStateException.class, contexto::encender);
        assertThrows(IllegalStateException.class, contexto::aterrizar);
        assertThrows(IllegalStateException.class, contexto::finalizarAterrizaje);
        assertInstanceOf(ReadyState.class, contexto.getEstado());
    }

    @Test
    void volandoSoloPermiteAterrizar() {
        DroneContext contexto = nuevoContexto();
        contexto.encender();
        contexto.despegar();

        assertThrows(IllegalStateException.class, contexto::encender);
        assertThrows(IllegalStateException.class, contexto::despegar);
        assertThrows(IllegalStateException.class, contexto::finalizarAterrizaje);
        assertThrows(IllegalStateException.class, contexto::apagar);
        assertInstanceOf(FlyingState.class, contexto.getEstado());
    }

    @Test
    void aterrizandoSoloPermiteFinalizarAterrizaje() {
        DroneContext contexto = nuevoContexto();
        contexto.encender();
        contexto.despegar();
        contexto.aterrizar();

        assertThrows(IllegalStateException.class, contexto::encender);
        assertThrows(IllegalStateException.class, contexto::despegar);
        assertThrows(IllegalStateException.class, contexto::aterrizar);
        assertThrows(IllegalStateException.class, contexto::apagar);
        assertInstanceOf(LandingState.class, contexto.getEstado());
    }

    @Test
    void elMensajeDeErrorIndicaLaAccionYElEstadoActual() {
        DroneContext contexto = nuevoContexto();

        IllegalStateException error = assertThrows(IllegalStateException.class, contexto::despegar);

        assertEquals("No se puede despegar en estado APAGADO.", error.getMessage());
    }
}
