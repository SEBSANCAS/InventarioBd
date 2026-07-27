package visual;

import DataBase.EquipoDAO;
import logico.Equipo;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

public class ListaEquipoController {

    @FXML private TableView<Equipo> tablaEquipos;
    @FXML private TableColumn<Equipo, String> colId;
    @FXML private TableColumn<Equipo, String> colLaptop;
    @FXML private TableColumn<Equipo, String> colSerie;
    @FXML private TableColumn<Equipo, String> colColor;
    @FXML private TableColumn<Equipo, String> colEstado;
    @FXML private TableColumn<Equipo, String> colDisponibilidad;
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
        // Inicializar opciones permitidas por la BD (Constraints)
        comboEstado.getItems().setAll("nuevo", "open-box", "reparado");
        comboDisponibilidad.getItems().setAll("Disponible", "En Reparacion", "Desechado", "Perdido/Robado", "Vendido");

        // Configurar Columnas
        colId.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getIdEquipo()));
        colSerie.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getNumeroSerie()));
        colColor.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getColor()));
        colEstado.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getEstado()));
        colDisponibilidad.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getDisponibilidad()));
        colFecha.setCellValueFactory(cell -> new SimpleObjectProperty<>(cell.getValue().getFechaIngreso()));

        // Para la laptop, extraemos el ID si el objeto no es nulo
        colLaptop.setCellValueFactory(cell -> new SimpleStringProperty(
                cell.getValue().getLaptop() != null ? cell.getValue().getLaptop().getIdLaptop() : "N/A"
        ));

        // Listener de selección
        tablaEquipos.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            if (newSel != null) {
                equipoSeleccionado = newSel;

                txtSerie.setText(equipoSeleccionado.getNumeroSerie() != null ? equipoSeleccionado.getNumeroSerie() : "");
                txtColor.setText(equipoSeleccionado.getColor() != null ? equipoSeleccionado.getColor() : "");
                comboEstado.setValue(equipoSeleccionado.getEstado());
                comboDisponibilidad.setValue(equipoSeleccionado.getDisponibilidad());
                dateFecha.setValue(equipoSeleccionado.getFechaIngreso());
                txtDescuento.setText(String.valueOf(equipoSeleccionado.getDescuentoPorCondicion()));
            }
        });

        cargarTabla();
    }

    private void cargarTabla() {
        listaEquipos.clear();
        // Asume que tu EquipoDAO tiene este método, igual que LaptopDAO
        ArrayList<Equipo> equiposBD = EquipoDAO.getInstance().EncontrarTodos();
        if (equiposBD != null) {
            listaEquipos.addAll(equiposBD);
        }
        tablaEquipos.setItems(listaEquipos);
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
            equipoSeleccionado.setDescuentoPorCondicion(Float.parseFloat(txtDescuento.getText().trim()));

            // Ejecuta el UPDATE en la BD
            EquipoDAO.getInstance().actualizar(equipoSeleccionado);

            mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "Equipo actualizado correctamente.");
            tablaEquipos.refresh();
            handleLimpiar();
        } catch (NumberFormatException e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error", "El descuento debe ser un valor numérico.");
        } catch (Exception e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error de Base de Datos", "No se pudo actualizar: " + e.getMessage());
        }
    }

    @FXML
    private void handleEliminar() {
        if (equipoSeleccionado == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Atención", "Selecciona un equipo de la tabla.");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "¿Estás seguro de que deseas eliminar permanentemente el equipo " + equipoSeleccionado.getIdEquipo() + "?");
        Optional<ButtonType> res = confirm.showAndWait();

        if (res.isPresent() && res.get() == ButtonType.OK) {
            EquipoDAO.getInstance().borrar(equipoSeleccionado.getIdEquipo());
            listaEquipos.remove(equipoSeleccionado);
            mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "Equipo eliminado.");
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
        Alert alert = new Alert(tipo, mensaje);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.showAndWait();
    }
}