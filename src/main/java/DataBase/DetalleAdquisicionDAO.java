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
        final String sql = "INSERT INTO Detalle_Orden_Compra (id_detalle_orden, id_orden_compra, id_laptop, cantidad_solicitada, costo_unitario_acordado, subtotal_linea) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            String idLaptop = null;
            if (detalle.getModeloLaptopAdquirida() != null) {
                idLaptop = detalle.getModeloLaptopAdquirida().getIdLaptop();
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
        final String sql = "UPDATE Detalle_Orden_Compra SET id_orden_compra=?, id_laptop=?, cantidad_solicitada=?, costo_unitario_acordado=?, subtotal_linea=? WHERE id_detalle_orden=?";

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
    public ArrayList<DetalleAdquisicion> encontrarPorIdAdquisicion(String idCompra) {

        ArrayList<DetalleAdquisicion> detalles = new ArrayList<>();

        final String sql = "SELECT * FROM Detalle_Adquisicion WHERE id_compra = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, idCompra);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {

                while (resultSet.next()) {

                    Laptop laptop = LaptopDAO.getInstance()
                            .encontrarPorId(resultSet.getString("id_laptop"));

                    DetalleAdquisicion detalle = new DetalleAdquisicion(
                            resultSet.getString("id_detalle"),
                            laptop,
                            resultSet.getInt("cantidad"),
                            resultSet.getFloat("precio_unitario_compra"),
                            resultSet.getFloat("subtotal_linea")
                    );

                    detalles.add(detalle);
                }
            }

        } catch (SQLException e) {
            System.out.println("No se pudieron obtener los detalles de la adquisición: " + e.getMessage());
        }

        return detalles;
    }
    public void borrar(String idDetalleAdquisicion) {
        final String sql = "DELETE FROM Detalle_Orden_Compra WHERE id_detalle_orden = ?";

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
        final String sql = "SELECT * FROM Detalle_Orden_Compra";

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
                        resultSet.getString("id_detalle_orden"),
                        laptopCascaron,
                        resultSet.getInt("cantidad_solicitada"),
                        resultSet.getFloat("costo_unitario_acordado"),
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
        final String sql = "SELECT * FROM Detalle_Orden_Compra WHERE id_detalle_orden = ?";

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
                            resultSet.getString("id_detalle_orden"),
                            laptopCascaron,
                            resultSet.getInt("cantidad_solicitada"),
                            resultSet.getFloat("costo_unitario_acordado"),
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