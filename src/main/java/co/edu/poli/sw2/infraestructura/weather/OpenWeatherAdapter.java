package co.edu.poli.sw2.infraestructura.weather;

import co.edu.poli.sw2.aplicacion.puerto.salida.ConsultaClimaException;
import co.edu.poli.sw2.aplicacion.puerto.salida.WeatherPort;
import co.edu.poli.sw2.dominio.weather.Weather;

import java.io.IOException;

/**
 * Adaptador de salida: implementa {@link WeatherPort} con las APIs de
 * Open-Meteo.
 *
 * <p>Hace las dos consultas en el orden que pide el enunciado: primero la
 * de geocodificacion (ciudad a coordenadas, via {@link GeoApiAdapter}) y
 * luego la de clima actual (via {@link OpenWeatherClient}). Con la respuesta
 * construye la entidad {@link Weather}; las unidades de la API ya coinciden
 * con las del dominio, asi que no hay conversiones.</p>
 */
public class OpenWeatherAdapter implements WeatherPort {

    /** Adaptador de la API de geocodificacion. */
    private final GeoApiAdapter geoApiAdapter;

    /** Cliente HTTP de la API de clima actual. */
    private final OpenWeatherClient client;

    /**
     * Crea el adaptador con sus dependencias.
     *
     * @param geoApiAdapter adaptador que convierte la ciudad en coordenadas.
     * @param client cliente HTTP de la API de clima actual.
     */
    public OpenWeatherAdapter(GeoApiAdapter geoApiAdapter, OpenWeatherClient client) {
        this.geoApiAdapter = geoApiAdapter;
        this.client = client;
    }

    /**
     * Consulta el clima actual de una ciudad.
     *
     * @param city nombre de la ciudad.
     * @return el clima con las unidades del dominio (C, %, km/h, hPa, m).
     * @throws IllegalArgumentException si el nombre de la ciudad esta vacio.
     * @throws ConsultaClimaException si la ciudad no existe o alguna API falla.
     */
    @Override
    public Weather getWeather(String city) {
        Coordinates coordenadas = geoApiAdapter.getCoordinates(city);

        try {
            ApiResponse respuesta = client.getCurrentWeather(coordenadas.lat(), coordenadas.lon());
            return new Weather(
                    respuesta.temperature(),
                    respuesta.humidity(),
                    respuesta.windSpeed(),
                    respuesta.pressure(),
                    respuesta.visibility());
        } catch (IOException e) {
            throw new ConsultaClimaException("No se pudo consultar el clima de '" + coordenadas.name() + "'.", e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new ConsultaClimaException("Se interrumpio la consulta del clima.", e);
        }
    }
}
