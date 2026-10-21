package co.edu.poli.sw2.infraestructura.weather;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

/**
 * Cliente HTTP de la API de clima actual de Open-Meteo
 * ({@code api.open-meteo.com/v1/forecast}): obtiene el clima de unas
 * coordenadas. No requiere clave de API.
 *
 * <p>Pide solo las cinco variables que usa el sistema en el bloque
 * {@code current}. Las unidades por defecto de la API ya son las del
 * dominio: grados Celsius, porcentaje, km/h, hPa y metros. Para la presion
 * se usa {@code pressure_msl} (reducida al nivel del mar), que es la que
 * informan los reportes del clima; la presion de superficie depende de la
 * altitud y en ciudades altas queda muy por debajo de 950 hPa.</p>
 */
public class OpenWeatherClient {

    /** URL de la API de clima actual. */
    private static final String URL = "https://api.open-meteo.com/v1/forecast";

    /** Variables pedidas en el bloque {@code current}. */
    private static final String VARIABLES =
            "temperature_2m,relative_humidity_2m,wind_speed_10m,pressure_msl,visibility";

    /** Tiempo maximo de espera de cada peticion. */
    private static final Duration TIMEOUT = Duration.ofSeconds(10);

    /** Cliente HTTP compartido para las peticiones. */
    private final HttpClient http;

    /**
     * Crea el cliente con el cliente HTTP que usa.
     *
     * @param http cliente HTTP con el que se hacen las peticiones.
     */
    public OpenWeatherClient(HttpClient http) {
        this.http = http;
    }

    /**
     * Consulta el clima actual en unas coordenadas.
     *
     * @param lat latitud en grados.
     * @param lon longitud en grados.
     * @return los datos de la API.
     * @throws IOException si la peticion falla, la API responde con error o
     *         la respuesta no trae alguno de los cinco datos.
     * @throws InterruptedException si se interrumpe la espera de la respuesta.
     */
    public ApiResponse getCurrentWeather(double lat, double lon) throws IOException, InterruptedException {
        String url = URL
                + "?latitude=" + lat
                + "&longitude=" + lon
                + "&current=" + VARIABLES;

        HttpRequest peticion = HttpRequest.newBuilder(URI.create(url)).timeout(TIMEOUT).GET().build();
        HttpResponse<String> respuesta = http.send(peticion, HttpResponse.BodyHandlers.ofString());

        if (respuesta.statusCode() != 200) {
            throw new IOException("La API de clima respondio con codigo " + respuesta.statusCode() + ".");
        }

        JsonObject json = JsonParser.parseString(respuesta.body()).getAsJsonObject();
        if (!json.has("current")) {
            throw new IOException("La respuesta de la API de clima no trae el bloque 'current'.");
        }
        JsonObject actual = json.getAsJsonObject("current");
        for (String variable : VARIABLES.split(",")) {
            if (!actual.has(variable) || actual.get(variable).isJsonNull()) {
                throw new IOException("La respuesta de la API de clima no trae el dato '" + variable + "'.");
            }
        }

        return new ApiResponse(
                actual.get("temperature_2m").getAsDouble(),
                actual.get("relative_humidity_2m").getAsInt(),
                actual.get("wind_speed_10m").getAsDouble(),
                (int) Math.round(actual.get("pressure_msl").getAsDouble()),
                (int) Math.round(actual.get("visibility").getAsDouble()));
    }
}
