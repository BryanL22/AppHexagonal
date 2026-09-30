package co.edu.poli.sw2.aplicacion.servicio;

import co.edu.poli.sw2.dominio.modelo.Drone;
import co.edu.poli.sw2.dominio.puerto.entrada.ActualizarDronUseCase;
import co.edu.poli.sw2.dominio.puerto.entrada.ConsultarDronUseCase;
import co.edu.poli.sw2.dominio.puerto.entrada.ConsultarDronesUseCase;
import co.edu.poli.sw2.dominio.puerto.entrada.CrearDronUseCase;
import co.edu.poli.sw2.dominio.puerto.entrada.EliminarDronUseCase;
import co.edu.poli.sw2.dominio.puerto.salida.ActualizarDronPort;
import co.edu.poli.sw2.dominio.puerto.salida.BuscarDronPort;
import co.edu.poli.sw2.dominio.puerto.salida.BuscarDronesPort;
import co.edu.poli.sw2.dominio.puerto.salida.EliminarDronPort;
import co.edu.poli.sw2.dominio.puerto.salida.GuardarDronPort;

import java.util.List;

/**
 * Servicio de aplicacion: es lo que la aplicacion puede hacer.
 *
 * <p>Implementa los cinco puertos de entrada (el CRUD que se ofrece al
 * exterior) y se apoya en los cinco puertos de salida, que recibe por
 * constructor. Es el centro del hexagono: no conoce JavaFX ni MySQL, solo
 * interfaces del dominio.</p>
 *
 * <p>Aqui vive la regla de negocio que antes estaba en la vista: no se puede
 * registrar un dron con un identificador que ya existe.</p>
 */
public class DronServicio implements CrearDronUseCase, ConsultarDronUseCase, ConsultarDronesUseCase,
        ActualizarDronUseCase, EliminarDronUseCase {

    private final GuardarDronPort guardarDron;
    private final BuscarDronPort buscarDron;
    private final BuscarDronesPort buscarDrones;
    private final ActualizarDronPort actualizarDron;
    private final EliminarDronPort eliminarDron;

    /**
     * Crea el servicio con los cinco puertos de salida que necesita.
     *
     * <p>Los recibe uno a uno, y no como una sola dependencia, para que quede
     * explicito que el servicio depende de contratos del dominio y no de una
     * clase concreta de infraestructura. Quien los cablea es
     * {@code App}.</p>
     *
     * @param guardarDron puerto para guardar.
     * @param buscarDron puerto para buscar por identificador.
     * @param buscarDrones puerto para listar.
     * @param actualizarDron puerto para actualizar.
     * @param eliminarDron puerto para eliminar.
     */
    public DronServicio(GuardarDronPort guardarDron, BuscarDronPort buscarDron, BuscarDronesPort buscarDrones,
            ActualizarDronPort actualizarDron, EliminarDronPort eliminarDron) {
        this.guardarDron = guardarDron;
        this.buscarDron = buscarDron;
        this.buscarDrones = buscarDrones;
        this.actualizarDron = actualizarDron;
        this.eliminarDron = eliminarDron;
    }

    /**
     * Registra el dron solo si su identificador esta libre.
     */
    @Override
    public boolean crear(Drone drone) {
        if (buscarDron.buscarPorId(drone.getId()) != null) {
            return false;
        }

        return guardarDron.guardar(drone);
    }

    @Override
    public Drone consultar(String id) {
        return buscarDron.buscarPorId(id);
    }

    @Override
    public List<Drone> consultarTodos() {
        return buscarDrones.buscarTodos();
    }

    @Override
    public boolean actualizar(Drone drone) {
        return actualizarDron.actualizar(drone);
    }

    @Override
    public boolean eliminar(String id) {
        return eliminarDron.eliminar(id);
    }
}
