package co.edu.poli.sw2.tests.unitaria;

import co.edu.poli.sw2.aplicacion.mediator.DroneMediator;
import co.edu.poli.sw2.aplicacion.mediator.MissionCoordinator;
import co.edu.poli.sw2.aplicacion.mediator.components.BatterySystem;
import co.edu.poli.sw2.aplicacion.mediator.components.CommunicationSystem;
import co.edu.poli.sw2.aplicacion.mediator.components.NavigationSystem;
import co.edu.poli.sw2.aplicacion.mediator.components.SensorSystem;
import co.edu.poli.sw2.aplicacion.servicio.MissionService;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Pruebas unitarias del patron Mediator: {@link MissionCoordinator} como
 * torre de control de la operacion de vigilancia y {@link MissionService}
 * como servicio de aplicacion.
 */
class MissionMediatorTest {

    private final MissionCoordinator coordinador = new MissionCoordinator(
            new NavigationSystem(), new SensorSystem(), new BatterySystem(), new CommunicationSystem());
    private final MissionService servicio = new MissionService(coordinador);

    @Test
    void conBateriaSuficienteRecorreLaSecuenciaCompleta() {
        List<String> bitacora = servicio.ejecutarMision("D1", 80);

        assertEquals(List.of(
                "Coordinador: iniciando operacion de vigilancia del dron D1.",
                "Navegacion: preparando la ruta...",
                "Navegacion: ruta lista.",
                "Sensores: iniciando los sensores...",
                "Sensores: sensores activos.",
                "Bateria: verificando disponibilidad energetica (80%)...",
                "Bateria: energia suficiente.",
                "Comunicacion: estableciendo enlace...",
                "Comunicacion: enlace establecido.",
                "Coordinador: mision iniciada."), bitacora);
    }

    @Test
    void conBateriaBajaAbortaSinEstablecerEnlace() {
        List<String> bitacora = servicio.ejecutarMision("D1", 29);

        assertEquals("Coordinador: mision abortada.", bitacora.get(bitacora.size() - 1));
        assertTrue(bitacora.contains("Bateria: energia insuficiente (minimo 30%)."));
        assertFalse(bitacora.stream().anyMatch(paso -> paso.startsWith("Comunicacion")));
    }

    @Test
    void elMinimoDeBateriaPermiteIniciar() {
        List<String> bitacora = servicio.ejecutarMision("D1", BatterySystem.NIVEL_MINIMO);

        assertEquals("Coordinador: mision iniciada.", bitacora.get(bitacora.size() - 1));
    }

    @Test
    void cadaMisionEmpiezaConLaBitacoraVacia() {
        servicio.ejecutarMision("D1", 10);
        List<String> bitacora = servicio.ejecutarMision("D2", 90);

        assertEquals("Coordinador: iniciando operacion de vigilancia del dron D2.", bitacora.get(0));
        assertFalse(bitacora.contains("Coordinador: mision abortada."));
    }

    @Test
    void losSistemasSoloConocenAlMediador() {
        for (Class<?> sistema : List.of(NavigationSystem.class, SensorSystem.class,
                BatterySystem.class, CommunicationSystem.class)) {
            for (Field campo : sistema.getDeclaredFields()) {
                if (!Modifier.isStatic(campo.getModifiers())) {
                    assertEquals(DroneMediator.class, campo.getType(), sistema.getSimpleName());
                }
            }
        }
    }

    @Test
    void unEventoDesconocidoSeRechaza() {
        assertThrows(IllegalArgumentException.class, () -> coordinador.notificar(this, "OTRO"));
    }

    @Test
    void elServicioValidaElIdYElNivelDeBateria() {
        assertThrows(IllegalArgumentException.class, () -> servicio.ejecutarMision(" ", 50));
        assertThrows(IllegalArgumentException.class, () -> servicio.ejecutarMision(null, 50));
        assertThrows(IllegalArgumentException.class, () -> servicio.ejecutarMision("D1", -1));
        assertThrows(IllegalArgumentException.class, () -> servicio.ejecutarMision("D1", 101));
    }
}
