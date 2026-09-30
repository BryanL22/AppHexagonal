package co.edu.poli.sw2.dominio.puerto.salida;

import co.edu.poli.sw2.dominio.modelo.Drone;

import java.util.List;

/**
 * Puerto de salida (driven port) del hexagono: obtener todos los drones.
 *
 * <p>Lo implementa un adaptador de infraestructura. Declara lo que la aplicacion necesita, sin decir con que tecnologia se resuelve.</p>
 */
public interface BuscarDronesPort {

    /**
     * Devuelve todos los drones almacenados.
     *
     * @return la lista de drones registrados.
     */
    List<Drone> buscarTodos();
}
