package DataBase;

import logico.Cliente;
import java.sql.*;
import java.util.ArrayList;

public class ClienteDAO {

    public static final ClienteDAO INSTANCE = new ClienteDAO();

    private ClienteDAO() {}

    public static ClienteDAO getInstance() {
        return INSTANCE;
    }

    public void guardar(Cliente cliente) {
        final String sql = "INSERT INTO Cliente (IdCliente, nombres, apellidos, tipo_cliente, numero_identificacion, correo, telefono) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, cliente.getIdCliente());
            preparedStatement.setString(2, cliente.getNombres());
            preparedStatement.setString(3, cliente.getApellidos());
            preparedStatement.setString(4, cliente.getTipoCLiente());
            preparedStatement.setString(5, cliente.getNumeroIdentificacion()); // Heredado de Persona
            preparedStatement.setString(6, cliente.getCorreo());              // Heredado de Persona
            preparedStatement.setString(7, cliente.getTelefono());            // Heredado de Persona

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            System.out.println("No se pudo guardar el cliente: " + e.getMessage());
        }
    }

    public void actualizar(Cliente cliente) {
        final String sql = "UPDATE Cliente SET nombres=?, apellidos=?, tipo_cliente=?, numero_identificacion=?, correo=?, telefono=? WHERE IdCliente=?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, cliente.getNombres());
            preparedStatement.setString(2, cliente.getApellidos());
            preparedStatement.setString(3, cliente.getTipoCLiente());
            preparedStatement.setString(4, cliente.getNumeroIdentificacion());
            preparedStatement.setString(5, cliente.getCorreo());
            preparedStatement.setString(6, cliente.getTelefono());
            preparedStatement.setString(7, cliente.getIdCliente());

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            System.out.println("No se pudo actualizar el cliente: " + e.getMessage());
        }
    }

    public void borrar(String idCliente) {
        final String sql = "DELETE FROM Cliente WHERE IdCliente = ?";

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
                        resultSet.getString("correo"),
                        resultSet.getString("telefono"),
                        resultSet.getString("IdCliente"),
                        resultSet.getString("nombres"),
                        resultSet.getString("apellidos"),
                        resultSet.getString("tipo_cliente")
                );
                clientes.add(cliente);
            }
        } catch (SQLException e) {
            System.out.println("No se pudo obtener la lista de clientes: " + e.getMessage());
        }
        return clientes;
    }

    public Cliente encontrarPorId(String idCliente) {
        Cliente cliente = null;
        final String sql = "SELECT * FROM Cliente WHERE IdCliente = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, idCliente);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    cliente = new Cliente(
                            resultSet.getString("numero_identificacion"),
                            resultSet.getString("correo"),
                            resultSet.getString("telefono"),
                            resultSet.getString("IdCliente"),
                            resultSet.getString("nombres"),
                            resultSet.getString("apellidos"),
                            resultSet.getString("tipo_cliente")
                    );
                }
            }
        } catch (SQLException e) {
            System.out.println("No se pudo obtener el cliente por ID: " + e.getMessage());
        }
        return cliente;
    }
}