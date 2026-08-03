package visual;

import DataBase.LaptopDAO;
import DataBase.SuplidorLaptopDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import logico.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RegistroAdquisicionController {

    @FXML
    private TextField campoIdAdquisicion;

    @FXML
    private ComboBox<String> comboSuplidor;

    @FXML
    private Label lblEstado;

    @FXML
    private DatePicker campoFechaEntrega;

    @FXML
    private VBox contenedorDetalles;

    @FXML
    private Button btnAgregarLinea;

    @FXML
    private Label lblMontoTotal;

    @FXML
    private Label lblMensaje;

    private final Map<String, Suplidor> mapaSuplidores = new HashMap<>();
    private final List<LineaDetalle> lineas = new ArrayList<>();
    private float totalAdquisicion = 0;

    @FXML
    public void initialize() {
        actualizarIdPreview();
        cargarSuplidores();

        comboSuplidor.setOnAction(e -> {
            boolean habilitar = comboSuplidor.getValue() != null;
            btnAgregarLinea.setDisable(!habilitar);
            lineas.clear();
            contenedorDetalles.getChildren().clear();
            recalcularTotal();
            campoFechaEntrega.setValue(null);
        });
    }

    private void actualizarIdPreview() {
        int siguiente = Servicio.getInstance().getGenIdAdquisicion();
        campoIdAdquisicion.setText(String.format("ADQ%03d", siguiente));
    }

    private void cargarSuplidores() {
        mapaSuplidores.clear();
        comboSuplidor.getItems().clear();
        for (Suplidor s : Servicio.getInstance().getMisSuplidores().values()) {
            String display = s.getIdSuplidor() + " - " + s.getNombreComercial();
            comboSuplidor.getItems().add(display);
            mapaSuplidores.put(display, s);
        }
    }

    @FXML
    private void ControlarAgregarLinea(ActionEvent event) {
        LineaDetalle nuevaLinea = new LineaDetalle();
        lineas.add(nuevaLinea);
        contenedorDetalles.getChildren().add(nuevaLinea.contenedor);
    }

    private void recalcularTotal() {
        totalAdquisicion = 0;
        for (LineaDetalle linea : lineas) {
            try {
                int cant = Integer.parseInt(linea.campoCantidad.getText().trim());
                float prec = Float.parseFloat(linea.campoPrecio.getText().trim());
                float sub = cant * prec;

                if (sub < 0) {
                    sub = 0;
                }

                linea.lblSubtotal.setText(String.format("%.2f", sub));
                totalAdquisicion += sub;
            } catch (Exception e) {
                linea.lblSubtotal.setText("0.00");
            }
        }
        lblMontoTotal.setText(String.format("RD$ %.2f", totalAdquisicion));
    }

    private void recalcularFechaEstimada() {
        Suplidor suplidor = mapaSuplidores.get(comboSuplidor.getValue());
        if (suplidor == null) {
            return;
        }

        HashMap<Laptop, DetalleLaptopSuplidor> detallesDAO = SuplidorLaptopDAO.getInstance().encontrarPorSuplidor(suplidor.getIdSuplidor());
        int maxDias = 0;
        boolean haySeleccion = false;

        for (LineaDetalle linea : lineas) {
            String selLaptop = linea.comboLaptop.getValue();
            if (selLaptop != null) {
                String idLaptop = extraerId(selLaptop);
                for (Map.Entry<Laptop, DetalleLaptopSuplidor> entry : detallesDAO.entrySet()) {
                    if (entry.getKey().getIdLaptop().equals(idLaptop)) {
                        int dias = entry.getValue().getDiasEntrega();
                        if (dias > maxDias) {
                            maxDias = dias;
                        }
                        haySeleccion = true;
                        break;
                    }
                }
            }
        }

        if (haySeleccion) {
            campoFechaEntrega.setValue(LocalDate.now().plusDays(maxDias));
        } else {
            campoFechaEntrega.setValue(null);
        }
    }

    private String extraerId(String seleccion) {
        if (seleccion != null && seleccion.contains(" - ")) {
            return seleccion.split(" - ")[0];
        }
        return seleccion;
    }

    @FXML
    private void ControlarGuardar(ActionEvent event) {
        if (!validarFormulario()) {
            return;
        }

        Suplidor suplidor = mapaSuplidores.get(comboSuplidor.getValue());
        String idAdq = Servicio.getInstance().generarIdAdquisicion();

        Adquisicion adquisicion = new Adquisicion(
                idAdq,
                suplidor,
                LocalDate.now(),
                campoFechaEntrega.getValue(),
                "Emitida",
                totalAdquisicion
        );

        ArrayList<DetalleAdquisicion> detalles = new ArrayList<>();
        int contador = 0;

        for (LineaDetalle linea : lineas) {
            String idLaptop = extraerId(linea.comboLaptop.getValue());
            Laptop laptop = Servicio.getInstance().getMisLaptops().get(idLaptop);
            int cant = Integer.parseInt(linea.campoCantidad.getText().trim());
            float prec = Float.parseFloat(linea.campoPrecio.getText().trim());
            float subtotal = prec * cant;

            if (subtotal < 0) {
                subtotal = 0;
            }

            String idDetalle = Servicio.getInstance().generarIdDependiente(idAdq, contador);

            DetalleAdquisicion detalle = new DetalleAdquisicion(
                    idDetalle,
                    idAdq,
                    laptop,
                    cant,
                    prec,
                    subtotal
            );

            detalles.add(detalle);
            contador++;
        }

        Servicio.getInstance().registrarNuevaAdquisicion(adquisicion, detalles);

        lblMensaje.setStyle("-fx-text-fill: #2e7d32;");
        lblMensaje.setText("Orden de compra registrada con éxito: " + idAdq);

        btnAgregarLinea.setDisable(true);
        comboSuplidor.getSelectionModel().clearSelection();
        lineas.clear();
        contenedorDetalles.getChildren().clear();
        campoFechaEntrega.setValue(null);
        recalcularTotal();
        actualizarIdPreview();
    }

    private boolean validarFormulario() {
        StringBuilder errores = new StringBuilder();

        if (comboSuplidor.getValue() == null) {
            errores.append("- Seleccione un suplidor.\n");
        }

        if (lineas.isEmpty()) {
            errores.append("- Agregue al menos un modelo de laptop a la orden.\n");
        }

        boolean errorLineas = false;
        for (LineaDetalle linea : lineas) {
            if (linea.comboLaptop.getValue() == null) {
                errorLineas = true;
            }
            try {
                int c = Integer.parseInt(linea.campoCantidad.getText().trim());
                float p = Float.parseFloat(linea.campoPrecio.getText().trim());
                if (c <= 0 || p < 0) {
                    errorLineas = true;
                }
            } catch (Exception e) {
                errorLineas = true;
            }
        }

        if (errorLineas) {
            errores.append("- Verifique que todas las líneas tengan modelo seleccionado y cantidades válidas (mayores a 0 y no negativas).\n");
        }

        if (errores.length() > 0) {
            lblMensaje.setStyle("-fx-text-fill: #b23b3b;");
            lblMensaje.setText(errores.toString());
            return false;
        }

        return true;
    }

    @FXML
    private void ControlarLimpiar(ActionEvent event) {
        comboSuplidor.getSelectionModel().clearSelection();
        campoFechaEntrega.setValue(null);
        lineas.clear();
        contenedorDetalles.getChildren().clear();
        btnAgregarLinea.setDisable(true);
        lblMensaje.setText("");
        recalcularTotal();
    }

    @FXML
    private void ControlarCancelar(ActionEvent event) {
        Stage stage = (Stage) btnAgregarLinea.getScene().getWindow();
        stage.close();
    }

    private class LineaDetalle {
        HBox contenedor;
        ComboBox<String> comboLaptop;
        TextField campoCantidad;
        TextField campoPrecio;
        Label lblSubtotal;
        Button btnEliminar;

        public LineaDetalle() {
            contenedor = new HBox(10);
            contenedor.setAlignment(Pos.CENTER_LEFT);

            comboLaptop = new ComboBox<>();
            comboLaptop.setPrefWidth(260);
            comboLaptop.setStyle("-fx-border-color: #AEDFF7; -fx-border-radius: 5; -fx-background-radius: 5;");
            comboLaptop.setPromptText("Seleccione modelo...");

            Suplidor suplidor = mapaSuplidores.get(comboSuplidor.getValue());
            if (suplidor != null) {
                HashMap<Laptop, DetalleLaptopSuplidor> dls = SuplidorLaptopDAO.getInstance().encontrarPorSuplidor(suplidor.getIdSuplidor());
                for (Laptop l : dls.keySet()) {
                    Laptop laptopCompleta = LaptopDAO.getInstance().encontrarPorId(l.getIdLaptop());
                    if (laptopCompleta != null) {
                        comboLaptop.getItems().add(laptopCompleta.getIdLaptop() + " - " + laptopCompleta.getNombreComercial());
                    } else {
                        comboLaptop.getItems().add(l.getIdLaptop() + " - Modelo Desconocido");
                    }
                }
            }

            campoCantidad = new TextField("1");
            campoCantidad.setPrefWidth(90);
            campoCantidad.setStyle("-fx-border-color: #AEDFF7; -fx-border-radius: 5; -fx-background-radius: 5;");

            campoPrecio = new TextField("0.00");
            campoPrecio.setPrefWidth(100);
            campoPrecio.setStyle("-fx-border-color: #AEDFF7; -fx-border-radius: 5; -fx-background-radius: 5;");

            lblSubtotal = new Label("0.00");
            lblSubtotal.setPrefWidth(100);
            lblSubtotal.setStyle("-fx-font-weight: bold;");

            btnEliminar = new Button("X");
            btnEliminar.setStyle("-fx-text-fill: #b23b3b; -fx-background-color: transparent; -fx-font-weight: bold;");

            contenedor.getChildren().addAll(comboLaptop, campoCantidad, campoPrecio, lblSubtotal, btnEliminar);

            comboLaptop.setOnAction(e -> {
                String sel = comboLaptop.getValue();
                if (sel != null && suplidor != null) {
                    String id = extraerId(sel);
                    HashMap<Laptop, DetalleLaptopSuplidor> dls = SuplidorLaptopDAO.getInstance().encontrarPorSuplidor(suplidor.getIdSuplidor());
                    for (Map.Entry<Laptop, DetalleLaptopSuplidor> entry : dls.entrySet()) {
                        if (entry.getKey().getIdLaptop().equals(id)) {
                            campoPrecio.setText(String.valueOf(entry.getValue().getPrecioAcordado()));
                            break;
                        }
                    }
                    recalcularFechaEstimada();
                    recalcularTotal();
                }
            });

            campoCantidad.textProperty().addListener((obs, oldVal, newVal) -> {
                if (!newVal.matches("\\d*")) {
                    campoCantidad.setText(newVal.replaceAll("[^\\d]", ""));
                }
                recalcularTotal();
            });

            campoPrecio.textProperty().addListener((obs, oldVal, newVal) -> {
                recalcularTotal();
            });

            btnEliminar.setOnAction(e -> {
                contenedorDetalles.getChildren().remove(contenedor);
                lineas.remove(this);
                recalcularFechaEstimada();
                recalcularTotal();
            });
        }
    }
}