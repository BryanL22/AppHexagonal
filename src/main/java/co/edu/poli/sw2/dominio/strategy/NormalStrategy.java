package co.edu.poli.sw2.dominio.strategy;

/**
 * Estrategia de consumo en modo Normal.
 *
 * <p>Aplica un factor de consumo estandar de 1.0 por cada kilometro recorrido.</p>
 */
public class NormalStrategy implements ConsumoStrategy {

    private static final double FACTOR = 1.0;

    @Override
    public double calcularConsumo(double distanciaKm) {
        if (distanciaKm < 0) {
            throw new IllegalArgumentException("La distancia no puede ser negativa.");
        }
        return distanciaKm * FACTOR;
    }

    @Override
    public String getNombre() {
        return "Normal";
    }

    @Override
    public String toString() {
        return getNombre() + " (x" + FACTOR + ")";
    }
}

