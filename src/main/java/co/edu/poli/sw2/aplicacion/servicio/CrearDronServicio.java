package co.edu.poli.sw2.aplicacion.servicio;

import co.edu.poli.sw2.aplicacion.puerto.entrada.CrearDronUseCase;
import co.edu.poli.sw2.aplicacion.puerto.salida.BuscarDronPort;
import co.edu.poli.sw2.aplicacion.puerto.salida.GuardarDronPort;
import co.edu.poli.sw2.dominio.modelo.Drone;

/**
 * Servicio de aplicacion: implementa el caso de uso {@link CrearDronUseCase}.
 *
 * <p>Antes de guardar hace cumplir las reglas de negocio: el dron debe ser
 * valido (ver {@link Drone#validar()}) y su identificador no puede estar
 * ocupado. Depende solo de puertos de salida (interfaces), nunca de la
 * implementacion concreta de persistencia.</p>
 */
public class CrearDronServicio implements CrearDronUseCase {

    /** Puerto de salida para verificar si el identificador ya esta ocupado. */
    private final BuscarDronPort buscarDron;

    /** Puerto de salida para guardar el dron nuevo. */
    private final GuardarDronPort guardarDron;

    /**
     * Crea el servicio con los puertos de salida que necesita.
     *
     * @param buscarDron puerto para buscar por identificador.
     * @param guardarDron puerto para guardar.
     */
    public CrearDronServicio(BuscarDronPort buscarDron, GuardarDronPort guardarDron) {
        this.buscarDron = buscarDron;
        this.guardarDron = guardarDron;
    }

    /**
     * Registra el dron solo si es valido y su identificador esta libre.
     *
     * @param drone dron a registrar.
     * @return {@code true} si se guardo; {@code false} si ya existia un dron
     *         con el mismo identificador.
     * @throws IllegalArgumentException si el dron es {@code null} o no cumple
     *         las reglas de negocio.
     * @throws co.edu.poli.sw2.aplicacion.puerto.salida.PersistenciaException
     *         si falla el almacenamiento.
     */
    @Override
    public boolean crear(Drone drone) {
        if (drone == null) {
            throw new IllegalArgumentException("El drone no puede ser nulo.");
        }
        drone.validar();

        if (buscarDron.buscarPorId(drone.getId()) != null) {
            return false;
        }

        return guardarDron.guardar(drone);
    }
}
