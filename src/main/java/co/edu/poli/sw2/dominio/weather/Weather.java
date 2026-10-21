package co.edu.poli.sw2.dominio.weather;

/**
 * Condiciones climaticas de una ciudad en un instante dado. Es el contexto
 * que se notifica a los drones suscritos.
 *
 * <p>Es inmutable: los cinco datos se fijan al crearla. Las unidades ya
 * vienen normalizadas por el adaptador de la API, de modo que coinciden con
 * las reglas de vuelo del dominio.</p>
 */
public class Weather {

    /** Temperatura en grados Celsius. */
    public final double temperature;

    /** Humedad relativa en porcentaje (0 a 100). */
    public final int humidity;

    /** Velocidad del viento en km/h. */
    public final double windSpeed;

    /** Presion atmosferica en hPa. */
    public final int pressure;

    /** Visibilidad en metros. */
    public final int visibility;

    /**
     * Crea un registro de clima con sus cinco datos.
     *
     * @param temperature temperatura en grados Celsius.
     * @param humidity humedad relativa en porcentaje.
     * @param windSpeed velocidad del viento en km/h.
     * @param pressure presion atmosferica en hPa.
     * @param visibility visibilidad en metros.
     */
    public Weather(double temperature, int humidity, double windSpeed, int pressure, int visibility) {
        this.temperature = temperature;
        this.humidity = humidity;
        this.windSpeed = windSpeed;
        this.pressure = pressure;
        this.visibility = visibility;
    }

    /**
     * Devuelve una representacion en texto con los cinco datos y sus unidades.
     *
     * @return los datos del clima en formato legible.
     */
    @Override
    public String toString() {
        return "Weather{" +
                "temperature=" + temperature + " C" +
                ", humidity=" + humidity + " %" +
                ", windSpeed=" + windSpeed + " km/h" +
                ", pressure=" + pressure + " hPa" +
                ", visibility=" + visibility + " m" +
                '}';
    }
}
