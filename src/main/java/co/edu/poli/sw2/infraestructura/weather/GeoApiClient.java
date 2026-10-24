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

/** Cliente HTTP de la API de geocodificacion de Open-Meteo. */
public class GeoApiClient {

    /** Endpoint oficial de geocodificacion directa de Open-Meteo. */
    static final String ENDPOINT = "https://geocoding-api.open-meteo.com/v1/search";
    /** Cliente HTTP con limite de conexion. */
    HttpClient httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(10)).build();
    /** Conversor para respuestas JSON. */
    ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Crea el cliente de geocodificacion.
     */
    public GeoApiClient() {
    }

    /**
     * Busca coincidencias de una ciudad y devuelve el JSON de la API.
     *
     * @param city ciudad que se buscara.
     * @return respuesta JSON de geocodificacion.
     */
    public JsonNode buscar(String city) {
        String query = "?name=" + codificar(city.trim()) + "&count=1&language=es&format=json";
        return solicitar(ENDPOINT + query);
    }

    /** Ejecuta una peticion GET y valida el estado y cuerpo HTTP.
     *
     * @param url URL completa de la peticion.
     * @return cuerpo JSON parseado.
     */
    private JsonNode solicitar(String url) {
        HttpRequest request = HttpRequest.newBuilder(URI.create(url))
                .timeout(Duration.ofSeconds(15))
                .header("Accept", "application/json")
                .GET().build();
        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                throw new WeatherQueryException("Open-Meteo rechazo la geocodificacion (HTTP "
                        + response.statusCode() + ").");
            }
            if (response.body() == null || response.body().isBlank()) {
                throw new WeatherQueryException("Open-Meteo devolvio una respuesta vacia de geocodificacion.");
            }
            return objectMapper.readTree(response.body());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new WeatherQueryException("La consulta de geocodificacion fue interrumpida.", e);
        } catch (IOException | IllegalArgumentException e) {
            throw new WeatherQueryException("No se pudo consultar la geocodificacion de Open-Meteo.", e);
        }
    }

    /** Codifica un parametro de consulta para una URL.
     *
     * @param value valor a codificar.
     * @return valor codificado en UTF-8.
     */
    private String codificar(String value) {
        return URLEncoder.encode(value, StandardCharsets.UTF_8);
    }
}