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
 * <p>Implementa {@link Cloneable} para poder obtener una copia independiente
 * de un dron (ver {@link #clone()}) sin conocer su tipo concreto.</p>
 *
 * <p>Pertenece al dominio: solo depende de {@code java.util}, sin JavaFX,
 * JDBC ni ninguna otra tecnologia.</p>
 */
public class Drone implements Cloneable {

    /** Identificador unico, asignado manualmente por el usuario. */
    private String id;
    /** Numero de serie del fabricante. */
    private String serial;
    /** Modelo del dron. */
    private String modelo;
    /** Empresa que fabrico el dron. */
    private String fabricante;
    /** Peso del dron en kilogramos. */
    private double peso;
    /** Piloto asignado; {@code null} si no tiene. */
    private Piloto piloto;
    /** Sensores instalados en el dron; nunca es {@code null}. */
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
     * Verifica las reglas de negocio de un dron: identificador, serial,
     * modelo y fabricante son obligatorios, y el peso debe ser mayor que 0.
     * Las subclases la amplian con las reglas de su especializacion.
     *
     * @throws IllegalArgumentException si alguna regla no se cumple; el
     *         mensaje indica cual.
     */
    public void validar() {
        exigirTexto(id, "El ID");
        exigirTexto(serial, "El serial");
        exigirTexto(modelo, "El modelo");
        exigirTexto(fabricante, "El fabricante");
        if (peso <= 0) {
            throw new IllegalArgumentException("El peso debe ser mayor que 0.");
        }
    }

    /**
     * Exige que un dato de texto tenga contenido.
     *
     * @param valor valor a revisar; puede ser {@code null}.
     * @param campo nombre del dato para el mensaje de error (p. ej. "El serial").
     * @throws IllegalArgumentException si el valor es {@code null} o solo tiene espacios.
     */
    private static void exigirTexto(String valor, String campo) {
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException(campo + " es obligatorio.");
        }
    }

    /**
     * Crea una copia de este dron. Los campos simples se
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

    /**
     * Devuelve una representacion en texto con todos los datos, util para depuracion.
     *
     * @return los atributos de este objeto en formato legible.
     */
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
