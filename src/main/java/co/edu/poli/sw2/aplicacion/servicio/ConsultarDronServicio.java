package co.edu.poli.sw2.aplicacion.servicio;

import co.edu.poli.sw2.aplicacion.puerto.entrada.ConsultarDronUseCase;
import co.edu.poli.sw2.aplicacion.puerto.salida.BuscarDronPort;
import co.edu.poli.sw2.dominio.modelo.Drone;

/**
 * Servicio de aplicacion: implementa el caso de uso
 * {@link ConsultarDronUseCase}. Exige un identificador y delega la busqueda
 * en el puerto de salida.
 */
public class ConsultarDronServicio implements ConsultarDronUseCase {

    /** Puerto de salida para buscar un dron por identificador. */
    private final BuscarDronPort buscarDron;

    /**
     * Crea el servicio con el puerto de salida que necesita.
     *
     * @param buscarDron puerto para buscar por identificador.
     */
    public ConsultarDronServicio(BuscarDronPort buscarDron) {
        this.buscarDron = buscarDron;
    }

    /**
     * Consulta un dron por su identificador.
     *
     * @param id identificador del dron.
     * @return el dron encontrado, o {@code null} si no existe.
     * @throws IllegalArgumentException si el identificador esta vacio.
     * @throws co.edu.poli.sw2.aplicacion.puerto.salida.PersistenciaException
     *         si falla el almacenamiento.
     */
    @Override
    public Drone consultar(String id) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("El ID del drone es obligatorio.");
        }

        return buscarDron.buscarPorId(id);
    }
}
