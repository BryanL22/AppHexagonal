package co.edu.poli.sw2.tests.unitaria;

import co.edu.poli.sw2.aplicacion.usecase.CalcularConsumoUseCase;
import co.edu.poli.sw2.dominio.strategy.AhorroStrategy;
import co.edu.poli.sw2.dominio.strategy.AltoRendimientoStrategy;
import co.edu.poli.sw2.dominio.strategy.NormalStrategy;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Pruebas unitarias para el caso de uso y contexto {@link CalcularConsumoUseCase}.
 */
class CalcularConsumoUseCaseTest {

    private static final double DELTA = 0.0001;

    @Test
    void calcularSinEstrategiaLanzaIllegalStateException() {
        CalcularConsumoUseCase useCase = new CalcularConsumoUseCase();
        assertThrows(IllegalStateException.class, () -> useCase.calcular(20.0));
    }

    @Test
    void permiteIntercambiarEstrategiasDinamicamenteSinCondicionales() {
        CalcularConsumoUseCase useCase = new CalcularConsumoUseCase();
        double distancia = 20.0;

        // 1. Probar con Ahorro (20 * 0.5 = 10)
        useCase.setEstrategia(new AhorroStrategy());
        assertEquals("Ahorro", useCase.getEstrategia().getNombre());
        assertEquals(10.0, useCase.calcular(distancia), DELTA);

        // 2. Intercambiar a Normal (20 * 1.0 = 20)
        useCase.setEstrategia(new NormalStrategy());
        assertEquals("Normal", useCase.getEstrategia().getNombre());
        assertEquals(20.0, useCase.calcular(distancia), DELTA);

        // 3. Intercambiar a Alto Rendimiento (20 * 1.5 = 30)
        useCase.setEstrategia(new AltoRendimientoStrategy());
        assertEquals("Alto rendimiento", useCase.getEstrategia().getNombre());
        assertEquals(30.0, useCase.calcular(distancia), DELTA);
    }

    @Test
    void constructorConEstrategiaInicializaCorrectamente() {
        CalcularConsumoUseCase useCase = new CalcularConsumoUseCase(new AhorroStrategy());
        assertNotNull(useCase.getEstrategia());
        assertEquals(5.0, useCase.calcular(10.0), DELTA);
    }

    @Test
    void calcularConEstrategiaDirectaAplicaCalculo() {
        CalcularConsumoUseCase useCase = new CalcularConsumoUseCase();
        double consumo = useCase.calcular(10.0, new AltoRendimientoStrategy());
        assertEquals(15.0, consumo, DELTA);
        assertEquals("Alto rendimiento", useCase.getEstrategia().getNombre());
    }

    @Test
    void distanciaNegativaLanzaExcepcion() {
        CalcularConsumoUseCase useCase = new CalcularConsumoUseCase(new NormalStrategy());
        assertThrows(IllegalArgumentException.class, () -> useCase.calcular(-10.0));
    }
}

