package co.edu.poli.sw2.domain.model;

/**
 * Dron especializado en labores agricolas, con un tanque para insumos.
 */
public class Agricultura extends Drone {

    private double capacidadTanque;

    /**
     * Crea un dron de agricultura sin datos.
     */
    public Agricultura() {
        super();
    }

    /**
     * Crea un dron de agricultura con sus datos basicos y su capacidad de tanque.
     *
     * @param id identificador unico asignado manualmente.
     * @param serial numero de serie del dron.
     * @param modelo modelo del dron.
     * @param fabricante fabricante del dron.
     * @param peso peso del dron en kilogramos.
     * @param capacidadTanque capacidad del tanque de insumos en litros.
     */
    public Agricultura(String id, String serial, String modelo, String fabricante, double peso,
                        double capacidadTanque) {
        super(id, serial, modelo, fabricante, peso);
        this.capacidadTanque = capacidadTanque;
    }

    /**
     * Devuelve la capacidad del tanque de insumos.
     *
     * @return la capacidad del tanque de insumos en litros.
     */
    public double getCapacidadTanque() {
        return capacidadTanque;
    }

    /**
     * Asigna la capacidad del tanque de insumos.
     *
     * @param capacidadTanque capacidad del tanque en litros a asignar.
     */
    public void setCapacidadTanque(double capacidadTanque) {
        this.capacidadTanque = capacidadTanque;
    }

    /**
     * Crea una copia de este dron de agricultura (patron Prototype), incluyendo
     * su capacidad de tanque. El objeto devuelto tiene una identidad (referencia
     * de memoria) distinta a la de {@code this}.
     *
     * @return una copia independiente de este dron de agricultura.
     */
    @Override
    public Agricultura clone() {
        return (Agricultura) super.clone();
    }

    @Override
    public String toString() {
        return "Agricultura{" +
                "capacidadTanque=" + capacidadTanque +
                "} " + super.toString();
    }
}
