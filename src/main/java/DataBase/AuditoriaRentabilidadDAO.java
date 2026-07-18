package DataBase;

import java.sql.*;

public class AuditoriaRentabilidadDAO {

    public static final AuditoriaRentabilidadDAO INSTANCE = new AuditoriaRentabilidadDAO();

    private AuditoriaRentabilidadDAO() {}

    public static AuditoriaRentabilidadDAO getInstance() {
        return INSTANCE;
    }

    public void guardar(String idAuditoria, String idDetalle, String idEquipo, float precioVentaFinal, float costoCompra) {
        final String sql = "INSERT INTO Auditoria_Rentabilidad (id_auditoria, id_detalle, IdEquipo, precio_venta_final, costo_compra) VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, idAuditoria);
            preparedStatement.setString(2, idDetalle);
            preparedStatement.setString(3, idEquipo);
            preparedStatement.setFloat(4, precioVentaFinal);
            preparedStatement.setFloat(5, costoCompra);

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            System.out.println("No se pudo guardar la auditoria: " + e.getMessage());
        }
    }

    public void actualizar(String idAuditoria, String idDetalle, String idEquipo, float precioVentaFinal, float costoCompra) {
        final String sql = "UPDATE Auditoria_Rentabilidad SET id_detalle=?, IdEquipo=?, precio_venta_final=?, costo_compra=? WHERE id_auditoria=?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, idDetalle);
            preparedStatement.setString(2, idEquipo);
            preparedStatement.setFloat(3, precioVentaFinal);
            preparedStatement.setFloat(4, costoCompra);
            preparedStatement.setString(5, idAuditoria);

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            System.out.println("No se pudo actualizar la auditoria: " + e.getMessage());
        }
    }

    public void borrar(String idAuditoria) {
        final String sql = "DELETE FROM Auditoria_Rentabilidad WHERE id_auditoria = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, idAuditoria);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            System.out.println("No se pudo eliminar la auditoria: " + e.getMessage());
        }
    }
}