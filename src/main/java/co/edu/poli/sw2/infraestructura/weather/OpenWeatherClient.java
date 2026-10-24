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

/** Cliente HTTP del endpoint de pronostico de Open-Meteo. */
public class OpenWeatherClient {

    /** Endpoint oficial de clima de Open-Meteo. */
    static final String ENDPOINT = "https://api.open-meteo.com/v1/forecast";
    /** Cliente HTTP con limite de conexion. */
    HttpClient httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(10)).build();
    /** Conversor para respuestas JSON. */
    ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Crea el cliente meteorologico.
     */
    public OpenWeatherClient() {
    }

    /**
     * Consulta el clima usando coordenadas; Open-Meteo entrega temperatura Celsius y viento km/h.
     *
     * @param latitud latitud de la ciudad.
     * @param longitud longitud de la ciudad.
     * @return respuesta JSON del clima actual.
     */
    public JsonNode consultar(double latitud, double longitud) {
        String query = String.format(java.util.Locale.ROOT,
                "?latitude=%.5f&longitude=%.5f&current=temperature_2m,relative_humidity_2m,wind_speed_10m,pressure_msl,visibility&hourly=visibility&forecast_days=1&timezone=auto",
                latitud, longitud);
        HttpRequest request = HttpRequest.newBuilder(URI.create(ENDPOINT + query))
                .timeout(Duration.ofSeconds(15))
                .header("Accept", "application/json")
                .GET().build();
        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                throw new WeatherQueryException("Open-Meteo rechazo la consulta del clima (HTTP "
                        + response.statusCode() + ").");
            }
            if (response.body() == null || response.body().isBlank()) {
                throw new WeatherQueryException("Open-Meteo devolvio una respuesta vacia del clima.");
            }
            return objectMapper.readTree(response.body());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new WeatherQueryException("La consulta meteorologica fue interrumpida.", e);
        } catch (IOException | IllegalArgumentException e) {
            throw new WeatherQueryException("No se pudo consultar el clima en Open-Meteo.", e);
        }
    }
}