package visual;

import DataBase.FacturaDAO;
import DataBase.ReclamoDAO;
import DataBase.DetalleFacturaDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import logico.Factura;
import logico.Reclamo;
import logico.DetalleFactura;
import logico.Servicio;

import java.time.LocalDate;
import java.util.ArrayList;

public class RegistroReclamoController {

    @FXML
    private TextField campoIdReclamo;

    @FXML
    private DatePicker dateFecha;

    @FXML
    private TextField campoIdFactura;

    @FXML
    private TextField campoIdCliente;

    @FXML
    private ComboBox<String> comboIdDetalle;

    @FXML
    private CheckBox checkEnGarantia;

    @FXML
    private ComboBox<String> comboTipoSolicitud;

    @FXML
    private ComboBox<String> comboEstadoCaso;

    @FXML
    private TextArea areaDiagnostico;

    @FXML
    private Label lblMensaje;

    @FXML
    public void initialize() {
        comboTipoSolicitud.getItems().setAll(
                "Devolución por Reembolso",
                "Reparación Técnica",
                "Cambio por Reemplazo de Equipo"
        );
        comboTipoSolicitud.getSelectionModel().selectFirst();

        comboEstadoCaso.getItems().setAll("Abierto", "En Revisión Técnica", "Cerrado");
        comboEstadoCaso.getSelectionModel().select("Abierto");

        dateFecha.setValue(LocalDate.now());
        actualizarIdPreview();
    }

    private void actualizarIdPreview() {
        // Asume que en tu clase Servicio tienes un generador para reclamos
        int siguiente = Servicio.getInstance().getGenIdReclamo();
        campoIdReclamo.setText(String.format("REC%03d", siguiente));
    }

    @FXML
    private void ControlarBuscarFactura(ActionEvent event) {
        String idFacturaBusqueda = campoIdFactura.getText();

        if (idFacturaBusqueda == null || idFacturaBusqueda.trim().isEmpty()) {
            mostrarError("- Por favor, introduzca un ID de Factura para buscar.");
            return;
        }

        try {
            // Buscamos la factura usando tu FacturaDAO
            Factura factura = FacturaDAO.getInstance().encontrarPorId(idFacturaBusqueda.trim());

            if (factura != null) {
                campoIdCliente.setText(factura.getCliente().getIdCliente());

                comboIdDetalle.getItems().clear();

                ArrayList<DetalleFactura> detalles = DetalleFacturaDAO.getInstance().encontrarPorFactura(factura.getIdFactura());

                if (detalles != null && !detalles.isEmpty()) {
                    for (DetalleFactura detalle : detalles) {
                        // Agregamos el ID del detalle al ComboBox
                        comboIdDetalle.getItems().add(detalle.getIdDetalleFactura());
                    }
                    comboIdDetalle.getSelectionModel().selectFirst();

                    lblMensaje.setStyle("-fx-text-fill: #2e7d32; -fx-font-weight: bold;");
                    lblMensaje.setText("Factura encontrada. Seleccione el equipo afectado.");
                } else {
                    mostrarError("- La factura existe, pero no tiene equipos registrados (Detalles vacíos).");
                }

            } else {
                mostrarError("- No se encontró ninguna factura con el ID proporcionado.");
                campoIdCliente.clear();
                comboIdDetalle.getItems().clear();
            }

        } catch (Exception e) {
            mostrarError("Error al buscar la factura: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private boolean validarCampos() {
        StringBuilder errores = new StringBuilder();

        if (campoIdFactura.getText() == null || campoIdFactura.getText().trim().isEmpty()) {
            errores.append("- Debe buscar y asociar una Factura.\n");
        }

        if (campoIdCliente.getText() == null || campoIdCliente.getText().trim().isEmpty()) {
            errores.append("- No hay un Cliente asociado (Busque la factura primero).\n");
        }

        // Descomenta esto cuando el ComboBox de detalles ya esté cargando datos reales
        // if (comboIdDetalle.getValue() == null || comboIdDetalle.getValue().trim().isEmpty()) {
        //     errores.append("- Debe seleccionar un Equipo (Detalle) de la factura.\n");
        // }

        if (dateFecha.getValue() == null) {
            errores.append("- Debe seleccionar la fecha del reclamo.\n");
        }

        if (comboTipoSolicitud.getValue() == null) {
            errores.append("- Debe seleccionar el tipo de solicitud.\n");
        }

        if (comboEstadoCaso.getValue() == null) {
            errores.append("- Debe seleccionar el estado inicial del caso.\n");
        }

        if (areaDiagnostico.getText() == null || areaDiagnostico.getText().trim().isEmpty()) {
            errores.append("- El diagnóstico técnico / razón es obligatorio.\n");
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

        String idReclamo = Servicio.getInstance().generarIdReclamo();
        String idFactura = campoIdFactura.getText().trim();
        String idCliente = campoIdCliente.getText().trim();

        // Si el combo está vacío temporalmente, mandamos un valor de prueba.
        // Cambia esto a comboIdDetalle.getValue() cuando conectes los detalles.
        String idDetalle = comboIdDetalle.getValue() != null ? comboIdDetalle.getValue() : "DET-PRUEBA";

        LocalDate fechaSeleccionada = dateFecha.getValue();
        boolean aplicaGarantia = checkEnGarantia.isSelected();
        String tipoSolicitud = comboTipoSolicitud.getValue();
        String estadoCaso = comboEstadoCaso.getValue();
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

            lblMensaje.setStyle("-fx-text-fill: #2e7d32; -fx-font-weight: bold;");
            lblMensaje.setText("¡Reclamo " + idReclamo + " registrado correctamente!");

            limpiarFormulario();

        } catch (Exception e) {
            mostrarError("Error al guardar en BD: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void limpiarFormulario() {
        campoIdFactura.clear();
        campoIdCliente.clear();
        comboIdDetalle.getItems().clear();
        areaDiagnostico.clear();
        checkEnGarantia.setSelected(false);
        dateFecha.setValue(LocalDate.now());

        comboTipoSolicitud.getSelectionModel().selectFirst();
        comboEstadoCaso.getSelectionModel().select("Abierto");

        actualizarIdPreview();
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
        Stage stage = (Stage) campoIdFactura.getScene().getWindow();
        stage.close();
    }
}