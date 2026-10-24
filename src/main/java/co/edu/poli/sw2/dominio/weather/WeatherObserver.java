package co.edu.poli.sw2.dominio.weather;

import co.edu.poli.sw2.dominio.modelo.Drone;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Gestiona los drones suscritos y les entrega cada actualizacion meteorologica.
 */
public class WeatherObserver {

    /** Drones suscritos, indexados por ID para impedir duplicados. */
    Map<String, Drone> dronesSuscritos = new LinkedHashMap<>();

    /** Crea un administrador sin drones suscritos. */
    public WeatherObserver() {
    }

    /**
     * Suscribe un dron; repetir la misma suscripcion no duplica notificaciones.
     *
     * @param drone dron que recibira las actualizaciones.
     */
    public synchronized void suscribir(Drone drone) {
        exigirDron(drone);
        if (drone.getId() == null || drone.getId().isBlank()) {
            throw new IllegalArgumentException("El dron debe tener un ID.");
        }
        dronesSuscritos.putIfAbsent(drone.getId().trim(), drone);
    }

    /**
     * Elimina la suscripcion del dron indicado.
     *
    * @param droneId identificador del dron que dejara de recibir actualizaciones.
     */
    public synchronized void desuscribir(String droneId) {
        exigirId(droneId);
        dronesSuscritos.remove(droneId.trim());
    }

    /** Devuelve una copia de los identificadores suscritos.
     *
     * @return identificadores suscritos.
     */
    public synchronized List<String> listarIdsSuscritos() {
        return new ArrayList<>(dronesSuscritos.keySet());
    }

    /**
     * Entrega los datos completos a cada dron suscrito.
     *
     * @param weather datos meteorologicos para entregar.
     */
    public void notificar(Weather weather) {
        if (weather == null) {
            throw new IllegalArgumentException("Los datos meteorologicos son obligatorios.");
        }
        List<Drone> destinatarios;
        synchronized (this) {
            destinatarios = new ArrayList<>(dronesSuscritos.values());
        }
        for (Drone drone : destinatarios) {
            drone.recibirActualizacionMeteorologica(weather);
        }
    }

    /** Rechaza suscripciones nulas.
     *
     * @param drone dron que se validara.
     */
    private void exigirDron(Drone drone) {
        if (drone == null) {
            throw new IllegalArgumentException("El dron es obligatorio.");
        }
    }

    /** Rechaza un identificador vacio.
     *
     * @param droneId identificador que se validara.
     */
    private void exigirId(String droneId) {
        if (droneId == null || droneId.isBlank()) {
            throw new IllegalArgumentException("El ID del dron es obligatorio.");
        }
    }
}