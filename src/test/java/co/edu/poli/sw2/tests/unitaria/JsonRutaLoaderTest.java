package co.edu.poli.sw2.tests.unitaria;

import co.edu.poli.sw2.dominio.strategy.PuntoGeografico;
import co.edu.poli.sw2.infraestructura.loader.JsonRutaLoader;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Pruebas unitarias para {@link JsonRutaLoader}.
 */
class JsonRutaLoaderTest {

    private static final String JSON_VALIDO = """
            {
              "origen": {
                "nombre": "Punto Origen",
                "latitud": 4.60971,
                "longitud": -74.08175
              },
              "destino": {
                "nombre": "Punto Destino",
                "latitud": 4.70159,
                "longitud": -74.14690
              }
            }
            """;

    @Test
    void cargaRutaDesdeStringValido() throws IOException {
        JsonRutaLoader loader = new JsonRutaLoader();
        JsonRutaLoader.Ruta ruta = loader.cargarRuta(JSON_VALIDO);

        assertNotNull(ruta);
        assertNotNull(ruta.origen());
        assertNotNull(ruta.destino());

        assertEquals("Punto Origen", ruta.origen().getNombre());
        assertEquals(4.60971, ruta.origen().getLatitud(), 0.00001);
        assertEquals(-74.08175, ruta.origen().getLongitud(), 0.00001);

        assertEquals("Punto Destino", ruta.destino().getNombre());
        assertEquals(4.70159, ruta.destino().getLatitud(), 0.00001);
        assertEquals(-74.14690, ruta.destino().getLongitud(), 0.00001);
    }

    @Test
    void cargaRutaDesdeArchivoValido(@TempDir Path tempDir) throws IOException {
        Path archivoRuta = tempDir.resolve("ruta.json");
        Files.writeString(archivoRuta, JSON_VALIDO);

        JsonRutaLoader loader = new JsonRutaLoader();
        JsonRutaLoader.Ruta ruta = loader.cargarRuta(archivoRuta.toFile());

        assertNotNull(ruta);
        assertEquals("Punto Origen", ruta.origen().getNombre());
        assertEquals("Punto Destino", ruta.destino().getNombre());
    }

    @Test
    void jsonIncompletoLanzaIOException() {
        String jsonSinDestino = """
                {
                  "origen": {
                    "nombre": "Punto Origen",
                    "latitud": 4.60971,
                    "longitud": -74.08175
                  }
                }
                """;
        JsonRutaLoader loader = new JsonRutaLoader();
        assertThrows(IOException.class, () -> loader.cargarRuta(jsonSinDestino));
    }

    @Test
    void archivoInexistenteLanzaIOException() {
        JsonRutaLoader loader = new JsonRutaLoader();
        assertThrows(IOException.class, () -> loader.cargarRuta(new File("archivo_inexistente_12345.json")));
    }
}

