package co.edu.poli.sw2.infraestructura.weather;

/**
 * Coordenadas geograficas de una ciudad, tal como las devuelve la API de
 * geocodificacion de Open-Meteo.
 *
 * @param name nombre de la ciudad encontrada.
 * @param country nombre del pais (por ejemplo, "Colombia").
 * @param lat latitud en grados.
 * @param lon longitud en grados.
 */
public record Coordinates(String name, String country, double lat, double lon) {
}
