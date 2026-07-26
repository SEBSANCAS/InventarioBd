package visual;

import DataBase.MarcaDAO;
import logico.Marca;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.ArrayList;
import java.util.Optional;

public class ListaMarcaController {

    @FXML private TableView<Marca> tablaMarcas;
    @FXML private TableColumn<Marca, Integer> colId;
    @FXML private TableColumn<Marca, String> colNombre;

    @FXML private TextField txtNombre;

    private final ObservableList<Marca> listaMarcas = FXCollections.observableArrayList();
    private Marca marcaSeleccionada;

    @FXML
    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("idMarca"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombreMarca"));

        tablaMarcas.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            if (newSel != null) {
                marcaSeleccionada = newSel;
                txtNombre.setText(marcaSeleccionada.getNombreMarca());
            }
        });

        cargarTabla();
    }

    private void cargarTabla() {
        listaMarcas.clear();
        ArrayList<Marca> marcasBD = MarcaDAO.getInstance().EncontrarTodos();
        if (marcasBD != null) {
            listaMarcas.addAll(marcasBD);
        }
        tablaMarcas.setItems(listaMarcas);
    }

    @FXML
    private void handleActualizar() {
        if (marcaSeleccionada == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Atención", "Selecciona una marca de la tabla.");
            return;
        }

        if (txtNombre.getText().trim().isEmpty()) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error", "El nombre de la marca es obligatorio.");
            return;
        }

        marcaSeleccionada.setNombreMarca(txtNombre.getText().trim());
        MarcaDAO.getInstance().actualizar(marcaSeleccionada);

        mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "Marca actualizada correctamente.");
        tablaMarcas.refresh();
        handleLimpiar();
    }

    @FXML
    private void handleEliminar() {
        if (marcaSeleccionada == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Atención", "Selecciona una marca de la tabla.");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "¿Deseas eliminar la marca " + marcaSeleccionada.getNombreMarca() + "?");
        Optional<ButtonType> res = confirm.showAndWait();

        if (res.isPresent() && res.get() == ButtonType.OK) {
            MarcaDAO.getInstance().borrar(marcaSeleccionada.getIdMarca());
            listaMarcas.remove(marcaSeleccionada);
            mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "Marca eliminada correctamente.");
            handleLimpiar();
        }
    }

    @FXML
    private void handleLimpiar() {
        tablaMarcas.getSelectionModel().clearSelection();
        marcaSeleccionada = null;
        txtNombre.clear();
    }

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensaje) {
        Alert alert = new Alert(tipo, mensaje);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.showAndWait();
    }
}