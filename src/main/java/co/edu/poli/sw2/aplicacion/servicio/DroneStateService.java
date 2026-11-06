package co.edu.poli.sw2.aplicacion.servicio;

import co.edu.poli.sw2.aplicacion.puerto.entrada.ManageDroneStateUseCase;
import co.edu.poli.sw2.aplicacion.puerto.salida.BuscarDronPort;
import co.edu.poli.sw2.dominio.modelo.Drone;
import co.edu.poli.sw2.dominio.state.DroneContext;
import co.edu.poli.sw2.dominio.state.DroneState;
import co.edu.poli.sw2.dominio.state.OffState;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Servicio de aplicacion: implementa {@link ManageDroneStateUseCase}.
 *
 * <p>El estado de cada dron se guarda solo en memoria, mientras la
 * aplicacion esta abierta: este servicio recuerda el ultimo estado de cada
 * dron por su identificador y, antes de cada operacion, arma su
 * {@link DroneContext} con ese estado. Un dron que nunca se ha operado
 * empieza en APAGADO ({@link OffState}). La transicion en si la decide el
 * propio estado (patron State).</p>
 */
public class DroneStateService implements ManageDroneStateUseCase {

    /** Puerto de salida para buscar el dron por identificador. */
    private final BuscarDronPort buscarDron;

    /** Ultimo estado de cada dron, por identificador; vive solo en memoria. */
    private final Map<String, DroneState> estados = new HashMap<>();

    /**
     * Crea el servicio con el puerto de salida que necesita.
     *
     * @param buscarDron puerto para buscar un dron por identificador.
     */
    public DroneStateService(BuscarDronPort buscarDron) {
        this.buscarDron = buscarDron;
    }

    /** {@inheritDoc} */
    @Override
    public String encender(String droneId) {
        return aplicar(droneId, DroneContext::encender);
    }

    /** {@inheritDoc} */
    @Override
    public String despegar(String droneId) {
        return aplicar(droneId, DroneContext::despegar);
    }

    /** {@inheritDoc} */
    @Override
    public String aterrizar(String droneId) {
        return aplicar(droneId, DroneContext::aterrizar);
    }

    /** {@inheritDoc} */
    @Override
    public String finalizarAterrizaje(String droneId) {
        return aplicar(droneId, DroneContext::finalizarAterrizaje);
    }

    /** {@inheritDoc} */
    @Override
    public String apagar(String droneId) {
        return aplicar(droneId, DroneContext::apagar);
    }

    /** {@inheritDoc} */
    @Override
    public String consultarEstado(String droneId) {
        return obtener(droneId).getEstado().getNombre();
    }

    /**
     * Aplica una transicion al dron y recuerda el estado en que queda. Si la
     * transicion no esta permitida, el estado guardado no cambia.
     *
     * @param droneId identificador del dron.
     * @param transicion operacion del contexto que se ejecuta.
     * @return el nombre del nuevo estado.
     */
    private String aplicar(String droneId, Consumer<DroneContext> transicion) {
        DroneContext contexto = obtener(droneId);
        transicion.accept(contexto);
        estados.put(contexto.getDrone().getId(), contexto.getEstado());
        return contexto.getEstado().getNombre();
    }

    /**
     * Busca el dron y arma su contexto con el ultimo estado conocido o, si
     * nunca se ha operado, con el estado inicial APAGADO ({@link OffState}).
     *
     * @param droneId identificador del dron.
     * @return el contexto del dron con su estado actual.
     * @throws IllegalArgumentException si el identificador esta vacio o el dron no existe.
     */
    private DroneContext obtener(String droneId) {
        if (droneId == null || droneId.isBlank()) {
            throw new IllegalArgumentException("El ID del dron es obligatorio.");
        }
        String id = droneId.trim();
        Drone drone = buscarDron.buscarPorId(id);
        if (drone == null) {
            estados.remove(id);
            throw new IllegalArgumentException("No existe un dron con el ID '" + id + "'.");
        }
        DroneState guardado = estados.get(id);
        return new DroneContext(drone, guardado != null ? guardado : new OffState());
    }
}
