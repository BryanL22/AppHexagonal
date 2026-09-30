package co.edu.poli.sw2.domain.model;

/**
 * Representa al piloto responsable de operar un dron.
 */
public class Piloto {

    private String id;
    private String nombre;
    private String licencia;
    private String telefono;

    /**
     * Crea un piloto sin datos.
     */
    public Piloto() {
    }

    /**
     * Crea un piloto con sus datos basicos.
     *
     * @param id identificador unico del piloto.
     * @param nombre nombre completo del piloto.
     * @param licencia numero de licencia del piloto.
     * @param telefono telefono de contacto del piloto.
     */
    public Piloto(String id, String nombre, String licencia, String telefono) {
        this.id = id;
        this.nombre = nombre;
        this.licencia = licencia;
        this.telefono = telefono;
    }

    /**
     * Devuelve el identificador del piloto.
     *
     * @return el identificador del piloto.
     */
    public String getId() {
        return id;
    }

    /**
     * Asigna el identificador del piloto.
     *
     * @param id identificador a asignar al piloto.
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Devuelve el nombre completo del piloto.
     *
     * @return el nombre completo del piloto.
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Asigna el nombre del piloto.
     *
     * @param nombre nombre a asignar al piloto.
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Devuelve el numero de licencia del piloto.
     *
     * @return el numero de licencia del piloto.
     */
    public String getLicencia() {
        return licencia;
    }

    /**
     * Asigna el numero de licencia del piloto.
     *
     * @param licencia numero de licencia a asignar al piloto.
     */
    public void setLicencia(String licencia) {
        this.licencia = licencia;
    }

    /**
     * Devuelve el telefono de contacto del piloto.
     *
     * @return el telefono de contacto del piloto.
     */
    public String getTelefono() {
        return telefono;
    }

    /**
     * Asigna el telefono de contacto del piloto.
     *
     * @param telefono telefono a asignar al piloto.
     */
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    @Override
    public String toString() {
        return "Piloto{" +
                "id='" + id + '\'' +
                ", nombre='" + nombre + '\'' +
                ", licencia='" + licencia + '\'' +
                ", telefono='" + telefono + '\'' +
                '}';
    }
}
