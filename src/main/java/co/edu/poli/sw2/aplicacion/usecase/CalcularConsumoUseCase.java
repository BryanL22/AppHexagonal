package co.edu.poli.sw2.aplicacion.usecase;

import co.edu.poli.sw2.dominio.strategy.ConsumoStrategy;

/**
 * Caso de uso y Contexto del patron Strategy para coordinar el calculo
 * del consumo energetico de una ruta de vuelo.
 *
 * <p>Mantiene una referencia a la estrategia activa ({@link ConsumoStrategy})
 * permitiendo intercambiarla en tiempo de ejecucion sin necesidad de estructuras
 * condicionales (if/switch) para seleccionar o ejecutar cada algoritmo.</p>
 */
public class CalcularConsumoUseCase {

    private ConsumoStrategy estrategia;

    /**
     * Crea el caso de uso sin una estrategia inicial asignada.
     */
    public CalcularConsumoUseCase() {
    }

    /**
     * Crea el caso de uso configurando una estrategia inicial.
     *
     * @param estrategia estrategia concreta de consumo a aplicar.
     */
    public CalcularConsumoUseCase(ConsumoStrategy estrategia) {
        this.estrategia = estrategia;
    }

    /**
     * Asigna o intercambia la estrategia de calculo en tiempo de ejecucion.
     *
     * @param estrategia nueva estrategia de consumo.
     */
    public void setEstrategia(ConsumoStrategy estrategia) {
        this.estrategia = estrategia;
    }

    /**
     * Obtiene la estrategia actualmente configurada.
     *
     * @return estrategia actual o {@code null} si no se ha asignado.
     */
    public ConsumoStrategy getEstrategia() {
        return estrategia;
    }

    /**
     * Calcula el consumo energetico delegando directamente en la estrategia configurada.
     *
     * @param distanciaKm distancia del trayecto en kilometros.
     * @return consumo energetico estimado.
     * @throws IllegalStateException si no se ha seleccionado ninguna estrategia.
     * @throws IllegalArgumentException si la distancia es negativa.
     */
    public double calcular(double distanciaKm) {
        if (estrategia == null) {
            throw new IllegalStateException("No se ha seleccionado ninguna estrategia de consumo.");
        }
        if (distanciaKm < 0) {
            throw new IllegalArgumentException("La distancia no puede ser negativa.");
        }
        return estrategia.calcularConsumo(distanciaKm);
    }

    /**
     * Asigna la estrategia y ejecuta el calculo de consumo en una sola llamada.
     *
     * @param distanciaKm distancia del trayecto en kilometros.
     * @param estrategia estrategia concreta a aplicar.
     * @return consumo energetico estimado.
     */
    public double calcular(double distanciaKm, ConsumoStrategy estrategia) {
        setEstrategia(estrategia);
        return calcular(distanciaKm);
    }
}

