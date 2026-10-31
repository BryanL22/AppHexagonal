package co.edu.poli.sw2.infraestructura.loader;

import co.edu.poli.sw2.dominio.strategy.PuntoGeografico;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

/**
 * Adaptador de infraestructura para cargar y deserializar una ruta desde un archivo JSON.
 *
 * <p>El formato esperado es:
 * <pre>{@code
 * {
 *   "origen": {
 *     "nombre": "Punto Origen",
 *     "latitud": 4.60971,
 *     "longitud": -74.08175
 *   },
 *   "destino": {
 *     "nombre": "Punto Destino",
 *     "latitud": 4.70159,
 *     "longitud": -74.14690
 *   }
 * }
 * }</pre>
 * </p>
 */
public class JsonRutaLoader {

    private final ObjectMapper objectMapper;

    /**
     * Representacion de una ruta compuesta por un punto de origen y un punto de destino.
     *
     * @param origen punto inicial de la ruta.
     * @param destino punto final de la ruta.
     */
    public record Ruta(PuntoGeografico origen, PuntoGeografico destino) {
    }

    /**
     * Crea una instancia de cargador utilizando una instancia predeterminada de {@link ObjectMapper}.
     */
    public JsonRutaLoader() {
        this.objectMapper = new ObjectMapper();
    }

    /**
     * Crea una instancia de cargador con un {@link ObjectMapper} inyectado.
     *
     * @param objectMapper conversor de JSON.
     */
    public JsonRutaLoader(ObjectMapper objectMapper) {
        this.objectMapper = (objectMapper != null) ? objectMapper : new ObjectMapper();
    }

    /**
     * Carga y parsea una ruta desde un archivo en disco.
     *
     * @param archivo archivo JSON en el sistema de archivos.
     * @return objeto {@link Ruta} con el origen y destino.
     * @throws IOException si ocurre un error de lectura o si el formato no es valido.
     */
    public Ruta cargarRuta(File archivo) throws IOException {
        if (archivo == null) {
            throw new IllegalArgumentException("El archivo de ruta no puede ser nulo.");
        }
        if (!archivo.exists() || !archivo.isFile()) {
            throw new IOException("El archivo especificado no existe o no es un archivo valido: " + archivo.getAbsolutePath());
        }
        JsonNode root = objectMapper.readTree(archivo);
        return parsearRuta(root);
    }

    /**
     * Carga y parsea una ruta a partir de un texto en formato JSON.
     *
     * @param jsonContenido cadena con el contenido JSON.
     * @return objeto {@link Ruta} con el origen y destino.
     * @throws IOException si el contenido no es JSON valido o no tiene la estructura requerida.
     */
    public Ruta cargarRuta(String jsonContenido) throws IOException {
        if (jsonContenido == null || jsonContenido.isBlank()) {
            throw new IllegalArgumentException("El contenido JSON no puede estar vacio.");
        }
        JsonNode root = objectMapper.readTree(jsonContenido);
        return parsearRuta(root);
    }

    private Ruta parsearRuta(JsonNode root) throws IOException {
        if (root == null || !root.isObject()) {
            throw new IOException("El JSON de ruta debe ser un objeto con nodos 'origen' y 'destino'.");
        }
        if (!root.has("origen") || !root.has("destino")) {
            throw new IOException("El archivo JSON debe contener obligatoriamente los nodos 'origen' y 'destino'.");
        }

        PuntoGeografico origen = parsearPunto(root.get("origen"), "origen");
        PuntoGeografico destino = parsearPunto(root.get("destino"), "destino");

        return new Ruta(origen, destino);
    }

    private PuntoGeografico parsearPunto(JsonNode nodo, String rol) throws IOException {
        if (nodo == null || !nodo.isObject()) {
            throw new IOException("El nodo '" + rol + "' debe ser un objeto JSON.");
        }
        if (!nodo.has("latitud") || !nodo.get("latitud").isNumber()) {
            throw new IOException("El nodo '" + rol + "' debe contener un campo numerico 'latitud'.");
        }
        if (!nodo.has("longitud") || !nodo.get("longitud").isNumber()) {
            throw new IOException("El nodo '" + rol + "' debe contener un campo numerico 'longitud'.");
        }

        String nombre = nodo.has("nombre") && !nodo.get("nombre").isNull()
                ? nodo.get("nombre").asText()
                : (rol.substring(0, 1).toUpperCase() + rol.substring(1));

        double latitud = nodo.get("latitud").asDouble();
        double longitud = nodo.get("longitud").asDouble();

        try {
            return new PuntoGeografico(nombre, latitud, longitud);
        } catch (IllegalArgumentException e) {
            throw new IOException("Coordenadas invalidas en el nodo '" + rol + "': " + e.getMessage(), e);
        }
    }
}

