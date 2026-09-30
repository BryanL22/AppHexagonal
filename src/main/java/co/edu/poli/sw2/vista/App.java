package co.edu.poli.sw2.vista;

import co.edu.poli.sw2.aplicacion.puerto.entrada.ActualizarDronUseCase;
import co.edu.poli.sw2.aplicacion.puerto.entrada.ConsultarDronUseCase;
import co.edu.poli.sw2.aplicacion.puerto.entrada.ConsultarDronesUseCase;
import co.edu.poli.sw2.aplicacion.puerto.entrada.CrearDronUseCase;
import co.edu.poli.sw2.aplicacion.puerto.entrada.EliminarDronUseCase;
import co.edu.poli.sw2.aplicacion.servicio.ActualizarDronServicio;
import co.edu.poli.sw2.aplicacion.servicio.ConsultarDronServicio;
import co.edu.poli.sw2.aplicacion.servicio.ConsultarDronesServicio;
import co.edu.poli.sw2.aplicacion.servicio.CrearDronServicio;
import co.edu.poli.sw2.aplicacion.servicio.EliminarDronServicio;
import co.edu.poli.sw2.infraestructura.persistencia.ConexionBD;
import co.edu.poli.sw2.infraestructura.persistencia.MySqlDronRepository;
import co.edu.poli.sw2.infraestructura.ui.MainController;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Punto de entrada de la aplicacion: conecta todo el hexagono.
 *
 * <p>Es el unico lugar donde se decide que implementacion concreta hay detras
 * de cada puerto:</p>
 * <pre>
 *   Adaptador de salida  →  Servicios de aplicacion  →  Adaptador de entrada
 *   MySqlDronRepository  →  CrearDronServicio        →  MainController
 *                        →  ConsultarDronServicio
 *                        →  ConsultarDronesServicio
 *                        →  ActualizarDronServicio
 *                        →  EliminarDronServicio
 * </pre>
 *
 * <p>Ninguna otra clase instancia sus dependencias: por eso cambiar de base de
 * datos o de tecnologia de interfaz solo obliga a tocar este archivo.</p>
 */
public class App extends Application {

    /**
     * Crea la aplicacion. No recibe dependencias: JavaFX la instancia por
     * reflexion al invocar {@link #launch(String...)}.
     */
    public App() {
    }

    /**
     * Arma el hexagono (repositorio, servicios y controlador), carga la vista
     * {@code GestorDrones.fxml} con su hoja de estilos y muestra la ventana.
     *
     * @param stage ventana principal que entrega JavaFX.
     * @throws IOException si no se pudo cargar el archivo FXML.
     */
    @Override
    public void start(Stage stage) throws IOException {
        // 1. Adaptador de salida: implementa los cinco puertos de salida contra MySQL.
        MySqlDronRepository dronRepo = new MySqlDronRepository();

        // 2. Servicios de aplicacion: uno por caso de uso, con los puertos que necesita.
        CrearDronUseCase crearUC = new CrearDronServicio(dronRepo, dronRepo);
        ConsultarDronUseCase consultarUC = new ConsultarDronServicio(dronRepo);
        ConsultarDronesUseCase consultarTodosUC = new ConsultarDronesServicio(dronRepo);
        ActualizarDronUseCase actualizarUC = new ActualizarDronServicio(dronRepo);
        EliminarDronUseCase eliminarUC = new EliminarDronServicio(dronRepo);

        // 3. Adaptador de entrada: el controlador recibe los casos de uso ya listos.
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/co/edu/poli/sw2/view/GestorDrones.fxml"));
        loader.setControllerFactory(tipo -> new MainController(
                crearUC, consultarUC, consultarTodosUC, actualizarUC, eliminarUC));
        Parent root = loader.load();

        ScrollPane contenedor = new ScrollPane(root);
        contenedor.setFitToWidth(true);
        contenedor.setFitToHeight(true);

        Scene scene = new Scene(contenedor);
        scene.getStylesheets().add(getClass().getResource("/co/edu/poli/sw2/css/style.css").toExternalForm());

        stage.setTitle("Gestion de Drones");
        stage.setScene(scene);
        stage.show();

        ajustarAPantalla(stage);
    }

    /**
     * Recorta la ventana al area visible de la pantalla y la centra.
     *
     * <p>La escena toma su tamano del contenido, que puede ser mas alto que el
     * monitor; sin este ajuste la barra de titulo quedaria fuera del area
     * visible y no se podria cerrar ni maximizar con el raton.</p>
     *
     * @param stage ventana principal ya mostrada, con su tamano calculado.
     */
    private void ajustarAPantalla(Stage stage) {
        Rectangle2D visible = Screen.getPrimary().getVisualBounds();
        stage.setWidth(Math.min(stage.getWidth(), visible.getWidth()));
        stage.setHeight(Math.min(stage.getHeight(), visible.getHeight()));
        stage.centerOnScreen();
    }

    /**
     * Cierra la conexion compartida a la base de datos al salir de la
     * aplicacion.
     *
     * @throws Exception si falla el cierre de la conexion.
     */
    @Override
    public void stop() throws Exception {
        ConexionBD.obtenerInstancia().cerrar();
    }

    /**
     * Punto de entrada del ejecutable; delega en JavaFX el arranque de la
     * aplicacion.
     *
     * @param args argumentos de linea de comandos (no se usan).
     */
    public static void main(String[] args) {
        launch(args);
    }
}
