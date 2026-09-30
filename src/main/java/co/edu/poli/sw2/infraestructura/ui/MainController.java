package co.edu.poli.sw2.infraestructura.ui;

import co.edu.poli.sw2.dominio.modelo.Agricultura;
import co.edu.poli.sw2.dominio.modelo.Drone;
import co.edu.poli.sw2.dominio.modelo.Vigilancia;
import co.edu.poli.sw2.dominio.puerto.entrada.ActualizarDronUseCase;
import co.edu.poli.sw2.dominio.puerto.entrada.ConsultarDronUseCase;
import co.edu.poli.sw2.dominio.puerto.entrada.ConsultarDronesUseCase;
import co.edu.poli.sw2.dominio.puerto.entrada.CrearDronUseCase;
import co.edu.poli.sw2.dominio.puerto.entrada.EliminarDronUseCase;
import co.edu.poli.sw2.dominio.puerto.salida.PersistenciaException;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * Adaptador de entrada (driving adapter): la interfaz JavaFX.
 *
 * <p>Su unica responsabilidad es leer y escribir los controles de la vista y
 * traducir las acciones del usuario en llamadas al puerto de entrada
 * los casos de uso. No conoce la base de datos, ni el DAO, ni
 * JDBC: para el, al otro lado solo hay casos de uso.</p>
 *
 * <p>Los casos de uso llegan por constructor, no se instancian aqui. Eso permite
 * cambiar la implementacion sin tocar la vista y mantiene la dependencia
 * apuntando hacia adentro del hexagono.</p>
 */
public class MainController {

    /** Tipo de dron especializado en agricultura. */
    public static final String TIPO_AGRICULTURA = "Agricultura";

    /** Tipo de dron especializado en vigilancia. */
    public static final String TIPO_VIGILANCIA = "Vigilancia";

    @FXML
    private TextField txtId;
    @FXML
    private TextField txtSerial;
    @FXML
    private TextField txtModelo;
    @FXML
    private TextField txtFabricante;
    @FXML
    private TextField txtPeso;
    @FXML
    private ComboBox<String> cbTipo;
    @FXML
    private Label lblCapacidadTanque;
    @FXML
    private TextField txtCapacidadTanque;
    @FXML
    private Label lblDeteccionTermica;
    @FXML
    private CheckBox chkDeteccionTermica;

    @FXML
    private TableView<Drone> tablaDrones;
    @FXML
    private TableColumn<Drone, String> colId;
    @FXML
    private TableColumn<Drone, String> colSerial;
    @FXML
    private TableColumn<Drone, String> colModelo;
    @FXML
    private TableColumn<Drone, String> colFabricante;
    @FXML
    private TableColumn<Drone, Double> colPeso;
    @FXML
    private TableColumn<Drone, String> colCapacidadTanque;
    @FXML
    private TableColumn<Drone, String> colDeteccionTermica;

    private final CrearDronUseCase crearDron;
    private final ConsultarDronUseCase consultarDron;
    private final ConsultarDronesUseCase consultarDrones;
    private final ActualizarDronUseCase actualizarDron;
    private final EliminarDronUseCase eliminarDron;
    private final ObservableList<Drone> drones = FXCollections.observableArrayList();

    /**
     * Crea el controlador con los cinco casos de uso que puede disparar.
     *
     * @param crearDron caso de uso de registro.
     * @param consultarDron caso de uso de consulta por identificador.
     * @param consultarDrones caso de uso de listado.
     * @param actualizarDron caso de uso de actualizacion.
     * @param eliminarDron caso de uso de eliminacion.
     */
    public MainController(CrearDronUseCase crearDron, ConsultarDronUseCase consultarDron,
            ConsultarDronesUseCase consultarDrones, ActualizarDronUseCase actualizarDron,
            EliminarDronUseCase eliminarDron) {
        this.crearDron = crearDron;
        this.consultarDron = consultarDron;
        this.consultarDrones = consultarDrones;
        this.actualizarDron = actualizarDron;
        this.eliminarDron = eliminarDron;
    }

    @FXML
    private void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colSerial.setCellValueFactory(new PropertyValueFactory<>("serial"));
        colModelo.setCellValueFactory(new PropertyValueFactory<>("modelo"));
        colFabricante.setCellValueFactory(new PropertyValueFactory<>("fabricante"));
        colPeso.setCellValueFactory(new PropertyValueFactory<>("peso"));
        colCapacidadTanque.setCellValueFactory(datos -> new SimpleStringProperty(capacidadTanqueDe(datos.getValue())));
        colDeteccionTermica
                .setCellValueFactory(datos -> new SimpleStringProperty(deteccionTermicaDe(datos.getValue())));

        cbTipo.setItems(FXCollections.observableArrayList(TIPO_AGRICULTURA, TIPO_VIGILANCIA));
        cbTipo.valueProperty().addListener((observable, anterior, nuevoTipo) -> mostrarCamposDeTipo(nuevoTipo));
        mostrarCamposDeTipo(null);

        tablaDrones.setItems(drones);
        tablaDrones.getSelectionModel().selectedItemProperty()
                .addListener((observable, anterior, seleccionado) -> {
                    if (seleccionado != null) {
                        llenarFormulario(seleccionado);
                    }
                });

        cargarDrones();
    }

    @FXML
    private void onCrear(ActionEvent event) {
        Drone drone = leerFormulario();
        if (drone == null) {
            return;
        }

        try {
            if (crearDron.crear(drone)) {
                limpiarFormulario();
                cargarDrones();
            } else {
                mostrarAlerta(AlertType.WARNING,
                        "Ya existe un drone con el ID '" + drone.getId() + "'. Elige otro.");
            }
        } catch (PersistenciaException e) {
            mostrarAlerta(AlertType.ERROR, e.getMessage());
        }
    }

    @FXML
    private void onConsultarTodos(ActionEvent event) {
        cargarDrones();
    }

    @FXML
    private void onConsultarPorId(ActionEvent event) {
        String id = parsearId();
        if (id == null) {
            return;
        }

        try {
            Drone drone = consultarDron.consultar(id);
            if (drone == null) {
                mostrarAlerta(AlertType.INFORMATION, "No existe un drone con el ID '" + id + "'.");
                return;
            }

            drones.setAll(drone);
            llenarFormulario(drone);
        } catch (PersistenciaException e) {
            mostrarAlerta(AlertType.ERROR, e.getMessage());
        }
    }

    @FXML
    private void onActualizar(ActionEvent event) {
        Drone drone = leerFormulario();
        if (drone == null) {
            return;
        }

        try {
            if (actualizarDron.actualizar(drone)) {
                limpiarFormulario();
                cargarDrones();
            } else {
                mostrarAlerta(AlertType.ERROR,
                        "No se pudo actualizar. Verifica que el ID exista y que el tipo coincida con el registrado.");
            }
        } catch (PersistenciaException e) {
            mostrarAlerta(AlertType.ERROR, e.getMessage());
        }
    }

    @FXML
    private void onEliminar(ActionEvent event) {
        String id = parsearId();
        if (id == null) {
            return;
        }

        try {
            if (eliminarDron.eliminar(id)) {
                limpiarFormulario();
                cargarDrones();
            } else {
                mostrarAlerta(AlertType.ERROR, "No se pudo eliminar el drone. Verifica que el ID exista.");
            }
        } catch (PersistenciaException e) {
            mostrarAlerta(AlertType.ERROR, e.getMessage());
        }
    }

    /**
     * Lee y valida el formulario completo y arma el dron del tipo elegido.
     *
     * @return el dron construido, o {@code null} si falta o falla algun dato.
     */
    private Drone leerFormulario() {
        String id = txtId.getText();
        String serial = txtSerial.getText();
        String modelo = txtModelo.getText();
        String fabricante = txtFabricante.getText();
        String pesoTexto = txtPeso.getText();
        String tipo = cbTipo.getValue();

        if (esVacio(id) || esVacio(serial) || esVacio(modelo) || esVacio(fabricante) || esVacio(pesoTexto)) {
            mostrarAlerta(AlertType.WARNING, "Todos los campos son obligatorios.");
            return null;
        }

        if (esVacio(tipo)) {
            mostrarAlerta(AlertType.WARNING, "Selecciona el tipo de drone (Agricultura o Vigilancia).");
            return null;
        }

        Double peso = parsearPeso(pesoTexto);
        if (peso == null) {
            return null;
        }

        if (TIPO_AGRICULTURA.equals(tipo)) {
            Double capacidadTanque = parsearCapacidadTanque();
            if (capacidadTanque == null) {
                return null;
            }

            return new Agricultura(id.trim(), serial, modelo, fabricante, peso, capacidadTanque);
        }

        return new Vigilancia(id.trim(), serial, modelo, fabricante, peso, chkDeteccionTermica.isSelected());
    }

    private void cargarDrones() {
        try {
            drones.setAll(consultarDrones.consultarTodos());
        } catch (PersistenciaException e) {
            mostrarAlerta(AlertType.ERROR, e.getMessage());
        }
    }

    /**
     * Capacidad del tanque para mostrar en la tabla; vacio si el dron no es
     * de agricultura.
     */
    private String capacidadTanqueDe(Drone drone) {
        if (drone instanceof Agricultura agricultura) {
            return String.valueOf(agricultura.getCapacidadTanque());
        }

        return "";
    }

    /**
     * Deteccion termica para mostrar en la tabla; vacio si el dron no es de
     * vigilancia.
     */
    private String deteccionTermicaDe(Drone drone) {
        if (drone instanceof Vigilancia vigilancia) {
            return vigilancia.isDeteccionTermica() ? "Si" : "No";
        }

        return "";
    }

    /**
     * Muestra unicamente el campo propio de la especializacion elegida.
     */
    private void mostrarCamposDeTipo(String tipo) {
        boolean esAgricultura = TIPO_AGRICULTURA.equals(tipo);
        boolean esVigilancia = TIPO_VIGILANCIA.equals(tipo);

        lblCapacidadTanque.setVisible(esAgricultura);
        lblCapacidadTanque.setManaged(esAgricultura);
        txtCapacidadTanque.setVisible(esAgricultura);
        txtCapacidadTanque.setManaged(esAgricultura);

        lblDeteccionTermica.setVisible(esVigilancia);
        lblDeteccionTermica.setManaged(esVigilancia);
        chkDeteccionTermica.setVisible(esVigilancia);
        chkDeteccionTermica.setManaged(esVigilancia);
    }

    private void llenarFormulario(Drone drone) {
        txtId.setText(drone.getId());
        txtSerial.setText(drone.getSerial());
        txtModelo.setText(drone.getModelo());
        txtFabricante.setText(drone.getFabricante());
        txtPeso.setText(String.valueOf(drone.getPeso()));

        if (drone instanceof Agricultura agricultura) {
            cbTipo.setValue(TIPO_AGRICULTURA);
            txtCapacidadTanque.setText(String.valueOf(agricultura.getCapacidadTanque()));
        } else if (drone instanceof Vigilancia vigilancia) {
            cbTipo.setValue(TIPO_VIGILANCIA);
            chkDeteccionTermica.setSelected(vigilancia.isDeteccionTermica());
        } else {
            cbTipo.setValue(null);
        }
    }

    private void limpiarFormulario() {
        txtId.clear();
        txtSerial.clear();
        txtModelo.clear();
        txtFabricante.clear();
        txtPeso.clear();
        txtCapacidadTanque.clear();
        chkDeteccionTermica.setSelected(false);
        cbTipo.setValue(null);
    }

    private boolean esVacio(String texto) {
        return texto == null || texto.isBlank();
    }

    private String parsearId() {
        String idTexto = txtId.getText();
        if (esVacio(idTexto)) {
            mostrarAlerta(AlertType.WARNING, "Debes indicar el ID del drone.");
            return null;
        }

        return idTexto.trim();
    }

    private Double parsearPeso(String pesoTexto) {
        try {
            return Double.parseDouble(pesoTexto.trim());
        } catch (NumberFormatException e) {
            mostrarAlerta(AlertType.WARNING, "El peso debe ser un valor numerico.");
            return null;
        }
    }

    private Double parsearCapacidadTanque() {
        String texto = txtCapacidadTanque.getText();
        if (esVacio(texto)) {
            mostrarAlerta(AlertType.WARNING, "Debes indicar la capacidad del tanque.");
            return null;
        }

        try {
            return Double.parseDouble(texto.trim());
        } catch (NumberFormatException e) {
            mostrarAlerta(AlertType.WARNING, "La capacidad del tanque debe ser un valor numerico.");
            return null;
        }
    }

    private void mostrarAlerta(AlertType tipo, String mensaje) {
        new Alert(tipo, mensaje).showAndWait();
    }
}
