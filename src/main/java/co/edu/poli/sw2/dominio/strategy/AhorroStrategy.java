package co.edu.poli.sw2.dominio.strategy;

/**
 * Estrategia de consumo en modo Ahorro de energia.
 *
 * <p>Aplica un factor de consumo de 0.5 por cada kilometro recorrido.</p>
 */
public class AhorroStrategy implements ConsumoStrategy {

    private static final double FACTOR = 0.5;

    @Override
    public double calcularConsumo(double distanciaKm) {
        if (distanciaKm < 0) {
            throw new IllegalArgumentException("La distancia no puede ser negativa.");
        }
        return distanciaKm * FACTOR;
    }

    @Override
    public String getNombre() {
        return "Ahorro";
    }

    @Override
    public String toString() {
        return getNombre() + " (x" + FACTOR + ")";
    }
}

