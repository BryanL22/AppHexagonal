package co.edu.poli.sw2.infrastructure.in.ui;

import co.edu.poli.sw2.application.DroneService;
import co.edu.poli.sw2.domain.port.in.GestionarDronesUseCase;
import co.edu.poli.sw2.domain.port.out.DroneRepositoryPort;
import co.edu.poli.sw2.infrastructure.out.persistence.Conexion;
import co.edu.poli.sw2.infrastructure.out.persistence.DroneJdbcAdapter;

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
 * Punto de entrada de la aplicacion y <em>composition root</em> del hexagono.
 *
 * <p>Es el unico lugar donde se decide que implementacion concreta usa cada
 * puerto: aqui se crea el adaptador de persistencia, se inyecta en el
 * servicio de aplicacion y el caso de uso resultante se le entrega al
 * controlador de JavaFX. Ninguna otra clase instancia sus dependencias, y por
 * eso cambiar de base de datos o de interfaz solo obliga a tocar este
 * archivo.</p>
 */
public class Main extends Application {

    /**
     * Crea la aplicacion. No recibe dependencias: JavaFX la instancia por
     * reflexion al invocar {@link #launch(String...)}.
     */
    public Main() {
    }

    @Override
    public void start(Stage stage) throws IOException {
        // Cableado del hexagono: adaptador de salida -> caso de uso -> adaptador de entrada.
        DroneRepositoryPort droneRepository = new DroneJdbcAdapter();
        GestionarDronesUseCase gestionarDrones = new DroneService(droneRepository);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/co/edu/poli/sw2/view/GestorDrones.fxml"));
        loader.setControllerFactory(tipo -> new MainController(gestionarDrones));
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
     * <p>La escena toma su tamano del contenido, que puede ser mas alto que
     * el monitor; sin este ajuste la barra de titulo quedaria fuera del area
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
