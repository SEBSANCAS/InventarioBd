package visual;

import DataBase.FacturaDAO;
import logico.Factura;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

public class ListaFacturaController {

    @FXML private TableView<Factura> tablaFacturas;
    @FXML private TableColumn<Factura, String> colId;
    @FXML private TableColumn<Factura, String> colComprobante;
    @FXML private TableColumn<Factura, String> colCliente;
    @FXML private TableColumn<Factura, LocalDate> colFecha;
    @FXML private TableColumn<Factura, Float> colSubtotal;
    @FXML private TableColumn<Factura, Float> colImpuestos;
    @FXML private TableColumn<Factura, Float> colTotal;

    @FXML private TextField txtComprobante;

    private final ObservableList<Factura> listaFacturas = FXCollections.observableArrayList();
    private Factura facturaSeleccionada;

    @FXML
    public void initialize() {
        // Mapeo directo mediante expresiones lambda usando getters exactos
        colId.setCellValueFactory(cell -> new SimpleStringProperty(
                cell.getValue().getIdFactura() != null ? cell.getValue().getIdFactura() : ""
        ));

        colComprobante.setCellValueFactory(cell -> new SimpleStringProperty(
                cell.getValue().getNumeroComprobante() != null ? cell.getValue().getNumeroComprobante() : ""
        ));

        // Extracción segura del nombre del cliente desde la relación
        colCliente.setCellValueFactory(cell -> new SimpleStringProperty(
                (cell.getValue().getCliente() != null && cell.getValue().getCliente().getNombres() != null)
                        ? cell.getValue().getCliente().getNombres()
                        : "Sin Cliente"
        ));

        colFecha.setCellValueFactory(cell -> new SimpleObjectProperty<>(cell.getValue().getFechaEmision()));
        colSubtotal.setCellValueFactory(cell -> new SimpleObjectProperty<>(cell.getValue().getSubtotal()));
        colImpuestos.setCellValueFactory(cell -> new SimpleObjectProperty<>(cell.getValue().getImpuestos()));
        colTotal.setCellValueFactory(cell -> new SimpleObjectProperty<>(cell.getValue().getMontoTotal()));

        // Listener de selección en la tabla
        tablaFacturas.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            if (newSel != null) {
                facturaSeleccionada = newSel;
                txtComprobante.setText(facturaSeleccionada.getNumeroComprobante() != null ? facturaSeleccionada.getNumeroComprobante() : "");
            }
        });

        cargarTabla();
    }

    private void cargarTabla() {
        listaFacturas.clear();
        ArrayList<Factura> facturasBD = FacturaDAO.getInstance().EncontrarTodos();
        if (facturasBD != null) {
            listaFacturas.addAll(facturasBD);
        }
        tablaFacturas.setItems(listaFacturas);
    }

    @FXML
    private void handleActualizarComprobante() {
        if (facturaSeleccionada == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Atención", "Selecciona una factura de la tabla.");
            return;
        }

        if (txtComprobante.getText().trim().isEmpty()) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error", "El número de comprobante no puede estar vacío.");
            return;
        }

        facturaSeleccionada.setNumeroComprobante(txtComprobante.getText().trim());
        FacturaDAO.getInstance().actualizar(facturaSeleccionada);

        mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "Comprobante actualizado correctamente.");
        tablaFacturas.refresh();
        handleLimpiar();
    }

    @FXML
    private void handleEliminar() {
        if (facturaSeleccionada == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Atención", "Selecciona una factura de la tabla.");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "¿Estás seguro de que deseas anular la factura ID: " + facturaSeleccionada.getIdFactura() + "?");
        Optional<ButtonType> res = confirm.showAndWait();

        if (res.isPresent() && res.get() == ButtonType.OK) {
            FacturaDAO.getInstance().borrar(facturaSeleccionada.getIdFactura());
            listaFacturas.remove(facturaSeleccionada);
            mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "Factura anulada/eliminada correctamente.");
            handleLimpiar();
        }
    }

    @FXML
    private void handleLimpiar() {
        tablaFacturas.getSelectionModel().clearSelection();
        facturaSeleccionada = null;
        txtComprobante.clear();
    }

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensaje) {
        Alert alert = new Alert(tipo, mensaje);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.showAndWait();
    }
}