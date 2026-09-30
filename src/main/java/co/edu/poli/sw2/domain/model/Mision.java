package co.edu.poli.sw2.domain.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Representa una mision de vuelo en la que participan uno o mas drones.
 */
public class Mision {

    private String id;
    private String nombre;
    private String ubicacion;
    private String fecha;
    private List<Drone> drones;

    /**
     * Crea una mision sin datos, con la lista de drones vacia.
     */
    public Mision() {
        this.drones = new ArrayList<>();
    }

    /**
     * Crea una mision con sus datos basicos.
     *
     * @param id identificador unico de la mision.
     * @param nombre nombre de la mision.
     * @param ubicacion lugar donde se realiza la mision.
     * @param fecha fecha de la mision.
     */
    public Mision(String id, String nombre, String ubicacion, String fecha) {
        this();
        this.id = id;
        this.nombre = nombre;
        this.ubicacion = ubicacion;
        this.fecha = fecha;
    }

    /**
     * Devuelve el identificador de la mision.
     *
     * @return el identificador de la mision.
     */
    public String getId() {
        return id;
    }

    /**
     * Asigna el identificador de la mision.
     *
     * @param id identificador a asignar a la mision.
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Devuelve el nombre de la mision.
     *
     * @return el nombre de la mision.
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Asigna el nombre de la mision.
     *
     * @param nombre nombre a asignar a la mision.
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Devuelve el lugar donde se realiza la mision.
     *
     * @return el lugar donde se realiza la mision.
     */
    public String getUbicacion() {
        return ubicacion;
    }

    /**
     * Asigna el lugar donde se realiza la mision.
     *
     * @param ubicacion ubicacion a asignar a la mision.
     */
    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    /**
     * Devuelve la fecha de la mision.
     *
     * @return la fecha de la mision.
     */
    public String getFecha() {
        return fecha;
    }

    /**
     * Asigna la fecha de la mision.
     *
     * @param fecha fecha a asignar a la mision.
     */
    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    /**
     * Devuelve los drones que participan en la mision.
     *
     * @return la lista de drones que participan en la mision.
     */
    public List<Drone> getDrones() {
        return drones;
    }

    /**
     * Asigna los drones que participan en la mision.
     *
     * @param drones lista de drones a asignar a la mision.
     */
    public void setDrones(List<Drone> drones) {
        this.drones = drones;
    }

    @Override
    public String toString() {
        return "Mision{" +
                "id='" + id + '\'' +
                ", nombre='" + nombre + '\'' +
                ", ubicacion='" + ubicacion + '\'' +
                ", fecha='" + fecha + '\'' +
                ", drones=" + drones +
                '}';
    }
}
