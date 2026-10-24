package co.edu.poli.sw2.aplicacion.puerto.salida;

/** Puerto de salida para resolver el nombre de una ciudad a coordenadas. */
public interface GeocodificarCiudadPort {

    /**
     * Busca las coordenadas de una ciudad.
     *
     * @param city nombre de la ciudad.
     * @return coordenadas de la primera coincidencia.
     * @throws WeatherQueryException si no se encuentra la ciudad o falla la consulta.
     */
    Coordenadas buscarCoordenadas(String city);

    /** Coordenadas geograficas de una ciudad.
     *
     * @param latitud latitud en grados decimales.
     * @param longitud longitud en grados decimales.
     */
    record Coordenadas(double latitud, double longitud) {
    }
}