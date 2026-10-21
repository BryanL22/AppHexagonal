package co.edu.poli.sw2.aplicacion.puerto.salida;

import co.edu.poli.sw2.dominio.weather.Weather;

/**
 * Puerto de salida (driven port) del hexagono: obtener el clima actual de
 * una ciudad desde un servicio externo.
 *
 * <p>Lo consume la aplicacion y lo implementa un adaptador de
 * infraestructura. La aplicacion no sabe que detras hay una API HTTP.</p>
 */
public interface WeatherPort {

    /**
     * Consulta el clima actual de una ciudad.
     *
     * @param city nombre de la ciudad (por ejemplo, "Bogota").
     * @return el clima actual de la ciudad.
     * @throws IllegalArgumentException si el nombre de la ciudad esta vacio.
     * @throws ConsultaClimaException si la ciudad no existe o el servicio
     *         externo no responde.
     */
    Weather getWeather(String city);
}
