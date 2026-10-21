/**
 * Adaptador de salida hacia las APIs de Open-Meteo (no requieren clave).
 *
 * <p>{@link co.edu.poli.sw2.infraestructura.weather.OpenWeatherAdapter}
 * implementa el puerto {@link co.edu.poli.sw2.aplicacion.puerto.salida.WeatherPort}.
 * Primero consulta la geocodificacion
 * ({@link co.edu.poli.sw2.infraestructura.weather.GeoApiAdapter} y
 * {@link co.edu.poli.sw2.infraestructura.weather.GeoApiClient}) y luego el
 * clima actual ({@link co.edu.poli.sw2.infraestructura.weather.OpenWeatherClient}).</p>
 */
package co.edu.poli.sw2.infraestructura.weather;
