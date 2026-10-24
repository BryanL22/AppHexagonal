package co.edu.poli.sw2.dominio.weather;

/**
 * Datos meteorologicos actuales. Temperatura en grados Celsius, viento en km/h,
 * humedad en porcentaje, presion en hPa y visibilidad en kilometros.
 */
public class Weather {

    /** Temperatura en grados Celsius. */
    public double temperature;
    /** Humedad relativa en porcentaje. */
    public double humidity;
    /** Velocidad del viento en kilometros por hora. */
    public double windSpeed;
    /** Presion atmosferica en hectopascales. */
    public double pressure;
    /** Visibilidad en kilometros. */
    public double visibility;

    /**
     * Crea los datos meteorologicos completos.
     *
     * @param temperature temperatura en grados Celsius.
     * @param humidity humedad relativa en porcentaje.
     * @param windSpeed velocidad del viento en kilometros por hora.
     * @param pressure presion atmosferica en hectopascales.
     * @param visibility visibilidad en kilometros.
     */
    public Weather(double temperature, double humidity, double windSpeed,
            double pressure, double visibility) {
        this.temperature = temperature;
        this.humidity = humidity;
        this.windSpeed = windSpeed;
        this.pressure = pressure;
        this.visibility = visibility;
    }

    /**
     * Valida los cinco datos antes de compartirlos con los suscriptores.
     *
     * @throws IllegalArgumentException si un valor no representa una medicion valida.
     */
    public void validar() {
        if (!Double.isFinite(temperature)
                || !Double.isFinite(humidity) || humidity < 0 || humidity > 100
                || !Double.isFinite(windSpeed) || windSpeed < 0
                || !Double.isFinite(pressure) || pressure <= 0
                || !Double.isFinite(visibility) || visibility < 0) {
            throw new IllegalArgumentException("Los datos meteorologicos no son validos.");
        }
    }
}