package visual;

import DataBase.DetalleFacturaDAO;
import DataBase.EquipoDAO;
import DataBase.MovimientoDAO;
import DataBase.ReclamoDAO;
import DataBase.ResolucionDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import logico.DetalleFactura;
import logico.Equipo;
import logico.Movimiento;
import logico.Reclamo;
import logico.Resolucion;
import logico.Servicio;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class ResolverReclamoController {

    @FXML
    private TextField campoIdResolucion;

    @FXML
    private ComboBox<String> comboReclamo;

    @FXML
    private DatePicker dateFechaResolucion;

    @FXML
    private ComboBox<String> comboAccionTomada;

    @FXML
    private TextField campoEquipoEntrante;

    @FXML
    private ComboBox<String> comboEquipoSaliente;

    @FXML
    private TextField campoMontoCobrado;

    @FXML
    private TextField campoMontoReembolsado;

    @FXML
    private Label lblMensaje;

    private Reclamo reclamoSeleccionado;
    private DetalleFactura detalleEntrante;

    @FXML
    public void initialize() {
        dateFechaResolucion.setValue(LocalDate.now());
        actualizarIdPreview();
        cargarReclamos();

        comboReclamo.setOnAction(e -> onReclamoSelected());
        comboAccionTomada.setOnAction(e -> onAccionSelected());
        comboEquipoSaliente.setOnAction(e -> onEquipoSalienteSelected());
    }

    private void actualizarIdPreview() {
        int siguiente = Servicio.getInstance().getGenIdResolucion();
        campoIdResolucion.setText(String.format("RES%03d", siguiente));
    }

    private void cargarReclamos() {
        comboReclamo.getItems().clear();
        for (Reclamo r : ReclamoDAO.getInstance().EncontrarTodos()) {
            if ("Activo".equalsIgnoreCase(r.getEstadoCaso()) || "Abierto".equalsIgnoreCase(r.getEstadoCaso())) {
                comboReclamo.getItems().add(r.getIdReclamo() + " - " + r.getTipoSolicitud());
            }
        }
    }

    private void onReclamoSelected() {
        String sel = comboReclamo.getValue();
        if (sel == null) return;

        String idRec = sel.split(" - ")[0];
        reclamoSeleccionado = ReclamoDAO.getInstance().encontrarPorId(idRec);

        if (reclamoSeleccionado != null) {
            ArrayList<DetalleFactura> detalles = DetalleFacturaDAO.getInstance().encontrarPorFactura(reclamoSeleccionado.getIdFactura());
            detalleEntrante = null;
            for (DetalleFactura df : detalles) {
                if (df.getIdDetalleFactura().equals(reclamoSeleccionado.getIdDetalleFactura())) {
                    detalleEntrante = df;
                    break;
                }
            }

            if (detalleEntrante != null) {
                Equipo eqEntrante = EquipoDAO.getInstance().encontrarPorId(detalleEntrante.getEquipoVendido().getIdEquipo());
                campoEquipoEntrante.setText(eqEntrante.getIdEquipo());

                if ("En Reparacion".equalsIgnoreCase(eqEntrante.getDisponibilidad())) {
                    lblMensaje.setStyle("-fx-text-fill: #b23b3b; -fx-font-weight: bold;");
                    lblMensaje.setText("Alerta: El equipo sigue en reparación. Debe reportarlo como arreglado en Soporte Técnico antes de resolver.");
                    comboAccionTomada.setDisable(true);
                    comboEquipoSaliente.setDisable(true);
                    return;
                } else {
                    lblMensaje.setText("");
                    comboAccionTomada.setDisable(false);
                }
            }

            comboAccionTomada.getItems().clear();
            comboEquipoSaliente.getItems().clear();
            comboEquipoSaliente.setDisable(true);
            campoMontoCobrado.setText("0.0");
            campoMontoReembolsado.setText("0.0");
            campoMontoCobrado.setEditable(false);
            campoMontoReembolsado.setEditable(false);

            String tipo = reclamoSeleccionado.getTipoSolicitud();
            boolean garantia = reclamoSeleccionado.isEnGarantia();

            if ("Reparacion Tecnica".equals(tipo)) {
                if (garantia) {
                    comboAccionTomada.getItems().add("Reparacion Cubierta");
                } else {
                    comboAccionTomada.getItems().addAll("Devuelta sin Reparar", "Reparacion Facturada");
                }
            } else if ("Devolucion por Reembolso".equals(tipo)) {
                comboAccionTomada.getItems().add("Reembolso de Dinero");
            } else if ("Cambio por Reemplazo de Equipo".equals(tipo)) {
                comboAccionTomada.getItems().add("Reemplazo por Garantia");
                comboEquipoSaliente.setDisable(false);
                cargarEquiposDisponibles();
            }

            comboAccionTomada.getSelectionModel().selectFirst();
            onAccionSelected();
        }
    }

    private void cargarEquiposDisponibles() {
        for (Equipo eq : EquipoDAO.getInstance().EncontrarTodos()) {
            if ("Disponible".equalsIgnoreCase(eq.getDisponibilidad())) {
                comboEquipoSaliente.getItems().add(eq.getIdEquipo() + " - " + eq.getLaptop().getNombreComercial());
            }
        }
    }

    private void onAccionSelected() {
        String accion = comboAccionTomada.getValue();
        if (accion == null || reclamoSeleccionado == null || detalleEntrante == null) return;

        campoMontoCobrado.setText("0.0");
        campoMontoReembolsado.setText("0.0");
        campoMontoCobrado.setEditable(false);
        campoMontoReembolsado.setEditable(false);

        if ("Reparacion Cubierta".equals(accion) || "Devuelta sin Reparar".equals(accion)) {
            campoMontoCobrado.setText("0.0");
            campoMontoReembolsado.setText("0.0");
        } else if ("Reparacion Facturada".equals(accion)) {
            campoMontoCobrado.setEditable(true);
            campoMontoCobrado.setText("0.0");
        } else if ("Reembolso de Dinero".equals(accion)) {
            campoMontoReembolsado.setText(String.valueOf(detalleEntrante.getSubtotalLinea()));
        } else if ("Reemplazo por Garantia".equals(accion)) {
            onEquipoSalienteSelected();
        }
    }

    private void onEquipoSalienteSelected() {
        if (reclamoSeleccionado == null || detalleEntrante == null || !"Reemplazo por Garantia".equals(comboAccionTomada.getValue())) return;

        String sel = comboEquipoSaliente.getValue();
        if (sel == null) {
            campoMontoCobrado.setText("0.0");
            campoMontoReembolsado.setText("0.0");
            return;
        }

        String idEqSaliente = sel.split(" - ")[0];
        Equipo eqSaliente = EquipoDAO.getInstance().encontrarPorId(idEqSaliente);

        if (eqSaliente != null) {
            float precioPagado = detalleEntrante.getSubtotalLinea();
            float precioNuevo = eqSaliente.getLaptop().getPrecioDetalle();

            if (reclamoSeleccionado.isEnGarantia()) {
                float diff = precioNuevo - precioPagado;
                if (diff > 0) {
                    campoMontoCobrado.setText(String.valueOf(diff));
                    campoMontoReembolsado.setText("0.0");
                } else {
                    campoMontoCobrado.setText("0.0");
                    campoMontoReembolsado.setText(String.valueOf(Math.abs(diff)));
                }
            } else {
                campoMontoCobrado.setText(String.valueOf(precioNuevo));
                campoMontoReembolsado.setText("0.0");
            }
        }
    }

    private boolean validarCampos() {
        StringBuilder errores = new StringBuilder();

        if (comboReclamo.getValue() == null) {
            errores.append("- Debe seleccionar un reclamo.\n");
        } else if (detalleEntrante != null) {
            Equipo eqEntrante = EquipoDAO.getInstance().encontrarPorId(detalleEntrante.getEquipoVendido().getIdEquipo());
            if (eqEntrante != null && "En Reparacion".equalsIgnoreCase(eqEntrante.getDisponibilidad())) {
                errores.append("- El equipo sigue en reparación.\n");
            }
        }

        if (dateFechaResolucion.getValue() == null) {
            errores.append("- La fecha de resolucion es obligatoria.\n");
        }

        if (comboAccionTomada.getValue() == null) {
            errores.append("- Debe seleccionar la accion tomada.\n");
        }

        if (comboAccionTomada.getValue() != null && "Reemplazo por Garantia".equals(comboAccionTomada.getValue())) {
            if (comboEquipoSaliente.getValue() == null) {
                errores.append("- Debe seleccionar el equipo de reemplazo (Equipo Saliente).\n");
            }
        }

        try {
            Float.parseFloat(campoMontoCobrado.getText().trim());
        } catch (Exception e) {
            errores.append("- El monto cobrado debe ser un numero valido.\n");
        }

        try {
            Float.parseFloat(campoMontoReembolsado.getText().trim());
        } catch (Exception e) {
            errores.append("- El monto reembolsado debe ser un numero valido.\n");
        }

        if (errores.length() > 0) {
            lblMensaje.setStyle("-fx-text-fill: #b23b3b; -fx-font-weight: bold;");
            lblMensaje.setText(errores.toString());
            return false;
        }
        return true;
    }

    @FXML
    private void ControlarGuardar(ActionEvent event) {
        if (!validarCampos()) {
            return;
        }

        String idResolucion = Servicio.getInstance().generarIdResolucion();
        String idReclamo = comboReclamo.getValue().split(" - ")[0];
        String accion = comboAccionTomada.getValue();
        float montoCobrado = Float.parseFloat(campoMontoCobrado.getText().trim());
        float montoReembolsado = Float.parseFloat(campoMontoReembolsado.getText().trim());
        LocalDate fecha = dateFechaResolucion.getValue();

        String equipoEntrante = campoEquipoEntrante.getText();
        String equipoSaliente = null;

        if ("Reparacion Cubierta".equals(accion) || "Reparacion Facturada".equals(accion) || "Devuelta sin Reparar".equals(accion)) {
            equipoSaliente = equipoEntrante;

            Equipo eqDevuelto = EquipoDAO.getInstance().encontrarPorId(equipoSaliente);
            if (eqDevuelto != null) {
                int countMov = MovimientoDAO.getInstance().encontrarPorEquipo(eqDevuelto.getIdEquipo()).size();
                String idMovimiento = Servicio.getInstance().generarIdDependiente(eqDevuelto.getIdEquipo(), countMov);

                Movimiento mov = new Movimiento();
                mov.setIdMovimiento(idMovimiento);
                mov.setIdEquipo(eqDevuelto.getIdEquipo());
                mov.setTipoMovimiento("Salida por Devolucion");
                mov.setDescripcionMovimiento("Resolucion: " + idResolucion);
                mov.setFechaHoraMovimiento(LocalDateTime.now());
                mov.setIdEstanteOrigen(null);
                mov.setNivelOrigen(0);
                mov.setIdEstanteDestino(null);
                mov.setNivelDestino(0);

                eqDevuelto.setDisponibilidad("Vendido");

                MovimientoDAO.getInstance().guardar(mov);
                EquipoDAO.getInstance().actualizar(eqDevuelto);
            }

        } else if ("Reemplazo por Garantia".equals(accion)) {
            equipoSaliente = comboEquipoSaliente.getValue().split(" - ")[0];

            Equipo eqSaliente = EquipoDAO.getInstance().encontrarPorId(equipoSaliente);
            if (eqSaliente != null) {
                int countMov = MovimientoDAO.getInstance().encontrarPorEquipo(eqSaliente.getIdEquipo()).size();
                String idMovimiento = Servicio.getInstance().generarIdDependiente(eqSaliente.getIdEquipo(), countMov);

                Movimiento mov = new Movimiento();
                mov.setIdMovimiento(idMovimiento);
                mov.setIdEquipo(eqSaliente.getIdEquipo());
                mov.setTipoMovimiento("Salida por Cambio de Equipo");
                mov.setDescripcionMovimiento("Resolucion: " + idResolucion);
                mov.setFechaHoraMovimiento(LocalDateTime.now());

                String idEstanteOrigen = null;
                for (logico.Estante estante : Servicio.getInstance().getMisEstantes().values()) {
                    for (Equipo eq : estante.getEquiposAlmacenados()) {
                        if (eq.getIdEquipo().equals(eqSaliente.getIdEquipo())) {
                            idEstanteOrigen = estante.getIdEstante();
                            estante.getEquiposAlmacenados().remove(eq);
                            break;
                        }
                    }
                    if (idEstanteOrigen != null) break;
                }

                mov.setIdEstanteOrigen(idEstanteOrigen);
                mov.setNivelOrigen(0);
                mov.setIdEstanteDestino(null);
                mov.setNivelDestino(0);

                eqSaliente.setDisponibilidad("Vendido");

                MovimientoDAO.getInstance().guardar(mov);
                EquipoDAO.getInstance().actualizar(eqSaliente);
            }
        }

        Resolucion resolucion = new Resolucion(
                idResolucion,
                idReclamo,
                equipoEntrante,
                equipoSaliente,
                accion,
                montoCobrado,
                montoReembolsado,
                fecha
        );

        try {
            ResolucionDAO.getInstance().guardar(resolucion);
            Servicio.getInstance().registrarResolucion(resolucion);

            reclamoSeleccionado.setEstadoCaso("Finalizado");
            ReclamoDAO.getInstance().actualizar(reclamoSeleccionado);

            lblMensaje.setStyle("-fx-text-fill: #2e7d32; -fx-font-weight: bold;");
            lblMensaje.setText("¡Resolucion " + idResolucion + " registrada con exito!");

            limpiarFormulario();

        } catch (Exception e) {
            lblMensaje.setStyle("-fx-text-fill: #b23b3b; -fx-font-weight: bold;");
            lblMensaje.setText("Error al guardar la resolucion: " + e.getMessage());
        }
    }

    private void limpiarFormulario() {
        comboReclamo.getSelectionModel().clearSelection();
        campoEquipoEntrante.clear();
        comboEquipoSaliente.getItems().clear();
        comboEquipoSaliente.setDisable(true);
        comboAccionTomada.getItems().clear();
        campoMontoCobrado.setText("0.0");
        campoMontoReembolsado.setText("0.0");
        campoMontoCobrado.setEditable(false);
        campoMontoReembolsado.setEditable(false);
        dateFechaResolucion.setValue(LocalDate.now());
        reclamoSeleccionado = null;
        detalleEntrante = null;
        actualizarIdPreview();
        cargarReclamos();
    }

    @FXML
    private void ControlarLimpiar(ActionEvent event) {
        limpiarFormulario();
        lblMensaje.setText("");
    }

    @FXML
    private void ControlarCancelar(ActionEvent event) {
        Stage stage = (Stage) comboReclamo.getScene().getWindow();
        stage.close();
    }
}