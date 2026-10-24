package co.edu.poli.sw2.tests.unitaria;

import co.edu.poli.sw2.aplicacion.puerto.salida.GeocodificarCiudadPort;
import co.edu.poli.sw2.dominio.weather.Weather;
import co.edu.poli.sw2.infraestructura.weather.OpenWeatherAdapter;
import co.edu.poli.sw2.infraestructura.weather.OpenWeatherClient;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class OpenWeatherAdapterTest {

    @Test
    void convierteVisibilidadDeMetrosAKilometrosDesdeCurrent() throws JsonProcessingException {
        String jsonTexto = """
            {
              "current": {
                "time": "2026-10-24T10:45",
                "temperature_2m": 22.4,
                "relative_humidity_2m": 58.0,
                "wind_speed_10m": 14.2,
                "pressure_msl": 1015.3,
                "visibility": 15400.0
              }
            }
            """;
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree(jsonTexto);

        OpenWeatherClient clienteMock = new OpenWeatherClient() {
            @Override
            public JsonNode consultar(double latitud, double longitud) {
                return jsonNode;
            }
        };

        GeocodificarCiudadPort geocodificadorMock = ciudad -> new GeocodificarCiudadPort.Coordenadas(4.60971, -74.08175);
        OpenWeatherAdapter adapter = new OpenWeatherAdapter(geocodificadorMock, clienteMock);

        Weather weather = adapter.consultarClima("Bogota");

        assertEquals(22.4, weather.temperature);
        assertEquals(58.0, weather.humidity);
        assertEquals(14.2, weather.windSpeed);
        assertEquals(1015.3, weather.pressure);
        // 15400 metros convertidos a kilometros -> 15.4 km
        assertEquals(15.4, weather.visibility);
    }

    @Test
    void convierteVisibilidadDeMetrosAKilometrosDesdeHourlySiNoEstaEnCurrent() throws JsonProcessingException {
        String jsonTexto = """
            {
              "current": {
                "time": "2026-10-24T10:45",
                "temperature_2m": 18.0,
                "relative_humidity_2m": 70.0,
                "wind_speed_10m": 8.0,
                "pressure_msl": 1016.0
              },
              "hourly": {
                "time": ["2026-10-24T09:00", "2026-10-24T10:00", "2026-10-24T11:00"],
                "visibility": [6000.0, 9200.0, 11000.0]
              }
            }
            """;
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree(jsonTexto);

        OpenWeatherClient clienteMock = new OpenWeatherClient() {
            @Override
            public JsonNode consultar(double latitud, double longitud) {
                return jsonNode;
            }
        };

        GeocodificarCiudadPort geocodificadorMock = ciudad -> new GeocodificarCiudadPort.Coordenadas(4.60971, -74.08175);
        OpenWeatherAdapter adapter = new OpenWeatherAdapter(geocodificadorMock, clienteMock);

        Weather weather = adapter.consultarClima("Bogota");

        assertEquals(18.0, weather.temperature);
        assertEquals(70.0, weather.humidity);
        assertEquals(8.0, weather.windSpeed);
        assertEquals(1016.0, weather.pressure);
        // Coincidencia con 10:00 -> 9200 metros -> 9.2 km
        assertEquals(9.2, weather.visibility);
    }
}
