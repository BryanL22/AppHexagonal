package co.edu.poli.sw2.aplicacion.servicio;

import co.edu.poli.sw2.aplicacion.puerto.entrada.EliminarDronUseCase;
import co.edu.poli.sw2.aplicacion.puerto.salida.EliminarDronPort;

/**
 * Servicio de aplicacion: implementa el caso de uso
 * {@link EliminarDronUseCase}. Exige un identificador y delega la
 * eliminacion en el puerto de salida.
 */
public class EliminarDronServicio implements EliminarDronUseCase {

    /** Puerto de salida para eliminar un dron. */
    private final EliminarDronPort eliminarDron;

    /**
     * Crea el servicio con el puerto de salida que necesita.
     *
     * @param eliminarDron puerto para eliminar.
     */
    public EliminarDronServicio(EliminarDronPort eliminarDron) {
        this.eliminarDron = eliminarDron;
    }

    /**
     * Elimina un dron.
     *
     * @param id identificador del dron.
     * @return {@code true} si existia y se elimino.
     * @throws IllegalArgumentException si el identificador esta vacio.
     * @throws co.edu.poli.sw2.aplicacion.puerto.salida.PersistenciaException
     *         si falla el almacenamiento.
     */
    @Override
    public boolean eliminar(String id) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("El ID del drone es obligatorio.");
        }

        return eliminarDron.eliminar(id);
    }
}
