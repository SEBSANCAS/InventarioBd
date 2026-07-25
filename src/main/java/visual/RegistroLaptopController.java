package visual;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import logico.Laptop;
import logico.Marca;
import logico.Servicio;

public class RegistroLaptopController {

    @FXML
    private TextField campoIdLaptop;
    @FXML
    private ComboBox<String> comboMarca;
    @FXML
    private TextField campoNumeroModelo;
    @FXML
    private TextField campoNombreComercial;

    @FXML
    private ComboBox<String> comboProcesador;
    @FXML
    private ComboBox<String> comboGpu;

    @FXML
    private ComboBox<String> comboTipoRam;
    @FXML
    private TextField campoCantidadRam;

    @FXML
    private ComboBox<String> comboTipoAlmacenamiento;
    @FXML
    private TextField campoCantidadAlmacenamiento;

    @FXML
    private TextField campoTamanoPantalla;
    @FXML
    private ComboBox<String> comboResolucion;
    @FXML
    private TextField campoPeso;

    @FXML
    private TextField campoPrecioDetalle;
    @FXML
    private TextField campoPrecioMayorista;
    @FXML
    private TextField campoCantMinMayorista;
    @FXML
    private TextField campoCantAlerta;
    @FXML
    private TextField campoGarantia;

    @FXML
    private Label lblMensaje;

    @FXML
    public void initialize() {
        actualizarIdPreview();
        cargarCombos();
    }

    private void actualizarIdPreview() {
        int siguiente = Servicio.getInstance().getGenIdLaptop();
        campoIdLaptop.setText(String.format("LAP%03d", siguiente));
        campoIdLaptop.setEditable(false);
    }

    private void cargarCombos() {
        comboMarca.getItems().clear();
        for (Marca marca : Servicio.getInstance().getMisMarcas().values()) {
            comboMarca.getItems().add(marca.getNombreMarca());
        }

        comboProcesador.getItems().setAll(
                "Intel Core i3", "Intel Core i5", "Intel Core i7", "Intel Core i9",
                "AMD Ryzen 3", "AMD Ryzen 5", "AMD Ryzen 7", "AMD Ryzen 9",
                "Apple M1", "Apple M2", "Apple M3", "Otro"
        );

        comboGpu.getItems().setAll(
                "Intel Iris Xe", "Intel UHD Graphics",
                "NVIDIA GTX 1650", "NVIDIA RTX 3050", "NVIDIA RTX 3060", "NVIDIA RTX 4060", "NVIDIA RTX 4070",
                "AMD Radeon Graphics", "AMD Radeon RX 6600M", "Apple Integrated", "Otro"
        );

        comboTipoRam.getItems().setAll("DDR3", "DDR4", "DDR5", "LPDDR4x", "LPDDR5", "Unified Memory");
        comboTipoAlmacenamiento.getItems().setAll("HDD", "SSD SATA", "SSD NVMe M.2", "eMMC");
        comboResolucion.getItems().setAll("1366x768 (HD)", "1920x1080 (FHD)", "2560x1440 (QHD)", "3840x2160 (4K)", "Otra");

        comboMarca.getSelectionModel().selectFirst();
        comboProcesador.getSelectionModel().selectFirst();
        comboGpu.getSelectionModel().selectFirst();
        comboTipoRam.getSelectionModel().selectFirst();
        comboTipoAlmacenamiento.getSelectionModel().selectFirst();
        comboResolucion.getSelectionModel().selectFirst();
    }

    private boolean validarCampos() {
        StringBuilder errores = new StringBuilder();

        if (comboMarca.getValue() == null) errores.append("- Seleccione una marca.\n");
        if (campoNumeroModelo.getText() == null || campoNumeroModelo.getText().trim().isEmpty()) errores.append("- El número de modelo es obligatorio.\n");
        if (campoNombreComercial.getText() == null || campoNombreComercial.getText().trim().isEmpty()) errores.append("- El nombre comercial es obligatorio.\n");

        validarFloat(campoCantidadRam.getText(), "Cantidad RAM", errores, false);
        validarFloat(campoCantidadAlmacenamiento.getText(), "Capacidad de Almacenamiento", errores, false);
        validarFloat(campoTamanoPantalla.getText(), "Tamaño de pantalla", errores, false);
        validarFloat(campoPeso.getText(), "Peso", errores, false);

        validarFloat(campoPrecioDetalle.getText(), "Precio detalle", errores, false);
        validarFloat(campoPrecioMayorista.getText(), "Precio mayorista", errores, false);

        validarEntero(campoCantMinMayorista.getText(), "Cantidad min. mayorista", errores, false);
        validarEntero(campoCantAlerta.getText(), "Cantidad alerta stock", errores, false);
        validarEntero(campoGarantia.getText(), "Meses de garantía", errores, true); // Permite 0

        if (errores.length() > 0) {
            lblMensaje.setStyle("-fx-text-fill: #b23b3b;");
            lblMensaje.setText(errores.toString());
            return false;
        }
        return true;
    }

    private void validarFloat(String valor, String nombreCampo, StringBuilder errores, boolean permiteCero) {
        if (valor == null || valor.trim().isEmpty()) {
            errores.append("- El campo ").append(nombreCampo).append(" es obligatorio.\n");
            return;
        }
        try {
            float num = Float.parseFloat(valor.trim());
            if (num < 0 || (!permiteCero && num == 0)) {
                errores.append("- ").append(nombreCampo).append(" debe ser mayor a ").append(permiteCero ? "-1" : "0").append(".\n");
            }
        } catch (NumberFormatException e) {
            errores.append("- ").append(nombreCampo).append(" debe ser un número válido.\n");
        }
    }

    private void validarEntero(String valor, String nombreCampo, StringBuilder errores, boolean permiteCero) {
        if (valor == null || valor.trim().isEmpty()) {
            errores.append("- El campo ").append(nombreCampo).append(" es obligatorio.\n");
            return;
        }
        try {
            int num = Integer.parseInt(valor.trim());
            if (num < 0 || (!permiteCero && num == 0)) {
                errores.append("- ").append(nombreCampo).append(" debe ser mayor a ").append(permiteCero ? "-1" : "0").append(".\n");
            }
        } catch (NumberFormatException e) {
            errores.append("- ").append(nombreCampo).append(" debe ser un número entero.\n");
        }
    }

    private Marca buscarMarcaPorNombre(String nombre) {
        for (Marca marca : Servicio.getInstance().getMisMarcas().values()) {
            if (marca.getNombreMarca().equals(nombre)) {
                return marca;
            }
        }
        return null;
    }

    @FXML
    private void ControlarGuardar(ActionEvent event) {
        if (!validarCampos()) {
            return;
        }

        String idLaptop = Servicio.getInstance().generarIdLaptop();
        Marca marca = buscarMarcaPorNombre(comboMarca.getValue());

        Laptop laptop = new Laptop(
                idLaptop,
                campoNumeroModelo.getText().trim(),
                campoNombreComercial.getText().trim(),
                marca,
                Float.parseFloat(campoPeso.getText().trim()),
                comboProcesador.getValue(),
                comboGpu.getValue(),
                comboTipoRam.getValue(),
                Float.parseFloat(campoCantidadRam.getText().trim()),
                comboTipoAlmacenamiento.getValue(),
                Float.parseFloat(campoCantidadAlmacenamiento.getText().trim()),
                Float.parseFloat(campoTamanoPantalla.getText().trim()),
                comboResolucion.getValue(),
                0.0f, // Costo promedio inicializado en 0 por trigger logic
                Float.parseFloat(campoPrecioDetalle.getText().trim()),
                Float.parseFloat(campoPrecioMayorista.getText().trim()),
                Integer.parseInt(campoCantMinMayorista.getText().trim()),
                Integer.parseInt(campoCantAlerta.getText().trim()),
                0, // Stock inicializado en 0 por trigger logic
                Integer.parseInt(campoGarantia.getText().trim())
        );

        Servicio.getInstance().registrarLaptop(laptop);

        limpiarFormulario();

        lblMensaje.setStyle("-fx-text-fill: #2e7d32;");
        lblMensaje.setText("Laptop " + idLaptop + " registrada correctamente.");
    }

    private void limpiarFormulario() {
        campoNumeroModelo.clear();
        campoNombreComercial.clear();
        campoCantidadRam.clear();
        campoCantidadAlmacenamiento.clear();
        campoTamanoPantalla.clear();
        campoPeso.clear();
        campoPrecioDetalle.clear();
        campoPrecioMayorista.clear();
        campoCantMinMayorista.clear();
        campoCantAlerta.clear();
        campoGarantia.clear();

        cargarCombos();
        actualizarIdPreview();
    }

    @FXML
    private void ControlarLimpiar(ActionEvent event) {
        limpiarFormulario();
        lblMensaje.setText("");
    }

    @FXML
    private void ControlarCancelar(ActionEvent event) {
        Stage stage = (Stage) campoIdLaptop.getScene().getWindow();
        stage.close();
    }
}