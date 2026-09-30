package co.edu.poli.sw2.aplicacion.puerto.salida;

import co.edu.poli.sw2.dominio.modelo.Drone;

/**
 * Puerto de salida (driven port) del hexagono: actualizar un dron almacenado.
 *
 * <p>Lo implementa un adaptador de infraestructura. Declara lo que la aplicacion necesita, sin decir con que tecnologia se resuelve.</p>
 */
public interface ActualizarDronPort {

    /**
     * Actualiza los datos del dron en el almacenamiento.
     *
     * @param drone dron sobre el que se opera.
     * @return {@code true} si la operacion tuvo efecto.
     */
    boolean actualizar(Drone drone);
}
