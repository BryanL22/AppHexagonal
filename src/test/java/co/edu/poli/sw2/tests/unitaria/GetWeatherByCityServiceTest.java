package co.edu.poli.sw2.tests.unitaria;

import co.edu.poli.sw2.aplicacion.puerto.salida.ConsultarClimaPorCiudadPort;
import co.edu.poli.sw2.aplicacion.puerto.salida.WeatherQueryException;
import co.edu.poli.sw2.aplicacion.servicio.GetWeatherByCityService;
import co.edu.poli.sw2.aplicacion.servicio.WeatherNotificationService;
import co.edu.poli.sw2.dominio.modelo.Drone;
import co.edu.poli.sw2.dominio.weather.Weather;
import co.edu.poli.sw2.dominio.weather.WeatherObserver;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class GetWeatherByCityServiceTest {

    @Test
    void administraSuscripcionesPorIdSinDuplicar() {
        Drone drone = new Drone();
        drone.setId("D-17");
        WeatherNotificationService suscripciones = new WeatherNotificationService(
                new WeatherObserver(), id -> "D-17".equals(id) ? drone : null);

        suscripciones.suscribir("D-17");
        suscripciones.suscribir("D-17");
        assertEquals(List.of("D-17"), suscripciones.listarSuscritos());

        suscripciones.desuscribir("D-17");
        assertEquals(List.of(), suscripciones.listarSuscritos());
    }

    @Test
    void noSuscribeUnIdInexistente() {
        WeatherNotificationService suscripciones = new WeatherNotificationService(
                new WeatherObserver(), id -> null);

        assertThrows(IllegalArgumentException.class, () -> suscripciones.suscribir("inexistente"));
        assertEquals(List.of(), suscripciones.listarSuscritos());
    }

    @Test
    void fallaLaConsultaYNoNotificaSuscriptores() {
        WeatherObserver observer = new WeatherObserver();
        DroneContador drone = new DroneContador();
        observer.suscribir(drone);
        ConsultarClimaPorCiudadPort puerto = city -> {
            throw new WeatherQueryException("API no disponible");
        };
        GetWeatherByCityService service = new GetWeatherByCityService(
                puerto, new WeatherNotificationService(observer, droneId -> drone));

        assertThrows(WeatherQueryException.class, () -> service.consultar("Bogota"));
        assertEquals(0, drone.notificaciones);
    }

    private static class DroneContador extends Drone {
        int notificaciones;

        DroneContador() {
            setId("test-drone");
        }

        @Override
        public void recibirActualizacionMeteorologica(Weather weather) {
            super.recibirActualizacionMeteorologica(weather);
            notificaciones++;
        }
    }
}