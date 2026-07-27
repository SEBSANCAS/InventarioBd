package visual;

import DataBase.AdquisicionDAO;
import DataBase.LaptopDAO;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import logico.Adquisicion;
import logico.DetalleAdquisicion;
import logico.Laptop;
import logico.Servicio;

import java.time.LocalDate;

public class ManejarOrdenesController {

    @FXML private TableView<Adquisicion> tablaPendientes;
    @FXML private TableColumn<Adquisicion, String> colPendId;
    @FXML private TableColumn<Adquisicion, String> colPendSuplidor;
    @FXML private TableColumn<Adquisicion, LocalDate> colPendFecha;
    @FXML private TableColumn<Adquisicion, Float> colPendTotal;
    @FXML private TableColumn<Adquisicion, String> colPendEstado;

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

    private MenuPrincipalController menuPrincipalController;

    public void setMenuPrincipalController(MenuPrincipalController menuPrincipalController) {
        this.menuPrincipalController = menuPrincipalController;
    }

    @FXML
    public void initialize() {
        configurarColumnas();

        tablaPendientes.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            ordenPendienteSeleccionada = newSel;
        });

        tablaAceptadas.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            ordenAceptadaSeleccionada = newSel;
        });

        cargarTablas();
    }

    private void configurarColumnas() {
        colPendId.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getIdCompra()));
        colPendFecha.setCellValueFactory(cell -> new SimpleObjectProperty<>(cell.getValue().getFechaEmision()));
        colPendTotal.setCellValueFactory(cell -> new SimpleObjectProperty<>(cell.getValue().getMontoTotal()));
        colPendEstado.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getEstado()));
        colPendSuplidor.setCellValueFactory(cell -> new SimpleStringProperty(
                cell.getValue().getSuplidor() != null ? cell.getValue().getSuplidor().getNombreComercial() : "N/A"
        ));

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

        for (Adquisicion ord : Servicio.getInstance().getMisAdquisiciones().values()) {
            if ("Emitida".equalsIgnoreCase(ord.getEstado())) {
                listaPendientes.add(ord);
            } else if ("Aceptada".equalsIgnoreCase(ord.getEstado())) {
                listaAceptadas.add(ord);
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

        cargarTablas();
        mostrarAlerta("Éxito", "Orden marcaba como Aceptada.");
    }

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

    @FXML
    private void handleCompraLlego() {
        if (ordenAceptadaSeleccionada == null) {
            mostrarAlerta("Atención", "Selecciona una orden de la lista de aceptadas.");
            return;
        }

        ordenAceptadaSeleccionada.setEstado("Recibida");
        ordenAceptadaSeleccionada.setFechaEntrega(LocalDate.now());

        AdquisicionDAO.getInstance().actualizar(ordenAceptadaSeleccionada);

        if (ordenAceptadaSeleccionada.getDetallesAdquision() != null) {
            for (DetalleAdquisicion detalle : ordenAceptadaSeleccionada.getDetallesAdquision()) {
                if (detalle.getModeloLaptopAdquirida() != null) {
                    String idLaptop = detalle.getModeloLaptopAdquirida().getIdLaptop();

                    Laptop laptopFrescaBD = LaptopDAO.getInstance().encontrarPorId(idLaptop);

                    if (laptopFrescaBD != null) {
                        Laptop laptopMemoria = Servicio.getInstance().getMisLaptops().get(idLaptop);
                        if (laptopMemoria != null) {
                            laptopMemoria.setCostoPromedioCompra(laptopFrescaBD.getCostoPromedioCompra());
                        }
                    }
                }
            }
        }

        cargarTablas();
        mostrarAlerta("Éxito", "¡Compra registrada como Recibida! El costo promedio ha sido sincronizado.");

        if (menuPrincipalController != null) {
            menuPrincipalController.cargarNotificaciones();
        }
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, mensaje);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.showAndWait();
    }
}