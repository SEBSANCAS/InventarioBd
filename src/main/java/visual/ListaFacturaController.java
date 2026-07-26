package visual;

import DataBase.FacturaDAO;
import logico.Factura;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

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
        // Mapeo de columnas con las propiedades directas de Factura
        colId.setCellValueFactory(new PropertyValueFactory<>("idFactura"));
        colComprobante.setCellValueFactory(new PropertyValueFactory<>("numeroComprobante"));
        colFecha.setCellValueFactory(new PropertyValueFactory<>("fechaEmision"));
        colSubtotal.setCellValueFactory(new PropertyValueFactory<>("subtotal"));
        colImpuestos.setCellValueFactory(new PropertyValueFactory<>("impuestos"));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("montoTotal"));

        // Extracción del nombre del Cliente desde la relación
        colCliente.setCellValueFactory(cellData -> {
            if (cellData.getValue().getCliente() != null) {
                return new SimpleStringProperty(cellData.getValue().getCliente().getNombres());
            }
            return new SimpleStringProperty("Sin Cliente");
        });

        // Evento de selección en la tabla
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

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "¿Estás seguro de que deseas anular/eliminar la factura " + facturaSeleccionada.getIdFactura() + "?");
        Optional<ButtonType> res = confirm.showAndWait();

        if (res.isPresent() && res.get() == ButtonType.OK) {
            FacturaDAO.getInstance().borrar(facturaSeleccionada.getIdFactura());
            listaFacturas.remove(facturaSeleccionada);
            mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "Factura eliminada correctamente.");
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