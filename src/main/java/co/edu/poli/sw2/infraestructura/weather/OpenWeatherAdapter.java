package co.edu.poli.sw2.infraestructura.weather;

import co.edu.poli.sw2.aplicacion.puerto.salida.ConsultarClimaPorCiudadPort;
import co.edu.poli.sw2.aplicacion.puerto.salida.GeocodificarCiudadPort;
import co.edu.poli.sw2.aplicacion.puerto.salida.GeocodificarCiudadPort.Coordenadas;
import co.edu.poli.sw2.aplicacion.puerto.salida.WeatherQueryException;
import co.edu.poli.sw2.dominio.weather.Weather;
import com.fasterxml.jackson.databind.JsonNode;

/** Coordina geocodificacion, consulta del clima y conversion al modelo de dominio. */
public class OpenWeatherAdapter implements ConsultarClimaPorCiudadPort {

    /** Puerto que resuelve la ciudad a coordenadas. */
    GeocodificarCiudadPort geocodificador;
    /** Cliente del endpoint de clima actual. */
    OpenWeatherClient client;

    /**
     * Crea el adaptador con las dos dependencias externas necesarias.
     *
     * @param geocodificador puerto para resolver la ciudad.
     * @param client cliente del clima actual.
     */
    public OpenWeatherAdapter(GeocodificarCiudadPort geocodificador, OpenWeatherClient client) {
        this.geocodificador = geocodificador;
        this.client = client;
    }

    /** {@inheritDoc} */
    @Override
    public Weather consultarClima(String city) {
        Coordenadas coordenadas = geocodificador.buscarCoordenadas(city);
        JsonNode respuesta = client.consultar(coordenadas.latitud(), coordenadas.longitud());
        try {
            if (respuesta == null || !respuesta.isObject()) {
                throw new IllegalArgumentException("La respuesta meteorologica no es un objeto JSON.");
            }
            JsonNode main = respuesta.get("main");
            double temperatura = numero(main, "temp");
            double humedad = numero(main, "humidity");
            double presion = numero(main, "pressure");
            double velocidadViento = numero(respuesta.get("wind"), "speed");
            double visibilidad = numero(respuesta, "visibility");
            Weather weather = new Weather(temperatura, humedad, velocidadViento, presion, visibilidad);
            weather.validar();
            return weather;
        } catch (IllegalArgumentException e) {
            throw new WeatherQueryException("OpenWeather devolvio datos meteorologicos invalidos.", e);
        }
    }

    /** Extrae un numero obligatorio de una propiedad JSON.
     *
     * @param objeto objeto JSON que contiene el campo.
     * @param campo nombre del campo requerido.
     * @return valor numerico finito.
     */
    private double numero(JsonNode objeto, String campo) {
        JsonNode valor = objeto == null ? null : objeto.get(campo);
        if (valor == null || !valor.isNumber() || !Double.isFinite(valor.asDouble())) {
            throw new IllegalArgumentException("Campo meteorologico invalido: " + campo);
        }
        return valor.asDouble();
    }
}