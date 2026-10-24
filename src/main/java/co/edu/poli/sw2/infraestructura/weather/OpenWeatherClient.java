package co.edu.poli.sw2.infraestructura.weather;

import co.edu.poli.sw2.aplicacion.puerto.salida.WeatherQueryException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

/** Cliente HTTP del endpoint de clima actual de OpenWeather. */
public class OpenWeatherClient {

    /** Endpoint oficial de clima actual de OpenWeather. */
    static final String ENDPOINT = "https://api.openweathermap.org/data/2.5/weather";
    /** Clave recibida desde la configuracion de la aplicacion. */
    String apiKey;
    /** Cliente HTTP con limite de conexion. */
    HttpClient httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(10)).build();
    /** Conversor para respuestas JSON. */
    ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Crea el cliente con la clave configurada externamente.
     *
     * @param apiKey clave de OpenWeather.
     */
    public OpenWeatherClient(String apiKey) {
        this.apiKey = apiKey;
    }

    /**
     * Consulta el clima usando coordenadas; metric entrega temperatura Celsius y viento m/s.
     *
     * @param latitud latitud de la ciudad.
     * @param longitud longitud de la ciudad.
     * @return respuesta JSON del clima actual.
     */
    public JsonNode consultar(double latitud, double longitud) {
        if (apiKey == null || apiKey.isBlank()) {
            throw new WeatherQueryException("Configura la variable de entorno OPENWEATHER_API_KEY.");
        }
        String query = "?lat=" + latitud + "&lon=" + longitud + "&units=metric&appid="
                + URLEncoder.encode(apiKey, StandardCharsets.UTF_8);
        HttpRequest request = HttpRequest.newBuilder(URI.create(ENDPOINT + query))
                .timeout(Duration.ofSeconds(15))
                .header("Accept", "application/json")
                .GET().build();
        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                throw new WeatherQueryException("OpenWeather rechazo la consulta del clima (HTTP "
                        + response.statusCode() + ").");
            }
            if (response.body() == null || response.body().isBlank()) {
                throw new WeatherQueryException("OpenWeather devolvio una respuesta vacia del clima.");
            }
            return objectMapper.readTree(response.body());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new WeatherQueryException("La consulta meteorologica fue interrumpida.", e);
        } catch (IOException | IllegalArgumentException e) {
            throw new WeatherQueryException("No se pudo consultar el clima en OpenWeather.", e);
        }
    }
}