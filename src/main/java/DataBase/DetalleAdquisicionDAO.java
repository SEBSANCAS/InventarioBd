package DataBase;

import logico.DetalleAdquisicion;
import logico.Laptop;
import java.sql.*;
import java.util.ArrayList;

public class DetalleAdquisicionDAO {

    public static final DetalleAdquisicionDAO INSTANCE = new DetalleAdquisicionDAO();

    private DetalleAdquisicionDAO() {}

    public static DetalleAdquisicionDAO getInstance() {
        return INSTANCE;
    }

    public void guardar(DetalleAdquisicion detalle, String idCompra) {
        final String sql = "INSERT INTO DetalleAdquisicion (IdDetalleAdquisicion, id_compra, id_laptop, cantidad, costo_unitario, subtotal_linea) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            String idLaptop = null;
            if (detalle.getModeloLaptopAdquirida() != null) {
                idLaptop = detalle.getModeloLaptopAdquirida().getIdLaptop(); // Asumiendo getIdLaptop() existente en Laptop
            }

            preparedStatement.setString(1, detalle.getIdDetalleAdquisicion());
            preparedStatement.setString(2, idCompra);
            preparedStatement.setString(3, idLaptop);
            preparedStatement.setInt(4, detalle.getCantidad());
            preparedStatement.setFloat(5, detalle.getCostoUnitario());
            preparedStatement.setFloat(6, detalle.getSubtotalLinea());

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            System.out.println("No se pudo guardar el detalle de adquisicion: " + e.getMessage());
        }
    }

    public void actualizar(DetalleAdquisicion detalle, String idCompra) {
        final String sql = "UPDATE DetalleAdquisicion SET id_compra=?, id_laptop=?, cantidad=?, costo_unitario=?, subtotal_linea=? WHERE IdDetalleAdquisicion=?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            String idLaptop = null;
            if (detalle.getModeloLaptopAdquirida() != null) {
                idLaptop = detalle.getModeloLaptopAdquirida().getIdLaptop();
            }

            preparedStatement.setString(1, idCompra);
            preparedStatement.setString(2, idLaptop);
            preparedStatement.setInt(3, detalle.getCantidad());
            preparedStatement.setFloat(4, detalle.getCostoUnitario());
            preparedStatement.setFloat(5, detalle.getSubtotalLinea());
            preparedStatement.setString(6, detalle.getIdDetalleAdquisicion());

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            System.out.println("No se pudo actualizar el detalle de adquisicion: " + e.getMessage());
        }
    }

    public void borrar(String idDetalleAdquisicion) {
        final String sql = "DELETE FROM DetalleAdquisicion WHERE IdDetalleAdquisicion = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, idDetalleAdquisicion);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            System.out.println("No se pudo eliminar el detalle de adquisicion: " + e.getMessage());
        }
    }

    public ArrayList<DetalleAdquisicion> EncontrarTodos() {
        ArrayList<DetalleAdquisicion> detalles = new ArrayList<>();
        final String sql = "SELECT * FROM DetalleAdquisicion";

        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            while (resultSet.next()) {
                Laptop laptopCascaron = null;
                String idLaptop = resultSet.getString("id_laptop");
                if (idLaptop != null) {
                    laptopCascaron = new Laptop(idLaptop, "", "", null, 0f, "", "", "", 0f, "", 0f, 0f, "", 0f, 0f, 0f, 0, 0, 0, 0);
                }

                DetalleAdquisicion det = new DetalleAdquisicion(
                        resultSet.getString("IdDetalleAdquisicion"),
                        laptopCascaron,
                        resultSet.getInt("cantidad"),
                        resultSet.getFloat("costo_unitario"),
                        resultSet.getFloat("subtotal_linea")
                );
                detalles.add(det);
            }
        } catch (SQLException e) {
            System.out.println("No se pudo obtener la lista de detalles: " + e.getMessage());
        }
        return detalles;
    }

    public DetalleAdquisicion encontrarPorId(String idDetalleAdquisicion) {
        DetalleAdquisicion det = null;
        final String sql = "SELECT * FROM DetalleAdquisicion WHERE IdDetalleAdquisicion = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, idDetalleAdquisicion);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    Laptop laptopCascaron = null;
                    String idLaptop = resultSet.getString("id_laptop");
                    if (idLaptop != null) {
                        laptopCascaron = new Laptop(idLaptop, "", "", null, 0f, "", "", "", 0f, "", 0f, 0f, "", 0f, 0f, 0f, 0, 0, 0, 0);
                    }

                    det = new DetalleAdquisicion(
                            resultSet.getString("IdDetalleAdquisicion"),
                            laptopCascaron,
                            resultSet.getInt("cantidad"),
                            resultSet.getFloat("costo_unitario"),
                            resultSet.getFloat("subtotal_linea")
                    );
                }
            }
        } catch (SQLException e) {
            System.out.println("No se pudo obtener el detalle por ID: " + e.getMessage());
        }
        return det;
    }
}