package DataBase;

import logico.DetalleFactura;
import logico.Equipo;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class DetalleFacturaDAO {

    public static final DetalleFacturaDAO INSTANCE = new DetalleFacturaDAO();

    private DetalleFacturaDAO() {}

    public static DetalleFacturaDAO getInstance() {
        return INSTANCE;
    }

    public void guardar(DetalleFactura detalle, String idFactura) {
        final String sql = "INSERT INTO DetalleFactura (IdDetalleFactura, id_factura, precio_unitario, descuento, subtotal_linea, meses_garantia, id_equipo) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            String idEquipo = null;
            if (detalle.getEquipoVendido() != null) {
                idEquipo = detalle.getEquipoVendido().getIdEquipo();
            }

            preparedStatement.setString(1, detalle.getIdDetalleFactura());
            preparedStatement.setString(2, idFactura);
            preparedStatement.setFloat(3, detalle.getPreciounitario());
            preparedStatement.setFloat(4, detalle.getDescuento());
            preparedStatement.setFloat(5, detalle.getSubtotalLinea());
            preparedStatement.setInt(6, detalle.getMesesGarantiaAplicados());
            preparedStatement.setString(7, idEquipo);

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            System.out.println("No se pudo guardar el detalle de factura: " + e.getMessage());
        }
    }

    public void actualizar(DetalleFactura detalle, String idFactura) {
        final String sql = "UPDATE DetalleFactura SET id_factura=?, precio_unitario=?, descuento=?, subtotal_linea=?, meses_garantia=?, id_equipo=? WHERE IdDetalleFactura=?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            String idEquipo = null;
            if (detalle.getEquipoVendido() != null) {
                idEquipo = detalle.getEquipoVendido().getIdEquipo();
            }

            preparedStatement.setString(1, idFactura);
            preparedStatement.setFloat(2, detalle.getPreciounitario());
            preparedStatement.setFloat(3, detalle.getDescuento());
            preparedStatement.setFloat(4, detalle.getSubtotalLinea());
            preparedStatement.setInt(5, detalle.getMesesGarantiaAplicados());
            preparedStatement.setString(6, idEquipo);
            preparedStatement.setString(7, detalle.getIdDetalleFactura());

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            System.out.println("No se pudo actualizar el detalle de factura: " + e.getMessage());
        }
    }

    public void borrar(String idDetalleFactura) {
        final String sql = "DELETE FROM DetalleFactura WHERE IdDetalleFactura = ?";

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
        final String sql = "SELECT * FROM DetalleFactura";

        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            while (resultSet.next()) {
                Equipo equipoCascaron = null;
                String idEquipo = resultSet.getString("id_equipo");
                if (idEquipo != null) {
                    equipoCascaron = new Equipo(
                            "", "", "", null, 0f, "", "", "", 0f, "", 0f, 0f, "", 0f, 0f, 0f, 0, 0, 0, 0,
                            idEquipo, "", "", null, 0, "", null, ""
                    );
                }

                DetalleFactura det = new DetalleFactura(
                        resultSet.getString("IdDetalleFactura"),
                        resultSet.getFloat("precio_unitario"),
                        resultSet.getFloat("descuento"),
                        resultSet.getFloat("subtotal_linea"),
                        resultSet.getInt("meses_garantia"),
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
        final String sql = "SELECT * FROM DetalleFactura WHERE IdDetalleFactura = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, idDetalleFactura);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    Equipo equipoCascaron = null;
                    String idEquipo = resultSet.getString("id_equipo");
                    if (idEquipo != null) {
                        equipoCascaron = new Equipo(
                                "", "", "", null, 0f, "", "", "", 0f, "", 0f, 0f, "", 0f, 0f, 0f, 0, 0, 0, 0,
                                idEquipo, "", "", null, 0, "", null, ""
                        );
                    }

                    det = new DetalleFactura(
                            resultSet.getString("IdDetalleFactura"),
                            resultSet.getFloat("precio_unitario"),
                            resultSet.getFloat("descuento"),
                            resultSet.getFloat("subtotal_linea"),
                            resultSet.getInt("meses_garantia"),
                            equipoCascaron
                    );
                }
            }
        } catch (SQLException e) {
            System.out.println("No se pudo obtener el detalle de factura por ID: " + e.getMessage());
        }
        return det;
    }
}