package visual;

import DataBase.CambioParametroLaptopDAO;
import DataBase.LaptopDAO;
import logico.Laptop;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

public class ListaLaptopController {

    @FXML private TableView<Laptop> tablaLaptops;
    @FXML private TableColumn<Laptop, String> colId;
    @FXML private TableColumn<Laptop, String> colNombre;
    @FXML private TableColumn<Laptop, String> colModelo;
    @FXML private TableColumn<Laptop, String> colProcesador;
    @FXML private TableColumn<Laptop, String> colGpu;
    @FXML private TableColumn<Laptop, String> colTipoRam;
    @FXML private TableColumn<Laptop, String> colRam;
    @FXML private TableColumn<Laptop, String> colTipoAlmacenamiento;
    @FXML private TableColumn<Laptop, String> colAlmacenamiento;
    @FXML private TableColumn<Laptop, String> colPantalla;
    @FXML private TableColumn<Laptop, String> colResolucion;
    @FXML private TableColumn<Laptop, String> colPeso;
    @FXML private TableColumn<Laptop, String> colGarantia;
    @FXML private TableColumn<Laptop, String> colCostoPromedio;
    @FXML private TableColumn<Laptop, String> colPrecio;
    @FXML private TableColumn<Laptop, String> colPrecioMayorista;
    @FXML private TableColumn<Laptop, String> colMinimaMayorista;
    @FXML private TableColumn<Laptop, String> colAlertaStock;
    @FXML private TableColumn<Laptop, String> colStock;

    @FXML private TextField txtNombre;
    @FXML private TextField txtModelo;
    @FXML private TextField txtProcesador;
    @FXML private TextField txtGpu;
    @FXML private TextField txtTipoRam;
    @FXML private TextField txtRam;
    @FXML private TextField txtTipoAlmacenamiento;
    @FXML private TextField txtAlmacenamiento;
    @FXML private TextField txtPantalla;
    @FXML private TextField txtResolucion;
    @FXML private TextField txtPeso;
    @FXML private TextField txtGarantia;
    @FXML private TextField txtCostoPromedio;
    @FXML private TextField txtPrecio;
    @FXML private TextField txtPrecioMayorista;
    @FXML private TextField txtMinimaMayorista;
    @FXML private TextField txtAlertaStock;
    @FXML private TextField txtStock;

    private final ObservableList<Laptop> listaLaptops = FXCollections.observableArrayList();
    private Laptop laptopSeleccionada;

    @FXML
    public void initialize() {
        if (txtStock != null) {
            txtStock.setEditable(false);
            txtStock.setDisable(true);
            txtStock.setStyle("-fx-opacity: 0.8; -fx-background-color: #f0f0f0;");
        }

        if (txtCostoPromedio != null) {
            txtCostoPromedio.setEditable(false);
            txtCostoPromedio.setStyle("-fx-opacity: 0.8; -fx-background-color: #f0f0f0;");
        }

        colId.setCellValueFactory(cell -> new SimpleStringProperty(formatearString(cell.getValue().getIdLaptop())));
        colNombre.setCellValueFactory(cell -> new SimpleStringProperty(formatearString(cell.getValue().getNombreComercial())));
        colModelo.setCellValueFactory(cell -> new SimpleStringProperty(formatearString(cell.getValue().getNumeroModelo())));
        colProcesador.setCellValueFactory(cell -> new SimpleStringProperty(formatearString(cell.getValue().getProcesador())));
        colGpu.setCellValueFactory(cell -> new SimpleStringProperty(formatearString(cell.getValue().getGpu())));
        colTipoRam.setCellValueFactory(cell -> new SimpleStringProperty(formatearString(cell.getValue().getTipoRam())));
        colTipoAlmacenamiento.setCellValueFactory(cell -> new SimpleStringProperty(formatearString(cell.getValue().getTipoAlmacenamiento())));
        colResolucion.setCellValueFactory(cell -> new SimpleStringProperty(formatearString(cell.getValue().getResolucionPantalla())));

        colRam.setCellValueFactory(cell -> new SimpleStringProperty(formatearNumero(cell.getValue().getCantidadRam())));
        colAlmacenamiento.setCellValueFactory(cell -> new SimpleStringProperty(formatearNumero(cell.getValue().getCantidadAlmacenamiento())));
        colPantalla.setCellValueFactory(cell -> new SimpleStringProperty(formatearNumero(cell.getValue().getTamanyoPantalla())));
        colPeso.setCellValueFactory(cell -> new SimpleStringProperty(formatearNumero(cell.getValue().getPeso())));
        colGarantia.setCellValueFactory(cell -> new SimpleStringProperty(formatearNumero(cell.getValue().getMesesGarantia())));
        colCostoPromedio.setCellValueFactory(cell -> new SimpleStringProperty(formatearNumero(cell.getValue().getCostoPromedioCompra())));
        colPrecio.setCellValueFactory(cell -> new SimpleStringProperty(formatearNumero(cell.getValue().getPrecioDetalle())));
        colPrecioMayorista.setCellValueFactory(cell -> new SimpleStringProperty(formatearNumero(cell.getValue().getPrecioMayorista())));
        colMinimaMayorista.setCellValueFactory(cell -> new SimpleStringProperty(formatearNumero(cell.getValue().getCantMinMayorista())));
        colAlertaStock.setCellValueFactory(cell -> new SimpleStringProperty(formatearNumero(cell.getValue().getCantidadAlerta())));
        colStock.setCellValueFactory(cell -> new SimpleStringProperty(formatearNumero(cell.getValue().getStockActual())));

        tablaLaptops.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            if (newSel != null) {
                laptopSeleccionada = newSel;

                setTextFieldSeguro(txtNombre, laptopSeleccionada.getNombreComercial());
                setTextFieldSeguro(txtModelo, laptopSeleccionada.getNumeroModelo());
                setTextFieldSeguro(txtProcesador, laptopSeleccionada.getProcesador());
                setTextFieldSeguro(txtGpu, laptopSeleccionada.getGpu());
                setTextFieldSeguro(txtTipoRam, laptopSeleccionada.getTipoRam());
                setTextFieldSeguro(txtRam, laptopSeleccionada.getCantidadRam());
                setTextFieldSeguro(txtTipoAlmacenamiento, laptopSeleccionada.getTipoAlmacenamiento());
                setTextFieldSeguro(txtAlmacenamiento, laptopSeleccionada.getCantidadAlmacenamiento());
                setTextFieldSeguro(txtPantalla, laptopSeleccionada.getTamanyoPantalla());
                setTextFieldSeguro(txtResolucion, laptopSeleccionada.getResolucionPantalla());
                setTextFieldSeguro(txtPeso, laptopSeleccionada.getPeso());
                setTextFieldSeguro(txtGarantia, laptopSeleccionada.getMesesGarantia());
                setTextFieldSeguro(txtCostoPromedio, laptopSeleccionada.getCostoPromedioCompra());
                setTextFieldSeguro(txtPrecio, laptopSeleccionada.getPrecioDetalle());
                setTextFieldSeguro(txtPrecioMayorista, laptopSeleccionada.getPrecioMayorista());
                setTextFieldSeguro(txtMinimaMayorista, laptopSeleccionada.getCantMinMayorista());
                setTextFieldSeguro(txtAlertaStock, laptopSeleccionada.getCantidadAlerta());
                setTextFieldSeguro(txtStock, laptopSeleccionada.getStockActual());
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
            float newPrecioDetalle = parsearFloatSeguro(txtPrecio.getText());
            float newPrecioMayorista = parsearFloatSeguro(txtPrecioMayorista.getText());
            int newGarantia = parsearIntSeguro(txtGarantia.getText());
            int newMinimaMayorista = parsearIntSeguro(txtMinimaMayorista.getText());

            float oldPrecioDetalle = getFloatSeguro(laptopSeleccionada.getPrecioDetalle());
            float oldPrecioMayorista = getFloatSeguro(laptopSeleccionada.getPrecioMayorista());
            int oldGarantia = getIntSeguro(laptopSeleccionada.getMesesGarantia());
            int oldMinimaMayorista = getIntSeguro(laptopSeleccionada.getCantMinMayorista());

            boolean changedPrecioDetalle = oldPrecioDetalle != newPrecioDetalle;
            boolean changedPrecioMayorista = oldPrecioMayorista != newPrecioMayorista;
            boolean changedGarantia = oldGarantia != newGarantia;
            boolean changedMinimaMayorista = oldMinimaMayorista != newMinimaMayorista;

            String descripcion = "";

            if (changedPrecioDetalle || changedPrecioMayorista || changedGarantia || changedMinimaMayorista) {
                TextInputDialog dialog = new TextInputDialog();
                dialog.setTitle("Descripción del Cambio");
                dialog.setHeaderText("Ha modificado parámetros rastreables.");
                dialog.setContentText("Ingrese la justificación del cambio:");
                Optional<String> result = dialog.showAndWait();

                if (result.isPresent() && !result.get().trim().isEmpty()) {
                    descripcion = result.get().trim();
                } else {
                    mostrarAlerta(Alert.AlertType.WARNING, "Cancelado", "Es obligatorio ingresar una justificación para guardar el cambio.");
                    return;
                }
            }

            laptopSeleccionada.setNombreComercial(parsearStringSeguro(txtNombre.getText()));
            laptopSeleccionada.setNumeroModelo(parsearStringSeguro(txtModelo.getText()));
            laptopSeleccionada.setProcesador(parsearStringSeguro(txtProcesador.getText()));
            laptopSeleccionada.setGpu(parsearStringSeguro(txtGpu.getText()));
            laptopSeleccionada.setTipoRam(parsearStringSeguro(txtTipoRam.getText()));
            laptopSeleccionada.setCantidadRam(parsearFloatSeguro(txtRam.getText()));
            laptopSeleccionada.setTipoAlmacenamiento(parsearStringSeguro(txtTipoAlmacenamiento.getText()));
            laptopSeleccionada.setCantidadAlmacenamiento(parsearFloatSeguro(txtAlmacenamiento.getText()));
            laptopSeleccionada.setTamanyoPantalla(parsearFloatSeguro(txtPantalla.getText()));
            laptopSeleccionada.setResolucionPantalla(parsearStringSeguro(txtResolucion.getText()));
            laptopSeleccionada.setPeso(parsearFloatSeguro(txtPeso.getText()));
            laptopSeleccionada.setMesesGarantia(newGarantia);
            laptopSeleccionada.setPrecioDetalle(newPrecioDetalle);
            laptopSeleccionada.setPrecioMayorista(newPrecioMayorista);
            laptopSeleccionada.setCantMinMayorista(newMinimaMayorista);
            laptopSeleccionada.setCantidadAlerta(parsearIntSeguro(txtAlertaStock.getText()));

            LaptopDAO.getInstance().actualizar(laptopSeleccionada);

            if (changedPrecioDetalle) {
                registrarCambio(laptopSeleccionada.getIdLaptop(), "precio_venta_detalle", String.valueOf(oldPrecioDetalle), String.valueOf(newPrecioDetalle), descripcion);
            }
            if (changedPrecioMayorista) {
                registrarCambio(laptopSeleccionada.getIdLaptop(), "precio_venta_mayorista", String.valueOf(oldPrecioMayorista), String.valueOf(newPrecioMayorista), descripcion);
            }
            if (changedGarantia) {
                registrarCambio(laptopSeleccionada.getIdLaptop(), "meses_garantia", String.valueOf(oldGarantia), String.valueOf(newGarantia), descripcion);
            }
            if (changedMinimaMayorista) {
                registrarCambio(laptopSeleccionada.getIdLaptop(), "cantidad_minima_mayorista", String.valueOf(oldMinimaMayorista), String.valueOf(newMinimaMayorista), descripcion);
            }

            mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "Laptop actualizada correctamente.");
            tablaLaptops.refresh();
            handleLimpiar();

        } catch (Exception e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error", "Ha ocurrido un problema al actualizar la laptop.");
        }
    }

    private void registrarCambio(String idLaptop, String campo, String oldVal, String newVal, String desc) {
        String idCambio = "CAM-" + UUID.randomUUID().toString().substring(0, 10).toUpperCase();
        CambioParametroLaptopDAO.getInstance().guardar(idCambio, idLaptop, campo, oldVal, newVal, desc);
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

        if(txtNombre != null) txtNombre.clear();
        if(txtModelo != null) txtModelo.clear();
        if(txtProcesador != null) txtProcesador.clear();
        if(txtGpu != null) txtGpu.clear();
        if(txtTipoRam != null) txtTipoRam.clear();
        if(txtRam != null) txtRam.clear();
        if(txtTipoAlmacenamiento != null) txtTipoAlmacenamiento.clear();
        if(txtAlmacenamiento != null) txtAlmacenamiento.clear();
        if(txtPantalla != null) txtPantalla.clear();
        if(txtResolucion != null) txtResolucion.clear();
        if(txtPeso != null) txtPeso.clear();
        if(txtGarantia != null) txtGarantia.clear();
        if(txtCostoPromedio != null) txtCostoPromedio.clear();
        if(txtPrecio != null) txtPrecio.clear();
        if(txtPrecioMayorista != null) txtPrecioMayorista.clear();
        if(txtMinimaMayorista != null) txtMinimaMayorista.clear();
        if(txtAlertaStock != null) txtAlertaStock.clear();
        if(txtStock != null) txtStock.clear();
    }

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensaje) {
        Alert alert = new Alert(tipo, mensaje);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.showAndWait();
    }

    private String formatearString(String valor) {
        return (valor != null && !valor.equals("null")) ? valor : "N/A";
    }

    private String formatearNumero(Object valor) {
        if (valor == null) return "N/A";
        String valStr = String.valueOf(valor);
        if (valStr.equals("null") || valStr.equals("0.0") || valStr.equals("0")) {
            return "N/A";
        }
        return valStr;
    }

    private void setTextFieldSeguro(TextField txt, Object valor) {
        if (txt == null) return;

        if (valor == null || String.valueOf(valor).equals("null")) {
            txt.setText("N/A");
        } else {
            txt.setText(String.valueOf(valor));
        }
    }

    private String parsearStringSeguro(String texto) {
        if (texto == null || texto.trim().isEmpty() || texto.trim().equalsIgnoreCase("N/A")) return "";
        return texto.trim();
    }

    private float parsearFloatSeguro(String texto) {
        if (texto == null || texto.trim().isEmpty() || texto.trim().equalsIgnoreCase("N/A")) return 0.0f;
        try {
            return Float.parseFloat(texto.trim());
        } catch (NumberFormatException e) {
            return 0.0f;
        }
    }

    private int parsearIntSeguro(String texto) {
        if (texto == null || texto.trim().isEmpty() || texto.trim().equalsIgnoreCase("N/A")) return 0;
        try {
            return Integer.parseInt(texto.trim());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private float getFloatSeguro(Object valor) {
        if (valor == null || String.valueOf(valor).equals("null")) return 0.0f;
        try {
            return Float.parseFloat(String.valueOf(valor));
        } catch(Exception e) { return 0.0f; }
    }

    private int getIntSeguro(Object valor) {
        if (valor == null || String.valueOf(valor).equals("null")) return 0;
        try {
            return Integer.parseInt(String.valueOf(valor));
        } catch(Exception e) { return 0; }
    }
}