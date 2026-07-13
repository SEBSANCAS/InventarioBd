package DataBase;

import logico.Adquisicion;
import logico.Suplidor;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class AdquisicionDAO {

    public static final AdquisicionDAO INSTANCE = new AdquisicionDAO();

    private AdquisicionDAO() {}

    public static AdquisicionDAO getInstance() {
        return INSTANCE;
    }

    public void guardar(Adquisicion adquisicion) {
        final String sql = "INSERT INTO Orden_Compra (id_orden_compra, id_suplidor, fecha_emision, fecha_entrega, estado_orden, monto_total_estimado) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            String idSuplidor = null;
            if (adquisicion.getSuplidor() != null) {
                idSuplidor = adquisicion.getSuplidor().getIdSuplidor();
            }

            preparedStatement.setString(1, adquisicion.getIdCompra());
            preparedStatement.setString(2, idSuplidor);
            preparedStatement.setObject(3, adquisicion.getFechaEmision());
            preparedStatement.setObject(4, adquisicion.getFechaEntrega());
            preparedStatement.setString(5, adquisicion.getEstado());
            preparedStatement.setFloat(6, adquisicion.getMontoTotal());

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            System.out.println("No se pudo guardar la adquisicion: " + e.getMessage());
        }
    }

    public void actualizar(Adquisicion adquisicion) {
        final String sql = "UPDATE Orden_Compra SET id_suplidor=?, fecha_emision=?, fecha_entrega=?, estado_orden=?, monto_total_estimado=? WHERE id_orden_compra=?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            String idSuplidor = null;
            if (adquisicion.getSuplidor() != null) {
                idSuplidor = adquisicion.getSuplidor().getIdSuplidor();
            }

            preparedStatement.setString(1, idSuplidor);
            preparedStatement.setObject(2, adquisicion.getFechaEmision());
            preparedStatement.setObject(3, adquisicion.getFechaEntrega());
            preparedStatement.setString(4, adquisicion.getEstado());
            preparedStatement.setFloat(5, adquisicion.getMontoTotal());
            preparedStatement.setString(6, adquisicion.getIdCompra());

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            System.out.println("No se pudo actualizar la adquisicion: " + e.getMessage());
        }
    }

    public void borrar(String idCompra) {
        final String sql = "DELETE FROM Orden_Compra WHERE id_orden_compra = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, idCompra);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            System.out.println("No se pudo eliminar la adquisicion: " + e.getMessage());
        }
    }

    public ArrayList<Adquisicion> EncontrarTodos() {
        ArrayList<Adquisicion> adquisiciones = new ArrayList<>();
        final String sql = "SELECT * FROM Orden_Compra";

        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            while (resultSet.next()) {
                Suplidor suplidorCascaron = null;
                String idSuplidor = resultSet.getString("id_suplidor");
                if (idSuplidor != null) {
                    suplidorCascaron = new Suplidor("", "", "", "", idSuplidor, "", "", "", "", null);
                }

                Adquisicion adq = new Adquisicion(
                        resultSet.getString("id_orden_compra"),
                        suplidorCascaron,
                        resultSet.getObject("fecha_emision", LocalDate.class),
                        resultSet.getObject("fecha_entrega", LocalDate.class),
                        resultSet.getString("estado_orden"),
                        resultSet.getFloat("monto_total_estimado")
                );

                adquisiciones.add(adq);
            }
        } catch (SQLException e) {
            System.out.println("No se pudo obtener la lista de adquisiciones: " + e.getMessage());
        }
        return adquisiciones;
    }

    public Adquisicion encontrarPorId(String idCompra) {
        Adquisicion adq = null;
        final String sql = "SELECT * FROM Orden_Compra WHERE id_orden_compra = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, idCompra);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    Suplidor suplidorCascaron = null;
                    String idSuplidor = resultSet.getString("id_suplidor");
                    if (idSuplidor != null) {
                        suplidorCascaron = new Suplidor("", "", "", "", idSuplidor, "", "", "", "", null);
                    }

                    adq = new Adquisicion(
                            resultSet.getString("id_orden_compra"),
                            suplidorCascaron,
                            resultSet.getObject("fecha_emision", LocalDate.class),
                            resultSet.getObject("fecha_entrega", LocalDate.class),
                            resultSet.getString("estado_orden"),
                            resultSet.getFloat("monto_total_estimado")
                    );
                }
            }
        } catch (SQLException e) {
            System.out.println("No se pudo obtener la adquisicion por ID: " + e.getMessage());
        }
        return adq;
    }
}