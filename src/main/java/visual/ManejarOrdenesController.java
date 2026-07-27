package visual;

import DataBase.AdquisicionDAO;
import javafx.beans.property.SimpleObjectProperty;
import logico.Adquisicion;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.LocalDate;
import java.util.ArrayList;

public class ManejarOrdenesController {

    // --- TABLA SUPERIOR: ÓRDES PENDIENTES ---
    @FXML private TableView<Adquisicion> tablaPendientes;
    @FXML private TableColumn<Adquisicion, String> colPendId;
    @FXML private TableColumn<Adquisicion, String> colPendSuplidor;
    @FXML private TableColumn<Adquisicion, LocalDate> colPendFecha;
    @FXML private TableColumn<Adquisicion, Float> colPendTotal;
    @FXML private TableColumn<Adquisicion, String> colPendEstado;

    // --- TABLA INFERIOR: ÓRDENES ACEPTADAS ---
    @FXML private TableView<Adquisicion> tablaAceptadas;
    @FXML private TableColumn<Adquisicion, String> colAceptId;
    @FXML private TableColumn<Adquisicion, String> colAceptSuplidor;
    @FXML private TableColumn<Adquisicion, LocalDate> colAceptFecha;
    @FXML private TableColumn<Adquisicion, Float> colAceptTotal;
    @FXML private TableColumn<Adquisicion, String> colAceptEstado;

    private final ObservableList<Adquisicion> listaPendientes = FXCollections.observableArrayList();
    private final ObservableList<Adquisicion> listaAceptadas = FXCollections.observableArrayList();

    private Adquisicion ordenPendienteSeleccionada;
    private Adquisicion ordenAceptadaSeleccionada;

    @FXML
    public void initialize() {
        configurarColumnas();

        // Listeners para captura de selecciones
        tablaPendientes.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            ordenPendienteSeleccionada = newSel;
        });

        tablaAceptadas.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            ordenAceptadaSeleccionada = newSel;
        });

        cargarTablas();
    }

    private void configurarColumnas() {
        // Mapeo Tabla Pendientes (Llamando directamente a tus métodos get)
        colPendId.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getIdCompra()));
        colPendFecha.setCellValueFactory(cell -> new SimpleObjectProperty<>(cell.getValue().getFechaEmision()));
        colPendTotal.setCellValueFactory(cell -> new SimpleObjectProperty<>(cell.getValue().getMontoTotal()));
        colPendEstado.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getEstado()));
        colPendSuplidor.setCellValueFactory(cell -> new SimpleStringProperty(
                cell.getValue().getSuplidor() != null ? cell.getValue().getSuplidor().getNombreComercial() : "N/A"
        ));

        // Mapeo Tabla Aceptadas (Llamando directamente a tus métodos get)
        colAceptId.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getIdCompra()));
        colAceptFecha.setCellValueFactory(cell -> new SimpleObjectProperty<>(cell.getValue().getFechaEmision()));
        colAceptTotal.setCellValueFactory(cell -> new SimpleObjectProperty<>(cell.getValue().getMontoTotal()));
        colAceptEstado.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getEstado()));
        colAceptSuplidor.setCellValueFactory(cell -> new SimpleStringProperty(
                cell.getValue().getSuplidor() != null ? cell.getValue().getSuplidor().getNombreComercial() : "N/A"
        ));
    }

    private void cargarTablas() {
        listaPendientes.clear();
        listaAceptadas.clear();

        ArrayList<Adquisicion> todas = AdquisicionDAO.getInstance().EncontrarTodos();
        if (todas != null) {
            for (Adquisicion ord : todas) {

                if ("Emitida".equalsIgnoreCase(ord.getEstado())) {
                    listaPendientes.add(ord);
                } else if ("Aceptada".equalsIgnoreCase(ord.getEstado())) {
                    listaAceptadas.add(ord);
                }
            }
        }

        tablaPendientes.setItems(listaPendientes);
        tablaAceptadas.setItems(listaAceptadas);
    }


    @FXML
    private void handleAceptarOrden() {
        if (ordenPendienteSeleccionada == null) {
            mostrarAlerta("Atención", "Selecciona una orden de la lista de pendientes.");
            return;
        }

        ordenPendienteSeleccionada.setEstado("Aceptada");
        AdquisicionDAO.getInstance().actualizar(ordenPendienteSeleccionada);

        cargarTablas(); // Refresca las dos listas
        mostrarAlerta("Éxito", "Orden marcaba como Aceptada.");
    }

    // ACCIÓN: Rechazar Orden Pendiente
    @FXML
    private void handleRechazarOrden() {
        if (ordenPendienteSeleccionada == null) {
            mostrarAlerta("Atención", "Selecciona una orden de la lista de pendientes.");
            return;
        }

        ordenPendienteSeleccionada.setEstado("Rechazada");
        AdquisicionDAO.getInstance().actualizar(ordenPendienteSeleccionada);

        cargarTablas();
        mostrarAlerta("Éxito", "Orden ha sido Rechazada.");
    }

    // ACCIÓN: Marcar "La compra llegó" (Pasa a estado Recibido)
    @FXML
    private void handleCompraLlego() {
        if (ordenAceptadaSeleccionada == null) {
            mostrarAlerta("Atención", "Selecciona una orden de la lista de aceptadas.");
            return;
        }

        ordenAceptadaSeleccionada.setEstado("Recibido");
        ordenAceptadaSeleccionada.setFechaEntrega(LocalDate.now());

        // Al guardar en BD como 'Recibido', el trigger de la base de datos se encargará de ajustar el stock
        AdquisicionDAO.getInstance().actualizar(ordenAceptadaSeleccionada);

        cargarTablas();
        mostrarAlerta("Éxito", "¡Compra registrada como Recibida! El inventario ha sido actualizado.");
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, mensaje);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.showAndWait();
    }
}