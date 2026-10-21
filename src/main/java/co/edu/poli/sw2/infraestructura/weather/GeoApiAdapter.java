package co.edu.poli.sw2.infraestructura.weather;

import co.edu.poli.sw2.aplicacion.puerto.salida.ConsultaClimaException;

import java.io.IOException;

/**
 * Adaptador sobre {@link GeoApiClient}: valida la ciudad y traduce los
 * errores tecnicos de la API de geocodificacion a {@link ConsultaClimaException}.
 */
public class GeoApiAdapter {

    /** Cliente HTTP de la API de geocodificacion. */
    private final GeoApiClient client;

    /**
     * Crea el adaptador con el cliente que usa.
     *
     * @param client cliente HTTP de la API de geocodificacion.
     */
    public GeoApiAdapter(GeoApiClient client) {
        this.client = client;
    }

    /**
     * Obtiene las coordenadas de una ciudad.
     *
     * @param city nombre de la ciudad.
     * @return las coordenadas de la ciudad; nunca {@code null}.
     * @throws IllegalArgumentException si el nombre de la ciudad esta vacio.
     * @throws ConsultaClimaException si la ciudad no existe o la API falla.
     */
    public Coordinates getCoordinates(String city) {
        if (city == null || city.isBlank()) {
            throw new IllegalArgumentException("El nombre de la ciudad es obligatorio.");
        }

        try {
            Coordinates coordenadas = client.getCoordinates(city.trim());
            if (coordenadas == null) {
                throw new ConsultaClimaException("No se encontro la ciudad '" + city.trim() + "'.");
            }
            return coordenadas;
        } catch (IOException e) {
            throw new ConsultaClimaException("No se pudo consultar la ubicacion de la ciudad.", e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new ConsultaClimaException("Se interrumpio la consulta de la ubicacion.", e);
        }
    }
}
