package visual;

import DataBase.EstanteDAO;
import logico.Estante;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.ArrayList;
import java.util.Optional;

public class ListaEstanteController {

    @FXML private TableView<Estante> tablaEstantes;
    @FXML private TableColumn<Estante, String> colId;
    @FXML private TableColumn<Estante, Integer> colCapacidad;
    @FXML private TableColumn<Estante, Integer> colNiveles;

    @FXML private TextField txtIdEstante;
    @FXML private TextField txtCapacidad;
    @FXML private TextField txtNiveles;

    private final ObservableList<Estante> listaEstantes = FXCollections.observableArrayList();
    private Estante estanteSeleccionado;

    @FXML
    public void initialize() {
        // Mapeo directo mediante expresiones lambda para evitar problemas de contraste o texto invisible
        colId.setCellValueFactory(cell -> new SimpleStringProperty(
                cell.getValue().getIdEstante() != null ? cell.getValue().getIdEstante() : ""
        ));
        colCapacidad.setCellValueFactory(cell -> new SimpleObjectProperty<>(cell.getValue().getCapacidad()));
        colNiveles.setCellValueFactory(cell -> new SimpleObjectProperty<>(cell.getValue().getCantidadNiveles()));

        // Evento al seleccionar una fila de la tabla
        tablaEstantes.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            if (newSel != null) {
                estanteSeleccionado = newSel;
                txtIdEstante.setText(estanteSeleccionado.getIdEstante() != null ? estanteSeleccionado.getIdEstante() : "");
                txtCapacidad.setText(String.valueOf(estanteSeleccionado.getCapacidad()));
                txtNiveles.setText(String.valueOf(estanteSeleccionado.getCantidadNiveles()));
            }
        });

        cargarTabla();
    }

    private void cargarTabla() {
        listaEstantes.clear();
        ArrayList<Estante> estantesBD = EstanteDAO.getInstance().EncontrarTodos();
        if (estantesBD != null) {
            listaEstantes.addAll(estantesBD);
        }
        tablaEstantes.setItems(listaEstantes);
    }

    @FXML
    private void handleActualizar() {
        if (estanteSeleccionado == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Atención", "Selecciona un estante de la tabla.");
            return;
        }

        try {
            int capacidad = Integer.parseInt(txtCapacidad.getText().trim());
            estanteSeleccionado.setCapacidad(capacidad);

            EstanteDAO.getInstance().actualizar(estanteSeleccionado);

            mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "Estante actualizado correctamente.");
            tablaEstantes.refresh();
            handleLimpiar();
        } catch (NumberFormatException e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error", "La capacidad debe ser un número entero válido.");
        }
    }

    @FXML
    private void handleEliminar() {
        if (estanteSeleccionado == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Atención", "Selecciona un estante de la tabla.");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "¿Deseas eliminar el estante " + estanteSeleccionado.getIdEstante() + "?");
        Optional<ButtonType> res = confirm.showAndWait();

        if (res.isPresent() && res.get() == ButtonType.OK) {
            EstanteDAO.getInstance().borrar(estanteSeleccionado.getIdEstante());
            listaEstantes.remove(estanteSeleccionado);
            mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "Estante eliminado correctamente.");
            handleLimpiar();
        }
    }

    @FXML
    private void handleLimpiar() {
        tablaEstantes.getSelectionModel().clearSelection();
        estanteSeleccionado = null;
        txtIdEstante.clear();
        txtCapacidad.clear();
        txtNiveles.clear();
    }

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensaje) {
        Alert alert = new Alert(tipo, mensaje);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.showAndWait();
    }
}