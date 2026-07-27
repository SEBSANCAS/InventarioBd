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
        // Se agregó 'genero' (9 parámetros en total)
        final String sql = "INSERT INTO Cliente (id_cliente, numero_identificacion, nombres, apellidos, genero, correo_electronico, fecha_registro, tipo_cliente, tipo_identificacion) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, cliente.getIdCliente());
            preparedStatement.setString(2, cliente.getNumeroIdentificacion());
            preparedStatement.setString(3, cliente.getNombres());

            // Manejo del apellido (NULL para Empresa)
            if (cliente.getApellidos() != null && !cliente.getApellidos().trim().isEmpty()) {
                preparedStatement.setString(4, cliente.getApellidos());
            } else {
                preparedStatement.setNull(4, Types.VARCHAR);
            }

            // Manejo del genero (NULL para Empresa)
            if (cliente.getGenero() != null && !cliente.getGenero().trim().isEmpty()) {
                preparedStatement.setString(5, cliente.getGenero());
            } else {
                preparedStatement.setNull(5, Types.VARCHAR);
            }

            preparedStatement.setString(6, cliente.getCorreo());
            preparedStatement.setObject(7, LocalDate.now());
            preparedStatement.setString(8, cliente.getTipoCLiente());
            preparedStatement.setString(9, cliente.getTipoIdentificacion());

            preparedStatement.executeUpdate();

            if (cliente.getTelefonos() != null && !cliente.getTelefonos().isEmpty()) {
                TelefonoDAO.getInstance().guardarTodosPorCliente(cliente.getTelefonos(), cliente.getIdCliente());
            }

        } catch (SQLException e) {
            System.out.println("No se pudo guardar el cliente: " + e.getMessage());
        }
    }

    public void actualizar(Cliente cliente) {
        // Se agregó 'genero' (8 parámetros en total)
        final String sql = "UPDATE Cliente SET numero_identificacion=?, nombres=?, apellidos=?, genero=?, correo_electronico=?, tipo_cliente=?, tipo_identificacion=? WHERE id_cliente=?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, cliente.getNumeroIdentificacion());
            preparedStatement.setString(2, cliente.getNombres());

            // Manejo del apellido
            if (cliente.getApellidos() != null && !cliente.getApellidos().trim().isEmpty()) {
                preparedStatement.setString(3, cliente.getApellidos());
            } else {
                preparedStatement.setNull(3, Types.VARCHAR);
            }

            // Manejo del genero
            if (cliente.getGenero() != null && !cliente.getGenero().trim().isEmpty()) {
                preparedStatement.setString(4, cliente.getGenero());
            } else {
                preparedStatement.setNull(4, Types.VARCHAR);
            }

            preparedStatement.setString(5, cliente.getCorreo());
            preparedStatement.setString(6, cliente.getTipoCLiente());
            preparedStatement.setString(7, cliente.getTipoIdentificacion());
            preparedStatement.setString(8, cliente.getIdCliente());

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
                // Instanciamos usando el constructor de 8 parámetros que incluye 'genero'
                Cliente cliente = new Cliente(
                        resultSet.getString("numero_identificacion"),
                        resultSet.getString("correo_electronico"),
                        resultSet.getString("id_cliente"),
                        resultSet.getString("nombres"),
                        resultSet.getString("apellidos"),
                        resultSet.getString("genero"), // Se lee el género de la BD
                        resultSet.getString("tipo_cliente"),
                        resultSet.getString("tipo_identificacion")
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
                    // Instanciamos usando el constructor de 8 parámetros que incluye 'genero'
                    cliente = new Cliente(
                            resultSet.getString("numero_identificacion"),
                            resultSet.getString("correo_electronico"),
                            resultSet.getString("id_cliente"),
                            resultSet.getString("nombres"),
                            resultSet.getString("apellidos"),
                            resultSet.getString("genero"), // Se lee el género de la BD
                            resultSet.getString("tipo_cliente"),
                            resultSet.getString("tipo_identificacion")
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