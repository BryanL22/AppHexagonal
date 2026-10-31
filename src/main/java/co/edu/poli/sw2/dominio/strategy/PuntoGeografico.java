package co.edu.poli.sw2.dominio.strategy;

import java.util.Objects;

/**
 * Representa un punto geografico con nombre, latitud y longitud.
 *
 * <p>Forma parte del modelo de dominio para el calculo de rutas
 * y distancias geograficas.</p>
 */
public class PuntoGeografico {

    private String nombre;
    private double latitud;
    private double longitud;

    /**
     * Constructor por defecto.
     */
    public PuntoGeografico() {
    }

    /**
     * Crea un punto geografico con nombre y coordenadas.
     *
     * @param nombre nombre identificativo del punto.
     * @param latitud latitud en grados decimales (-90 a 90).
     * @param longitud longitud en grados decimales (-180 a 180).
     */
    public PuntoGeografico(String nombre, double latitud, double longitud) {
        validarCoordenadas(latitud, longitud);
        this.nombre = (nombre == null || nombre.isBlank()) ? "Punto" : nombre.trim();
        this.latitud = latitud;
        this.longitud = longitud;
    }

    private static void validarCoordenadas(double latitud, double longitud) {
        if (!Double.isFinite(latitud) || latitud < -90.0 || latitud > 90.0) {
            throw new IllegalArgumentException("La latitud debe estar en el rango [-90, 90].");
        }
        if (!Double.isFinite(longitud) || longitud < -180.0 || longitud > 180.0) {
            throw new IllegalArgumentException("La longitud debe estar en el rango [-180, 180].");
        }
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(double latitud) {
        validarCoordenadas(latitud, this.longitud);
        this.latitud = latitud;
    }

    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(double longitud) {
        validarCoordenadas(this.latitud, longitud);
        this.longitud = longitud;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PuntoGeografico that = (PuntoGeografico) o;
        return Double.compare(that.latitud, latitud) == 0
                && Double.compare(that.longitud, longitud) == 0
                && Objects.equals(nombre, that.nombre);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nombre, latitud, longitud);
    }

    @Override
    public String toString() {
        return nombre + " (" + latitud + ", " + longitud + ")";
    }
}

