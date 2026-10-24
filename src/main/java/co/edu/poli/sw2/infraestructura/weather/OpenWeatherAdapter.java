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
            JsonNode current = respuesta.get("current");
            if (current == null || !current.isObject()) {
                throw new IllegalArgumentException("La respuesta meteorologica no contiene datos actuales.");
            }
            double temperatura = numero(current, "temperature_2m");
            double humedad = numero(current, "relative_humidity_2m");
            double velocidadViento = numero(current, "wind_speed_10m");
            double presion = numero(current, "pressure_msl");
            double visibilidadMetros = extraerVisibilidad(respuesta, current);
            double visibilidadKm = visibilidadMetros / 1000.0;
            Weather weather = new Weather(temperatura, humedad, velocidadViento, presion, visibilidadKm);
            weather.validar();
            return weather;
        } catch (IllegalArgumentException e) {
            throw new WeatherQueryException("Open-Meteo devolvio datos meteorologicos invalidos.", e);
        }
    }

    /**
     * Obtiene la visibilidad en metros desde current o desde la serie horaria.
     *
     * @param respuesta objeto JSON completo.
     * @param current objeto JSON de las condiciones actuales.
     * @return visibilidad en metros.
     */
    private double extraerVisibilidad(JsonNode respuesta, JsonNode current) {
        JsonNode visNode = current.get("visibility");
        if (visNode != null && visNode.isNumber() && Double.isFinite(visNode.asDouble())) {
            return visNode.asDouble();
        }
        JsonNode hourly = respuesta.get("hourly");
        if (hourly != null && hourly.isObject()) {
            JsonNode timeArray = hourly.get("time");
            JsonNode visArray = hourly.get("visibility");
            if (timeArray != null && timeArray.isArray() && visArray != null && visArray.isArray()) {
                String horaBuscada = "";
                JsonNode currentTimeNode = current.get("time");
                if (currentTimeNode != null && currentTimeNode.isTextual()) {
                    String timeText = currentTimeNode.asText();
                    int indexT = timeText.indexOf('T');
                    if (indexT > 0 && timeText.length() >= indexT + 3) {
                        horaBuscada = timeText.substring(0, indexT + 3) + ":00";
                    }
                }
                int indice = -1;
                for (int i = 0; i < timeArray.size(); i++) {
                    if (timeArray.get(i).asText().equals(horaBuscada)) {
                        indice = i;
                        break;
                    }
                }
                if (indice < 0 && !visArray.isEmpty()) {
                    indice = 0;
                }
                if (indice >= 0 && indice < visArray.size()) {
                    JsonNode valor = visArray.get(indice);
                    if (valor != null && valor.isNumber() && Double.isFinite(valor.asDouble())) {
                        return valor.asDouble();
                    }
                }
            }
        }
        throw new IllegalArgumentException("No se encontro informacion valida de visibilidad.");
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