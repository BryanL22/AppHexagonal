package co.edu.poli.sw2.vista;

import co.edu.poli.sw2.aplicacion.servicio.DronServicio;
import co.edu.poli.sw2.infraestructura.persistencia.Conexion;
import co.edu.poli.sw2.infraestructura.persistencia.DronRepository;
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
 * de cada puerto. Aqui se crea el repositorio de salida
 * ({@link DronRepository}), se inyecta en el servicio de aplicacion
 * ({@link DronServicio}) y los casos de uso resultantes se le entregan al
 * controlador de la interfaz.</p>
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

    @Override
    public void start(Stage stage) throws IOException {
        // Adaptador de salida: implementa los cinco puertos de salida.
        DronRepository dronRepository = new DronRepository();

        // Servicio de aplicacion: implementa los cinco puertos de entrada.
        DronServicio dronServicio = new DronServicio(
                dronRepository, dronRepository, dronRepository, dronRepository, dronRepository);

        // Adaptador de entrada: el controlador recibe los casos de uso ya listos.
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/co/edu/poli/sw2/view/GestorDrones.fxml"));
        loader.setControllerFactory(tipo -> new MainController(
                dronServicio, dronServicio, dronServicio, dronServicio, dronServicio));
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

    @Override
    public void stop() throws Exception {
        Conexion.obtenerInstancia().cerrar();
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
