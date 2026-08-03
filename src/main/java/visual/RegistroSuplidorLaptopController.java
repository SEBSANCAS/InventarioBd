package visual;

import DataBase.LaptopDAO;
import DataBase.SuplidorDAO;
import DataBase.SuplidorLaptopDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import logico.DetalleLaptopSuplidor;
import logico.Laptop;
import logico.Suplidor;

import java.util.HashMap;
import java.util.Map;

public class RegistroSuplidorLaptopController {

    @FXML
    private ComboBox<String> comboSuplidor;

    @FXML
    private ComboBox<String> comboLaptop;

    @FXML
    private TextField campoPrecio;

    @FXML
    private TextField campoDias;

    @FXML
    private Label lblMensaje;

    private final Map<String, Suplidor> mapaSuplidores = new HashMap<>();
    private final Map<String, Laptop> mapaLaptops = new HashMap<>();

    @FXML
    public void initialize() {
        cargarDatos();
        comboSuplidor.setOnAction(e -> verificarExistencia());
        comboLaptop.setOnAction(e -> verificarExistencia());
    }

    private void cargarDatos() {
        mapaSuplidores.clear();
        comboSuplidor.getItems().clear();
        for (Suplidor s : SuplidorDAO.getInstance().EncontrarTodos()) {
            String display = s.getIdSuplidor() + " - " + s.getNombreComercial();
            comboSuplidor.getItems().add(display);
            mapaSuplidores.put(display, s);
        }

        mapaLaptops.clear();
        comboLaptop.getItems().clear();
        for (Laptop l : LaptopDAO.getInstance().EncontrarTodos()) {
            String display = l.getIdLaptop() + " - " + l.getNombreComercial();
            comboLaptop.getItems().add(display);
            mapaLaptops.put(display, l);
        }
    }

    private void verificarExistencia() {
        lblMensaje.setText("");
        lblMensaje.setStyle("-fx-text-fill: black;");

        String selSuplidor = comboSuplidor.getValue();
        String selLaptop = comboLaptop.getValue();

        if (selSuplidor != null && selLaptop != null) {
            Suplidor suplidor = mapaSuplidores.get(selSuplidor);
            Laptop laptop = mapaLaptops.get(selLaptop);

            HashMap<Laptop, DetalleLaptopSuplidor> detalles = SuplidorLaptopDAO.getInstance().encontrarPorSuplidor(suplidor.getIdSuplidor());

            boolean existe = false;
            for (Laptop l : detalles.keySet()) {
                if (l.getIdLaptop().equals(laptop.getIdLaptop())) {
                    existe = true;
                    break;
                }
            }

            if (existe) {
                lblMensaje.setStyle("-fx-text-fill: #b8860b; -fx-font-weight: bold;");
                lblMensaje.setText("Advertencia: Esta laptop ya está en los detalles del suplidor. Se sugiere actualizar.");
            }
        }
    }

    @FXML
    private void ControlarGuardar(ActionEvent event) {
        if (!validarCampos()) {
            return;
        }

        Suplidor suplidor = mapaSuplidores.get(comboSuplidor.getValue());
        Laptop laptop = mapaLaptops.get(comboLaptop.getValue());

        HashMap<Laptop, DetalleLaptopSuplidor> detallesActuales = SuplidorLaptopDAO.getInstance().encontrarPorSuplidor(suplidor.getIdSuplidor());
        boolean existe = false;

        for (Laptop l : detallesActuales.keySet()) {
            if (l.getIdLaptop().equals(laptop.getIdLaptop())) {
                existe = true;
                break;
            }
        }

        if (existe) {
            lblMensaje.setStyle("-fx-text-fill: #b8860b; -fx-font-weight: bold;");
            lblMensaje.setText("No se puede guardar: Esta laptop ya está registrada para este suplidor. Se sugiere actualizar.");
            return;
        }

        float precio = Float.parseFloat(campoPrecio.getText().trim());
        int dias = Integer.parseInt(campoDias.getText().trim());

        DetalleLaptopSuplidor detalle = new DetalleLaptopSuplidor(dias, precio);

        SuplidorLaptopDAO.getInstance().guardar(suplidor.getIdSuplidor(), laptop.getIdLaptop(), detalle);
        lblMensaje.setStyle("-fx-text-fill: #2e7d32;");
        lblMensaje.setText("Laptop asignada al suplidor correctamente.");

        limpiarFormularioSoloCampos();
    }

    private boolean validarCampos() {
        StringBuilder errores = new StringBuilder();

        if (comboSuplidor.getValue() == null) {
            errores.append("- Debe seleccionar un suplidor.\n");
        }

        if (comboLaptop.getValue() == null) {
            errores.append("- Debe seleccionar una laptop.\n");
        }

        try {
            float precio = Float.parseFloat(campoPrecio.getText().trim());
            if (precio <= 0) {
                errores.append("- El monto no puede ser 0 o menor a 0.\n");
            }
        } catch (NumberFormatException e) {
            errores.append("- El precio ingresado no es válido.\n");
        }

        try {
            int dias = Integer.parseInt(campoDias.getText().trim());
            if (dias < 0) {
                errores.append("- Los días no pueden ser negativos.\n");
            }
        } catch (NumberFormatException e) {
            errores.append("- Los días ingresados no son válidos.\n");
        }

        if (errores.length() > 0) {
            lblMensaje.setStyle("-fx-text-fill: #b23b3b;");
            lblMensaje.setText(errores.toString());
            return false;
        }
        return true;
    }

    private void limpiarFormularioSoloCampos() {
        campoPrecio.clear();
        campoDias.clear();
    }

    private void limpiarFormularioCompleto() {
        comboSuplidor.getSelectionModel().clearSelection();
        comboLaptop.getSelectionModel().clearSelection();
        limpiarFormularioSoloCampos();
    }

    @FXML
    private void ControlarLimpiar(ActionEvent event) {
        limpiarFormularioCompleto();
        lblMensaje.setText("");
    }

    @FXML
    private void ControlarCancelar(ActionEvent event) {
        Stage stage = (Stage) campoPrecio.getScene().getWindow();
        stage.close();
    }
}