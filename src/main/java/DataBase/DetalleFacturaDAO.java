package DataBase;

import logico.DetalleFactura;
import logico.Equipo;
import java.sql.*;
import java.util.ArrayList;

public class DetalleFacturaDAO {

    public static final DetalleFacturaDAO INSTANCE = new DetalleFacturaDAO();

    private DetalleFacturaDAO() {}

    public static DetalleFacturaDAO getInstance() {
        return INSTANCE;
    }

    public void guardar(DetalleFactura detalle, String idFactura) {
        final String sql = "INSERT INTO Detalle_Factura (id_detalle, id_factura, IdEquipo, precio_unitario_venta, monto_descuento, subtotal_linea) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            String idEquipo = null;
            if (detalle.getEquipoVendido() != null) {
                idEquipo = detalle.getEquipoVendido().getIdEquipo();
            }

            preparedStatement.setString(1, detalle.getIdDetalleFactura());
            preparedStatement.setString(2, idFactura);
            preparedStatement.setString(3, idEquipo);
            preparedStatement.setFloat(4, detalle.getPreciounitario());
            preparedStatement.setFloat(5, detalle.getDescuento());
            preparedStatement.setFloat(6, detalle.getSubtotalLinea());

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            System.out.println("No se pudo guardar el detalle de factura: " + e.getMessage());
        }
    }

    public void actualizar(DetalleFactura detalle, String idFactura) {
        final String sql = "UPDATE Detalle_Factura SET id_factura=?, IdEquipo=?, precio_unitario_venta=?, monto_descuento=?, subtotal_linea=? WHERE id_detalle=?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            String idEquipo = null;
            if (detalle.getEquipoVendido() != null) {
                idEquipo = detalle.getEquipoVendido().getIdEquipo();
            }

            preparedStatement.setString(1, idFactura);
            preparedStatement.setString(2, idEquipo);
            preparedStatement.setFloat(3, detalle.getPreciounitario());
            preparedStatement.setFloat(4, detalle.getDescuento());
            preparedStatement.setFloat(5, detalle.getSubtotalLinea());
            preparedStatement.setString(6, detalle.getIdDetalleFactura());

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            System.out.println("No se pudo actualizar el detalle de factura: " + e.getMessage());
        }
    }

    public void borrar(String idDetalleFactura) {
        final String sql = "DELETE FROM Detalle_Factura WHERE id_detalle = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, idDetalleFactura);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            System.out.println("No se pudo eliminar el detalle de factura: " + e.getMessage());
        }
    }

    public ArrayList<DetalleFactura> EncontrarTodos() {
        ArrayList<DetalleFactura> detalles = new ArrayList<>();
        final String sql = "SELECT * FROM Detalle_Factura";

        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            while (resultSet.next()) {
                Equipo equipoCascaron = null;
                String idEquipo = resultSet.getString("IdEquipo");
                if (idEquipo != null) {
                    equipoCascaron = new Equipo(idEquipo);
                }

                DetalleFactura det = new DetalleFactura(
                        resultSet.getString("id_detalle"),
                        resultSet.getString("id_factura"),
                        resultSet.getFloat("precio_unitario_venta"),
                        resultSet.getFloat("monto_descuento"),
                        resultSet.getFloat("subtotal_linea"),
                        0,
                        equipoCascaron
                );
                detalles.add(det);
            }
        } catch (SQLException e) {
            System.out.println("No se pudo obtener la lista de detalles de factura: " + e.getMessage());
        }
        return detalles;
    }

    public DetalleFactura encontrarPorId(String idDetalleFactura) {
        DetalleFactura det = null;
        final String sql = "SELECT * FROM Detalle_Factura WHERE id_detalle = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, idDetalleFactura);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    Equipo equipoCascaron = null;
                    String idEquipo = resultSet.getString("IdEquipo");
                    if (idEquipo != null) {
                        equipoCascaron = new Equipo(idEquipo);
                    }

                    det = new DetalleFactura(
                            resultSet.getString("id_detalle"),
                            resultSet.getString("id_factura"),
                            resultSet.getFloat("precio_unitario_venta"),
                            resultSet.getFloat("monto_descuento"),
                            resultSet.getFloat("subtotal_linea"),
                            0,
                            equipoCascaron
                    );
                }
            }
        } catch (SQLException e) {
            System.out.println("No se pudo obtener el detalle de factura por ID: " + e.getMessage());
        }
        return det;
    }

    public ArrayList<DetalleFactura> encontrarPorFactura(String idFactura) {

        ArrayList<DetalleFactura> detalles = new ArrayList<>();
        final String sql = "SELECT * FROM Detalle_Factura WHERE id_factura = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, idFactura);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {

                while (resultSet.next()) {

                    Equipo equipo = EquipoDAO.getInstance()
                            .encontrarPorId(resultSet.getString("IdEquipo"));

                    DetalleFactura detalle = new DetalleFactura(
                            resultSet.getString("id_detalle"),
                            resultSet.getString("id_factura"),
                            resultSet.getFloat("precio_unitario_venta"),
                            resultSet.getFloat("monto_descuento"),
                            resultSet.getFloat("subtotal_linea"),
                            0,
                            equipo
                    );

                    detalles.add(detalle);
                }
            }

        } catch (SQLException e) {
            System.out.println("No se pudieron obtener los detalles de la factura: " + e.getMessage());
        }

        return detalles;
    }
}