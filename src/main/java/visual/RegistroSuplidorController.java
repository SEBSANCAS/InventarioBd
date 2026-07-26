package visual;

import DataBase.CiudadDAO;
import DataBase.SuplidorDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import logico.Ciudad;
import logico.Servicio;
import logico.Suplidor;
import logico.Telefono;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class RegistroSuplidorController {

    @FXML
    private TextField campoIdSuplidor;

    @FXML
    private TextField campoRazonComercial;

    @FXML
    private TextField campoNombreComercial;

    @FXML
    private ComboBox<String> comboTipoIdentificacion;

    @FXML
    private Label lblIdentificacion;

    @FXML
    private TextField campoIdentificacion;

    @FXML
    private TextField campoCorreo;

    @FXML
    private ComboBox<Ciudad> comboCiudad;

    @FXML
    private VBox contenedorTelefonos;

    @FXML
    private Label lblMensaje;

    private final List<FilaTelefono> filasTelefono = new ArrayList<>();

    private static final Pattern PATRON_RNC = Pattern.compile("^([0-9]{9}|[0-9]{11})$");
    private static final Pattern PATRON_CORREO = Pattern.compile("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$");

    private static class FilaTelefono {
        CheckBox checkPrincipal;
        TextField campoNumero;
        HBox contenedorFila;
    }

    @FXML
    public void initialize() {
        comboTipoIdentificacion.getItems().setAll("Rnc", "Tax_id_intl", "Otro");
        comboTipoIdentificacion.getSelectionModel().selectFirst();
        comboTipoIdentificacion.setOnAction(e -> actualizarPlaceholderIdentificacion());

        cargarCiudades();
        actualizarIdPreview();
        actualizarPlaceholderIdentificacion();
        limpiarFilasTelefono();
        agregarFilaTelefono();
    }

    private void cargarCiudades() {
        ArrayList<Ciudad> ciudadesBD = CiudadDAO.getInstance().EncontrarTodas();
        if (ciudadesBD != null && !ciudadesBD.isEmpty()) {
            comboCiudad.getItems().setAll(ciudadesBD);
            comboCiudad.getSelectionModel().selectFirst();
        }
    }

    private void actualizarIdPreview() {
        // Se obtiene el número correlativo desde Servicio (de forma idéntica a cliente)
        int siguiente = Servicio.getInstance().getGenIdSuplidor();
        campoIdSuplidor.setText(String.format("SUP%03d", siguiente));
    }

    private void actualizarPlaceholderIdentificacion() {
        String tipo = comboTipoIdentificacion.getValue();
        if (tipo == null) {
            return;
        }

        switch (tipo) {
            case "Rnc":
                lblIdentificacion.setText("RNC:");
                campoIdentificacion.setPromptText("000000000 (9 u 11 dígitos, sin guiones)");
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

        if (campoNombreComercial.getText() == null || campoNombreComercial.getText().trim().isEmpty()) {
            errores.append("- El nombre comercial es obligatorio.\n");
        }

        String tipoIdentificacion = comboTipoIdentificacion.getValue();
        if (tipoIdentificacion == null) {
            errores.append("- Debe seleccionar un tipo de identificación.\n");
        }

        String identificacion = campoIdentificacion.getText();
        if (identificacion == null || identificacion.trim().isEmpty()) {
            errores.append("- La identificación es obligatoria.\n");
        } else if ("Rnc".equals(tipoIdentificacion) && !PATRON_RNC.matcher(identificacion.trim()).matches()) {
            errores.append("- El RNC debe tener 9 u 11 dígitos numéricos, sin guiones ni espacios.\n");
        }

        if (campoCorreo.getText() != null && !campoCorreo.getText().trim().isEmpty()) {
            if (!PATRON_CORREO.matcher(campoCorreo.getText().trim()).matches()) {
                errores.append("- El correo no tiene un formato válido.\n");
            }
        }

        if (comboCiudad.getValue() == null) {
            errores.append("- Debe seleccionar una ciudad obligatoriamente.\n");
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

        String tipoIdentificacion = comboTipoIdentificacion.getValue();

        // 1. Obtención del ID desde la lógica del Servicio
        String idSuplidor = Servicio.getInstance().generarIdSuplidor();
        Ciudad ciudadSeleccionada = comboCiudad.getValue();

        // 2. Creación del objeto Suplidor con sus atributos
        Suplidor suplidor = new Suplidor(
                campoIdentificacion.getText().trim(),
                campoCorreo.getText() != null ? campoCorreo.getText().trim() : "",
                campoNombreComercial.getText().trim(),
                idSuplidor,
                campoRazonComercial.getText() != null ? campoRazonComercial.getText().trim() : "",
                ciudadSeleccionada.getIdCiudad(),
                LocalDate.now(),
                tipoIdentificacion
        );

        // 3. Creación y adición de los objetos Telefono
        int contador = 0;
        for (FilaTelefono fila : filasTelefono) {
            String numero = fila.campoNumero.getText();
            if (numero != null && !numero.trim().isEmpty()) {
                String idTelefono = Servicio.getInstance().generarIdDependiente(idSuplidor, contador);
                Telefono telefono = new Telefono(idTelefono, numero.trim(), fila.checkPrincipal.isSelected());
                suplidor.agregarTelefono(telefono);
                contador++;
            }
        }

        try {
            // 4. Guardado en la Base de Datos mediante el DAO
            SuplidorDAO.getInstance().guardar(suplidor);

            // 5. Limpiar formulario y actualizar IDs (se llama antes del mensaje para que el preview se actualice)
            limpiarFormulario();

            lblMensaje.setStyle("-fx-text-fill: #2e7d32; -fx-font-weight: bold;");
            lblMensaje.setText("¡Suplidor " + idSuplidor + " registrado correctamente! Siguiente ID: " + campoIdSuplidor.getText());

        } catch (Exception e) {
            lblMensaje.setStyle("-fx-text-fill: #b23b3b; -fx-font-weight: bold;");
            lblMensaje.setText("Error de inserción en BD: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void limpiarFormulario() {
        campoRazonComercial.clear();
        campoNombreComercial.clear();
        campoCorreo.clear();
        campoIdentificacion.clear();

        comboTipoIdentificacion.getSelectionModel().selectFirst();
        if (comboCiudad.getItems() != null && !comboCiudad.getItems().isEmpty()) {
            comboCiudad.getSelectionModel().selectFirst();
        }

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
        Stage stage = (Stage) campoNombreComercial.getScene().getWindow();
        stage.close();
    }
}