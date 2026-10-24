package co.edu.poli.sw2.infraestructura.weather;

import co.edu.poli.sw2.aplicacion.puerto.salida.GeocodificarCiudadPort;
import co.edu.poli.sw2.aplicacion.puerto.salida.WeatherQueryException;
import com.fasterxml.jackson.databind.JsonNode;

/** Adapta la respuesta de geocodificacion al puerto de coordenadas de aplicacion. */
public class GeoApiAdapter implements GeocodificarCiudadPort {

    /** Cliente que ejecuta la solicitud HTTP externa. */
    GeoApiClient client;

    /**
     * Crea el adaptador con el cliente HTTP de geocodificacion.
     *
     * @param client cliente de geocodificacion.
     */
    public GeoApiAdapter(GeoApiClient client) {
        this.client = client;
    }

    /** {@inheritDoc} */
    @Override
    public Coordenadas buscarCoordenadas(String city) {
        JsonNode respuesta = client.buscar(city);
        if (respuesta == null || !respuesta.has("results") || !respuesta.get("results").isArray()
                || respuesta.get("results").isEmpty()) {
            throw new WeatherQueryException("No se encontro la ciudad '" + city + "'.");
        }
        JsonNode primerResultado = respuesta.get("results").get(0);
        JsonNode latitud = primerResultado.get("latitude");
        JsonNode longitud = primerResultado.get("longitude");
        if (latitud == null || !latitud.isNumber() || longitud == null || !longitud.isNumber()
                || !Double.isFinite(latitud.asDouble()) || !Double.isFinite(longitud.asDouble())
                || latitud.asDouble() < -90 || latitud.asDouble() > 90
                || longitud.asDouble() < -180 || longitud.asDouble() > 180) {
            throw new WeatherQueryException("Open-Meteo devolvio coordenadas invalidas para '" + city + "'.");
        }
        return new Coordenadas(latitud.asDouble(), longitud.asDouble());
    }
}