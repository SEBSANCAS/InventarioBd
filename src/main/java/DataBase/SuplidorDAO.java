package DataBase;

import logico.Suplidor;
import logico.Laptop;
import logico.DetalleLaptopSuplidor;
import logico.Servicio;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Map;

public class SuplidorDAO {

    public static final SuplidorDAO INSTANCE = new SuplidorDAO();

    private SuplidorDAO() {}

    public static SuplidorDAO getInstance() {
        return INSTANCE;
    }

    public void guardar(Suplidor suplidor) {
        final String sqlSuplidor = "INSERT INTO Suplidor (IdSuplidor, razon_comercial, nombre_comercial, pais, ciudad, calle, fecha_registro, numero_identificacion, correo, telefono) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        final String sqlDetalle = "INSERT INTO DetalleLaptopSuplidor (id_laptop, id_suplidor, dias_entrega, precio_acordado) VALUES (?, ?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection()) {
            connection.setAutoCommit(false);

            try (PreparedStatement psSuplidor = connection.prepareStatement(sqlSuplidor)) {
                psSuplidor.setString(1, suplidor.getIdSuplidor());
                psSuplidor.setString(2, suplidor.getRazonComercial());
                psSuplidor.setString(3, suplidor.getNombreComercial());
                psSuplidor.setString(4, suplidor.getPais());
                psSuplidor.setString(5, suplidor.getCiudad());
                psSuplidor.setString(6, suplidor.getCalle());
                psSuplidor.setObject(7, suplidor.getFechaRegistro());
                psSuplidor.setString(8, suplidor.getNumeroIdentificacion());
                psSuplidor.setString(9, suplidor.getCorreo());
                psSuplidor.setString(10, suplidor.getTelefono());
                psSuplidor.executeUpdate();
            }

            if (suplidor.getLaptopsSuplidor() != null && !suplidor.getLaptopsSuplidor().isEmpty()) {
                try (PreparedStatement psDetalle = connection.prepareStatement(sqlDetalle)) {
                    for (Map.Entry<Laptop, DetalleLaptopSuplidor> entry : suplidor.getLaptopsSuplidor().entrySet()) {
                        Laptop laptop = entry.getKey();
                        DetalleLaptopSuplidor detalle = entry.getValue();

                        if (laptop != null && detalle != null) {
                            psDetalle.setString(1, laptop.getIdLaptop());
                            psDetalle.setString(2, suplidor.getIdSuplidor());
                            psDetalle.setInt(3, detalle.getDiasEntrega());
                            psDetalle.setFloat(4, detalle.getPrecioAcordado());
                            psDetalle.addBatch();
                        }
                    }
                    psDetalle.executeBatch();
                }
            }

            connection.commit();

        } catch (SQLException e) {
            System.out.println("No se pudo guardar el suplidor y sus detalles: " + e.getMessage());
        }
    }

    public void actualizar(Suplidor suplidor) {
        final String sqlSuplidor = "UPDATE Suplidor SET razon_comercial=?, nombre_comercial=?, pais=?, ciudad=?, calle=?, fecha_registro=?, numero_identificacion=?, correo=?, telefono=? WHERE IdSuplidor=?";
        final String sqlDeleteDetalles = "DELETE FROM DetalleLaptopSuplidor WHERE id_suplidor = ?";
        final String sqlInsertDetalle = "INSERT INTO DetalleLaptopSuplidor (id_laptop, id_suplidor, dias_entrega, precio_acordado) VALUES (?, ?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection()) {
            connection.setAutoCommit(false);

            try (PreparedStatement psSuplidor = connection.prepareStatement(sqlSuplidor)) {
                psSuplidor.setString(1, suplidor.getRazonComercial());
                psSuplidor.setString(2, suplidor.getNombreComercial());
                psSuplidor.setString(3, suplidor.getPais());
                psSuplidor.setString(4, suplidor.getCiudad());
                psSuplidor.setString(5, suplidor.getCalle());
                psSuplidor.setObject(6, suplidor.getFechaRegistro());
                psSuplidor.setString(7, suplidor.getNumeroIdentificacion());
                psSuplidor.setString(8, suplidor.getCorreo());
                psSuplidor.setString(9, suplidor.getTelefono());
                psSuplidor.setString(10, suplidor.getIdSuplidor());
                psSuplidor.executeUpdate();
            }

            try (PreparedStatement psDelete = connection.prepareStatement(sqlDeleteDetalles)) {
                psDelete.setString(1, suplidor.getIdSuplidor());
                psDelete.executeUpdate();
            }

            if (suplidor.getLaptopsSuplidor() != null && !suplidor.getLaptopsSuplidor().isEmpty()) {
                try (PreparedStatement psInsert = connection.prepareStatement(sqlInsertDetalle)) {
                    for (Map.Entry<Laptop, DetalleLaptopSuplidor> entry : suplidor.getLaptopsSuplidor().entrySet()) {
                        Laptop laptop = entry.getKey();
                        DetalleLaptopSuplidor detalle = entry.getValue();

                        if (laptop != null && detalle != null) {
                            psInsert.setString(1, laptop.getIdLaptop());
                            psInsert.setString(2, suplidor.getIdSuplidor());
                            psInsert.setInt(3, detalle.getDiasEntrega());
                            psInsert.setFloat(4, detalle.getPrecioAcordado());
                            psInsert.addBatch();
                        }
                    }
                    psInsert.executeBatch();
                }
            }

            connection.commit();

        } catch (SQLException e) {
            System.out.println("No se pudo actualizar el suplidor y sus detalles: " + e.getMessage());
        }
    }

    public void borrar(String idSuplidor) {
        final String sqlDeleteDetalles = "DELETE FROM DetalleLaptopSuplidor WHERE id_suplidor = ?";
        final String sqlSuplidor = "DELETE FROM Suplidor WHERE IdSuplidor = ?";

        try (Connection connection = DatabaseConnection.getConnection()) {
            connection.setAutoCommit(false);

            try (PreparedStatement psDelete = connection.prepareStatement(sqlDeleteDetalles)) {
                psDelete.setString(1, idSuplidor);
                psDelete.executeUpdate();
            }

            try (PreparedStatement psSuplidor = connection.prepareStatement(sqlSuplidor)) {
                psSuplidor.setString(1, idSuplidor);
                psSuplidor.executeUpdate();
            }

            connection.commit();

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
                String idSuplidor = resultSet.getString("IdSuplidor");
                Suplidor suplidor = new Suplidor(
                        resultSet.getString("numero_identificacion"),
                        resultSet.getString("correo"),
                        resultSet.getString("telefono"),
                        resultSet.getString("nombre_comercial"),
                        idSuplidor,
                        resultSet.getString("razon_comercial"),
                        resultSet.getString("pais"),
                        resultSet.getString("ciudad"),
                        resultSet.getString("calle"),
                        resultSet.getObject("fecha_registro", LocalDate.class)
                );

                cargarDetallesRelacion(connection, suplidor);
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
                            idSuplidor,
                            resultSet.getString("razon_comercial"),
                            resultSet.getString("pais"),
                            resultSet.getString("ciudad"),
                            resultSet.getString("calle"),
                            resultSet.getObject("fecha_registro", LocalDate.class)
                    );
                    cargarDetallesRelacion(connection, suplidor);
                }
            }
        } catch (SQLException e) {
            System.out.println("No se pudo obtener el suplidor por ID: " + e.getMessage());
        }
        return suplidor;
    }

    private void cargarDetallesRelacion(Connection connection, Suplidor suplidor) throws SQLException {
        final String sqlDetalles = "SELECT * FROM DetalleLaptopSuplidor WHERE id_suplidor = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlDetalles)) {
            preparedStatement.setString(1, suplidor.getIdSuplidor());
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    String idLaptop = resultSet.getString("id_laptop");
                    if (idLaptop != null) {
                        Laptop laptopReal = Servicio.getInstance().getMisLaptops().get(idLaptop);
                        DetalleLaptopSuplidor detalle = new DetalleLaptopSuplidor(
                                resultSet.getInt("dias_entrega"),
                                resultSet.getFloat("precio_acordado")
                        );
                        if (laptopReal != null) {
                            suplidor.getLaptopsSuplidor().put(laptopReal, detalle);
                        } else {
                            Laptop laptopCascaron = new Laptop(idLaptop, "", "", null, 0f, "", "", "", 0f, "", 0f, 0f, "", 0f, 0f, 0f, 0, 0, 0, 0);
                            suplidor.getLaptopsSuplidor().put(laptopCascaron, detalle);
                        }
                    }
                }
            }
        }
    }
}