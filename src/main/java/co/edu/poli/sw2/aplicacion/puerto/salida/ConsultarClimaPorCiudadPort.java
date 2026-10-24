package co.edu.poli.sw2.aplicacion.puerto.salida;

import co.edu.poli.sw2.dominio.weather.Weather;

/** Puerto de salida para obtener el clima actual de una ciudad. */
public interface ConsultarClimaPorCiudadPort {

    /**
     * Obtiene el clima actual para la ciudad indicada.
     *
     * @param city nombre de la ciudad.
     * @return datos meteorologicos validados.
     * @throws WeatherQueryException si no existe la ciudad o falla la consulta.
     */
    Weather consultarClima(String city);
}