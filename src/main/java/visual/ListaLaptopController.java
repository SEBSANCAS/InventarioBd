package visual;

import DataBase.LaptopDAO;
import logico.Laptop;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.ArrayList;
import java.util.Optional;

public class ListaLaptopController {

    @FXML private TableView<Laptop> tablaLaptops;
    @FXML private TableColumn<Laptop, String> colId;
    @FXML private TableColumn<Laptop, String> colNombre;
    @FXML private TableColumn<Laptop, String> colModelo;
    @FXML private TableColumn<Laptop, String> colProcesador;
    @FXML private TableColumn<Laptop, String> colGpu;
    @FXML private TableColumn<Laptop, Float> colRam;
    @FXML private TableColumn<Laptop, String> colAlmacenamiento;
    @FXML private TableColumn<Laptop, Integer> colGarantia;
    @FXML private TableColumn<Laptop, Float> colPrecio;
    @FXML private TableColumn<Laptop, Integer> colStock;

    @FXML private TextField txtNombre;
    @FXML private TextField txtModelo;
    @FXML private TextField txtProcesador;
    @FXML private TextField txtGpu;
    @FXML private TextField txtRam;
    @FXML private TextField txtAlmacenamiento;
    @FXML private TextField txtGarantia;
    @FXML private TextField txtPrecio;
    @FXML private TextField txtStock;

    private final ObservableList<Laptop> listaLaptops = FXCollections.observableArrayList();
    private Laptop laptopSeleccionada;

    @FXML
    public void initialize() {
        // --- MAPEO DE COLUMNAS CON LAMBDAS ---
        colId.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getIdLaptop() != null ? cell.getValue().getIdLaptop() : ""));
        colNombre.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getNombreComercial() != null ? cell.getValue().getNombreComercial() : ""));
        colModelo.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getNumeroModelo() != null ? cell.getValue().getNumeroModelo() : ""));
        colProcesador.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getProcesador() != null ? cell.getValue().getProcesador() : ""));
        colGpu.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getGpu() != null ? cell.getValue().getGpu() : ""));

        colRam.setCellValueFactory(cell -> new SimpleFloatProperty(cell.getValue().getCantidadRam()).asObject());

        colAlmacenamiento.setCellValueFactory(cell -> {
            String tipo = cell.getValue().getTipoAlmacenamiento() != null ? cell.getValue().getTipoAlmacenamiento() : "";
            float cap = cell.getValue().getCantidadAlmacenamiento();
            return new SimpleStringProperty(tipo + " " + cap + " GB");
        });

        colGarantia.setCellValueFactory(cell -> new SimpleIntegerProperty(cell.getValue().getMesesGarantia()).asObject());
        colPrecio.setCellValueFactory(cell -> new SimpleFloatProperty(cell.getValue().getPrecioDetalle()).asObject());
        colStock.setCellValueFactory(cell -> new SimpleIntegerProperty(cell.getValue().getStockActual()).asObject());

        // --- LISTENER DE SELECCIÓN (Para llenar los TextFields al hacer clic) ---
        tablaLaptops.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            if (newSel != null) {
                laptopSeleccionada = newSel;

                txtNombre.setText(laptopSeleccionada.getNombreComercial() != null ? laptopSeleccionada.getNombreComercial() : "");
                txtModelo.setText(laptopSeleccionada.getNumeroModelo() != null ? laptopSeleccionada.getNumeroModelo() : "");
                txtProcesador.setText(laptopSeleccionada.getProcesador() != null ? laptopSeleccionada.getProcesador() : "");
                txtGpu.setText(laptopSeleccionada.getGpu() != null ? laptopSeleccionada.getGpu() : "");

                txtRam.setText(String.valueOf(laptopSeleccionada.getCantidadRam()));
                txtAlmacenamiento.setText(String.valueOf(laptopSeleccionada.getCantidadAlmacenamiento()));

                txtGarantia.setText(String.valueOf(laptopSeleccionada.getMesesGarantia()));
                txtPrecio.setText(String.valueOf(laptopSeleccionada.getPrecioDetalle()));
                txtStock.setText(String.valueOf(laptopSeleccionada.getStockActual()));
            }
        });

        cargarTabla();
    }

    private void cargarTabla() {
        listaLaptops.clear();
        ArrayList<Laptop> laptopsBD = LaptopDAO.getInstance().EncontrarTodos();
        if (laptopsBD != null) {
            listaLaptops.addAll(laptopsBD);
        }
        tablaLaptops.setItems(listaLaptops);
    }

    @FXML
    private void handleActualizar() {
        if (laptopSeleccionada == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Atención", "Selecciona una laptop de la tabla.");
            return;
        }

        try {
            laptopSeleccionada.setNombreComercial(txtNombre.getText().trim());
            laptopSeleccionada.setNumeroModelo(txtModelo.getText().trim());
            laptopSeleccionada.setProcesador(txtProcesador.getText().trim());
            laptopSeleccionada.setGpu(txtGpu.getText().trim());
            laptopSeleccionada.setCantidadRam(Float.parseFloat(txtRam.getText().trim()));
            laptopSeleccionada.setCantidadAlmacenamiento(Float.parseFloat(txtAlmacenamiento.getText().trim()));
            laptopSeleccionada.setMesesGarantia(Integer.parseInt(txtGarantia.getText().trim()));
            laptopSeleccionada.setPrecioDetalle(Float.parseFloat(txtPrecio.getText().trim()));
            laptopSeleccionada.setStockActual(Integer.parseInt(txtStock.getText().trim()));

            LaptopDAO.getInstance().actualizar(laptopSeleccionada);

            mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "Laptop actualizada correctamente.");
            tablaLaptops.refresh();
            handleLimpiar();
        } catch (NumberFormatException e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error", "Por favor, verifica que los campos numéricos contengan valores válidos.");
        }
    }

    @FXML
    private void handleEliminar() {
        if (laptopSeleccionada == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Atención", "Selecciona una laptop de la tabla.");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "¿Deseas eliminar esta laptop?");
        Optional<ButtonType> res = confirm.showAndWait();

        if (res.isPresent() && res.get() == ButtonType.OK) {
            LaptopDAO.getInstance().borrar(laptopSeleccionada.getIdLaptop());
            listaLaptops.remove(laptopSeleccionada);
            mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "Laptop eliminada correctamente.");
            handleLimpiar();
        }
    }

    @FXML
    private void handleLimpiar() {
        tablaLaptops.getSelectionModel().clearSelection();
        laptopSeleccionada = null;
        txtNombre.clear();
        txtModelo.clear();
        txtProcesador.clear();
        txtGpu.clear();
        txtRam.clear();
        txtAlmacenamiento.clear();
        txtGarantia.clear();
        txtPrecio.clear();
        txtStock.clear();
    }

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensaje) {
        Alert alert = new Alert(tipo, mensaje);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.showAndWait();
    }
}