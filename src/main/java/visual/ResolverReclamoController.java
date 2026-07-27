package visual;

import DataBase.ReclamoDAO;
import DataBase.ResolucionDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import logico.Reclamo;
import logico.Resolucion;
import logico.Servicio;

import java.time.LocalDate;

public class ResolverReclamoController {

    @FXML
    private TextField campoIdResolucion;

    @FXML
    private TextField campoIdReclamo;

    @FXML
    private TextField campoInfoReclamo;

    @FXML
    private DatePicker dateFechaResolucion;

    @FXML
    private ComboBox<String> comboAccionTomada;

    @FXML
    private TextField campoEquipoEntrante;

    @FXML
    private TextField campoEquipoSaliente;

    @FXML
    private TextField campoMontoCobrado;

    @FXML
    private TextField campoMontoReembolsado;

    @FXML
    private Label lblMensaje;

    private Reclamo reclamoEncontrado = null;

    @FXML
    public void initialize() {
        // Cargar las acciones exactas permitidas por el constraint CHECK de la base de datos
        comboAccionTomada.getItems().setAll(
                "Reembolso de Dinero",
                "Reparación Cubierta",
                "Reparación Facturada",
                "Reemplazo por Garantía",
                "Devuelta sin Reparar"
        );
        comboAccionTomada.getSelectionModel().selectFirst();

        dateFechaResolucion.setValue(LocalDate.now());
        actualizarIdPreview();
    }

    private void actualizarIdPreview() {
        int siguiente = Servicio.getInstance().getGenIdResolucion();
        campoIdResolucion.setText(String.format("RES%03d", siguiente));
    }

    @FXML
    private void ControlarBuscarReclamo(ActionEvent event) {
        String idBuscado = campoIdReclamo.getText();

        if (idBuscado == null || idBuscado.trim().isEmpty()) {
            mostrarError("- Por favor, introduzca un ID de Reclamo.");
            return;
        }

        try {
            reclamoEncontrado = ReclamoDAO.getInstance().encontrarPorId(idBuscado.trim());

            if (reclamoEncontrado != null) {
                campoInfoReclamo.setText("Cliente: " + reclamoEncontrado.getIdCliente() + " | Solicitud: " + reclamoEncontrado.getTipoSolicitud());
                campoEquipoEntrante.setText(reclamoEncontrado.getIdDetalleFactura()); // O el ID del equipo asociado

                lblMensaje.setStyle("-fx-text-fill: #2e7d32; -fx-font-weight: bold;");
                lblMensaje.setText("Reclamo encontrado correctamente.");
            } else {
                mostrarError("- No se encontró ningún reclamo con ese ID.");
                campoInfoReclamo.clear();
                reclamoEncontrado = null;
            }
        } catch (Exception e) {
            mostrarError("Error al buscar el reclamo: " + e.getMessage());
        }
    }

    private boolean validarCampos() {
        StringBuilder errores = new StringBuilder();

        if (reclamoEncontrado == null) {
            errores.append("- Debe buscar y validar un Reclamo existente.\n");
        }

        if (dateFechaResolucion.getValue() == null) {
            errores.append("- La fecha de resolución es obligatoria.\n");
        }

        if (comboAccionTomada.getValue() == null) {
            errores.append("- Debe seleccionar la acción tomada.\n");
        }

        try {
            if (campoMontoCobrado.getText() == null || campoMontoCobrado.getText().trim().isEmpty()) {
                campoMontoCobrado.setText("0.0");
            } else {
                Float.parseFloat(campoMontoCobrado.getText().trim());
            }
        } catch (NumberFormatException e) {
            errores.append("- El monto cobrado debe ser un número válido.\n");
        }

        try {
            if (campoMontoReembolsado.getText() == null || campoMontoReembolsado.getText().trim().isEmpty()) {
                campoMontoReembolsado.setText("0.0");
            } else {
                Float.parseFloat(campoMontoReembolsado.getText().trim());
            }
        } catch (NumberFormatException e) {
            errores.append("- El monto reembolsado debe ser un número válido.\n");
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

        String idResolucion = Servicio.getInstance().generarIdResolucion();
        String idReclamo = reclamoEncontrado.getIdReclamo();
        String equipoEntrante = campoEquipoEntrante.getText().trim().isEmpty() ? null : campoEquipoEntrante.getText().trim();
        String equipoSaliente = campoEquipoSaliente.getText().trim().isEmpty() ? null : campoEquipoSaliente.getText().trim();
        String accion = comboAccionTomada.getValue();
        float montoCobrado = Float.parseFloat(campoMontoCobrado.getText().trim());
        float montoReembolsado = Float.parseFloat(campoMontoReembolsado.getText().trim());
        LocalDate fecha = dateFechaResolucion.getValue();

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
            Servicio.getInstance().registrarResolucion(resolucion);

            // Opcional: Actualizar el estado del caso del reclamo a "Cerrado"
            reclamoEncontrado.setEstadoCaso("Cerrado");
            ReclamoDAO.getInstance().actualizar(reclamoEncontrado);

            lblMensaje.setStyle("-fx-text-fill: #2e7d32; -fx-font-weight: bold;");
            lblMensaje.setText("¡Resolución " + idResolucion + " registrada con éxito!");

            limpiarFormulario();

        } catch (Exception e) {
            mostrarError("Error al guardar la resolución: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void limpiarFormulario() {
        campoIdReclamo.clear();
        campoInfoReclamo.clear();
        campoEquipoEntrante.clear();
        campoEquipoSaliente.clear();
        campoMontoCobrado.setText("0.0");
        campoMontoReembolsado.setText("0.0");
        dateFechaResolucion.setValue(LocalDate.now());
        comboAccionTomada.getSelectionModel().selectFirst();
        reclamoEncontrado = null;
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
        Stage stage = (Stage) campoIdReclamo.getScene().getWindow();
        stage.close();
    }
}