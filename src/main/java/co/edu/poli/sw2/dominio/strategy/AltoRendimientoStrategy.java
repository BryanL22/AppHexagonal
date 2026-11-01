package co.edu.poli.sw2.dominio.strategy;

/**
 * Estrategia de consumo en modo Alto Rendimiento.
 *
 * <p>Aplica un factor de consumo intensivo de 1.5 por cada kilometro recorrido.</p>
 */
public class AltoRendimientoStrategy implements ConsumoStrategy {

    private static final double FACTOR = 1.5;

    @Override
    public double calcularConsumo(double distanciaKm) {
        if (distanciaKm < 0) {
            throw new IllegalArgumentException("La distancia no puede ser negativa.");
        }
        return distanciaKm * FACTOR;
    }

    @Override
    public String getNombre() {
        return "Alto rendimiento";
    }

    @Override
    public String toString() {
        return getNombre() + " (x" + FACTOR + ")";
    }
}

