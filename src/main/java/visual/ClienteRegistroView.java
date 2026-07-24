package visual;

import DataBase.ClienteDAO;
import logico.Cliente;
import logico.Servicio;
import DataBase.ServicioDAO;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.time.LocalDate;

public class ClienteRegistroView extends BorderPane {

    private TextField txtIdCliente, txtNombresCliente, txtApellidosCliente, txtNumIdentificacion, txtTelCliente, txtEmailCliente;
    private ComboBox<String> cboTipoCliente, cboTipoIdentificacion;
    private DatePicker dpFechaRegistro;
    private Label lblApellidosCliente, lblStatusCliente;
    private Button btnGuardarCliente, btnLimpiarCliente, btnCancelarCliente;

    public ClienteRegistroView() {
        this.setPrefSize(700, 600);
        this.setStyle("-fx-background-color: #F4F6F9;");

        // Header
        this.setTop(crearHeader());

        // Formulario central
        this.setCenter(crearFormulario());

        // Cargar el ID que le toca al nuevo cliente
        actualizarSiguienteIdCliente();
    }

    private VBox crearHeader() {
        VBox header = new VBox(4);
        header.setPadding(new Insets(18, 30, 18, 30));
        header.setStyle("-fx-background-color: #1B4F72;");

        Label title = new Label("Registro de Cliente");
        title.setTextFill(Color.WHITE);
        title.setFont(Font.font("System", FontWeight.BOLD, 18));

        Label subtitle = new Label("Complete los datos para agregar un nuevo cliente al sistema.");
        subtitle.setTextFill(Color.web("#ECF0F1"));
        subtitle.setFont(Font.font("System", 12));

        header.getChildren().addAll(title, subtitle);
        return header;
    }

    private StackPane crearFormulario() {
        VBox card = new VBox(15);
        card.setMaxWidth(600);
        card.setPadding(new Insets(25));
        card.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 8px; -fx-border-radius: 8px; -fx-border-color: #E0E0E0;");

        GridPane grid = new GridPane();
        grid.setHgap(15);
        grid.setVgap(12);

        ColumnConstraints col1 = new ColumnConstraints(140);
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setHgrow(Priority.ALWAYS);
        grid.getColumnConstraints().addAll(col1, col2);

        // --- Campo ID Cliente (Deshabilitado pero mostrando el ID asignado) ---
        txtIdCliente = crearTextField("", true);
        txtIdCliente.setStyle("-fx-background-color: #EAEDED; -fx-border-color: #BDC3C7; -fx-border-radius: 4px; -fx-padding: 6px; -fx-font-weight: bold; -fx-text-fill: #1B4F72;");

        cboTipoCliente = new ComboBox<>();
        cboTipoCliente.getItems().addAll("Persona", "Empresa");
        cboTipoCliente.getSelectionModel().select("Persona");

        cboTipoIdentificacion = new ComboBox<>();
        cboTipoIdentificacion.getItems().addAll("Cedula", "Pasaporte", "Tax_id_intl", "Otro");
        cboTipoIdentificacion.getSelectionModel().select("Cedula");

        txtNombresCliente = crearTextField("Nombres o Razón Social", false);
        lblApellidosCliente = crearLabel("Apellidos:");
        txtApellidosCliente = crearTextField("Apellidos", false);
        txtNumIdentificacion = crearTextField("Ej: 40212345678", false);
        txtTelCliente = crearTextField("8090000000", false);
        txtEmailCliente = crearTextField("cliente@correo.com", false);
        dpFechaRegistro = new DatePicker(LocalDate.now());

        // Evento cambio de Persona / Empresa
        cboTipoCliente.setOnAction(e -> {
            boolean esEmpresa = "Empresa".equals(cboTipoCliente.getValue());
            cboTipoIdentificacion.getItems().clear();

            if (esEmpresa) {
                cboTipoIdentificacion.getItems().addAll("Rnc", "Tax_id_intl", "Otro");
                cboTipoIdentificacion.getSelectionModel().select("Rnc");
                lblApellidosCliente.setVisible(false);
                lblApellidosCliente.setManaged(false);
                txtApellidosCliente.setVisible(false);
                txtApellidosCliente.setManaged(false);
                txtApellidosCliente.clear();
            } else {
                cboTipoIdentificacion.getItems().addAll("Cedula", "Pasaporte", "Tax_id_intl", "Otro");
                cboTipoIdentificacion.getSelectionModel().select("Cedula");
                lblApellidosCliente.setVisible(true);
                lblApellidosCliente.setManaged(true);
                txtApellidosCliente.setVisible(true);
                txtApellidosCliente.setManaged(true);
            }
        });

        // Ensamblar Grid
        grid.add(crearLabel("ID Cliente:"), 0, 0); grid.add(txtIdCliente, 1, 0);
        grid.add(crearLabel("Tipo Cliente:"), 0, 1); grid.add(cboTipoCliente, 1, 1);
        grid.add(crearLabel("Tipo Documento:"), 0, 2); grid.add(cboTipoIdentificacion, 1, 2);
        grid.add(crearLabel("Nombres / Razón:"), 0, 3); grid.add(txtNombresCliente, 1, 3);
        grid.add(lblApellidosCliente, 0, 4); grid.add(txtApellidosCliente, 1, 4);
        grid.add(crearLabel("No. Identificación:"), 0, 5); grid.add(txtNumIdentificacion, 1, 5);
        grid.add(crearLabel("Teléfono:"), 0, 6); grid.add(txtTelCliente, 1, 6);
        grid.add(crearLabel("Correo:"), 0, 7); grid.add(txtEmailCliente, 1, 7);

        lblStatusCliente = new Label();
        lblStatusCliente.setVisible(false);

        btnCancelarCliente = crearBoton("Cancelar", "transparent", "#C0392B");
        btnLimpiarCliente = crearBoton("Limpiar", "#E0E0E0", "#2C3E50");
        btnGuardarCliente = crearBoton("Guardar", "#1B4F72", "#FFFFFF");

        btnCancelarCliente.setOnAction(e -> cerrar());
        btnLimpiarCliente.setOnAction(e -> limpiar());
        btnGuardarCliente.setOnAction(e -> guardar());

        HBox acciones = new HBox(10, btnCancelarCliente, btnLimpiarCliente, btnGuardarCliente);
        acciones.setAlignment(Pos.CENTER_RIGHT);

        card.getChildren().addAll(grid, lblStatusCliente, acciones);

        StackPane container = new StackPane(card);
        container.setPadding(new Insets(20));
        return container;
    }

    // --- MÉTODO PARA CALCULAR EL SIGUIENTE ID ---
    private void actualizarSiguienteIdCliente() {
        int siguienteId = Servicio.getInstance().getGenIdCliente();
        // Muestra el ID formateado (puedes cambiar "CLI-" por el formato que prefieras o dejar solo String.valueOf(siguienteId))
        txtIdCliente.setText("CLI-" + siguienteId);
    }

    private void guardar() {
        String idAsignado = txtIdCliente.getText().trim();
        String tipoCliente = cboTipoCliente.getValue();
        String tipoId = cboTipoIdentificacion.getValue();
        String nombres = txtNombresCliente.getText().trim();
        String apellidos = txtApellidosCliente.getText().trim();
        String numId = txtNumIdentificacion.getText().trim();
        String telefono = txtTelCliente.getText().trim();
        String correo = txtEmailCliente.getText().trim();

        if (nombres.isEmpty()) {
            mostrarMensajeStatus("El nombre es obligatorio.", true);
            return;
        }

        try {
            String apellidosFinales = "Empresa".equals(tipoCliente) ? null : apellidos;
            Cliente nuevoCliente = new Cliente(numId, correo, idAsignado, nombres, apellidosFinales, tipoCliente, tipoId);

            if (!telefono.isEmpty()) {
                nuevoCliente.agregarTelefono(new logico.Telefono("TEL-" + idAsignado, telefono, true));
            }

            // 1. Aumentamos el contador en Servicio para el PRÓXIMO registro
            int actual = Servicio.getInstance().getGenIdCliente();
            Servicio.getInstance().setGenIdCliente(actual + 1);

            // 2. Guardamos el cliente (registrarCliente internamente guarda el cliente Y los contadores nuevos en la BD)
            Servicio.getInstance().registrarCliente(nuevoCliente);

            mostrarMensajeStatus("¡Cliente " + idAsignado + " registrado con éxito!", false);

            // 3. Limpiamos y actualizamos la vista con el nuevo ID incrementado
            limpiar();

        } catch (Exception ex) {
            mostrarMensajeStatus("Error al guardar: " + ex.getMessage(), true);
            ex.printStackTrace();
        }
    }

    private void mostrarMensajeStatus(String mensaje, boolean esError) {
        lblStatusCliente.setText(mensaje);
        lblStatusCliente.setStyle(esError
                ? "-fx-text-fill: #E74C3C; -fx-font-weight: bold;"
                : "-fx-text-fill: #27AE60; -fx-font-weight: bold;");
        lblStatusCliente.setVisible(true);
    }

    private void limpiar() {
        txtNombresCliente.clear();
        txtApellidosCliente.clear();
        txtNumIdentificacion.clear();
        txtTelCliente.clear();
        txtEmailCliente.clear();

        // Al limpiar, se vuelve a calcular el ID asignado
        actualizarSiguienteIdCliente();
    }

    private void cerrar() {
        Stage stage = (Stage) this.getScene().getWindow();
        stage.close();
    }

    private Label crearLabel(String text) {
        Label l = new Label(text);
        l.setFont(Font.font("System", FontWeight.BOLD, 12));
        l.setTextFill(Color.web("#2C3E50"));
        return l;
    }

    private TextField crearTextField(String prompt, boolean disabled) {
        TextField tf = new TextField();
        tf.setPromptText(prompt);
        tf.setDisable(disabled);
        tf.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #BDC3C7; -fx-border-radius: 4px; -fx-padding: 6px;");
        return tf;
    }

    private Button crearBoton(String texto, String bg, String color) {
        Button b = new Button(texto);
        b.setStyle(String.format("-fx-background-color: %s; -fx-text-fill: %s; -fx-font-weight: bold; -fx-cursor: hand; -fx-padding: 6 14;", bg, color));
        return b;
    }
}