package visual;

import DataBase.AuditoriaRentabilidadDAO;
import DataBase.MovimientoDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import logico.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class RegistroFacturacionController {

    @FXML
    private ComboBox<String> comboCliente;

    @FXML
    private TextField campoNCF;

    @FXML
    private VBox contenedorDetalles;

    @FXML
    private Button btnAgregarLinea;

    @FXML
    private Label lblSubtotalG;

    @FXML
    private Label lblImpuestos;

    @FXML
    private Label lblTotalGeneral;

    @FXML
    private Label lblMensaje;

    private final Map<String, Cliente> mapaClientes = new HashMap<>();
    private final List<LineaFactura> lineas = new ArrayList<>();

    private float subtotalGeneral = 0;
    private float impuestosGeneral = 0;
    private float totalGeneral = 0;
    private final float TASA_ITBIS = 0.18f;

    @FXML
    public void initialize() {
        cargarClientes();

        comboCliente.setOnAction(e -> {
            boolean habilitar = comboCliente.getValue() != null;
            btnAgregarLinea.setDisable(!habilitar);
        });
    }

    private void cargarClientes() {
        mapaClientes.clear();
        comboCliente.getItems().clear();
        for (Cliente c : Servicio.getInstance().getMisClientes().values()) {
            String display = c.getIdCliente() + " - " + c.getNombres();
            comboCliente.getItems().add(display);
            mapaClientes.put(display, c);
        }
    }

    @FXML
    private void ControlarAgregarLinea(ActionEvent event) {
        LineaFactura nuevaLinea = new LineaFactura();
        lineas.add(nuevaLinea);
        contenedorDetalles.getChildren().add(nuevaLinea.contenedor);
    }

    private void actualizarPreciosPorVolumen() {
        Map<String, Integer> conteoModelos = new HashMap<>();

        for (LineaFactura linea : lineas) {
            String selEq = linea.comboEquipo.getValue();
            if (selEq != null) {
                String idEq = extraerId(selEq);
                Equipo eq = Servicio.getInstance().getMisEquipos().get(idEq);
                if (eq != null) {
                    String idLaptop = eq.getLaptop().getIdLaptop();
                    conteoModelos.put(idLaptop, conteoModelos.getOrDefault(idLaptop, 0) + 1);
                }
            }
        }

        for (LineaFactura linea : lineas) {
            String selEq = linea.comboEquipo.getValue();
            if (selEq != null) {
                String idEq = extraerId(selEq);
                Equipo eq = Servicio.getInstance().getMisEquipos().get(idEq);
                if (eq != null) {
                    Laptop lap = eq.getLaptop();
                    int cantidadModeloEnFactura = conteoModelos.getOrDefault(lap.getIdLaptop(), 0);

                    int cantMinima = lap.getCantMinMayorista() > 0 ? lap.getCantMinMayorista() : Integer.MAX_VALUE;

                    if (cantidadModeloEnFactura >= cantMinima) {
                        linea.campoPrecio.setText(String.valueOf(lap.getPrecioMayorista()));
                    } else {
                        linea.campoPrecio.setText(String.valueOf(lap.getPrecioDetalle()));
                    }

                    linea.campoDescuento.setText(String.valueOf(eq.getDescuentoPorCondicion()));
                }
            } else {
                linea.campoPrecio.setText("0.00");
                linea.campoDescuento.setText("0.00");
            }
        }
    }

    private void recalcularTotales() {
        subtotalGeneral = 0;

        for (LineaFactura linea : lineas) {
            try {
                float precio = Float.parseFloat(linea.campoPrecio.getText().trim());
                float desc = Float.parseFloat(linea.campoDescuento.getText().trim());
                float subtotalLinea = precio - desc;

                if(subtotalLinea < 0) subtotalLinea = 0;

                linea.lblSubtotal.setText(String.format("%.2f", subtotalLinea));
                subtotalGeneral += subtotalLinea;
            } catch (Exception e) {
                linea.lblSubtotal.setText("0.00");
            }
        }

        impuestosGeneral = subtotalGeneral * TASA_ITBIS;
        totalGeneral = subtotalGeneral + impuestosGeneral;

        lblSubtotalG.setText(String.format("RD$ %.2f", subtotalGeneral));
        lblImpuestos.setText(String.format("RD$ %.2f", impuestosGeneral));
        lblTotalGeneral.setText(String.format("RD$ %.2f", totalGeneral));
    }

    private String extraerId(String seleccion) {
        if (seleccion != null && seleccion.contains(" - ")) {
            return seleccion.split(" - ")[0];
        }
        return seleccion;
    }

    @FXML
    private void ControlarFacturar(ActionEvent event) {
        if (!validarFormulario()) {
            return;
        }

        String idFactura = Servicio.getInstance().generarIdFactura();
        Cliente cliente = mapaClientes.get(comboCliente.getValue());

        Factura factura = new Factura();
        factura.setIdFactura(idFactura);
        factura.setCliente(cliente);
        factura.setFechaEmision(LocalDate.now());
        factura.setNumeroComprobante(campoNCF.getText().trim());
        factura.setSubtotal(subtotalGeneral);
        factura.setImpuestos(impuestosGeneral);
        factura.setMontoTotal(totalGeneral);

        ArrayList<DetalleFactura> detalles = new ArrayList<>();
        int index = 0;

        for (LineaFactura linea : lineas) {
            String selEquipo = linea.comboEquipo.getValue();
            String idEquipo = extraerId(selEquipo);
            Equipo equipo = Servicio.getInstance().getMisEquipos().get(idEquipo);

            float precio = Float.parseFloat(linea.campoPrecio.getText().trim());
            float descuento = Float.parseFloat(linea.campoDescuento.getText().trim());
            float subtotal = precio - descuento;

            String idDetalle = Servicio.getInstance().generarIdDependiente(idFactura, index);

            DetalleFactura detalle = new DetalleFactura(
                    idDetalle,
                    precio,
                    descuento,
                    subtotal,
                    1,
                    equipo
            );

            detalles.add(detalle);
            index++;
        }

        Servicio.getInstance().registrarNuevaFactura(factura, detalles);

        Map<String, Integer> offsetsAuditorias = new HashMap<>();
        Map<String, Integer> offsetsMovimientos = new HashMap<>();

        for (DetalleFactura det : detalles) {
            Equipo eq = det.getEquipoVendido();
            Laptop lap = eq.getLaptop();

            int countAud = AuditoriaRentabilidadDAO.getInstance().busca(lap.getIdLaptop()).size() + offsetsAuditorias.getOrDefault(lap.getIdLaptop(), 0);
            String idAuditoria = Servicio.getInstance().generarIdDependiente(lap.getIdLaptop(), countAud);
            offsetsAuditorias.put(lap.getIdLaptop(), countAud + 1);

            AuditoriaRentabilidadDAO.getInstance().guardar(
                    idAuditoria,
                    det.getIdDetalleFactura(),
                    eq.getIdEquipo(),
                    det.getSubtotalLinea(),
                    lap.getCostoPromedioCompra()
            );

            int countMov = MovimientoDAO.getInstance().encontrarPorEquipo(eq.getIdEquipo()).size() + offsetsMovimientos.getOrDefault(eq.getIdEquipo(), 0);
            String idMovimiento = Servicio.getInstance().generarIdDependiente(eq.getIdEquipo(), countMov);
            offsetsMovimientos.put(eq.getIdEquipo(), countMov + 1);

            String estanteOrigenId = null;
            for (Estante estante : Servicio.getInstance().getMisEstantes().values()) {
                boolean retirado = estante.getEquiposAlmacenados().removeIf(e -> e.getIdEquipo().equals(eq.getIdEquipo()));
                if (retirado) {
                    estanteOrigenId = estante.getIdEstante();
                    break;
                }
            }

            Movimiento mov = new Movimiento();
            mov.setIdMovimiento(idMovimiento);
            mov.setIdEquipo(eq.getIdEquipo());
            mov.setTipoMovimiento("Salida/Venta");
            mov.setDescripcionMovimiento("Venta bajo la factura: " + idFactura);
            mov.setFechaHoraMovimiento(LocalDateTime.now());
            mov.setIdEstanteOrigen(estanteOrigenId);
            mov.setNivelOrigen(1);
            mov.setIdEstanteDestino(null);

            MovimientoDAO.getInstance().guardar(mov);
        }

        lblMensaje.setStyle("-fx-text-fill: #2e7d32; -fx-font-weight: bold;");
        lblMensaje.setText("¡Factura procesada con éxito! ID: " + idFactura);

        btnAgregarLinea.setDisable(true);
        lineas.clear();
        contenedorDetalles.getChildren().clear();
        recalcularTotales();
        campoNCF.clear();
        comboCliente.getSelectionModel().clearSelection();
    }

    private boolean validarFormulario() {
        StringBuilder errores = new StringBuilder();

        if (comboCliente.getValue() == null) {
            errores.append("- Seleccione un cliente.\n");
        }

        if (campoNCF.getText().trim().isEmpty()) {
            errores.append("- Ingrese el comprobante / NCF.\n");
        }

        if (lineas.isEmpty()) {
            errores.append("- Agregue al menos un equipo a la factura.\n");
        }

        boolean errorLineas = false;
        List<String> equiposSeleccionados = new ArrayList<>();

        for (LineaFactura linea : lineas) {
            if (linea.comboEquipo.getValue() == null) {
                errorLineas = true;
            } else {
                String idEq = extraerId(linea.comboEquipo.getValue());
                if(equiposSeleccionados.contains(idEq)) {
                    errores.append("- El equipo " + idEq + " está duplicado en la factura.\n");
                }
                equiposSeleccionados.add(idEq);
            }
            try {
                Float.parseFloat(linea.campoDescuento.getText().trim());
                Float.parseFloat(linea.campoPrecio.getText().trim());
            } catch (Exception e) {
                errorLineas = true;
            }
        }

        if (errorLineas) {
            errores.append("- Verifique que todas las líneas tengan un equipo seleccionado y montos válidos.\n");
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
        comboCliente.getSelectionModel().clearSelection();
        campoNCF.clear();
        lineas.clear();
        contenedorDetalles.getChildren().clear();
        btnAgregarLinea.setDisable(true);
        lblMensaje.setText("");
        recalcularTotales();
    }

    @FXML
    private void ControlarCancelar(ActionEvent event) {
        Stage stage = (Stage) btnAgregarLinea.getScene().getWindow();
        stage.close();
    }

    private class LineaFactura {
        HBox contenedor;
        ComboBox<String> comboLaptop;
        ComboBox<String> comboEquipo;
        TextField campoPrecio;
        TextField campoDescuento;
        Label lblSubtotal;
        Button btnEliminar;

        public LineaFactura() {
            contenedor = new HBox(10);
            contenedor.setAlignment(Pos.CENTER_LEFT);

            comboLaptop = new ComboBox<>();
            comboLaptop.setPrefWidth(200);
            comboLaptop.setStyle("-fx-border-color: #AEDFF7; -fx-border-radius: 5; -fx-background-radius: 5;");
            comboLaptop.setPromptText("Filtrar por modelo...");

            List<Equipo> disponibles = Servicio.getInstance().getMisEquipos().values().stream()
                    .filter(e -> e.getDisponibilidad().equalsIgnoreCase("Disponible"))
                    .collect(Collectors.toList());

            List<String> idsLaptopsConStock = disponibles.stream()
                    .map(e -> e.getLaptop().getIdLaptop())
                    .distinct()
                    .collect(Collectors.toList());

            for (String idLap : idsLaptopsConStock) {
                Laptop l = Servicio.getInstance().getMisLaptops().get(idLap);
                if (l != null) {
                    comboLaptop.getItems().add(l.getIdLaptop() + " - " + l.getNombreComercial());
                }
            }

            comboEquipo = new ComboBox<>();
            comboEquipo.setPrefWidth(200);
            comboEquipo.setStyle("-fx-border-color: #AEDFF7; -fx-border-radius: 5; -fx-background-radius: 5;");
            comboEquipo.setPromptText("Seleccione Serial...");

            campoPrecio = new TextField("0.00");
            campoPrecio.setPrefWidth(100);
            campoPrecio.setStyle("-fx-border-color: #AEDFF7; -fx-border-radius: 5; -fx-background-radius: 5;");
            campoPrecio.setEditable(false);

            campoDescuento = new TextField("0.00");
            campoDescuento.setPrefWidth(90);
            campoDescuento.setStyle("-fx-border-color: #AEDFF7; -fx-border-radius: 5; -fx-background-radius: 5;");

            lblSubtotal = new Label("0.00");
            lblSubtotal.setPrefWidth(100);
            lblSubtotal.setStyle("-fx-font-weight: bold;");

            btnEliminar = new Button("X");
            btnEliminar.setStyle("-fx-text-fill: #b23b3b; -fx-background-color: transparent; -fx-font-weight: bold;");

            contenedor.getChildren().addAll(comboLaptop, comboEquipo, campoPrecio, campoDescuento, lblSubtotal, btnEliminar);

            comboLaptop.setOnAction(e -> {
                String selLap = comboLaptop.getValue();
                comboEquipo.getItems().clear();
                campoPrecio.setText("0.00");
                campoDescuento.setText("0.00");

                if (selLap != null) {
                    String idLap = extraerId(selLap);
                    List<Equipo> eqDisponibles = disponibles.stream()
                            .filter(eq -> eq.getLaptop().getIdLaptop().equals(idLap))
                            .collect(Collectors.toList());

                    for(Equipo eq : eqDisponibles) {
                        boolean yaSeleccionado = lineas.stream()
                                .filter(l -> l != this && l.comboEquipo.getValue() != null)
                                .anyMatch(l -> extraerId(l.comboEquipo.getValue()).equals(eq.getIdEquipo()));

                        if(!yaSeleccionado) {
                            comboEquipo.getItems().add(eq.getIdEquipo() + " - " + eq.getNumeroSerie());
                        }
                    }
                }
                actualizarPreciosPorVolumen();
                recalcularTotales();
            });

            comboEquipo.setOnAction(e -> {
                actualizarPreciosPorVolumen();
                recalcularTotales();
            });

            campoDescuento.textProperty().addListener((obs, oldVal, newVal) -> {
                recalcularTotales();
            });

            campoPrecio.textProperty().addListener((obs, oldVal, newVal) -> {
                recalcularTotales();
            });

            btnEliminar.setOnAction(e -> {
                contenedorDetalles.getChildren().remove(contenedor);
                lineas.remove(this);
                actualizarPreciosPorVolumen();
                recalcularTotales();
            });
        }
    }
}