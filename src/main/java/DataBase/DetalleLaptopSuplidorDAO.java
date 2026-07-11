package DataBase;

import logico.DetalleLaptopSuplidor;
import java.sql.*;
import java.util.ArrayList;

public class DetalleLaptopSuplidorDAO {

    public static final DetalleLaptopSuplidorDAO INSTANCE = new DetalleLaptopSuplidorDAO();

    private DetalleLaptopSuplidorDAO() {}

    public static DetalleLaptopSuplidorDAO getInstance() {
        return INSTANCE;
    }

    public void guardar(DetalleLaptopSuplidor detalle, String idLaptop, String idSuplidor) {
        final String sql = "INSERT INTO DetalleLaptopSuplidor (id_laptop, id_suplidor, dias_entrega, precio_acordado) VALUES (?, ?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, idLaptop);
            preparedStatement.setString(2, idSuplidor);
            preparedStatement.setInt(3, detalle.getDiasEntrega());
            preparedStatement.setFloat(4, detalle.getPrecioAcordado());

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            System.out.println("No se pudo guardar el detalle laptop suplidor: " + e.getMessage());
        }
    }

    public void actualizar(DetalleLaptopSuplidor detalle, String idLaptop, String idSuplidor) {
        final String sql = "UPDATE DetalleLaptopSuplidor SET dias_entrega=?, precio_acordado=? WHERE id_laptop=? AND id_suplidor=?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, detalle.getDiasEntrega());
            preparedStatement.setFloat(2, detalle.getPrecioAcordado());
            preparedStatement.setString(3, idLaptop);
            preparedStatement.setString(4, idSuplidor);

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            System.out.println("No se pudo actualizar el detalle laptop suplidor: " + e.getMessage());
        }
    }

    public void borrar(String idLaptop, String idSuplidor) {
        final String sql = "DELETE FROM DetalleLaptopSuplidor WHERE id_laptop = ? AND id_suplidor = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, idLaptop);
            preparedStatement.setString(2, idSuplidor);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            System.out.println("No se pudo eliminar el detalle laptop suplidor: " + e.getMessage());
        }
    }

    public ArrayList<DetalleLaptopSuplidor> EncontrarTodos() {
        ArrayList<DetalleLaptopSuplidor> detalles = new ArrayList<>();
        final String sql = "SELECT * FROM DetalleLaptopSuplidor";

        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            while (resultSet.next()) {
                DetalleLaptopSuplidor det = new DetalleLaptopSuplidor(
                        resultSet.getInt("dias_entrega"),
                        resultSet.getFloat("precio_acordado")
                );
                detalles.add(det);
            }
        } catch (SQLException e) {
            System.out.println("No se pudo obtener la lista de detalles laptop suplidor: " + e.getMessage());
        }
        return detalles;
    }

    public DetalleLaptopSuplidor encontrarPorIdComposite(String idLaptop, String idSuplidor) {
        DetalleLaptopSuplidor det = null;
        final String sql = "SELECT * FROM DetalleLaptopSuplidor WHERE id_laptop = ? AND id_suplidor = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, idLaptop);
            preparedStatement.setString(2, idSuplidor);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    det = new DetalleLaptopSuplidor(
                            resultSet.getInt("dias_entrega"),
                            resultSet.getFloat("precio_acordado")
                    );
                }
            }
        } catch (SQLException e) {
            System.out.println("No se pudo obtener el detalle por ID compuesto: " + e.getMessage());
        }
        return det;
    }
}