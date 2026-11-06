package co.edu.poli.sw2.aplicacion.servicio;

import co.edu.poli.sw2.aplicacion.mediator.MissionCoordinator;
import co.edu.poli.sw2.aplicacion.puerto.entrada.ExecuteMissionUseCase;

import java.util.List;

/**
 * Servicio de aplicacion: implementa {@link ExecuteMissionUseCase}.
 *
 * <p>Valida los datos de entrada y delega la operacion en el coordinador de
 * mision (patron Mediator), que controla la secuencia entre los sistemas.</p>
 */
public class MissionService implements ExecuteMissionUseCase {

    /** Torre de control que coordina la operacion. */
    private final MissionCoordinator coordinador;

    /**
     * Crea el servicio con el coordinador que necesita.
     *
     * @param coordinador torre de control de la operacion.
     */
    public MissionService(MissionCoordinator coordinador) {
        this.coordinador = coordinador;
    }

    /** {@inheritDoc} */
    @Override
    public List<String> ejecutarMision(String droneId, int nivelBateria) {
        if (droneId == null || droneId.isBlank()) {
            throw new IllegalArgumentException("El ID del dron es obligatorio.");
        }
        if (nivelBateria < 0 || nivelBateria > 100) {
            throw new IllegalArgumentException("El nivel de bateria debe estar entre 0 y 100.");
        }
        return coordinador.iniciarMision(droneId.trim(), nivelBateria);
    }
}
