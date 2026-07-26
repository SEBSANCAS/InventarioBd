package visual;

import DataBase.AdquisicionDAO;
import logico.Adquisicion;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

public class ListaAdquisicionController {

    @FXML private TableView<Adquisicion> tablaAdquisiciones;
    @FXML private TableColumn<Adquisicion, String> colId;
    @FXML private TableColumn<Adquisicion, String> colSuplidor;
    @FXML private TableColumn<Adquisicion, LocalDate> colFechaEmision;
    @FXML private TableColumn<Adquisicion, LocalDate> colFechaEntrega;
    @FXML private TableColumn<Adquisicion, String> colEstado;
    @FXML private TableColumn<Adquisicion, Float> colTotal;

    @FXML private ComboBox<String> cbEstado;

    private final ObservableList<Adquisicion> listaAdquisiciones = FXCollections.observableArrayList();
    private Adquisicion adquisicionSeleccionada;

    @FXML
    public void initialize() {
        // Mapeo simple de campos directos
        colId.setCellValueFactory(new PropertyValueFactory<>("idCompra"));
        colFechaEmision.setCellValueFactory(new PropertyValueFactory<>("fechaEmision"));
        colFechaEntrega.setCellValueFactory(new PropertyValueFactory<>("fechaEntrega"));
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("montoTotal"));

        // Mapeo personalizado para extraer el Nombre Comercial de la relación con Suplidor
        colSuplidor.setCellValueFactory(cellData -> {
            if (cellData.getValue().getSuplidor() != null) {
                return new SimpleStringProperty(cellData.getValue().getSuplidor().getNombreComercial());
            }
            return new SimpleStringProperty("N/A");
        });

        // Opciones para cambiar estado
        cbEstado.setItems(FXCollections.observableArrayList("Pendiente", "Recibido", "Cancelado"));

        // Listener de selección en la tabla
        tablaAdquisiciones.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            if (newSel != null) {
                adquisicionSeleccionada = newSel;
                cbEstado.setValue(adquisicionSeleccionada.getEstado());
            }
        });

        cargarTabla();
    }

    private void cargarTabla() {
        listaAdquisiciones.clear();
        ArrayList<Adquisicion> adquisicionesBD = AdquisicionDAO.getInstance().EncontrarTodos();
        if (adquisicionesBD != null) {
            listaAdquisiciones.addAll(adquisicionesBD);
        }
        tablaAdquisiciones.setItems(listaAdquisiciones);
    }

    @FXML
    private void handleActualizarEstado() {
        if (adquisicionSeleccionada == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Atención", "Selecciona una adquisición de la tabla.");
            return;
        }

        if (cbEstado.getValue() == null) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error", "Selecciona un estado válido.");
            return;
        }

        adquisicionSeleccionada.setEstado(cbEstado.getValue());
        AdquisicionDAO.getInstance().actualizar(adquisicionSeleccionada);

        mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "Estado de la adquisición actualizado correctamente.");
        tablaAdquisiciones.refresh();
        handleLimpiar();
    }

    @FXML
    private void handleEliminar() {
        if (adquisicionSeleccionada == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Atención", "Selecciona una adquisición de la tabla.");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "¿Deseas eliminar la adquisición " + adquisicionSeleccionada.getIdCompra() + "?");
        Optional<ButtonType> res = confirm.showAndWait();

        if (res.isPresent() && res.get() == ButtonType.OK) {
            AdquisicionDAO.getInstance().borrar(adquisicionSeleccionada.getIdCompra());
            listaAdquisiciones.remove(adquisicionSeleccionada);
            mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "Adquisición eliminada correctamente.");
            handleLimpiar();
        }
    }

    @FXML
    private void handleLimpiar() {
        tablaAdquisiciones.getSelectionModel().clearSelection();
        adquisicionSeleccionada = null;
        cbEstado.setValue(null);
    }

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensaje) {
        Alert alert = new Alert(tipo, mensaje);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.showAndWait();
    }
}