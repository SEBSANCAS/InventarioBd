package visual;

import DataBase.ClienteDAO;
import logico.Cliente;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

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
    @FXML private TextField txtTipoCliente;
    @FXML private TextField txtTipoIdentificacion;

    private final ObservableList<Cliente> listaClientes = FXCollections.observableArrayList();
    private Cliente clienteSeleccionado;

    @FXML
    public void initialize() {
        // 1. Mapeo de columnas con los getters de logico.Cliente
        colId.setCellValueFactory(new PropertyValueFactory<>("idCliente"));
        colIdentificacion.setCellValueFactory(new PropertyValueFactory<>("numeroIdentificacion"));
        colTipoIdentificacion.setCellValueFactory(new PropertyValueFactory<>("tipoIdentificacion"));
        colNombres.setCellValueFactory(new PropertyValueFactory<>("nombres"));
        colApellidos.setCellValueFactory(new PropertyValueFactory<>("apellidos"));
        colCorreo.setCellValueFactory(new PropertyValueFactory<>("correo"));
        colTipoCliente.setCellValueFactory(new PropertyValueFactory<>("tipoCLiente"));

        // 2. Cargar campos al seleccionar una fila de la tabla
        tablaClientes.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                clienteSeleccionado = newSelection;
                cargarDatosEnFormulario(clienteSeleccionado);
            }
        });

        // 3. Cargar lista de clientes desde la BD
        cargarTabla();
    }

    private void cargarTabla() {
        listaClientes.clear();
        // Llamada a tu método Singleton EncontrarTodos()
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
        txtTipoCliente.setText(cliente.getTipoCLiente());
        txtTipoIdentificacion.setText(cliente.getTipoIdentificacion());
    }

    // --- BOTÓN ACTUALIZAR ---
    @FXML
    private void handleActualizar() {
        if (clienteSeleccionado == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Atención", "Selecciona un cliente de la tabla para modificar.");
            return;
        }

        if (txtNombres.getText().trim().isEmpty() || txtIdentificacion.getText().trim().isEmpty()) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error de Validación", "El número de identificación y el nombre son obligatorios.");
            return;
        }

        // Modificamos el objeto con los datos introducidos
        clienteSeleccionado.setNumeroIdentificacion(txtIdentificacion.getText().trim());
        clienteSeleccionado.setNombres(txtNombres.getText().trim());
        clienteSeleccionado.setApellidos(txtApellidos.getText().trim());
        clienteSeleccionado.setCorreo(txtCorreo.getText().trim());
        clienteSeleccionado.setTipoCLiente(txtTipoCliente.getText().trim());
        clienteSeleccionado.setTipoIdentificacion(txtTipoIdentificacion.getText().trim());

        // Llamada a tu método actualizar
        ClienteDAO.getInstance().actualizar(clienteSeleccionado);

        mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "Cliente actualizado correctamente.");
        tablaClientes.refresh();
        handleLimpiar();
    }

    // --- BOTÓN ELIMINAR ---
    @FXML
    private void handleEliminar() {
        if (clienteSeleccionado == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Atención", "Selecciona un cliente de la tabla para eliminar.");
            return;
        }

        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Confirmar eliminación");
        confirmacion.setHeaderText(null);
        confirmacion.setContentText("¿Estás seguro de que deseas eliminar al cliente: " + clienteSeleccionado.getNombres() + "?");

        Optional<ButtonType> resultado = confirmacion.showAndWait();
        if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
            // Llamada a tu método borrar mandando el idCliente (String)
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
        txtTipoCliente.clear();
        txtTipoIdentificacion.clear();
    }

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensaje) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}