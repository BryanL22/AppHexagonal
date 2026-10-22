package co.edu.poli.sw2.aplicacion.servicio;

import co.edu.poli.sw2.aplicacion.puerto.entrada.GetWeatherByCityUseCase;
import co.edu.poli.sw2.aplicacion.puerto.salida.ConsultarClimaPorCiudadPort;
import co.edu.poli.sw2.aplicacion.puerto.salida.WeatherQueryException;
import co.edu.poli.sw2.dominio.weather.Weather;

/** Ejecuta la consulta meteorologica y notifica solo resultados validos. */
public class GetWeatherByCityService implements GetWeatherByCityUseCase {

    /** Puerto para consultar el clima sin depender del proveedor. */
    ConsultarClimaPorCiudadPort consultarClima;
    /** Servicio que publica los datos validados a los suscriptores. */
    WeatherNotificationService notificaciones;

    /**
     * Crea el caso de uso con el puerto de consulta y el servicio de notificacion.
     *
     * @param consultarClima puerto para consultar el clima.
     * @param notificaciones coordinador de suscripciones.
     */
    public GetWeatherByCityService(ConsultarClimaPorCiudadPort consultarClima,
            WeatherNotificationService notificaciones) {
        this.consultarClima = consultarClima;
        this.notificaciones = notificaciones;
    }

    /** {@inheritDoc} */
    @Override
    public Weather consultar(String city) {
        if (city == null || city.isBlank()) {
            throw new IllegalArgumentException("La ciudad es obligatoria.");
        }

        Weather weather;
        try {
            weather = consultarClima.consultarClima(city.trim());
            if (weather == null) {
                throw new WeatherQueryException("La consulta no devolvio datos meteorologicos.");
            }
            weather.validar();
        } catch (IllegalArgumentException e) {
            throw new WeatherQueryException("La respuesta meteorologica no es valida.", e);
        }

        notificaciones.notificarNuevosDatos(weather);
        return weather;
    }
}