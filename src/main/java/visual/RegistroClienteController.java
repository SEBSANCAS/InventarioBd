package visual;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import logico.Cliente;
import logico.Servicio;
import logico.Telefono;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class RegistroClienteController {

    @FXML
    private TextField campoIdCliente;

    @FXML
    private ComboBox<String> comboTipoCliente;

    @FXML
    private TextField campoNombres;

    @FXML
    private Label lblApellidos;

    @FXML
    private TextField campoApellidos;

    // NUEVOS COMPONENTES PARA EL GÉNERO
    @FXML
    private Label lblGenero;

    @FXML
    private HBox contenedorGenero;

    @FXML
    private ToggleGroup grupoGenero;

    @FXML
    private RadioButton rbMasculino;

    @FXML
    private RadioButton rbFemenino;

    @FXML
    private RadioButton rbOtro;

    @FXML
    private ComboBox<String> comboTipoIdentificacion;

    @FXML
    private Label lblIdentificacion;

    @FXML
    private TextField campoIdentificacion;

    @FXML
    private TextField campoCorreo;

    @FXML
    private TextField campoDireccion;

    @FXML
    private VBox contenedorTelefonos;

    @FXML
    private Label lblMensaje;

    private final List<FilaTelefono> filasTelefono = new ArrayList<>();

    private static final Pattern PATRON_CEDULA = Pattern.compile("^[0-9]{11}$");
    private static final Pattern PATRON_RNC = Pattern.compile("^([0-9]{9}|[0-9]{11})$");
    private static final Pattern PATRON_CORREO = Pattern.compile("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$");

    private static class FilaTelefono {
        CheckBox checkPrincipal;
        TextField campoNumero;
        HBox contenedorFila;
    }

    @FXML
    public void initialize() {
        comboTipoCliente.getItems().setAll("Persona", "Empresa");
        comboTipoCliente.getSelectionModel().select("Persona");
        comboTipoCliente.setOnAction(e -> actualizarSegunTipoCliente());
        comboTipoIdentificacion.setOnAction(e -> actualizarPlaceholderIdentificacion());

        actualizarIdPreview();
        actualizarSegunTipoCliente();
        limpiarFilasTelefono();
        agregarFilaTelefono();
    }

    private void actualizarIdPreview() {
        int siguiente = Servicio.getInstance().getGenIdCliente();
        campoIdCliente.setText(String.format("CLI%03d", siguiente));
    }

    private void actualizarSegunTipoCliente() {
        String tipo = comboTipoCliente.getValue();
        boolean esEmpresa = "Empresa".equals(tipo);

        // Ocultar apellidos si es empresa
        lblApellidos.setVisible(!esEmpresa);
        lblApellidos.setManaged(!esEmpresa);
        campoApellidos.setVisible(!esEmpresa);
        campoApellidos.setManaged(!esEmpresa);

        // Ocultar género si es empresa
        lblGenero.setVisible(!esEmpresa);
        lblGenero.setManaged(!esEmpresa);
        contenedorGenero.setVisible(!esEmpresa);
        contenedorGenero.setManaged(!esEmpresa);

        if (esEmpresa) {
            campoApellidos.clear();
            if (grupoGenero != null) {
                grupoGenero.selectToggle(null); // Limpiar selección de género
            }
        }

        comboTipoIdentificacion.getItems().clear();
        if (esEmpresa) {
            comboTipoIdentificacion.getItems().addAll("Rnc", "Tax_id_intl", "Otro");
        } else {
            comboTipoIdentificacion.getItems().addAll("Cedula", "Pasaporte", "Tax_id_intl", "Otro");
        }
        comboTipoIdentificacion.getSelectionModel().selectFirst();
        actualizarPlaceholderIdentificacion();
    }

    private void actualizarPlaceholderIdentificacion() {
        String tipo = comboTipoIdentificacion.getValue();
        if (tipo == null) {
            return;
        }

        switch (tipo) {
            case "Cedula":
                lblIdentificacion.setText("Cédula:");
                campoIdentificacion.setPromptText("000-0000000-0 (11 dígitos, sin guiones)");
                break;
            case "Rnc":
                lblIdentificacion.setText("RNC:");
                campoIdentificacion.setPromptText("000000000 (9 u 11 dígitos, sin guiones)");
                break;
            case "Pasaporte":
                lblIdentificacion.setText("Pasaporte:");
                campoIdentificacion.setPromptText("Ej: AB1234567");
                break;
            case "Tax_id_intl":
                lblIdentificacion.setText("Tax ID Internacional:");
                campoIdentificacion.setPromptText("Ej: US123456789");
                break;
            default:
                lblIdentificacion.setText("Identificación:");
                campoIdentificacion.setPromptText("Número de identificación");
        }
        campoIdentificacion.clear();
    }

    @FXML
    private void ControlarAgregarTelefono(ActionEvent event) {
        agregarFilaTelefono();
    }

    private void agregarFilaTelefono() {
        FilaTelefono fila = new FilaTelefono();

        CheckBox check = new CheckBox();
        check.setStyle("-fx-mark-color: #1c3f6e;");

        TextField campo = new TextField();
        campo.setPromptText("(809) 000-0000");
        campo.setPrefWidth(260);
        campo.setStyle("-fx-border-color: #AEDFF7; -fx-border-radius: 8; -fx-background-radius: 8; -fx-padding: 8;");

        Button btnQuitar = new Button("－");
        btnQuitar.setStyle("-fx-background-color: transparent; -fx-text-fill: #b23b3b; -fx-font-weight: bold;");

        fila.checkPrincipal = check;
        fila.campoNumero = campo;

        check.setOnAction(e -> {
            if (check.isSelected()) {
                for (FilaTelefono f : filasTelefono) {
                    if (f != fila) {
                        f.checkPrincipal.setSelected(false);
                    }
                }
            }
        });

        btnQuitar.setOnAction(e -> {
            if (filasTelefono.size() > 1) {
                contenedorTelefonos.getChildren().remove(fila.contenedorFila);
                filasTelefono.remove(fila);
                boolean hayPrincipal = filasTelefono.stream().anyMatch(f -> f.checkPrincipal.isSelected());
                if (!hayPrincipal) {
                    filasTelefono.get(0).checkPrincipal.setSelected(true);
                }
            }
        });

        HBox filaHBox = new HBox(10, check, campo, btnQuitar);
        filaHBox.setAlignment(Pos.CENTER_LEFT);
        fila.contenedorFila = filaHBox;

        if (filasTelefono.isEmpty()) {
            check.setSelected(true);
        }

        filasTelefono.add(fila);
        contenedorTelefonos.getChildren().add(filaHBox);
    }

    private void limpiarFilasTelefono() {
        filasTelefono.clear();
        contenedorTelefonos.getChildren().clear();
    }

    private boolean validarCampos() {
        StringBuilder errores = new StringBuilder();

        if (campoNombres.getText() == null || campoNombres.getText().trim().isEmpty()) {
            errores.append("- El nombre es obligatorio.\n");
        }

        boolean esEmpresa = "Empresa".equals(comboTipoCliente.getValue());

        // Validación estricta para Personas (Apellidos y Género)
        if (!esEmpresa) {
            if (campoApellidos.getText() == null || campoApellidos.getText().trim().isEmpty()) {
                errores.append("- El apellido es obligatorio para clientes tipo Persona.\n");
            }
            if (grupoGenero == null || grupoGenero.getSelectedToggle() == null) {
                errores.append("- Debe seleccionar un género para clientes tipo Persona.\n");
            }
        }

        String tipoIdentificacion = comboTipoIdentificacion.getValue();
        if (tipoIdentificacion == null) {
            errores.append("- Debe seleccionar un tipo de identificación.\n");
        }

        String identificacion = campoIdentificacion.getText();
        if (identificacion == null || identificacion.trim().isEmpty()) {
            errores.append("- La identificación es obligatoria.\n");
        } else if ("Cedula".equals(tipoIdentificacion) && !PATRON_CEDULA.matcher(identificacion.trim()).matches()) {
            errores.append("- La cédula debe tener exactamente 11 dígitos numéricos, sin guiones ni espacios.\n");
        } else if ("Rnc".equals(tipoIdentificacion) && !PATRON_RNC.matcher(identificacion.trim()).matches()) {
            errores.append("- El RNC debe tener 9 u 11 dígitos numéricos, sin guiones ni espacios.\n");
        }

        if (campoCorreo.getText() == null || campoCorreo.getText().trim().isEmpty()) {
            errores.append("- El correo es obligatorio.\n");
        } else if (!PATRON_CORREO.matcher(campoCorreo.getText().trim()).matches()) {
            errores.append("- El correo no tiene un formato válido.\n");
        }

        long telefonosConNumero = filasTelefono.stream()
                .filter(f -> f.campoNumero.getText() != null && !f.campoNumero.getText().trim().isEmpty())
                .count();
        if (telefonosConNumero == 0) {
            errores.append("- Debe ingresar al menos un teléfono.\n");
        }

        long principalesMarcados = filasTelefono.stream()
                .filter(f -> f.checkPrincipal.isSelected()
                        && f.campoNumero.getText() != null
                        && !f.campoNumero.getText().trim().isEmpty())
                .count();
        if (telefonosConNumero > 0 && principalesMarcados != 1) {
            errores.append("- Debe marcar exactamente un teléfono como principal.\n");
        }

        if (errores.length() > 0) {
            lblMensaje.setStyle("-fx-text-fill: #b23b3b;");
            lblMensaje.setText(errores.toString());
            return false;
        }
        return true;
    }

    @FXML
    private void ControlarGuardar(ActionEvent event) {
        if (!validarCampos()) {
            return;
        }

        boolean esEmpresa = "Empresa".equals(comboTipoCliente.getValue());
        String tipoIdentificacion = comboTipoIdentificacion.getValue();
        String idCliente = Servicio.getInstance().generarIdCliente();

        // Extracción del valor del género seleccionado
        String generoSeleccionado = null;
        if (!esEmpresa && grupoGenero != null && grupoGenero.getSelectedToggle() != null) {
            RadioButton seleccionado = (RadioButton) grupoGenero.getSelectedToggle();
            generoSeleccionado = seleccionado.getText(); // Captura "Masculino", "Femenino" u "Otro"
        }

        // Se usa el constructor actualizado con los 8 parámetros
        Cliente cliente = new Cliente(
                campoIdentificacion.getText().trim(),
                campoCorreo.getText().trim(),
                idCliente,
                campoNombres.getText().trim(),
                esEmpresa ? null : campoApellidos.getText().trim(),
                generoSeleccionado, // Se inyecta el género
                esEmpresa ? "Empresa" : "Persona",
                tipoIdentificacion
        );

        int contador = 0;
        for (FilaTelefono fila : filasTelefono) {
            String numero = fila.campoNumero.getText();
            if (numero != null && !numero.trim().isEmpty()) {
                String idTelefono = Servicio.getInstance().generarIdDependiente(idCliente, contador);
                Telefono telefono = new Telefono(idTelefono, numero.trim(), fila.checkPrincipal.isSelected());
                cliente.agregarTelefono(telefono);
                contador++;
            }
        }

        Servicio.getInstance().registrarCliente(cliente);

        limpiarFormulario();

        lblMensaje.setStyle("-fx-text-fill: #2e7d32;");
        lblMensaje.setText("Cliente " + idCliente + " registrado correctamente. Siguiente ID: " + campoIdCliente.getText());
    }

    private void limpiarFormulario() {
        campoNombres.clear();
        campoApellidos.clear();
        campoCorreo.clear();
        campoIdentificacion.clear();

        if (grupoGenero != null) {
            grupoGenero.selectToggle(null);
        }

        comboTipoCliente.getSelectionModel().select("Persona");
        actualizarSegunTipoCliente();

        limpiarFilasTelefono();
        agregarFilaTelefono();

        actualizarIdPreview();
    }

    @FXML
    private void ControlarLimpiar(ActionEvent event) {
        limpiarFormulario();
        lblMensaje.setText("");
    }

    @FXML
    private void ControlarCancelar(ActionEvent event) {
        Stage stage = (Stage) campoNombres.getScene().getWindow();
        stage.close();
    }
}