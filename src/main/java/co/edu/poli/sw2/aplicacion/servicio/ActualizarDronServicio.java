package co.edu.poli.sw2.aplicacion.servicio;

import co.edu.poli.sw2.aplicacion.puerto.entrada.ActualizarDronUseCase;
import co.edu.poli.sw2.aplicacion.puerto.salida.ActualizarDronPort;
import co.edu.poli.sw2.dominio.modelo.Drone;

/**
 * Servicio de aplicacion: implementa el caso de uso
 * {@link ActualizarDronUseCase}. Antes de actualizar exige que el dron sea
 * valido (ver {@link Drone#validar()}).
 */
public class ActualizarDronServicio implements ActualizarDronUseCase {

    /** Puerto de salida para actualizar un dron existente. */
    private final ActualizarDronPort actualizarDron;

    /**
     * Crea el servicio con el puerto de salida que necesita.
     *
     * @param actualizarDron puerto para actualizar.
     */
    public ActualizarDronServicio(ActualizarDronPort actualizarDron) {
        this.actualizarDron = actualizarDron;
    }

    /**
     * Actualiza los datos de un dron existente.
     *
     * @param drone dron con los datos nuevos; se localiza por su identificador.
     * @return {@code true} si se actualizo; {@code false} si no existe o si su
     *         tipo no coincide con el registrado.
     * @throws IllegalArgumentException si el dron es {@code null} o no cumple
     *         las reglas de negocio.
     * @throws co.edu.poli.sw2.aplicacion.puerto.salida.PersistenciaException
     *         si falla el almacenamiento.
     */
    @Override
    public boolean actualizar(Drone drone) {
        if (drone == null) {
            throw new IllegalArgumentException("El drone no puede ser nulo.");
        }
        drone.validar();

        return actualizarDron.actualizar(drone);
    }
}
