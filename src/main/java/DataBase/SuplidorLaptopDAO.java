package DataBase;

import logico.Laptop;
import logico.DetalleLaptopSuplidor;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class SuplidorLaptopDAO {

    public static final SuplidorLaptopDAO INSTANCE = new SuplidorLaptopDAO();

    private SuplidorLaptopDAO() {}

    public static SuplidorLaptopDAO getInstance() {
        return INSTANCE;
    }

    public void guardar(String idSuplidor, String idLaptop, DetalleLaptopSuplidor detalle) {
        final String sql = "INSERT INTO Suplidor_Laptop (id_suplidor, id_laptop, precio_compra_acordado, dias_tiempo_entrega) VALUES (?, ?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, idSuplidor);
            preparedStatement.setString(2, idLaptop);
            preparedStatement.setFloat(3, detalle.getPrecioAcordado());
            preparedStatement.setInt(4, detalle.getDiasEntrega());

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            System.out.println("No se pudo guardar la relación suplidor-laptop: " + e.getMessage());
        }
    }

    public void guardarLaptopsPorSuplidor(String idSuplidor, HashMap<Laptop, DetalleLaptopSuplidor> laptopsSuplidor) {
        final String sql = "INSERT INTO Suplidor_Laptop (id_suplidor, id_laptop, precio_compra_acordado, dias_tiempo_entrega) VALUES (?, ?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            for (Map.Entry<Laptop, DetalleLaptopSuplidor> entry : laptopsSuplidor.entrySet()) {
                preparedStatement.setString(1, idSuplidor);
                preparedStatement.setString(2, entry.getKey().getIdLaptop());
                preparedStatement.setFloat(3, entry.getValue().getPrecioAcordado());
                preparedStatement.setInt(4, entry.getValue().getDiasEntrega());
                preparedStatement.addBatch();
            }
            preparedStatement.executeBatch();

        } catch (SQLException e) {
            System.out.println("No se pudieron guardar las laptops del suplidor: " + e.getMessage());
        }
    }

    public void actualizar(String idSuplidor, String idLaptop, DetalleLaptopSuplidor detalle) {
        final String sql = "UPDATE Suplidor_Laptop SET precio_compra_acordado = ?, dias_tiempo_entrega = ? WHERE id_suplidor = ? AND id_laptop = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setFloat(1, detalle.getPrecioAcordado());
            preparedStatement.setInt(2, detalle.getDiasEntrega());
            preparedStatement.setString(3, idSuplidor);
            preparedStatement.setString(4, idLaptop);

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            System.out.println("No se pudo actualizar la relación suplidor-laptop: " + e.getMessage());
        }
    }

    public void actualizarLaptopsPorSuplidor(String idSuplidor, HashMap<Laptop, DetalleLaptopSuplidor> laptopsSuplidor) {
        borrarPorSuplidor(idSuplidor);
        if (laptopsSuplidor != null && !laptopsSuplidor.isEmpty()) {
            guardarLaptopsPorSuplidor(idSuplidor, laptopsSuplidor);
        }
    }

    public void borrar(String idSuplidor, String idLaptop) {
        final String sql = "DELETE FROM Suplidor_Laptop WHERE id_suplidor = ? AND id_laptop = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, idSuplidor);
            preparedStatement.setString(2, idLaptop);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            System.out.println("No se pudo eliminar la relación suplidor-laptop: " + e.getMessage());
        }
    }

    public void borrarPorSuplidor(String idSuplidor) {
        final String sql = "DELETE FROM Suplidor_Laptop WHERE id_suplidor = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, idSuplidor);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            System.out.println("No se pudieron eliminar las laptops del suplidor: " + e.getMessage());
        }
    }

    public HashMap<Laptop, DetalleLaptopSuplidor> encontrarPorSuplidor(String idSuplidor) {
        HashMap<Laptop, DetalleLaptopSuplidor> laptopsSuplidor = new HashMap<>();
        final String sql = "SELECT sl.dias_tiempo_entrega, sl.precio_compra_acordado, l.* FROM Suplidor_Laptop sl JOIN Laptop l ON sl.id_laptop = l.id_laptop WHERE sl.id_suplidor = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, idSuplidor);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Laptop laptop = new Laptop(
                            resultSet.getString("id_laptop")
                    );

                    DetalleLaptopSuplidor detalle = new DetalleLaptopSuplidor(
                            resultSet.getInt("dias_tiempo_entrega"),
                            resultSet.getFloat("precio_compra_acordado")
                    );

                    laptopsSuplidor.put(laptop, detalle);
                }
            }
        } catch (SQLException e) {
            System.out.println("No se pudieron obtener las laptops del suplidor: " + e.getMessage());
        }
        return laptopsSuplidor;
    }
}