package co.edu.poli.sw2.aplicacion.puerto.entrada;

import co.edu.poli.sw2.dominio.weather.Weather;

/** Caso de uso para consultar el clima de una ciudad. */
public interface GetWeatherByCityUseCase {

    /**
     * Consulta el clima y notifica a los drones suscritos.
     *
     * @param city nombre de la ciudad.
     * @return datos meteorologicos actuales.
     */
    Weather consultar(String city);
}