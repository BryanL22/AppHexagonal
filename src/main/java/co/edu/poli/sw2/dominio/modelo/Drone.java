package co.edu.poli.sw2.dominio.modelo;

import java.util.ArrayList;
import java.util.List;

/**
 * Representa un dron administrado por la aplicacion, junto con el piloto
 * que lo opera y los sensores instalados en el.
 *
 * <p>Es la superclase de los tipos especializados de dron
 * ({@link co.edu.poli.sw2.dominio.modelo.Agricultura} y {@link co.edu.poli.sw2.dominio.modelo.Vigilancia}).</p>
 *
 * <p>Implementa {@link Cloneable} para soportar el patron Prototype: permite
 * obtener una copia de un dron (con una identidad de objeto distinta a la
 * del original) sin depender de sus constructores ni conocer su tipo
 * concreto. El punto de entrada para el resto de la aplicacion es
 * {@link co.edu.poli.sw2.services.DronePrototype}.</p>
 *
 * <p>Esta clase no tiene ninguna responsabilidad del patron Bridge: no
 * almacena ni conoce el tipo de control de vuelo asignado. El control
 * (basico o autonomo) se selecciona y aplica en tiempo de ejecucion desde
 * {@link co.edu.poli.sw2.services.ControlVuelo} (la Abstraccion del Bridge),
 * que referencia al dron unicamente mientras se ejecuta la accion de
 * control, sin que esa asociacion se persista en base de datos.</p>
 */
public class Drone implements Cloneable {

    private String id;
    private String serial;
    private String modelo;
    private String fabricante;
    private double peso;
    private Piloto piloto;
    private List<Sensor> sensores;

    /**
     * Crea un dron sin datos, con la lista de sensores vacia.
     */
    public Drone() {
        this.sensores = new ArrayList<>();
    }

    /**
     * Crea un dron con sus datos basicos.
     *
     * @param id identificador unico asignado manualmente (no se genera automaticamente).
     * @param serial numero de serie del dron.
     * @param modelo modelo del dron.
     * @param fabricante fabricante del dron.
     * @param peso peso del dron en kilogramos.
     */
    public Drone(String id, String serial, String modelo, String fabricante, double peso) {
        this();
        this.id = id;
        this.serial = serial;
        this.modelo = modelo;
        this.fabricante = fabricante;
        this.peso = peso;
    }

    /**
     * Devuelve el identificador del dron.
     *
     * @return el identificador del dron.
     */
    public String getId() {
        return id;
    }

    /**
     * Asigna el identificador del dron.
     *
     * @param id identificador a asignar al dron.
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Devuelve el numero de serie del dron.
     *
     * @return el numero de serie del dron.
     */
    public String getSerial() {
        return serial;
    }

    /**
     * Asigna el numero de serie del dron.
     *
     * @param serial numero de serie a asignar al dron.
     */
    public void setSerial(String serial) {
        this.serial = serial;
    }

    /**
     * Devuelve el modelo del dron.
     *
     * @return el modelo del dron.
     */
    public String getModelo() {
        return modelo;
    }

    /**
     * Asigna el modelo del dron.
     *
     * @param modelo modelo a asignar al dron.
     */
    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    /**
     * Devuelve el fabricante del dron.
     *
     * @return el fabricante del dron.
     */
    public String getFabricante() {
        return fabricante;
    }

    /**
     * Asigna el fabricante del dron.
     *
     * @param fabricante fabricante a asignar al dron.
     */
    public void setFabricante(String fabricante) {
        this.fabricante = fabricante;
    }

    /**
     * Devuelve el peso del dron.
     *
     * @return el peso del dron en kilogramos.
     */
    public double getPeso() {
        return peso;
    }

    /**
     * Asigna el peso del dron.
     *
     * @param peso peso en kilogramos a asignar al dron.
     */
    public void setPeso(double peso) {
        this.peso = peso;
    }

    /**
     * Devuelve el piloto asignado al dron.
     *
     * @return el piloto asignado al dron, o {@code null} si no tiene.
     */
    public Piloto getPiloto() {
        return piloto;
    }

    /**
     * Asigna el piloto del dron.
     *
     * @param piloto piloto a asignar al dron.
     */
    public void setPiloto(Piloto piloto) {
        this.piloto = piloto;
    }

    /**
     * Devuelve los sensores instalados en el dron.
     *
     * @return la lista de sensores instalados en el dron.
     */
    public List<Sensor> getSensores() {
        return sensores;
    }

    /**
     * Asigna los sensores instalados en el dron.
     *
     * @param sensores lista de sensores a asignar al dron.
     */
    public void setSensores(List<Sensor> sensores) {
        this.sensores = sensores;
    }

    /**
     * Crea una copia de este dron (patron Prototype). Los campos simples se
     * copian por valor y la lista de sensores se duplica para que el clon no
     * comparta su lista mutable con el original; el objeto devuelto tiene una
     * identidad (referencia de memoria) distinta a la de {@code this}.
     *
     * @return una copia independiente de este dron.
     */
    @Override
    public Drone clone() {
        try {
            Drone copia = (Drone) super.clone();
            copia.sensores = new ArrayList<>(this.sensores);
            return copia;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError("Drone implementa Cloneable: no deberia fallar.", e);
        }
    }

    @Override
    public String toString() {
        return "Drone{" +
                "id='" + id + '\'' +
                ", serial='" + serial + '\'' +
                ", modelo='" + modelo + '\'' +
                ", fabricante='" + fabricante + '\'' +
                ", peso=" + peso +
                ", piloto=" + piloto +
                ", sensores=" + sensores +
                '}';
    }
}
