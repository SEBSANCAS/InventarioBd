package visual;

import DataBase.ClienteDAO;
import javafx.scene.control.cell.PropertyValueFactory;
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
    @FXML private TableColumn<Cliente, String> colGenero;
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
        // Mapeo explicito con Lambdas para asegurar visibilidad
        colId.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getIdCliente() != null ? cell.getValue().getIdCliente() : ""));
        colTipoCliente.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getTipoCLiente() != null ? cell.getValue().getTipoCLiente() : ""));
        colTipoIdentificacion.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getTipoIdentificacion() != null ? cell.getValue().getTipoIdentificacion() : ""));
        colIdentificacion.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getNumeroIdentificacion() != null ? cell.getValue().getNumeroIdentificacion() : ""));
        colNombres.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getNombres() != null ? cell.getValue().getNombres() : ""));
        colApellidos.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getApellidos() != null ? cell.getValue().getApellidos() : ""));
        colGenero.setCellValueFactory(new PropertyValueFactory<>("genero"));
        colCorreo.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getCorreo() != null ? cell.getValue().getCorreo() : ""));

        cbTipoCliente.setItems(FXCollections.observableArrayList("Persona", "Empresa"));
        cbTipoIdentificacion.setItems(FXCollections.observableArrayList("Cedula", "Rnc", "Pasaporte", "Tax_id_intl", "Otro"));

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
        txtCorreo.setText(cliente.getCorreo());

        cbTipoCliente.setValue(cliente.getTipoCLiente());
        cbTipoIdentificacion.setValue(cliente.getTipoIdentificacion());

        // Regla: Si es Empresa, apellidos va nulo/vacío y deshabilitado
        if ("Empresa".equalsIgnoreCase(cliente.getTipoCLiente())) {
            txtApellidos.clear();
            txtApellidos.setDisable(true);
        } else {
            txtApellidos.setDisable(false);
            txtApellidos.setText(cliente.getApellidos() != null ? cliente.getApellidos() : "");
        }
    }

    @FXML
    private void handleActualizar() {
        if (clienteSeleccionado == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Atención", "Selecciona un cliente de la tabla.");
            return;
        }

        String numId = txtIdentificacion.getText().trim();
        String nombres = txtNombres.getText().trim();
        String apellidos = txtApellidos.getText().trim();
        String tipoCliente = clienteSeleccionado.getTipoCLiente();
        String tipoId = clienteSeleccionado.getTipoIdentificacion();

        // Validaciones básicas
        if (numId.isEmpty() || nombres.isEmpty()) {
            mostrarAlerta(Alert.AlertType.ERROR, "Validación", "Por favor completa el número de identificación y nombres.");
            return;
        }

        // Validación de Apellidos según tipo_cliente
        if ("Persona".equalsIgnoreCase(tipoCliente) && apellidos.isEmpty()) {
            mostrarAlerta(Alert.AlertType.ERROR, "Validación SQL", "Para clientes de tipo 'Persona', los apellidos son obligatorios.");
            return;
        }

        // Validación de formato de documento segun la restriccion CHECK de MySQL
        if ("Cedula".equalsIgnoreCase(tipoId) && !numId.matches("^[0-9]{11}$")) {
            mostrarAlerta(Alert.AlertType.ERROR, "Validación SQL", "La Cédula debe tener exactamente 11 dígitos numéricos.");
            return;
        }
        if ("Rnc".equalsIgnoreCase(tipoId) && !numId.matches("^([0-9]{9}|[0-9]{11})$")) {
            mostrarAlerta(Alert.AlertType.ERROR, "Validación SQL", "El RNC debe tener 9 u 11 dígitos numéricos.");
            return;
        }

        // Se actualizan únicamente los campos permitidos (sin tocar los tipos congelados)
        clienteSeleccionado.setNumeroIdentificacion(numId);
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
            mostrarAlerta(Alert.AlertType.WARNING, "Atención", "Selecciona un cliente de la tabla.");
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
        txtApellidos.setDisable(false);
    }

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensaje) {
        Alert alert = new Alert(tipo, mensaje);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.showAndWait();
    }
}