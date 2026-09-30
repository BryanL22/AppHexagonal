package co.edu.poli.sw2.dominio.modelo;

/**
 * Representa un sensor instalado en un dron (por ejemplo, camara, GPS o LIDAR).
 */
public class Sensor {

    /** Identificador unico del sensor. */
    private String id;
    /** Tipo de sensor (por ejemplo, camara, GPS o LIDAR). */
    private String tipo;
    /** Fabricante del sensor. */
    private String fabricante;

    /**
     * Crea un sensor sin datos.
     */
    public Sensor() {
    }

    /**
     * Crea un sensor con sus datos basicos.
     *
     * @param id identificador unico del sensor.
     * @param tipo tipo de sensor (por ejemplo, camara, GPS o LIDAR).
     * @param fabricante fabricante del sensor.
     */
    public Sensor(String id, String tipo, String fabricante) {
        this.id = id;
        this.tipo = tipo;
        this.fabricante = fabricante;
    }

    /**
     * Devuelve el identificador del sensor.
     *
     * @return el identificador del sensor.
     */
    public String getId() {
        return id;
    }

    /**
     * Asigna el identificador del sensor.
     *
     * @param id identificador a asignar al sensor.
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Devuelve el tipo de sensor.
     *
     * @return el tipo de sensor.
     */
    public String getTipo() {
        return tipo;
    }

    /**
     * Asigna el tipo de sensor.
     *
     * @param tipo tipo a asignar al sensor.
     */
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    /**
     * Devuelve el fabricante del sensor.
     *
     * @return el fabricante del sensor.
     */
    public String getFabricante() {
        return fabricante;
    }

    /**
     * Asigna el fabricante del sensor.
     *
     * @param fabricante fabricante a asignar al sensor.
     */
    public void setFabricante(String fabricante) {
        this.fabricante = fabricante;
    }

    /**
     * Devuelve una representacion en texto con todos los datos, util para depuracion.
     *
     * @return los atributos de este objeto en formato legible.
     */
    @Override
    public String toString() {
        return "Sensor{" +
                "id='" + id + '\'' +
                ", tipo='" + tipo + '\'' +
                ", fabricante='" + fabricante + '\'' +
                '}';
    }
}
