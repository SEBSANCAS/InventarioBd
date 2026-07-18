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
        final String sql = "INSERT INTO Suplidor (id_suplidor, rnc_identificador, razon_social, nombre_comercial, correo_electronico, pais, ciudad, calle, fecha_registro) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, suplidor.getIdSuplidor());
            preparedStatement.setString(2, suplidor.getNumeroIdentificacion());
            preparedStatement.setString(3, suplidor.getRazonComercial());
            preparedStatement.setString(4, suplidor.getNombreComercial());
            preparedStatement.setString(5, suplidor.getCorreo());
            preparedStatement.setString(6, suplidor.getPais());
            preparedStatement.setString(7, suplidor.getCiudad());
            preparedStatement.setString(8, suplidor.getCalle());
            preparedStatement.setObject(9, LocalDate.now());

            preparedStatement.executeUpdate();

            if (suplidor.getTelefonos() != null && !suplidor.getTelefonos().isEmpty()) {
                TelefonoDAO.getInstance().guardarTodosPorSuplidor(suplidor.getTelefonos(), suplidor.getIdSuplidor());
            }

        } catch (SQLException e) {
            System.out.println("No se pudo guardar el suplidor: " + e.getMessage());
        }
    }

    public void actualizar(Suplidor suplidor) {
        final String sql = "UPDATE Suplidor SET rnc_identificador=?, razon_social=?, nombre_comercial=?, correo_electronico=?, pais=?, ciudad=?, calle=? WHERE id_suplidor=?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, suplidor.getNumeroIdentificacion());
            preparedStatement.setString(2, suplidor.getRazonComercial());
            preparedStatement.setString(3, suplidor.getNombreComercial());
            preparedStatement.setString(4, suplidor.getCorreo());
            preparedStatement.setString(5, suplidor.getPais());
            preparedStatement.setString(6, suplidor.getCiudad());
            preparedStatement.setString(7, suplidor.getCalle());
            preparedStatement.setString(8, suplidor.getIdSuplidor());

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            System.out.println("No se pudo actualizar el suplidor: " + e.getMessage());
        }
    }

    public void borrar(String idSuplidor) {
        final String sql = "DELETE FROM Suplidor WHERE id_suplidor = ?";

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
                        resultSet.getString("rnc_identificador"),
                        resultSet.getString("correo_electronico"),
                        resultSet.getString("nombre_comercial"),
                        resultSet.getString("id_suplidor"),
                        resultSet.getString("razon_social"),
                        resultSet.getString("pais"),
                        resultSet.getString("ciudad"),
                        resultSet.getString("calle"),
                        resultSet.getObject("fecha_registro", LocalDate.class)
                );
                suplidor.setTelefonos(TelefonoDAO.getInstance().encontrarPorSuplidor(suplidor.getIdSuplidor()));
                suplidores.add(suplidor);
            }
        } catch (SQLException e) {
            System.out.println("No se pudo obtener la lista de suplidores: " + e.getMessage());
        }
        return suplidores;
    }

    public Suplidor encontrarPorId(String idSuplidor) {
        Suplidor suplidor = null;
        final String sql = "SELECT * FROM Suplidor WHERE id_suplidor = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, idSuplidor);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    suplidor = new Suplidor(
                            resultSet.getString("rnc_identificador"),
                            resultSet.getString("correo_electronico"),
                            resultSet.getString("nombre_comercial"),
                            resultSet.getString("id_suplidor"),
                            resultSet.getString("razon_social"),
                            resultSet.getString("pais"),
                            resultSet.getString("ciudad"),
                            resultSet.getString("calle"),
                            resultSet.getObject("fecha_registro", LocalDate.class)
                    );
                    suplidor.setTelefonos(TelefonoDAO.getInstance().encontrarPorSuplidor(idSuplidor));
                }
            }
        } catch (SQLException e) {
            System.out.println("No se pudo obtener el suplidor por ID: " + e.getMessage());
        }
        return suplidor;
    }
}