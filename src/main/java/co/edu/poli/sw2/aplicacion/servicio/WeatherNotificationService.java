package co.edu.poli.sw2.aplicacion.servicio;

import co.edu.poli.sw2.aplicacion.puerto.entrada.GestionarSuscripcionesClimaUseCase;
import co.edu.poli.sw2.aplicacion.puerto.salida.BuscarDronPort;
import co.edu.poli.sw2.dominio.modelo.Drone;
import co.edu.poli.sw2.dominio.weather.Weather;
import co.edu.poli.sw2.dominio.weather.WeatherObserver;

import java.util.List;

/** Coordina las suscripciones y notificaciones meteorologicas de drones. */
public class WeatherNotificationService implements GestionarSuscripcionesClimaUseCase {

    /** Administrador unico de las suscripciones. */
    WeatherObserver observer;
    /** Puerto para resolver el ID a un dron registrado. */
    BuscarDronPort buscarDron;

    /**
     * Crea el servicio con el administrador de suscripciones del dominio.
     *
     * @param observer administrador de suscripciones.
     * @param buscarDron puerto de busqueda de drones.
     */
    public WeatherNotificationService(WeatherObserver observer, BuscarDronPort buscarDron) {
        this.observer = observer;
        this.buscarDron = buscarDron;
    }

    /**
     * Suscribe un dron a las actualizaciones meteorologicas.
     *
    * @param droneId identificador del dron que recibira las actualizaciones.
     */
    @Override
    public void suscribir(String droneId) {
        if (droneId == null || droneId.isBlank()) {
            throw new IllegalArgumentException("El ID del dron es obligatorio.");
        }
        Drone drone = buscarDron.buscarPorId(droneId.trim());
        if (drone == null) {
            throw new IllegalArgumentException("No existe un dron con el ID '" + droneId + "'.");
        }
        observer.suscribir(drone);
    }

    /**
     * Elimina la suscripcion de un dron.
     *
    * @param droneId identificador del dron que dejara de recibir actualizaciones.
     */
    @Override
    public void desuscribir(String droneId) {
        observer.desuscribir(droneId);
    }

    /** {@inheritDoc} */
    @Override
    public List<String> listarSuscritos() {
        return observer.listarIdsSuscritos();
    }

    /**
     * Notifica a los suscriptores con datos ya obtenidos y validados.
     *
     * @param weather datos meteorologicos que se entregaran.
     */
    public void notificarNuevosDatos(Weather weather) {
        observer.notificar(weather);
    }
}