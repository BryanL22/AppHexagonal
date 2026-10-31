package co.edu.poli.sw2.tests.unitaria;

import co.edu.poli.sw2.dominio.strategy.AhorroStrategy;
import co.edu.poli.sw2.dominio.strategy.AltoRendimientoStrategy;
import co.edu.poli.sw2.dominio.strategy.ConsumoStrategy;
import co.edu.poli.sw2.dominio.strategy.NormalStrategy;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Pruebas unitarias para las implementaciones concretas de {@link ConsumoStrategy}.
 */
class ConsumoStrategyTest {

    private static final double DELTA = 0.0001;

    @Test
    void estrategiaAhorroAplicaFactorCeroPuntoCinco() {
        ConsumoStrategy estrategia = new AhorroStrategy();
        double distancia = 10.0;
        double esperado = 5.0; // 10.0 * 0.5

        assertEquals(esperado, estrategia.calcularConsumo(distancia), DELTA);
        assertEquals("Ahorro", estrategia.getNombre());
    }

    @Test
    void estrategiaNormalAplicaFactorUnoPuntoCero() {
        ConsumoStrategy estrategia = new NormalStrategy();
        double distancia = 10.0;
        double esperado = 10.0; // 10.0 * 1.0

        assertEquals(esperado, estrategia.calcularConsumo(distancia), DELTA);
        assertEquals("Normal", estrategia.getNombre());
    }

    @Test
    void estrategiaAltoRendimientoAplicaFactorUnoPuntoCinco() {
        ConsumoStrategy estrategia = new AltoRendimientoStrategy();
        double distancia = 10.0;
        double esperado = 15.0; // 10.0 * 1.5

        assertEquals(esperado, estrategia.calcularConsumo(distancia), DELTA);
        assertEquals("Alto rendimiento", estrategia.getNombre());
    }

    @Test
    void distanciaCeroRetornaConsumoCeroEnTodasLasEstrategias() {
        assertEquals(0.0, new AhorroStrategy().calcularConsumo(0.0), DELTA);
        assertEquals(0.0, new NormalStrategy().calcularConsumo(0.0), DELTA);
        assertEquals(0.0, new AltoRendimientoStrategy().calcularConsumo(0.0), DELTA);
    }

    @Test
    void distanciaNegativaLanzaExcepcion() {
        assertThrows(IllegalArgumentException.class, () -> new AhorroStrategy().calcularConsumo(-5.0));
        assertThrows(IllegalArgumentException.class, () -> new NormalStrategy().calcularConsumo(-5.0));
        assertThrows(IllegalArgumentException.class, () -> new AltoRendimientoStrategy().calcularConsumo(-5.0));
    }
}

