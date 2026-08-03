package visual;

import DataBase.EquipoDAO;
import logico.Equipo;
import logico.Servicio;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.LocalDate;
import java.util.Optional;

public class ListaEquipoController {

    @FXML private TableView<Equipo> tablaEquipos;
    @FXML private TableColumn<Equipo, String> colId;
    @FXML private TableColumn<Equipo, String> colLaptop;
    @FXML private TableColumn<Equipo, String> colSerie;
    @FXML private TableColumn<Equipo, String> colColor;
    @FXML private TableColumn<Equipo, String> colEstado;
    @FXML private TableColumn<Equipo, String> colDisponibilidad;
    @FXML private TableColumn<Equipo, String> colDescuento;
    @FXML private TableColumn<Equipo, LocalDate> colFecha;

    @FXML private TextField txtSerie;
    @FXML private TextField txtColor;
    @FXML private ComboBox<String> comboEstado;
    @FXML private ComboBox<String> comboDisponibilidad;
    @FXML private DatePicker dateFecha;
    @FXML private TextField txtDescuento;

    private final ObservableList<Equipo> listaEquipos = FXCollections.observableArrayList();
    private Equipo equipoSeleccionado;

    @FXML
    public void initialize() {
        // ARREGLO: Se agregaron "Listo para Entrega" y "Ajuste - Inactivo"
        comboEstado.setItems(FXCollections.observableArrayList("nuevo", "open-box", "reparado"));
        comboDisponibilidad.setItems(FXCollections.observableArrayList(
                "Disponible", "En Reparacion", "Desechado", "Perdido/Robado", "Vendido", "Listo para Entrega", "Ajuste - Inactivo"
        ));

        colId.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getIdEquipo() != null ? cell.getValue().getIdEquipo() : ""));

        colLaptop.setCellValueFactory(cell -> new SimpleStringProperty(
                (cell.getValue().getLaptop() != null && cell.getValue().getLaptop().getIdLaptop() != null)
                        ? cell.getValue().getLaptop().getIdLaptop()
                        : "N/A"
        ));

        colSerie.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getNumeroSerie() != null ? cell.getValue().getNumeroSerie() : ""));
        colColor.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getColor() != null ? cell.getValue().getColor() : ""));
        colEstado.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getEstado() != null ? cell.getValue().getEstado() : ""));
        colDisponibilidad.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getDisponibilidad() != null ? cell.getValue().getDisponibilidad() : ""));
        colDescuento.setCellValueFactory(cell -> new SimpleStringProperty(String.valueOf(cell.getValue().getDescuentoPorCondicion())));
        colFecha.setCellValueFactory(cell -> new SimpleObjectProperty<>(cell.getValue().getFechaIngreso()));

        tablaEquipos.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            if (newSel != null) {
                equipoSeleccionado = newSel;
                cargarDatosEnFormulario(equipoSeleccionado);
            }
        });

        cargarTabla();
    }

    private void cargarTabla() {
        listaEquipos.clear();
        // ARREGLO: Ahora leemos directamente desde la memoria RAM central para evitar fantasmas
        if (Servicio.getInstance().getMisEquipos() != null) {
            listaEquipos.addAll(Servicio.getInstance().getMisEquipos().values());
        }
        tablaEquipos.setItems(listaEquipos);
    }

    private void cargarDatosEnFormulario(Equipo equipo) {
        txtSerie.setText(equipo.getNumeroSerie() != null ? equipo.getNumeroSerie() : "");
        txtColor.setText(equipo.getColor() != null ? equipo.getColor() : "");
        comboEstado.setValue(equipo.getEstado());
        comboDisponibilidad.setValue(equipo.getDisponibilidad());
        dateFecha.setValue(equipo.getFechaIngreso());
        txtDescuento.setText(String.valueOf(equipo.getDescuentoPorCondicion()));
    }

    @FXML
    private void handleActualizar() {
        if (equipoSeleccionado == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Atención", "Selecciona un equipo de la tabla.");
            return;
        }

        try {
            equipoSeleccionado.setNumeroSerie(txtSerie.getText().trim());
            equipoSeleccionado.setColor(txtColor.getText().trim());
            equipoSeleccionado.setEstado(comboEstado.getValue());
            equipoSeleccionado.setDisponibilidad(comboDisponibilidad.getValue());
            equipoSeleccionado.setFechaIngreso(dateFecha.getValue());

            if (txtDescuento.getText().trim().isEmpty()) {
                equipoSeleccionado.setDescuentoPorCondicion(0.0f);
            } else {
                equipoSeleccionado.setDescuentoPorCondicion(Float.parseFloat(txtDescuento.getText().trim()));
            }

            // Al actualizar la BD aquí, la memoria RAM también se actualiza porque "equipoSeleccionado"
            // es una referencia directa al objeto guardado en Servicio.getInstance().getMisEquipos()
            EquipoDAO.getInstance().actualizar(equipoSeleccionado);

            mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "Equipo actualizado correctamente.");
            tablaEquipos.refresh();
            handleLimpiar();

        } catch (NumberFormatException e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error de Formato", "El campo 'Descuento' debe ser un número válido.");
        } catch (Exception e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error al Actualizar", e.getMessage());
        }
    }

    @FXML
    private void handleEliminar() {
        if (equipoSeleccionado == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Atención", "Selecciona un equipo de la tabla.");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "¿Deseas eliminar permanentemente el equipo " + equipoSeleccionado.getIdEquipo() + "?");
        Optional<ButtonType> res = confirm.showAndWait();

        if (res.isPresent() && res.get() == ButtonType.OK) {

            // ARREGLO: Se elimina de la base de datos
            EquipoDAO.getInstance().borrar(equipoSeleccionado.getIdEquipo());

            // ARREGLO: Se elimina de la memoria central para no causar errores de sincronización
            Servicio.getInstance().getMisEquipos().remove(equipoSeleccionado.getIdEquipo());

            // Se elimina de la tabla visual
            listaEquipos.remove(equipoSeleccionado);

            mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "Equipo eliminado correctamente.");
            handleLimpiar();
        }
    }

    @FXML
    private void handleLimpiar() {
        tablaEquipos.getSelectionModel().clearSelection();
        equipoSeleccionado = null;

        txtSerie.clear();
        txtColor.clear();
        comboEstado.getSelectionModel().clearSelection();
        comboDisponibilidad.getSelectionModel().clearSelection();
        dateFecha.setValue(null);
        txtDescuento.clear();
    }

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensaje) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}