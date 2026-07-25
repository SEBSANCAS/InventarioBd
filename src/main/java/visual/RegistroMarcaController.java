package visual;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import logico.Marca;
import logico.Servicio;

public class RegistroMarcaController {

    @FXML
    private TextField campoIdMarca;

    @FXML
    private TextField campoNombreMarca;

    @FXML
    private Label lblMensaje;

    @FXML
    public void initialize() {
        actualizarIdPreview();
    }

    private void actualizarIdPreview() {
        int siguiente = Servicio.getInstance().getGenIdMarca();
        campoIdMarca.setText(String.format("MAR%03d", siguiente));
    }

    @FXML
    private void ControlarGuardar(ActionEvent event) {
        String nombre = campoNombreMarca.getText();

        if (nombre == null || nombre.trim().isEmpty()) {
            lblMensaje.setStyle("-fx-text-fill: red;");
            lblMensaje.setText("El nombre de la marca no puede estar vacio.");
            return;
        }

        String idMarca = Servicio.getInstance().generarIdMarca();
        Marca marca = new Marca(idMarca, nombre.trim());
        Servicio.getInstance().registrarMarca(marca);

        lblMensaje.setStyle("-fx-text-fill: green;");
        lblMensaje.setText("Marca " + idMarca + " registrada correctamente.");

        campoNombreMarca.clear();
        actualizarIdPreview();
    }

    @FXML
    private void ControlarCancelar(ActionEvent event) {
        Stage stage = (Stage) campoNombreMarca.getScene().getWindow();
        stage.close();
    }
}