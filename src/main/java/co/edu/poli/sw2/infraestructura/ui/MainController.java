package co.edu.poli.sw2.infraestructura.ui;

import co.edu.poli.sw2.dominio.modelo.Agricultura;
import co.edu.poli.sw2.dominio.modelo.Drone;
import co.edu.poli.sw2.dominio.modelo.Vigilancia;
import co.edu.poli.sw2.aplicacion.puerto.entrada.ActualizarDronUseCase;
import co.edu.poli.sw2.aplicacion.puerto.entrada.ConsultarDronUseCase;
import co.edu.poli.sw2.aplicacion.puerto.entrada.ConsultarDronesUseCase;
import co.edu.poli.sw2.aplicacion.puerto.entrada.CrearDronUseCase;
import co.edu.poli.sw2.aplicacion.puerto.entrada.EliminarDronUseCase;
import co.edu.poli.sw2.aplicacion.puerto.salida.PersistenciaException;

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
 * traducir las acciones del usuario en llamadas a los puertos de entrada
 * (los casos de uso). No conoce la base de datos, ni el DAO, ni JDBC: para
 * el, al otro lado solo hay casos de uso.</p>
 *
 * <p>No tiene constructor vacio, asi que {@code App} lo crea con
 * {@code FXMLLoader.setControllerFactory}. Los metodos {@code on...} estan
 * enlazados a los botones desde {@code GestorDrones.fxml}.</p>
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

    /** Campo del identificador del dron. */
    @FXML
    private TextField txtId;
    /** Campo del numero de serie. */
    @FXML
    private TextField txtSerial;
    /** Campo del modelo. */
    @FXML
    private TextField txtModelo;
    /** Campo del fabricante. */
    @FXML
    private TextField txtFabricante;
    /** Campo del peso en kilogramos. */
    @FXML
    private TextField txtPeso;
    /** Selector del tipo de dron (agricultura o vigilancia). */
    @FXML
    private ComboBox<String> cbTipo;
    /** Etiqueta del campo de capacidad del tanque; solo visible para agricultura. */
    @FXML
    private Label lblCapacidadTanque;
    /** Campo de la capacidad del tanque en litros; solo visible para agricultura. */
    @FXML
    private TextField txtCapacidadTanque;
    /** Etiqueta de la deteccion termica; solo visible para vigilancia. */
    @FXML
    private Label lblDeteccionTermica;
    /** Casilla de deteccion termica; solo visible para vigilancia. */
    @FXML
    private CheckBox chkDeteccionTermica;

    /** Tabla con los drones consultados. */
    @FXML
    private TableView<Drone> tablaDrones;
    /** Columna del identificador. */
    @FXML
    private TableColumn<Drone, String> colId;
    /** Columna del numero de serie. */
    @FXML
    private TableColumn<Drone, String> colSerial;
    /** Columna del modelo. */
    @FXML
    private TableColumn<Drone, String> colModelo;
    /** Columna del fabricante. */
    @FXML
    private TableColumn<Drone, String> colFabricante;
    /** Columna del peso. */
    @FXML
    private TableColumn<Drone, Double> colPeso;
    /** Columna de la capacidad del tanque; vacia si el dron no es de agricultura. */
    @FXML
    private TableColumn<Drone, String> colCapacidadTanque;
    /** Columna de la deteccion termica; vacia si el dron no es de vigilancia. */
    @FXML
    private TableColumn<Drone, String> colDeteccionTermica;

    /** Caso de uso para registrar drones. */
    private final CrearDronUseCase crearDron;
    /** Caso de uso para consultar un dron por identificador. */
    private final ConsultarDronUseCase consultarDron;
    /** Caso de uso para listar todos los drones. */
    private final ConsultarDronesUseCase consultarDrones;
    /** Caso de uso para actualizar drones. */
    private final ActualizarDronUseCase actualizarDron;
    /** Caso de uso para eliminar drones. */
    private final EliminarDronUseCase eliminarDron;
    /** Lista observable enlazada a {@link #tablaDrones}. */
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

    /**
     * Lo invoca JavaFX despues de inyectar los controles. Configura las
     * columnas de la tabla, el selector de tipo y la seleccion de filas, y
     * carga los drones existentes.
     */
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

    /**
     * Boton "Crear": registra el dron del formulario. Avisa si el
     * identificador ya esta en uso.
     *
     * @param event evento del boton (no se usa).
     */
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
        } catch (IllegalArgumentException e) {
            mostrarAlerta(AlertType.WARNING, e.getMessage());
        } catch (PersistenciaException e) {
            mostrarAlerta(AlertType.ERROR, e.getMessage());
        }
    }

    /**
     * Boton "Consultar todos": recarga la tabla con todos los drones.
     *
     * @param event evento del boton (no se usa).
     */
    @FXML
    private void onConsultarTodos(ActionEvent event) {
        cargarDrones();
    }

    /**
     * Boton "Consultar por ID": muestra solo el dron con el identificador
     * escrito y llena el formulario con sus datos.
     *
     * @param event evento del boton (no se usa).
     */
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
        } catch (IllegalArgumentException e) {
            mostrarAlerta(AlertType.WARNING, e.getMessage());
        } catch (PersistenciaException e) {
            mostrarAlerta(AlertType.ERROR, e.getMessage());
        }
    }

    /**
     * Boton "Actualizar": guarda los cambios del formulario sobre el dron
     * con ese identificador.
     *
     * @param event evento del boton (no se usa).
     */
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
        } catch (IllegalArgumentException e) {
            mostrarAlerta(AlertType.WARNING, e.getMessage());
        } catch (PersistenciaException e) {
            mostrarAlerta(AlertType.ERROR, e.getMessage());
        }
    }

    /**
     * Boton "Eliminar": borra el dron con el identificador escrito.
     *
     * @param event evento del boton (no se usa).
     */
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
        } catch (IllegalArgumentException e) {
            mostrarAlerta(AlertType.WARNING, e.getMessage());
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

    /**
     * Consulta todos los drones y los muestra en la tabla. Si falla la
     * persistencia, muestra el error en un dialogo.
     */
    private void cargarDrones() {
        try {
            drones.setAll(consultarDrones.consultarTodos());
        } catch (PersistenciaException e) {
            mostrarAlerta(AlertType.ERROR, e.getMessage());
        }
    }

    /**
     * Texto de la columna de capacidad del tanque.
     *
     * @param drone dron de la fila.
     * @return la capacidad en litros, o vacio si el dron no es de agricultura.
     */
    private String capacidadTanqueDe(Drone drone) {
        if (drone instanceof Agricultura agricultura) {
            return String.valueOf(agricultura.getCapacidadTanque());
        }

        return "";
    }

    /**
     * Texto de la columna de deteccion termica.
     *
     * @param drone dron de la fila.
     * @return "Si" o "No", o vacio si el dron no es de vigilancia.
     */
    private String deteccionTermicaDe(Drone drone) {
        if (drone instanceof Vigilancia vigilancia) {
            return vigilancia.isDeteccionTermica() ? "Si" : "No";
        }

        return "";
    }

    /**
     * Muestra unicamente el campo propio de la especializacion elegida y
     * oculta el otro (sin reservarle espacio).
     *
     * @param tipo tipo elegido en {@link #cbTipo}, o {@code null} para ocultar ambos.
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

    /**
     * Copia los datos de un dron en los campos del formulario.
     *
     * @param drone dron a mostrar.
     */
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

    /**
     * Deja todos los campos del formulario vacios.
     */
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

    /**
     * Indica si un texto no tiene contenido.
     *
     * @param texto texto a revisar; puede ser {@code null}.
     * @return {@code true} si es {@code null} o solo tiene espacios.
     */
    private boolean esVacio(String texto) {
        return texto == null || texto.isBlank();
    }

    /**
     * Lee el identificador del formulario. Si esta vacio, avisa al usuario.
     *
     * @return el identificador sin espacios en los extremos, o {@code null} si falta.
     */
    private String parsearId() {
        String idTexto = txtId.getText();
        if (esVacio(idTexto)) {
            mostrarAlerta(AlertType.WARNING, "Debes indicar el ID del drone.");
            return null;
        }

        return idTexto.trim();
    }

    /**
     * Convierte el peso escrito a numero. Si no es valido, avisa al usuario.
     *
     * @param pesoTexto texto del campo de peso.
     * @return el peso en kilogramos, o {@code null} si no es numerico.
     */
    private Double parsearPeso(String pesoTexto) {
        try {
            return Double.parseDouble(pesoTexto.trim());
        } catch (NumberFormatException e) {
            mostrarAlerta(AlertType.WARNING, "El peso debe ser un valor numerico.");
            return null;
        }
    }

    /**
     * Lee y convierte la capacidad del tanque. Si falta o no es valida,
     * avisa al usuario.
     *
     * @return la capacidad en litros, o {@code null} si falta o no es numerica.
     */
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

    /**
     * Muestra un dialogo modal y espera a que el usuario lo cierre.
     *
     * @param tipo tipo de alerta (error, advertencia o informacion).
     * @param mensaje texto a mostrar.
     */
    private void mostrarAlerta(AlertType tipo, String mensaje) {
        new Alert(tipo, mensaje).showAndWait();
    }
}
