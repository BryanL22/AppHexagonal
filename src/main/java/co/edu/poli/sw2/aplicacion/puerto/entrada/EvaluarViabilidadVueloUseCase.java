package co.edu.poli.sw2.aplicacion.puerto.entrada;

import co.edu.poli.sw2.dominio.weather.Weather;
import co.edu.poli.sw2.dominio.weather.chain.ViabilityResult;

/**
 * Puerto de entrada para evaluar la viabilidad de vuelo segun las condiciones meteorologicas.
 */
public interface EvaluarViabilidadVueloUseCase {

    /**
     * Evalua si las condiciones meteorologicas son aptas para la operacion de drones.
     *
     * @param weather datos meteorologicos a evaluar.
     * @return resultado de viabilidad que contiene si es apto y el motivo en caso contrario.
     */
    ViabilityResult evaluar(Weather weather);
}
