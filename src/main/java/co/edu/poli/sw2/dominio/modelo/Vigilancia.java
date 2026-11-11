package co.edu.poli.sw2.dominio.modelo;

/**
 * Dron especializado en labores de vigilancia, con deteccion termica opcional.
 */
public class Vigilancia extends Drone {

    /** Indica si el dron cuenta con camara de deteccion termica. */
    private boolean deteccionTermica;

    /**
     * Crea un dron de vigilancia sin datos.
     */
    public Vigilancia() {
        super();
    }

    /**
     * Crea un dron de vigilancia con sus datos basicos y si tiene deteccion termica.
     *
     * @param id identificador unico asignado manualmente.
     * @param serial numero de serie del dron.
     * @param modelo modelo del dron.
     * @param fabricante fabricante del dron.
     * @param peso peso del dron en kilogramos.
     * @param deteccionTermica si el dron cuenta con deteccion termica.
     */
    public Vigilancia(String id, String serial, String modelo, String fabricante, double peso,
                       boolean deteccionTermica) {
        super(id, serial, modelo, fabricante, peso);
        this.deteccionTermica = deteccionTermica;
    }

    /**
     * Indica si el dron cuenta con deteccion termica.
     *
     * @return si el dron cuenta con deteccion termica.
     */
    public boolean isDeteccionTermica() {
        return deteccionTermica;
    }

    /**
     * Asigna si el dron cuenta con deteccion termica.
     *
     * @param deteccionTermica si el dron debe contar con deteccion termica.
     */
    public void setDeteccionTermica(boolean deteccionTermica) {
        this.deteccionTermica = deteccionTermica;
    }

    /**
     * Devuelve una representacion en texto con todos los datos, util para depuracion.
     *
     * @return los atributos de este objeto en formato legible.
     */
    @Override
    public String toString() {
        return "Vigilancia{" +
                "deteccionTermica=" + deteccionTermica +
                "} " + super.toString();
    }
}
