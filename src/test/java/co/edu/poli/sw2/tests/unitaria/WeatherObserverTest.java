package co.edu.poli.sw2.tests.unitaria;

import co.edu.poli.sw2.dominio.modelo.Drone;
import co.edu.poli.sw2.dominio.weather.Weather;
import co.edu.poli.sw2.dominio.weather.WeatherObserver;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

class WeatherObserverTest {

    @Test
    void suscribirNotificaUnaSolaVezConLosCincoDatos() {
        WeatherObserver observer = new WeatherObserver();
        DroneDePrueba drone = new DroneDePrueba();
        DroneDePrueba segundoDrone = new DroneDePrueba();
        Weather weather = new Weather(21.5, 68, 4.2, 1013, 10);

        observer.suscribir(drone);
        observer.suscribir(drone);
        observer.suscribir(segundoDrone);
        observer.notificar(weather);

        assertEquals(1, drone.notificaciones);
        assertEquals(1, segundoDrone.notificaciones);
        assertSame(weather, drone.ultimaActualizacion);
        assertEquals(21.5, drone.ultimaActualizacion.temperature);
        assertEquals(68, drone.ultimaActualizacion.humidity);
        assertEquals(4.2, drone.ultimaActualizacion.windSpeed);
        assertEquals(1013, drone.ultimaActualizacion.pressure);
        assertEquals(10, drone.ultimaActualizacion.visibility);
    }

    @Test
    void desuscribirEvitaNotificacionesPosteriores() {
        WeatherObserver observer = new WeatherObserver();
        DroneDePrueba drone = new DroneDePrueba();
        observer.suscribir(drone);
        observer.desuscribir(drone.getId());

        observer.notificar(new Weather(20, 50, 3, 1000, 9));

        assertEquals(0, drone.notificaciones);
        assertNull(drone.ultimaActualizacion);
    }

    @Test
    void dronNoSuscritoNoRecibeActualizacion() {
        WeatherObserver observer = new WeatherObserver();
        DroneDePrueba suscrito = new DroneDePrueba();
        DroneDePrueba drone = new DroneDePrueba();
        observer.suscribir(suscrito);

        observer.notificar(new Weather(20, 50, 3, 1000, 9));

        assertEquals(1, suscrito.notificaciones);
        assertEquals(0, drone.notificaciones);
        assertNull(drone.ultimaActualizacion);
    }

    private static class DroneDePrueba extends Drone {
        int notificaciones;
        Weather ultimaActualizacion;

        DroneDePrueba() {
            setId("test-" + System.identityHashCode(this));
        }

        @Override
        public void recibirActualizacionMeteorologica(Weather weather) {
            super.recibirActualizacionMeteorologica(weather);
            notificaciones++;
            ultimaActualizacion = weather;
        }
    }
}