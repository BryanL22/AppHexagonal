package co.edu.poli.sw2.infraestructura.weather;

/**
 * Datos que entrega la API de clima actual de Open-Meteo para unas
 * coordenadas.
 *
 * <p>No es la entidad del dominio: {@link OpenWeatherAdapter} la convierte
 * en {@link co.edu.poli.sw2.dominio.weather.Weather}.</p>
 *
 * @param temperature temperatura a 2 m en grados Celsius.
 * @param humidity humedad relativa a 2 m en porcentaje.
 * @param windSpeed velocidad del viento a 10 m en km/h.
 * @param pressure presion atmosferica al nivel del mar en hPa (redondeada).
 * @param visibility visibilidad en metros (redondeada).
 */
public record ApiResponse(double temperature, int humidity, double windSpeed, int pressure, int visibility) {
}
