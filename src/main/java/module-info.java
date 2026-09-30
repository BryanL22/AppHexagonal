/**
 * Modulo de la aplicacion de gestion de drones con arquitectura hexagonal.
 */
module co.edu.poli.sw2 {

    // ── Dependencias de JavaFX ────────────────────────────────────────────
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;

    // ── Dependencias de infraestructura ──────────────────────────────────
    // El driver de MySQL no se declara: DriverManager lo encuentra solo.
    requires java.sql;

    // ── Apertura al framework JavaFX ─────────────────────────────────────
    // vista: App necesita ser instanciada por javafx.graphics
    opens co.edu.poli.sw2.vista to javafx.fxml, javafx.graphics;

    // ui: MainController necesita que javafx.fxml inyecte los campos @FXML
    opens co.edu.poli.sw2.infraestructura.ui to javafx.fxml;

    // dominio: la tabla lee los getters de Drone con PropertyValueFactory
    opens co.edu.poli.sw2.dominio.modelo to javafx.base;

    // ── Exportaciones ────────────────────────────────────────────────────
    exports co.edu.poli.sw2.vista;
}
