package co.edu.poli.sw2.aplicacion.puerto.salida;

import co.edu.poli.sw2.dominio.modelo.Drone;

/**
 * Puerto de salida (driven port) del hexagono: buscar un dron por su identificador.
 *
 * <p>Lo implementa un adaptador de infraestructura. Declara lo que la aplicacion necesita, sin decir con que tecnologia se resuelve.</p>
 */
public interface BuscarDronPort {

    /**
     * Busca un dron por su identificador.
     *
     * @param id identificador del dron.
     * @return el dron encontrado, o {@code null} si no existe.
     */
    Drone buscarPorId(String id);
}
