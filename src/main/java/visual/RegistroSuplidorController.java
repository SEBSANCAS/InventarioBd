package visual;

import DataBase.DatabaseConnection;
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
import logico.Servicio;
import logico.Suplidor;
import logico.Telefono;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    private ComboBox<String> comboPais;

    @FXML
    private ComboBox<String> comboCiudad;

    @FXML
    private ComboBox<String> comboCalle;

    @FXML
    private VBox contenedorTelefonos;

    @FXML
    private Label lblMensaje;

    private final List<FilaTelefono> filasTelefono = new ArrayList<>();

    private final Map<String, String> mapaPaises = new HashMap<>();
    private final Map<String, String> mapaCiudades = new HashMap<>();
    private final Map<String, String> mapaCalles = new HashMap<>();

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
        comboTipoIdentificacion.getItems().addAll("Rnc", "Tax_id_intl", "Otro");
        comboTipoIdentificacion.getSelectionModel().selectFirst();
        comboTipoIdentificacion.setOnAction(e -> actualizarPlaceholderIdentificacion());

        comboPais.setOnAction(e -> {
            String nombrePais = comboPais.getValue();
            if (nombrePais != null) {
                cargarCiudades(mapaPaises.get(nombrePais));
            }
        });

        comboCiudad.setOnAction(e -> {
            String nombreCiudad = comboCiudad.getValue();
            if (nombreCiudad != null) {
                cargarCalles(mapaCiudades.get(nombreCiudad));
            }
        });

        actualizarPlaceholderIdentificacion();
        actualizarIdPreview();
        limpiarFilasTelefono();
        agregarFilaTelefono();
        cargarPaises();
    }

    private void actualizarIdPreview() {
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

    private void cargarPaises() {
        mapaPaises.clear();
        comboPais.getItems().clear();
        comboCiudad.getItems().clear();
        comboCalle.getItems().clear();

        String sql = "SELECT id_pais, nombre_pais FROM Pais";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                String id = rs.getString("id_pais");
                String nombre = rs.getString("nombre_pais");
                mapaPaises.put(nombre, id);
                comboPais.getItems().add(nombre);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void cargarCiudades(String idPais) {
        mapaCiudades.clear();
        comboCiudad.getItems().clear();
        comboCalle.getItems().clear();

        String sql = "SELECT id_ciudad, nombre_ciudad FROM Ciudad WHERE id_pais = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, idPais);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String id = rs.getString("id_ciudad");
                    String nombre = rs.getString("nombre_ciudad");
                    mapaCiudades.put(nombre, id);
                    comboCiudad.getItems().add(nombre);
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void cargarCalles(String idCiudad) {
        mapaCalles.clear();
        comboCalle.getItems().clear();

        String sql = "SELECT id_calle, nombre_calle FROM Calle WHERE id_ciudad = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, idCiudad);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String id = rs.getString("id_calle");
                    String nombre = rs.getString("nombre_calle");
                    mapaCalles.put(nombre, id);
                    comboCalle.getItems().add(nombre);
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
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
        campo.setPrefWidth(300);
        campo.setPrefHeight(34);
        campo.setMaxWidth(420);
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

        if (campoRazonComercial.getText() == null || campoRazonComercial.getText().trim().isEmpty()) {
            errores.append("- La razón comercial es obligatoria.\n");
        }

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
        } else if ("Cedula".equals(tipoIdentificacion) && !PATRON_CEDULA.matcher(identificacion.trim()).matches()) {
            errores.append("- La cédula debe tener exactamente 11 dígitos numéricos, sin guiones.\n");
        } else if ("Rnc".equals(tipoIdentificacion) && !PATRON_RNC.matcher(identificacion.trim()).matches()) {
            errores.append("- El RNC debe tener 9 u 11 dígitos numéricos, sin guiones.\n");
        }

        if (campoCorreo.getText() == null || campoCorreo.getText().trim().isEmpty()) {
            errores.append("- El correo es obligatorio.\n");
        } else if (!PATRON_CORREO.matcher(campoCorreo.getText().trim()).matches()) {
            errores.append("- El correo no tiene un formato valido.\n");
        }

        if (comboPais.getValue() == null) {
            errores.append("- Debe seleccionar un país.\n");
        }
        if (comboCiudad.getValue() == null) {
            errores.append("- Debe seleccionar una ciudad.\n");
        }
        if (comboCalle.getValue() == null) {
            errores.append("- Debe seleccionar una calle.\n");
        }

        long telefonosConNumero = filasTelefono.stream()
                .filter(f -> f.campoNumero.getText() != null && !f.campoNumero.getText().trim().isEmpty())
                .count();
        if (telefonosConNumero == 0) {
            errores.append("- Debe ingresar al menos un telefono.\n");
        }

        long principalesMarcados = filasTelefono.stream()
                .filter(f -> f.checkPrincipal.isSelected()
                        && f.campoNumero.getText() != null
                        && !f.campoNumero.getText().trim().isEmpty())
                .count();
        if (telefonosConNumero > 0 && principalesMarcados != 1) {
            errores.append("- Debe marcar exactamente un telefono como principal.\n");
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

        String idSuplidor = Servicio.getInstance().generarIdSuplidor();
        String idCalle = mapaCalles.get(comboCalle.getValue());

        Suplidor suplidor = new Suplidor(
                campoIdentificacion.getText().trim(),
                campoCorreo.getText().trim(),
                campoNombreComercial.getText().trim(),
                idSuplidor,
                campoRazonComercial.getText().trim(),
                idCalle,
                LocalDate.now(),
                comboTipoIdentificacion.getValue()
        );

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

        Servicio.getInstance().registrarSuplidor(suplidor);

        limpiarFormulario();

        lblMensaje.setStyle("-fx-text-fill: #2e7d32;");
        lblMensaje.setText("Suplidor " + idSuplidor + " registrado correctamente.");
    }

    private void limpiarFormulario() {
        campoRazonComercial.clear();
        campoNombreComercial.clear();
        campoCorreo.clear();
        campoIdentificacion.clear();

        comboTipoIdentificacion.getSelectionModel().select("Rnc");
        actualizarPlaceholderIdentificacion();

        comboPais.getSelectionModel().clearSelection();
        comboCiudad.getItems().clear();
        comboCalle.getItems().clear();

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
        Stage stage = (Stage) campoRazonComercial.getScene().getWindow();
        stage.close();
    }
}