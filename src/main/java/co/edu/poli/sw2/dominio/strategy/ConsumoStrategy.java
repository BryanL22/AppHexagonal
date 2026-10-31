package co.edu.poli.sw2.dominio.strategy;

/**
 * Interfaz que define el contrato del patron Strategy para el calculo
 * del consumo energetico estimado de un dron durante una ruta.
 */
public interface ConsumoStrategy {

    /**
     * Calcula el consumo energetico estimado a partir de la distancia recorrida en kilometros.
     *
     * @param distanciaKm distancia recorrida en kilometros (debe ser mayor o igual a 0).
     * @return consumo energetico estimado.
     */
    double calcularConsumo(double distanciaKm);

    /**
     * Devuelve el nombre descriptivo de la estrategia.
     *
     * @return nombre de la estrategia.
     */
    String getNombre();
}

