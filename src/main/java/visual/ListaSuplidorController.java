package visual;

import DataBase.SuplidorDAO;
import logico.Suplidor;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.ArrayList;
import java.util.Optional;

public class ListaSuplidorController {

    @FXML private TableView<Suplidor> tablaSuplidores;
    @FXML private TableColumn<Suplidor, String> colId;
    @FXML private TableColumn<Suplidor, String> colNombreComercial;
    @FXML private TableColumn<Suplidor, String> colRazonComercial;
    @FXML private TableColumn<Suplidor, String> colIdentificacion;
    @FXML private TableColumn<Suplidor, String> colCorreo;

    @FXML private TextField txtNombreComercial;
    @FXML private TextField txtRazonComercial;
    @FXML private TextField txtIdentificacion;
    @FXML private TextField txtCorreo;

    private final ObservableList<Suplidor> listaSuplidores = FXCollections.observableArrayList();
    private Suplidor suplidorSeleccionado;

    @FXML
    public void initialize() {
        // Mapeo según los getters de Persona y Suplidor
        colId.setCellValueFactory(new PropertyValueFactory<>("idSuplidor"));
        colNombreComercial.setCellValueFactory(new PropertyValueFactory<>("nombreComercial"));
        colRazonComercial.setCellValueFactory(new PropertyValueFactory<>("razonComercial"));
        colIdentificacion.setCellValueFactory(new PropertyValueFactory<>("numeroIdentificacion"));
        colCorreo.setCellValueFactory(new PropertyValueFactory<>("correo"));

        // Listener de selección en la tabla
        tablaSuplidores.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            if (newSel != null) {
                suplidorSeleccionado = newSel;
                txtNombreComercial.setText(suplidorSeleccionado.getNombreComercial());
                txtRazonComercial.setText(suplidorSeleccionado.getRazonComercial());
                txtIdentificacion.setText(suplidorSeleccionado.getNumeroIdentificacion());
                txtCorreo.setText(suplidorSeleccionado.getCorreo());
            }
        });

        cargarTabla();
    }

    private void cargarTabla() {
        listaSuplidores.clear();
        ArrayList<Suplidor> suplidoresBD = SuplidorDAO.getInstance().EncontrarTodos();
        if (suplidoresBD != null) {
            listaSuplidores.addAll(suplidoresBD);
        }
        tablaSuplidores.setItems(listaSuplidores);
    }

    @FXML
    private void handleActualizar() {
        if (suplidorSeleccionado == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Atención", "Selecciona un suplidor de la tabla.");
            return;
        }

        if (txtNombreComercial.getText().trim().isEmpty() || txtIdentificacion.getText().trim().isEmpty()) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error", "El nombre comercial y el número de identificación son obligatorios.");
            return;
        }

        // Modificamos el objeto seleccionado
        suplidorSeleccionado.setNombreComercial(txtNombreComercial.getText().trim());
        suplidorSeleccionado.setRazonComercial(txtRazonComercial.getText().trim());
        suplidorSeleccionado.setNumeroIdentificacion(txtIdentificacion.getText().trim());
        suplidorSeleccionado.setCorreo(txtCorreo.getText().trim());

        // Llamada a tu DAO
        SuplidorDAO.getInstance().actualizar(suplidorSeleccionado);

        mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "Suplidor actualizado correctamente.");
        tablaSuplidores.refresh();
        handleLimpiar();
    }

    @FXML
    private void handleEliminar() {
        if (suplidorSeleccionado == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Atención", "Selecciona un suplidor de la tabla.");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "¿Deseas eliminar al suplidor " + suplidorSeleccionado.getNombreComercial() + "?");
        Optional<ButtonType> res = confirm.showAndWait();

        if (res.isPresent() && res.get() == ButtonType.OK) {
            SuplidorDAO.getInstance().borrar(suplidorSeleccionado.getIdSuplidor());
            listaSuplidores.remove(suplidorSeleccionado);
            mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "Suplidor eliminado correctamente.");
            handleLimpiar();
        }
    }

    @FXML
    private void handleLimpiar() {
        tablaSuplidores.getSelectionModel().clearSelection();
        suplidorSeleccionado = null;
        txtNombreComercial.clear();
        txtRazonComercial.clear();
        txtIdentificacion.clear();
        txtCorreo.clear();
    }

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensaje) {
        Alert alert = new Alert(tipo, mensaje);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.showAndWait();
    }
}