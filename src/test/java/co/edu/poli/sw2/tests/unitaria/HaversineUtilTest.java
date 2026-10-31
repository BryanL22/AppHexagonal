package co.edu.poli.sw2.tests.unitaria;

import co.edu.poli.sw2.dominio.strategy.PuntoGeografico;
import co.edu.poli.sw2.infraestructura.util.HaversineUtil;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Pruebas unitarias para {@link HaversineUtil}.
 */
class HaversineUtilTest {

    private static final double DELTA = 0.1; // Margen de 100 metros

    @Test
    void distanciaMismoPuntoEsCero() {
        PuntoGeografico punto = new PuntoGeografico("Origen", 4.60971, -74.08175);
        double distancia = HaversineUtil.calcularDistanciaKm(punto, punto);

        assertEquals(0.0, distancia, 0.0001);
    }

    @Test
    void calculaDistanciaCorrectaEntreBogotaCentroYSuba() {
        // Coordenadas especificadas en el requerimiento del usuario
        PuntoGeografico origen = new PuntoGeografico("Punto Origen", 4.60971, -74.08175);
        PuntoGeografico destino = new PuntoGeografico("Punto Destino", 4.70159, -74.14690);

        double distancia = HaversineUtil.calcularDistanciaKm(origen, destino);

        // Distancia aproximada esperada ~12.51 km
        assertEquals(12.51, distancia, DELTA);
    }

    @Test
    void puntosNulosLanzanExcepcion() {
        PuntoGeografico punto = new PuntoGeografico("P1", 4.0, -74.0);
        assertThrows(IllegalArgumentException.class, () -> HaversineUtil.calcularDistanciaKm(null, punto));
        assertThrows(IllegalArgumentException.class, () -> HaversineUtil.calcularDistanciaKm(punto, null));
    }
}

