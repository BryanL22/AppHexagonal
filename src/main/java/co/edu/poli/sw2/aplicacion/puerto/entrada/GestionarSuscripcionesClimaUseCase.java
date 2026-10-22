package co.edu.poli.sw2.aplicacion.puerto.entrada;

import java.util.List;

/** Operaciones de entrada para administrar drones suscritos al clima. */
public interface GestionarSuscripcionesClimaUseCase {

    /** Agrega un dron existente a las suscripciones.
     *
     * @param droneId identificador del dron.
     */
    void suscribir(String droneId);

    /** Quita un dron de las suscripciones.
     *
     * @param droneId identificador del dron.
     */
    void desuscribir(String droneId);

    /** Lista los identificadores de drones suscritos.
     *
     * @return identificadores suscritos.
     */
    List<String> listarSuscritos();
}