package visual;

import DataBase.MarcaDAO;
import logico.Marca;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.ArrayList;
import java.util.Optional;

public class ListaMarcaController {

    @FXML private TableView<Marca> tablaMarcas;
    @FXML private TableColumn<Marca, String> colId;
    @FXML private TableColumn<Marca, String> colNombre;

    @FXML private TextField txtIdMarca;
    @FXML private TextField txtNombreMarca;

    private final ObservableList<Marca> listaMarcas = FXCollections.observableArrayList();
    private Marca marcaSeleccionada;

    @FXML
    public void initialize() {
        // Mapeo utilizando getNombreMarca() de tu clase logico.Marca
        colId.setCellValueFactory(cell -> new SimpleStringProperty(
                cell.getValue().getIdMarca() != null ? cell.getValue().getIdMarca() : ""
        ));
        colNombre.setCellValueFactory(cell -> new SimpleStringProperty(
                cell.getValue().getNombreMarca() != null ? cell.getValue().getNombreMarca() : ""
        ));

        // Evento de selección en la tabla
        tablaMarcas.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            if (newSel != null) {
                marcaSeleccionada = newSel;
                txtIdMarca.setText(marcaSeleccionada.getIdMarca() != null ? marcaSeleccionada.getIdMarca() : "");
                txtNombreMarca.setText(marcaSeleccionada.getNombreMarca() != null ? marcaSeleccionada.getNombreMarca() : "");
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

        String nuevoNombre = txtNombreMarca.getText().trim();
        if (nuevoNombre.isEmpty()) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error", "El nombre de la marca no puede estar vacío.");
            return;
        }

        marcaSeleccionada.setNombreMarca(nuevoNombre);
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
        txtIdMarca.clear();
        txtNombreMarca.clear();
    }

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensaje) {
        Alert alert = new Alert(tipo, mensaje);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.showAndWait();
    }
}