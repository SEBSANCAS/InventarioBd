package visual;

import DataBase.DetalleFacturaDAO;
import DataBase.EquipoDAO;
import DataBase.EstanteDAO;
import DataBase.FacturaDAO;
import DataBase.MovimientoDAO;
import DataBase.ReclamoDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import logico.Cliente;
import logico.DetalleFactura;
import logico.Equipo;
import logico.Factura;
import logico.Laptop;
import logico.Movimiento;
import logico.Reclamo;
import logico.Servicio;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class RegistroReclamoController {

    @FXML
    private TextField campoIdReclamo;

    @FXML
    private DatePicker dateFecha;

    @FXML
    private ComboBox<String> comboCliente;

    @FXML
    private ComboBox<String> comboFactura;

    @FXML
    private ComboBox<String> comboIdDetalle;

    @FXML
    private CheckBox checkEnGarantia;

    @FXML
    private ComboBox<String> comboTipoSolicitud;

    @FXML
    private TextArea areaDiagnostico;

    @FXML
    private Label lblMensaje;

    private Factura facturaSeleccionada;
    private DetalleFactura detalleSeleccionado;

    @FXML
    public void initialize() {
        comboTipoSolicitud.getItems().setAll(
                "Devolucion por Reembolso",
                "Reparacion Tecnica",
                "Cambio por Reemplazo de Equipo"
        );
        comboTipoSolicitud.getSelectionModel().selectFirst();

        dateFecha.setValue(LocalDate.now());
        checkEnGarantia.setDisable(true);
        actualizarIdPreview();
        cargarClientes();

        comboCliente.setOnAction(e -> cargarFacturas());
        comboFactura.setOnAction(e -> cargarDetalles());
        comboIdDetalle.setOnAction(e -> evaluarGarantia());
    }

    private void actualizarIdPreview() {
        int siguiente = Servicio.getInstance().getGenIdReclamo();
        campoIdReclamo.setText(String.format("REC%03d", siguiente));
    }

    private void cargarClientes() {
        comboCliente.getItems().clear();
        for (Cliente c : Servicio.getInstance().getMisClientes().values()) {
            comboCliente.getItems().add(c.getIdCliente() + " - " + c.getNombres());
        }
    }

    private void cargarFacturas() {
        comboFactura.getItems().clear();
        comboIdDetalle.getItems().clear();
        facturaSeleccionada = null;
        detalleSeleccionado = null;
        checkEnGarantia.setSelected(false);

        String clienteSel = comboCliente.getValue();
        if (clienteSel != null) {
            String idCliente = clienteSel.split(" - ")[0];
            for (Factura f : Servicio.getInstance().getMiInventarioFacturas().values()) {
                if (f.getCliente() != null && f.getCliente().getIdCliente().equals(idCliente)) {
                    comboFactura.getItems().add(f.getIdFactura());
                }
            }
        }
    }

    private void cargarDetalles() {
        comboIdDetalle.getItems().clear();
        detalleSeleccionado = null;
        checkEnGarantia.setSelected(false);

        String idFactura = comboFactura.getValue();
        if (idFactura != null) {
            facturaSeleccionada = Servicio.getInstance().getMiInventarioFacturas().get(idFactura);
            if (facturaSeleccionada != null) {
                ArrayList<DetalleFactura> detalles = DetalleFacturaDAO.getInstance().encontrarPorFactura(idFactura);
                if (detalles != null) {
                    for (DetalleFactura df : detalles) {
                        comboIdDetalle.getItems().add(df.getIdDetalleFactura() + " - " + df.getEquipoVendido().getIdEquipo());
                    }
                }
            }
        }
    }

    private void evaluarGarantia() {
        String selDetalle = comboIdDetalle.getValue();
        checkEnGarantia.setSelected(false);
        detalleSeleccionado = null;

        if (selDetalle != null && facturaSeleccionada != null) {
            String idDet = selDetalle.split(" - ")[0];
            ArrayList<DetalleFactura> detalles = DetalleFacturaDAO.getInstance().encontrarPorFactura(facturaSeleccionada.getIdFactura());
            for (DetalleFactura df : detalles) {
                if (df.getIdDetalleFactura().equals(idDet)) {
                    detalleSeleccionado = df;
                    break;
                }
            }

            if (detalleSeleccionado != null && detalleSeleccionado.getEquipoVendido() != null) {
                Laptop laptop = detalleSeleccionado.getEquipoVendido().getLaptop();
                long mesesTranscurridos = ChronoUnit.MONTHS.between(facturaSeleccionada.getFechaEmision(), LocalDate.now());

                int mesesGarantia = laptop.getMesesGarantia();
                checkEnGarantia.setSelected(mesesTranscurridos <= mesesGarantia);
            }
        }
    }

    private boolean validarCampos() {
        StringBuilder errores = new StringBuilder();

        if (comboFactura.getValue() == null) {
            errores.append("- Debe seleccionar una Factura.\n");
        }

        if (comboCliente.getValue() == null) {
            errores.append("- Debe seleccionar un Cliente.\n");
        }

        if (comboIdDetalle.getValue() == null) {
            errores.append("- Debe seleccionar un Equipo de la factura.\n");
        }

        if (dateFecha.getValue() == null) {
            errores.append("- Debe seleccionar la fecha del reclamo.\n");
        }

        if (comboTipoSolicitud.getValue() == null) {
            errores.append("- Debe seleccionar el tipo de solicitud.\n");
        }

        if (areaDiagnostico.getText() == null || areaDiagnostico.getText().trim().isEmpty()) {
            errores.append("- El diagnostico tecnico es obligatorio.\n");
        }

        if (errores.length() > 0) {
            mostrarError(errores.toString());
            return false;
        }
        return true;
    }

    @FXML
    private void ControlarGuardar(ActionEvent event) {
        if (!validarCampos()) {
            return;
        }

        String tipoSolicitud = comboTipoSolicitud.getValue();

        if (!checkEnGarantia.isSelected() && (tipoSolicitud.equals("Devolucion por Reembolso") || tipoSolicitud.equals("Cambio por Reemplazo de Equipo"))) {
            mostrarError("- No se aceptan reembolsos ni devoluciones fuera de garantia.");
            return;
        }

        String idReclamo = Servicio.getInstance().generarIdReclamo();
        String idFactura = comboFactura.getValue();
        String idCliente = comboCliente.getValue().split(" - ")[0];
        String idDetalle = comboIdDetalle.getValue().split(" - ")[0];
        LocalDate fechaSeleccionada = dateFecha.getValue();
        boolean aplicaGarantia = checkEnGarantia.isSelected();
        String estadoCaso = "Activo";
        String diagnostico = areaDiagnostico.getText().trim();

        Reclamo nuevoReclamo = new Reclamo(
                idReclamo,
                idFactura,
                idDetalle,
                idCliente,
                fechaSeleccionada,
                aplicaGarantia,
                tipoSolicitud,
                diagnostico,
                estadoCaso
        );

        try {
            ReclamoDAO.getInstance().guardar(nuevoReclamo);
            Servicio.getInstance().registrarReclamo(nuevoReclamo);

            if (detalleSeleccionado != null && detalleSeleccionado.getEquipoVendido() != null) {
                Equipo equipo = EquipoDAO.getInstance().encontrarPorId(detalleSeleccionado.getEquipoVendido().getIdEquipo());
                if (equipo != null) {
                    int countMov = MovimientoDAO.getInstance().encontrarPorEquipo(equipo.getIdEquipo()).size();
                    String idMovimiento = Servicio.getInstance().generarIdDependiente(equipo.getIdEquipo(), countMov);

                    Movimiento mov = new Movimiento();
                    mov.setIdMovimiento(idMovimiento);
                    mov.setIdEquipo(equipo.getIdEquipo());
                    mov.setFechaHoraMovimiento(LocalDateTime.now());
                    mov.setDescripcionMovimiento("Reclamo: " + idReclamo);

                    if (tipoSolicitud.equals("Devolucion por Reembolso") || tipoSolicitud.equals("Cambio por Reemplazo de Equipo")) {
                        String idUbicacionDestino = EstanteDAO.getInstance().obtenerPrimeraUbicacionDisponible();
                        mov.setTipoMovimiento("Entrada por Devolucion de Cliente");
                        mov.setIdEstanteOrigen(null);
                        mov.setNivelOrigen(0);

                        if (idUbicacionDestino != null) {
                            String[] partes = idUbicacionDestino.split("-N");
                            if (partes.length == 2) {
                                mov.setIdEstanteDestino(partes[0]);
                                mov.setNivelDestino(Integer.parseInt(partes[1]));
                            }
                        } else {
                            mov.setIdEstanteDestino(null);
                            mov.setNivelDestino(0);
                        }

                        equipo.setDisponibilidad("Disponible");
                        equipo.setEstado("open-box");
                        equipo.setDescuentoPorCondicion((float)(equipo.getLaptop().getPrecioDetalle() * 0.15));

                    } else if (tipoSolicitud.equals("Reparacion Tecnica")) {
                        mov.setTipoMovimiento("Ingreso a Soporte Tecnico");
                        mov.setIdEstanteOrigen(null);
                        mov.setNivelOrigen(0);
                        mov.setIdEstanteDestino(null);
                        mov.setNivelDestino(0);

                        equipo.setDisponibilidad("En Reparacion");
                    }

                    MovimientoDAO.getInstance().guardar(mov);
                    EquipoDAO.getInstance().actualizar(equipo);

                    logico.Estante estante = null;
                    if (mov.getIdEstanteDestino() != null) {
                        estante = Servicio.getInstance().getMisEstantes().get(mov.getIdEstanteDestino());
                        if (estante != null) {
                            estante.getEquiposAlmacenados().add(equipo);
                        }
                    }
                }
            }

            lblMensaje.setStyle("-fx-text-fill: #2e7d32; -fx-font-weight: bold;");
            lblMensaje.setText("¡Reclamo " + idReclamo + " registrado correctamente!");

            limpiarFormulario();

        } catch (Exception e) {
            mostrarError("Error al procesar el reclamo: " + e.getMessage());
        }
    }

    private void limpiarFormulario() {
        comboFactura.getItems().clear();
        comboCliente.getSelectionModel().clearSelection();
        comboIdDetalle.getItems().clear();
        areaDiagnostico.clear();
        checkEnGarantia.setSelected(false);
        dateFecha.setValue(LocalDate.now());

        comboTipoSolicitud.getSelectionModel().selectFirst();

        actualizarIdPreview();
        facturaSeleccionada = null;
        detalleSeleccionado = null;
    }

    private void mostrarError(String mensaje) {
        lblMensaje.setStyle("-fx-text-fill: #b23b3b; -fx-font-weight: bold;");
        lblMensaje.setText(mensaje);
    }

    @FXML
    private void ControlarLimpiar(ActionEvent event) {
        limpiarFormulario();
        lblMensaje.setText("");
    }

    @FXML
    private void ControlarCancelar(ActionEvent event) {
        Stage stage = (Stage) comboCliente.getScene().getWindow();
        stage.close();
    }
}