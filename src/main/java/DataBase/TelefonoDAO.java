package DataBase;

import logico.Telefono;
import java.sql.*;
import java.util.ArrayList;

public class TelefonoDAO {

    public static final TelefonoDAO INSTANCE = new TelefonoDAO();

    private TelefonoDAO() {}

    public static TelefonoDAO getInstance() {
        return INSTANCE;
    }

    public void guardarTelefonoCliente(Telefono telefono, String idCliente) {
        final String sql = "INSERT INTO Telefono_Cliente (id_telefono, id_cliente, numero_telefono, es_principal) VALUES (?, ?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, telefono.getIdTelefono());
            preparedStatement.setString(2, idCliente);
            preparedStatement.setString(3, telefono.getNumeroTelefono());
            preparedStatement.setBoolean(4, telefono.isEsPrincipal());

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            System.out.println("No se pudo guardar el telefono del cliente: " + e.getMessage());
        }
    }

    public void actualizarTelefonoCliente(Telefono telefono) {
        final String sql = "UPDATE Telefono_Cliente SET numero_telefono=?, es_principal=? WHERE id_telefono=?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, telefono.getNumeroTelefono());
            preparedStatement.setBoolean(2, telefono.isEsPrincipal());
            preparedStatement.setString(3, telefono.getIdTelefono());

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            System.out.println("No se pudo actualizar el telefono del cliente: " + e.getMessage());
        }
    }

    public void borrarTelefonoCliente(String idTelefono) {
        final String sql = "DELETE FROM Telefono_Cliente WHERE id_telefono = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, idTelefono);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            System.out.println("No se pudo eliminar el telefono del cliente: " + e.getMessage());
        }
    }

    public void borrarTodosPorCliente(String idCliente) {
        final String sql = "DELETE FROM Telefono_Cliente WHERE id_cliente = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, idCliente);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            System.out.println("No se pudo eliminar los telefonos del cliente: " + e.getMessage());
        }
    }

    public ArrayList<Telefono> encontrarPorCliente(String idCliente) {
        ArrayList<Telefono> telefonos = new ArrayList<>();
        final String sql = "SELECT * FROM Telefono_Cliente WHERE id_cliente = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, idCliente);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Telefono telefono = new Telefono(
                            resultSet.getString("id_telefono"),
                            resultSet.getString("numero_telefono"),
                            resultSet.getBoolean("es_principal")
                    );
                    telefonos.add(telefono);
                }
            }
        } catch (SQLException e) {
            System.out.println("No se pudo obtener los telefonos del cliente: " + e.getMessage());
        }
        return telefonos;
    }

    public void guardarTodosPorCliente(ArrayList<Telefono> telefonos, String idCliente) {
        borrarTodosPorCliente(idCliente);
        for (Telefono telefono : telefonos) {
            guardarTelefonoCliente(telefono, idCliente);
        }
    }

    public void guardarTelefonoSuplidor(Telefono telefono, String idSuplidor) {
        final String sql = "INSERT INTO Telefono_Suplidor (id_telefono, id_suplidor, numero_telefono, es_principal) VALUES (?, ?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, telefono.getIdTelefono());
            preparedStatement.setString(2, idSuplidor);
            preparedStatement.setString(3, telefono.getNumeroTelefono());
            preparedStatement.setBoolean(4, telefono.isEsPrincipal());

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            System.out.println("No se pudo guardar el telefono del suplidor: " + e.getMessage());
        }
    }

    public void actualizarTelefonoSuplidor(Telefono telefono) {
        final String sql = "UPDATE Telefono_Suplidor SET numero_telefono=?, es_principal=? WHERE id_telefono=?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, telefono.getNumeroTelefono());
            preparedStatement.setBoolean(2, telefono.isEsPrincipal());
            preparedStatement.setString(3, telefono.getIdTelefono());

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            System.out.println("No se pudo actualizar el telefono del suplidor: " + e.getMessage());
        }
    }

    public void borrarTelefonoSuplidor(String idTelefono) {
        final String sql = "DELETE FROM Telefono_Suplidor WHERE id_telefono = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, idTelefono);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            System.out.println("No se pudo eliminar el telefono del suplidor: " + e.getMessage());
        }
    }

    public void borrarTodosPorSuplidor(String idSuplidor) {
        final String sql = "DELETE FROM Telefono_Suplidor WHERE id_suplidor = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, idSuplidor);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            System.out.println("No se pudo eliminar los telefonos del suplidor: " + e.getMessage());
        }
    }

    public ArrayList<Telefono> encontrarPorSuplidor(String idSuplidor) {
        ArrayList<Telefono> telefonos = new ArrayList<>();
        final String sql = "SELECT * FROM Telefono_Suplidor WHERE id_suplidor = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, idSuplidor);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Telefono telefono = new Telefono(
                            resultSet.getString("id_telefono"),
                            resultSet.getString("numero_telefono"),
                            resultSet.getBoolean("es_principal")
                    );
                    telefonos.add(telefono);
                }
            }
        } catch (SQLException e) {
            System.out.println("No se pudo obtener los telefonos del suplidor: " + e.getMessage());
        }
        return telefonos;
    }

    public void guardarTodosPorSuplidor(ArrayList<Telefono> telefonos, String idSuplidor) {
        borrarTodosPorSuplidor(idSuplidor);
        for (Telefono telefono : telefonos) {
            guardarTelefonoSuplidor(telefono, idSuplidor);
        }
    }
}