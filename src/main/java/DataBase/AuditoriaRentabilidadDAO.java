package DataBase;

import logico.Auditoria;

import java.sql.*;
import java.util.ArrayList;

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
    public ArrayList<Auditoria> busca(String idLaptop) {

        ArrayList<Auditoria> lista = new ArrayList<>();

        final String sql =
                "SELECT ar.* " + "FROM Auditoria_Rentabilidad ar " + "INNER JOIN Equipo e ON ar.IdEquipo = e.IdEquipo " + "WHERE e.id_laptop = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, idLaptop);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {

                while (resultSet.next()) {

                    Auditoria auditoria = new Auditoria();

                    auditoria.setIdAuditoria(resultSet.getString("id_auditoria"));
                    auditoria.setPrecioVentaFinal(resultSet.getFloat("precio_venta_final"));
                    auditoria.setCostoCompra(resultSet.getFloat("costo_compra"));

                    lista.add(auditoria);
                }

            }

        } catch (SQLException e) {
            System.out.println("No se pudieron obtener las auditorías: " + e.getMessage());
        }

        return lista;
    }
}