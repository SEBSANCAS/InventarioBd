package visual;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import logico.Estante;
import logico.Servicio;

public class RegistroEstanteController {

    @FXML
    private TextField campoIdEstante;

    @FXML
    private TextField campoCapacidad;

    @FXML
    private TextField campoNiveles;

    @FXML
    private Label lblMensaje;

    @FXML
    public void initialize() {
        actualizarIdPreview();
    }

    private void actualizarIdPreview() {
        int siguiente = Servicio.getInstance().getGenIdEstante();
        campoIdEstante.setText(String.format("EST%03d", siguiente));
        campoIdEstante.setEditable(false);
    }

    private boolean validarCampos() {
        StringBuilder errores = new StringBuilder();

        if (campoCapacidad.getText() == null || campoCapacidad.getText().trim().isEmpty()) {
            errores.append("- La capacidad total es obligatoria.\n");
        } else {
            try {
                int capacidad = Integer.parseInt(campoCapacidad.getText().trim());
                if (capacidad <= 0) {
                    errores.append("- La capacidad debe ser un número mayor a 0.\n");
                }
            } catch (NumberFormatException e) {
                errores.append("- La capacidad debe ser un valor numérico entero.\n");
            }
        }

        if (campoNiveles.getText() == null || campoNiveles.getText().trim().isEmpty()) {
            errores.append("- La cantidad de niveles es obligatoria.\n");
        } else {
            try {
                int niveles = Integer.parseInt(campoNiveles.getText().trim());
                if (niveles <= 0) {
                    errores.append("- La cantidad de niveles debe ser un número mayor a 0.\n");
                }
            } catch (NumberFormatException e) {
                errores.append("- La cantidad de niveles debe ser un valor numérico entero.\n");
            }
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

        String idEstante = Servicio.getInstance().generarIdEstante();
        int capacidad = Integer.parseInt(campoCapacidad.getText().trim());
        int niveles = Integer.parseInt(campoNiveles.getText().trim());

        Estante estante = new Estante(idEstante, capacidad, niveles);

        Servicio.getInstance().registrarEstante(estante);

        limpiarFormulario();

        lblMensaje.setStyle("-fx-text-fill: #2e7d32;");
        lblMensaje.setText("Estante " + idEstante + " registrado correctamente. Siguiente ID: " + campoIdEstante.getText());
    }

    private void limpiarFormulario() {
        campoCapacidad.clear();
        campoNiveles.clear();
        actualizarIdPreview();
    }

    @FXML
    private void ControlarLimpiar(ActionEvent event) {
        limpiarFormulario();
        lblMensaje.setText("");
    }

    @FXML
    private void ControlarCancelar(ActionEvent event) {
        Stage stage = (Stage) campoIdEstante.getScene().getWindow();
        stage.close();
    }
}