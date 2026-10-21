package co.edu.poli.sw2.infraestructura.weather;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

/**
 * Cliente HTTP de la API de geocodificacion de Open-Meteo
 * ({@code geocoding-api.open-meteo.com/v1/search}): convierte el nombre de
 * una ciudad en coordenadas. No requiere clave de API.
 *
 * <p>Solo habla HTTP y JSON; no conoce el dominio. Los errores tecnicos
 * los traduce {@link GeoApiAdapter}.</p>
 */
public class GeoApiClient {

    /** URL de la API de geocodificacion. */
    private static final String URL = "https://geocoding-api.open-meteo.com/v1/search";

    /** Tiempo maximo de espera de cada peticion. */
    private static final Duration TIMEOUT = Duration.ofSeconds(10);

    /** Cliente HTTP compartido para las peticiones. */
    private final HttpClient http;

    /**
     * Crea el cliente con el cliente HTTP que usa.
     *
     * @param http cliente HTTP con el que se hacen las peticiones.
     */
    public GeoApiClient(HttpClient http) {
        this.http = http;
    }

    /**
     * Busca las coordenadas de una ciudad. Si hay varias con el mismo nombre,
     * devuelve la primera que entrega la API (la mas relevante).
     *
     * @param city nombre de la ciudad.
     * @return las coordenadas encontradas, o {@code null} si la ciudad no existe.
     * @throws IOException si la peticion falla o la API responde con error.
     * @throws InterruptedException si se interrumpe la espera de la respuesta.
     */
    public Coordinates getCoordinates(String city) throws IOException, InterruptedException {
        String url = URL
                + "?name=" + URLEncoder.encode(city, StandardCharsets.UTF_8)
                + "&count=1"
                + "&language=es"
                + "&format=json";

        HttpRequest peticion = HttpRequest.newBuilder(URI.create(url)).timeout(TIMEOUT).GET().build();
        HttpResponse<String> respuesta = http.send(peticion, HttpResponse.BodyHandlers.ofString());

        if (respuesta.statusCode() != 200) {
            throw new IOException("La API de geocodificacion respondio con codigo " + respuesta.statusCode() + ".");
        }

        // Cuando la ciudad no existe, la API no incluye el campo "results".
        JsonObject json = JsonParser.parseString(respuesta.body()).getAsJsonObject();
        if (!json.has("results")) {
            return null;
        }
        JsonArray resultados = json.getAsJsonArray("results");
        if (resultados.isEmpty()) {
            return null;
        }

        JsonObject lugar = resultados.get(0).getAsJsonObject();
        return new Coordinates(
                lugar.get("name").getAsString(),
                lugar.has("country") ? lugar.get("country").getAsString() : "",
                lugar.get("latitude").getAsDouble(),
                lugar.get("longitude").getAsDouble());
    }
}
