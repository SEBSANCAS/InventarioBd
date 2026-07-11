package DataBase;

import logico.Suplidor;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class SuplidorDAO {

    public static final SuplidorDAO INSTANCE = new SuplidorDAO();

    private SuplidorDAO() {}

    public static SuplidorDAO getInstance() {
        return INSTANCE;
    }

    public void guardar(Suplidor suplidor) {
        final String sql = "INSERT INTO Suplidor (IdSuplidor, razon_comercial, nombre_comercial, pais, ciudad, calle, fecha_registro, numero_identificacion, correo, telefono) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, suplidor.getIdSuplidor());
            preparedStatement.setString(2, suplidor.getRazonComercial());
            preparedStatement.setString(3, suplidor.getNombreComercial());
            preparedStatement.setString(4, suplidor.getPais());
            preparedStatement.setString(5, suplidor.getCiudad());
            preparedStatement.setString(6, suplidor.getCalle());
            preparedStatement.setObject(7, suplidor.getFechaRegistro());
            preparedStatement.setString(8, suplidor.getNumeroIdentificacion());
            preparedStatement.setString(9, suplidor.getCorreo());
            preparedStatement.setString(10, suplidor.getTelefono());

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            System.out.println("No se pudo guardar el suplidor: " + e.getMessage());
        }
    }

    public void actualizar(Suplidor suplidor) {
        final String sql = "UPDATE Suplidor SET razon_comercial=?, nombre_comercial=?, pais=?, ciudad=?, calle=?, fecha_registro=?, numero_identificacion=?, correo=?, telefono=? WHERE IdSuplidor=?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, suplidor.getRazonComercial());
            preparedStatement.setString(2, suplidor.getNombreComercial());
            preparedStatement.setString(3, suplidor.getPais());
            preparedStatement.setString(4, suplidor.getCiudad());
            preparedStatement.setString(5, suplidor.getCalle());
            preparedStatement.setObject(6, suplidor.getFechaRegistro());
            preparedStatement.setString(7, suplidor.getNumeroIdentificacion());
            preparedStatement.setString(8, suplidor.getCorreo());
            preparedStatement.setString(9, suplidor.getTelefono());
            preparedStatement.setString(10, suplidor.getIdSuplidor());

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            System.out.println("No se pudo actualizar el suplidor: " + e.getMessage());
        }
    }

    public void borrar(String idSuplidor) {
        final String sql = "DELETE FROM Suplidor WHERE IdSuplidor = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, idSuplidor);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            System.out.println("No se pudo eliminar el suplidor: " + e.getMessage());
        }
    }

    public ArrayList<Suplidor> EncontrarTodos() {
        ArrayList<Suplidor> suplidores = new ArrayList<>();
        final String sql = "SELECT * FROM Suplidor";

        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            while (resultSet.next()) {
                Suplidor suplidor = new Suplidor(
                        resultSet.getString("numero_identificacion"),
                        resultSet.getString("correo"),
                        resultSet.getString("telefono"),
                        resultSet.getString("nombre_comercial"),
                        resultSet.getString("IdSuplidor"),
                        resultSet.getString("razon_comercial"),
                        resultSet.getString("pais"),
                        resultSet.getString("ciudad"),
                        resultSet.getString("calle"),
                        resultSet.getObject("fecha_registro", LocalDate.class)
                );
                suplidores.add(suplidor);
            }
        } catch (SQLException e) {
            System.out.println("No se pudo obtener la lista de suplidores: " + e.getMessage());
        }
        return suplidores;
    }

    public Suplidor encontrarPorId(String idSuplidor) {
        Suplidor suplidor = null;
        final String sql = "SELECT * FROM Suplidor WHERE IdSuplidor = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, idSuplidor);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    suplidor = new Suplidor(
                            resultSet.getString("numero_identificacion"),
                            resultSet.getString("correo"),
                            resultSet.getString("telefono"),
                            resultSet.getString("nombre_comercial"),
                            resultSet.getString("IdSuplidor"),
                            resultSet.getString("razon_comercial"),
                            resultSet.getString("pais"),
                            resultSet.getString("ciudad"),
                            resultSet.getString("calle"),
                            resultSet.getObject("fecha_registro", LocalDate.class)
                    );
                }
            }
        } catch (SQLException e) {
            System.out.println("No se pudo obtener el suplidor por ID: " + e.getMessage());
        }
        return suplidor;
    }
}
