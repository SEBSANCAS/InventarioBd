package DataBase;

import logico.Cliente;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class ClienteDAO {

    public static final ClienteDAO INSTANCE = new ClienteDAO();

    private ClienteDAO() {}

    public static ClienteDAO getInstance() {
        return INSTANCE;
    }

    public void guardar(Cliente cliente) {
        final String sql = "INSERT INTO Cliente (id_cliente, numero_identificacion, nombres, apellidos, correo_electronico, fecha_registro) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, cliente.getIdCliente());
            preparedStatement.setString(2, cliente.getNumeroIdentificacion());
            preparedStatement.setString(3, cliente.getNombres());
            preparedStatement.setString(4, cliente.getApellidos());
            preparedStatement.setString(5, cliente.getCorreo());
            preparedStatement.setObject(6, LocalDate.now());

            preparedStatement.executeUpdate();

            if (cliente.getTelefonos() != null && !cliente.getTelefonos().isEmpty()) {
                TelefonoDAO.getInstance().guardarTodosPorCliente(cliente.getTelefonos(), cliente.getIdCliente());
            }

        } catch (SQLException e) {
            System.out.println("No se pudo guardar el cliente: " + e.getMessage());
        }
    }

    public void actualizar(Cliente cliente) {
        final String sql = "UPDATE Cliente SET numero_identificacion=?, nombres=?, apellidos=?, correo_electronico=? WHERE id_cliente=?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, cliente.getNumeroIdentificacion());
            preparedStatement.setString(2, cliente.getNombres());
            preparedStatement.setString(3, cliente.getApellidos());
            preparedStatement.setString(4, cliente.getCorreo());
            preparedStatement.setString(5, cliente.getIdCliente());

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            System.out.println("No se pudo actualizar el cliente: " + e.getMessage());
        }
    }

    public void borrar(String idCliente) {
        final String sql = "DELETE FROM Cliente WHERE id_cliente = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, idCliente);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            System.out.println("No se pudo eliminar el cliente: " + e.getMessage());
        }
    }

    public ArrayList<Cliente> EncontrarTodos() {
        ArrayList<Cliente> clientes = new ArrayList<>();
        final String sql = "SELECT * FROM Cliente";

        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            while (resultSet.next()) {
                Cliente cliente = new Cliente(
                        resultSet.getString("numero_identificacion"),
                        resultSet.getString("correo_electronico"),
                        resultSet.getString("id_cliente"),
                        resultSet.getString("nombres"),
                        resultSet.getString("apellidos")
                );
                cliente.setTelefonos(TelefonoDAO.getInstance().encontrarPorCliente(cliente.getIdCliente()));
                clientes.add(cliente);
            }
        } catch (SQLException e) {
            System.out.println("No se pudo obtener la lista de clientes: " + e.getMessage());
        }
        return clientes;
    }

    public Cliente encontrarPorId(String idCliente) {
        Cliente cliente = null;
        final String sql = "SELECT * FROM Cliente WHERE id_cliente = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, idCliente);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    cliente = new Cliente(
                            resultSet.getString("numero_identificacion"),
                            resultSet.getString("correo_electronico"),
                            resultSet.getString("id_cliente"),
                            resultSet.getString("nombres"),
                            resultSet.getString("apellidos")
                    );
                    cliente.setTelefonos(TelefonoDAO.getInstance().encontrarPorCliente(idCliente));
                }
            }
        } catch (SQLException e) {
            System.out.println("No se pudo obtener el cliente por ID: " + e.getMessage());
        }
        return cliente;
    }
}