package co.edu.poli.sw2.application;

import co.edu.poli.sw2.domain.model.Drone;
import co.edu.poli.sw2.domain.port.in.GestionarDronesUseCase;
import co.edu.poli.sw2.domain.port.out.DroneRepositoryPort;

import java.util.List;

/**
 * Servicio de aplicacion: implementa los casos de uso del hexagono.
 *
 * <p>Es el centro de la arquitectura. Implementa el puerto de entrada
 * {@link GestionarDronesUseCase} y se apoya en el puerto de salida
 * {@link DroneRepositoryPort}, que recibe por constructor. No sabe que del
 * otro lado hay MySQL ni que quien lo llama es JavaFX: solo conoce
 * interfaces del dominio.</p>
 *
 * <p>Aqui vive la regla de negocio que antes estaba repartida en la vista:
 * no se puede registrar un dron con un identificador que ya existe.</p>
 */
public class DroneService implements GestionarDronesUseCase {

    private final DroneRepositoryPort droneRepository;

    /**
     * Crea el servicio sobre el repositorio indicado.
     *
     * @param droneRepository adaptador de persistencia a utilizar.
     */
    public DroneService(DroneRepositoryPort droneRepository) {
        this.droneRepository = droneRepository;
    }

    /**
     * Registra el dron solo si su identificador esta libre.
     */
    @Override
    public boolean registrarDrone(Drone drone) {
        if (droneRepository.buscarPorId(drone.getId()) != null) {
            return false;
        }

        return droneRepository.guardar(drone);
    }

    @Override
    public Drone consultarDrone(String id) {
        return droneRepository.buscarPorId(id);
    }

    @Override
    public List<Drone> consultarDrones() {
        return droneRepository.buscarTodos();
    }

    @Override
    public boolean actualizarDrone(Drone drone) {
        return droneRepository.actualizar(drone);
    }

    @Override
    public boolean eliminarDrone(String id) {
        return droneRepository.eliminar(id);
    }
}
