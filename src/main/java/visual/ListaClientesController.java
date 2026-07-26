package visual;

import DataBase.ClienteDAO;
import logico.Cliente;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.ArrayList;
import java.util.Optional;

public class ListaClientesController {

    @FXML private TableView<Cliente> tablaClientes;
    @FXML private TableColumn<Cliente, String> colId;
    @FXML private TableColumn<Cliente, String> colIdentificacion;
    @FXML private TableColumn<Cliente, String> colTipoIdentificacion;
    @FXML private TableColumn<Cliente, String> colNombres;
    @FXML private TableColumn<Cliente, String> colApellidos;
    @FXML private TableColumn<Cliente, String> colCorreo;
    @FXML private TableColumn<Cliente, String> colTipoCliente;

    @FXML private TextField txtIdentificacion;
    @FXML private TextField txtNombres;
    @FXML private TextField txtApellidos;
    @FXML private TextField txtCorreo;
    @FXML private ComboBox<String> cbTipoCliente;
    @FXML private ComboBox<String> cbTipoIdentificacion;

    private final ObservableList<Cliente> listaClientes = FXCollections.observableArrayList();
    private Cliente clienteSeleccionado;

    @FXML
    public void initialize() {
        colId.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getIdCliente() != null ? cell.getValue().getIdCliente() : ""));
        colTipoCliente.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getTipoCLiente() != null ? cell.getValue().getTipoCLiente() : ""));
        colTipoIdentificacion.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getTipoIdentificacion() != null ? cell.getValue().getTipoIdentificacion() : ""));
        colIdentificacion.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getNumeroIdentificacion() != null ? cell.getValue().getNumeroIdentificacion() : ""));
        colNombres.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getNombres() != null ? cell.getValue().getNombres() : ""));
        colApellidos.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getApellidos() != null ? cell.getValue().getApellidos() : ""));
        colCorreo.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getCorreo() != null ? cell.getValue().getCorreo() : ""));

        cbTipoCliente.setItems(FXCollections.observableArrayList("Persona", "Empresa"));
        cbTipoIdentificacion.setItems(FXCollections.observableArrayList("Cedula", "Rnc", "Pasaporte", "Tax_id_intl", "Otro"));

        cbTipoCliente.valueProperty().addListener((obs, oldVal, newVal) -> {
            if ("Empresa".equalsIgnoreCase(newVal)) {
                txtApellidos.clear();
                txtApellidos.setDisable(true);
            } else {
                txtApellidos.setDisable(false);
            }
        });

        tablaClientes.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            if (newSel != null) {
                clienteSeleccionado = newSel;
                cargarDatosEnFormulario(clienteSeleccionado);
            }
        });

        cargarTabla();
    }

    private void cargarTabla() {
        listaClientes.clear();
        ArrayList<Cliente> clientesBD = ClienteDAO.getInstance().EncontrarTodos();
        if (clientesBD != null) {
            listaClientes.addAll(clientesBD);
        }
        tablaClientes.setItems(listaClientes);
    }

    private void cargarDatosEnFormulario(Cliente cliente) {
        txtIdentificacion.setText(cliente.getNumeroIdentificacion());
        txtNombres.setText(cliente.getNombres());
        txtApellidos.setText(cliente.getApellidos() != null ? cliente.getApellidos() : "");
        txtCorreo.setText(cliente.getCorreo());
        cbTipoCliente.setValue(cliente.getTipoCLiente());
        cbTipoIdentificacion.setValue(cliente.getTipoIdentificacion());
    }

    @FXML
    private void handleActualizar() {
        if (clienteSeleccionado == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Atención", "Selecciona un cliente de la tabla.");
            return;
        }

        String tipoCliente = cbTipoCliente.getValue();
        String tipoId = cbTipoIdentificacion.getValue();
        String numId = txtIdentificacion.getText().trim();
        String nombres = txtNombres.getText().trim();
        String apellidos = txtApellidos.getText().trim();

        if (tipoCliente == null || tipoId == null || numId.isEmpty() || nombres.isEmpty()) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error", "Completa todos los campos obligatorios.");
            return;
        }

        clienteSeleccionado.setNumeroIdentificacion(numId);
        clienteSeleccionado.setTipoIdentificacion(tipoId);
        clienteSeleccionado.setTipoCLiente(tipoCliente);
        clienteSeleccionado.setNombres(nombres);
        clienteSeleccionado.setApellidos("Empresa".equalsIgnoreCase(tipoCliente) ? null : apellidos);
        clienteSeleccionado.setCorreo(txtCorreo.getText().trim());

        ClienteDAO.getInstance().actualizar(clienteSeleccionado);

        mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "Cliente actualizado correctamente.");
        tablaClientes.refresh();
        handleLimpiar();
    }

    @FXML
    private void handleEliminar() {
        if (clienteSeleccionado == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Atención", "Selecciona un cliente.");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "¿Deseas eliminar al cliente " + clienteSeleccionado.getNombres() + "?");
        Optional<ButtonType> res = confirm.showAndWait();

        if (res.isPresent() && res.get() == ButtonType.OK) {
            ClienteDAO.getInstance().borrar(clienteSeleccionado.getIdCliente());
            listaClientes.remove(clienteSeleccionado);
            mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "Cliente eliminado correctamente.");
            handleLimpiar();
        }
    }

    @FXML
    private void handleLimpiar() {
        tablaClientes.getSelectionModel().clearSelection();
        clienteSeleccionado = null;
        txtIdentificacion.clear();
        txtNombres.clear();
        txtApellidos.clear();
        txtCorreo.clear();
        cbTipoCliente.setValue(null);
        cbTipoIdentificacion.setValue(null);
    }

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensaje) {
        Alert alert = new Alert(tipo, mensaje);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.showAndWait();
    }
}