package co.edu.poli.sw2.dominio.modelo;

/**
 * Dron especializado en labores de vigilancia, con deteccion termica opcional.
 */
public class Vigilancia extends Drone {

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
     * Crea una copia de este dron de vigilancia (patron Prototype), incluyendo
     * si tiene deteccion termica. El objeto devuelto tiene una identidad
     * (referencia de memoria) distinta a la de {@code this}.
     *
     * @return una copia independiente de este dron de vigilancia.
     */
    @Override
    public Vigilancia clone() {
        return (Vigilancia) super.clone();
    }

    @Override
    public String toString() {
        return "Vigilancia{" +
                "deteccionTermica=" + deteccionTermica +
                "} " + super.toString();
    }
}
