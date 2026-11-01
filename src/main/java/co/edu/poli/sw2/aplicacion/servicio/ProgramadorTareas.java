package co.edu.poli.sw2.aplicacion.servicio;

import co.edu.poli.sw2.dominio.command.DroneCommand;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Invoker del patron Command. Gestiona y ejecuta comandos de operacion
 * de drones a traves de la interfaz {@link DroneCommand}, sin conocer
 * los detalles de implementacion de cada operacion.
 *
 * <p>Recibe comandos, los almacena en cola y los ejecuta en bloque,
 * manteniendo el historial de operaciones tanto general como filtrado
 * por dron.</p>
 */
public class ProgramadorTareas {

    /** Formateador de hora para los registros del historial. */
    private static final DateTimeFormatter FORMATO_HORA = DateTimeFormatter.ofPattern("HH:mm:ss");

    /** Lista de comandos programados para ejecucion. */
    private final List<DroneCommand> tareas = new ArrayList<>();

    /** Historial de resultados de comandos ejecutados en general. */
    private final List<String> historial = new ArrayList<>();

    /** Historial de operaciones clasificado por identificador de dron. */
    private final Map<String, List<String>> historialPorDron = new LinkedHashMap<>();

    /**
     * Agrega un comando a la lista de tareas a ejecutar.
     *
     * @param comando el comando a programar.
     */
    public void agregarTarea(DroneCommand comando) {
        if (comando != null) {
            this.tareas.add(comando);
        }
    }

    /**
     * Limpia la lista de comandos programados.
     */
    public void limpiarTareas() {
        this.tareas.clear();
    }

    /**
     * Asigna un unico comando reemplazando los anteriores.
     *
     * @param comando el comando a programar para su ejecucion.
     */
    public void setComando(DroneCommand comando) {
        limpiarTareas();
        agregarTarea(comando);
    }

    /**
     * Ejecuta todas las tareas programadas asociandolas al dron indicado,
     * las registra en su historial particular y limpia la cola.
     *
     * @param dronId identificador del dron que ejecuta las tareas.
     * @return lista con los resultados de cada operacion.
     */
    public List<String> ejecutarTareasParaDron(String dronId) {
        List<String> resultados = new ArrayList<>();
        String hora = LocalTime.now().format(FORMATO_HORA);

        for (DroneCommand cmd : tareas) {
            String resultado = cmd.ejecutar();
            resultados.add(resultado);
            historial.add(resultado);

            if (dronId != null && !dronId.isBlank()) {
                historialPorDron.computeIfAbsent(dronId, k -> new ArrayList<>())
                        .add("[" + hora + "] " + resultado);
            }
        }
        tareas.clear();
        return resultados;
    }

    /**
     * Ejecuta todas las tareas programadas en orden y limpia la cola.
     *
     * @return lista con los resultados de cada operacion ejecutada.
     */
    public List<String> ejecutarTareas() {
        return ejecutarTareasParaDron(null);
    }

    /**
     * Ejecuta las tareas y retorna el resultado como un solo String.
     *
     * @return texto con los resultados o mensaje si no hay tareas.
     */
    public String ejecutarTarea() {
        if (tareas.isEmpty()) {
            return "No hay ninguna tarea programada.";
        }
        List<String> resultados = ejecutarTareas();
        return String.join("\n", resultados);
    }

    /**
     * Retorna el historial de tareas filtrado para un dron en particular.
     *
     * @param dronId identificador del dron.
     * @return lista inmutable o vacia con los registros de ese dron.
     */
    public List<String> obtenerHistorialDeDron(String dronId) {
        if (dronId == null) {
            return Collections.emptyList();
        }
        return historialPorDron.getOrDefault(dronId, Collections.emptyList());
    }

    /**
     * Retorna el historial general de tareas ejecutadas.
     *
     * @return lista con los resultados de las operaciones.
     */
    public List<String> getHistorial() {
        return historial;
    }
}
