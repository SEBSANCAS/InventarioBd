package visual;

import DataBase.EquipoDAO;
import DataBase.MovimientoDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import logico.Equipo;
import logico.Estante;
import logico.Movimiento;
import logico.Servicio;

import java.time.LocalDateTime;

public class RegistrarMovimientoController {

    @FXML
    private ComboBox<String> comboTipoMovimiento;

    @FXML
    private ComboBox<String> comboEquipo;

    @FXML
    private TextField campoUbicacionActual;

    @FXML
    private ComboBox<String> comboEstanteDestino;

    @FXML
    private TextArea areaDescripcion;

    @FXML
    private Label lblMensaje;

    private Equipo equipoSeleccionado;
    private String idEstanteOrigen;
    private int nivelOrigen = 0;

    @FXML
    public void initialize() {
        comboTipoMovimiento.getItems().setAll(
                "Traslado Interno",
                "Salida por Robo/Perdida",
                "Salida por mal Estado",
                "Ajuste de Inventario",
                "Reingreso desde Soporte Tecnico"
        );

        comboTipoMovimiento.setOnAction(e -> onTipoMovimientoSelected());
        comboEquipo.setOnAction(e -> onEquipoSelected());
    }

    private void onTipoMovimientoSelected() {
        String tipo = comboTipoMovimiento.getValue();
        if (tipo == null) return;

        comboEquipo.getItems().clear();
        comboEstanteDestino.getItems().clear();
        campoUbicacionActual.clear();
        equipoSeleccionado = null;
        idEstanteOrigen = null;
        nivelOrigen = 0;
        areaDescripcion.clear();
        lblMensaje.setText("");

        if ("Reingreso desde Soporte Tecnico".equals(tipo)) {
            for (Equipo eq : EquipoDAO.getInstance().EncontrarTodos()) {
                if ("En Reparacion".equalsIgnoreCase(eq.getDisponibilidad())) {
                    comboEquipo.getItems().add(eq.getIdEquipo() + " - " + eq.getLaptop().getNombreComercial());
                }
            }
            comboEstanteDestino.setDisable(false);
            cargarEstantesDestino();
        } else {
            for (Equipo eq : EquipoDAO.getInstance().EncontrarTodos()) {
                if ("Disponible".equalsIgnoreCase(eq.getDisponibilidad())) {
                    comboEquipo.getItems().add(eq.getIdEquipo() + " - " + eq.getLaptop().getNombreComercial());
                }
            }

            if ("Traslado Interno".equals(tipo)) {
                comboEstanteDestino.setDisable(false);
                cargarEstantesDestino();
            } else {
                comboEstanteDestino.setDisable(true);
            }
        }
    }

    private void cargarEstantesDestino() {
        comboEstanteDestino.getItems().clear();
        for (Estante e : Servicio.getInstance().getMisEstantes().values()) {
            comboEstanteDestino.getItems().add(e.getIdEstante() + " - " + e.getIdEstante());
        }
    }

    private void onEquipoSelected() {
        String sel = comboEquipo.getValue();
        if (sel == null) return;

        String idEquipo = sel.split(" - ")[0];
        equipoSeleccionado = EquipoDAO.getInstance().encontrarPorId(idEquipo);
        idEstanteOrigen = null;
        nivelOrigen = 0;
        campoUbicacionActual.setText("No ubicado en estantes (Ej. Soporte/Vendido)");

        if (equipoSeleccionado != null) {
            String sqlUbicacion = "SELECT id_ubicacion FROM Equipo WHERE IdEquipo = '" + idEquipo + "'";
            try (java.sql.Connection conn = DataBase.DatabaseConnection.getConnection();
                 java.sql.Statement stmt = conn.createStatement();
                 java.sql.ResultSet rs = stmt.executeQuery(sqlUbicacion)) {
                if (rs.next()) {
                    String idUbi = rs.getString("id_ubicacion");
                    if (idUbi != null && !idUbi.trim().isEmpty()) {
                        String[] partes = idUbi.split("-N");
                        if (partes.length == 2) {
                            idEstanteOrigen = partes[0];
                            nivelOrigen = Integer.parseInt(partes[1]);
                        } else {
                            idEstanteOrigen = idUbi;
                        }
                        campoUbicacionActual.setText(idUbi);
                    }
                }
            } catch (Exception e) {
            }
        }
    }

    private boolean validarCampos() {
        StringBuilder errores = new StringBuilder();

        if (comboTipoMovimiento.getValue() == null) {
            errores.append("- Debe seleccionar un tipo de movimiento.\n");
        }

        if (equipoSeleccionado == null) {
            errores.append("- Debe seleccionar un equipo.\n");
        }

        if (!comboEstanteDestino.isDisabled() && comboEstanteDestino.getValue() == null) {
            errores.append("- Debe seleccionar un estante destino.\n");
        }

        if (areaDescripcion.getText() == null || areaDescripcion.getText().trim().isEmpty()) {
            errores.append("- La justificacion/descripcion es obligatoria.\n");
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

        String tipo = comboTipoMovimiento.getValue();
        String idDestino = comboEstanteDestino.isDisabled() || comboEstanteDestino.getValue() == null ? null : comboEstanteDestino.getValue().split(" - ")[0];

        int countMov = MovimientoDAO.getInstance().encontrarPorEquipo(equipoSeleccionado.getIdEquipo()).size();
        String idMovimiento = Servicio.getInstance().generarIdDependiente(equipoSeleccionado.getIdEquipo(), countMov);

        Movimiento mov = new Movimiento();
        mov.setIdMovimiento(idMovimiento);
        mov.setIdEquipo(equipoSeleccionado.getIdEquipo());
        mov.setTipoMovimiento(tipo);
        mov.setDescripcionMovimiento(areaDescripcion.getText().trim());
        mov.setFechaHoraMovimiento(LocalDateTime.now());

        if ("Reingreso desde Soporte Tecnico".equals(tipo)) {
            mov.setIdEstanteOrigen(null);
            mov.setNivelOrigen(0);
        } else {
            mov.setIdEstanteOrigen(idEstanteOrigen);
            mov.setNivelOrigen(nivelOrigen);
        }

        mov.setIdEstanteDestino(idDestino);
        mov.setNivelDestino(idDestino != null ? 1 : 0);

        if ("Traslado Interno".equals(tipo)) {
            equipoSeleccionado.setDisponibilidad("Disponible");
        } else if ("Salida por Robo/Perdida".equals(tipo)) {
            equipoSeleccionado.setDisponibilidad("Perdido/Robado");
        } else if ("Salida por mal Estado".equals(tipo)) {
            equipoSeleccionado.setDisponibilidad("Desechado");
        } else if ("Ajuste de Inventario".equals(tipo)) {
            equipoSeleccionado.setDisponibilidad("Ajuste - Inactivo");
        } else if ("Reingreso desde Soporte Tecnico".equals(tipo)) {
            equipoSeleccionado.setDisponibilidad("Listo para Entrega");
        }

        try {
            MovimientoDAO.getInstance().guardar(mov);
            EquipoDAO.getInstance().actualizar(equipoSeleccionado);
            Servicio.getInstance().getMisEquipos().put(equipoSeleccionado.getIdEquipo(), equipoSeleccionado);

            String sqlUpdateUbi = "UPDATE Equipo SET id_ubicacion = ? WHERE IdEquipo = ?";
            try (java.sql.Connection conn = DataBase.DatabaseConnection.getConnection();
                 java.sql.PreparedStatement ps = conn.prepareStatement(sqlUpdateUbi)) {
                if (idDestino != null) {
                    ps.setString(1, idDestino + "-N1");
                } else {
                    ps.setNull(1, java.sql.Types.VARCHAR);
                }
                ps.setString(2, equipoSeleccionado.getIdEquipo());
                ps.executeUpdate();
            } catch(Exception e) {}

            lblMensaje.setStyle("-fx-text-fill: #2e7d32; -fx-font-weight: bold;");
            lblMensaje.setText("¡Movimiento registrado correctamente!");

            limpiarFormulario();
        } catch (Exception e) {
            lblMensaje.setStyle("-fx-text-fill: #b23b3b; -fx-font-weight: bold;");
            lblMensaje.setText("Error al guardar el movimiento: " + e.getMessage());
        }
    }

    private void limpiarFormulario() {
        comboTipoMovimiento.getSelectionModel().clearSelection();
        comboEquipo.getItems().clear();
        comboEstanteDestino.getItems().clear();
        comboEstanteDestino.setDisable(true);
        campoUbicacionActual.clear();
        areaDescripcion.clear();
        equipoSeleccionado = null;
        idEstanteOrigen = null;
        nivelOrigen = 0;
    }

    @FXML
    private void ControlarLimpiar(ActionEvent event) {
        limpiarFormulario();
        lblMensaje.setText("");
    }

    @FXML
    private void ControlarCancelar(ActionEvent event) {
        Stage stage = (Stage) comboTipoMovimiento.getScene().getWindow();
        stage.close();
    }
}