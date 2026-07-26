package visual;

import DataBase.SuplidorDAO;
import logico.Suplidor;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

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
        // Mapeo mediante expresiones lambda usando los getters exactos
        colId.setCellValueFactory(cell -> new SimpleStringProperty(
                cell.getValue().getIdSuplidor() != null ? cell.getValue().getIdSuplidor() : ""
        ));
        colNombreComercial.setCellValueFactory(cell -> new SimpleStringProperty(
                cell.getValue().getNombreComercial() != null ? cell.getValue().getNombreComercial() : ""
        ));
        colRazonComercial.setCellValueFactory(cell -> new SimpleStringProperty(
                cell.getValue().getRazonComercial() != null ? cell.getValue().getRazonComercial() : ""
        ));
        colIdentificacion.setCellValueFactory(cell -> new SimpleStringProperty(
                cell.getValue().getNumeroIdentificacion() != null ? cell.getValue().getNumeroIdentificacion() : ""
        ));
        colCorreo.setCellValueFactory(cell -> new SimpleStringProperty(
                cell.getValue().getCorreo() != null ? cell.getValue().getCorreo() : ""
        ));

        // Listener de selección en la tabla
        tablaSuplidores.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            if (newSel != null) {
                suplidorSeleccionado = newSel;
                txtNombreComercial.setText(suplidorSeleccionado.getNombreComercial() != null ? suplidorSeleccionado.getNombreComercial() : "");
                txtRazonComercial.setText(suplidorSeleccionado.getRazonComercial() != null ? suplidorSeleccionado.getRazonComercial() : "");
                txtIdentificacion.setText(suplidorSeleccionado.getNumeroIdentificacion() != null ? suplidorSeleccionado.getNumeroIdentificacion() : "");
                txtCorreo.setText(suplidorSeleccionado.getCorreo() != null ? suplidorSeleccionado.getCorreo() : "");
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
            mostrarAlerta(Alert.AlertType.ERROR, "Error", "El Nombre Comercial y el No. de Identificación son obligatorios.");
            return;
        }

        // Actualización de propiedades del objeto
        suplidorSeleccionado.setNombreComercial(txtNombreComercial.getText().trim());
        suplidorSeleccionado.setRazonComercial(txtRazonComercial.getText().trim());
        suplidorSeleccionado.setNumeroIdentificacion(txtIdentificacion.getText().trim());
        suplidorSeleccionado.setCorreo(txtCorreo.getText().trim());

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