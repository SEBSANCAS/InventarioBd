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
        final String sql = "INSERT INTO Adquisicion (IdCompra, id_suplidor, fecha_emision, fecha_entrega, estado, monto_total) VALUES (?, ?, ?, ?, ?, ?)";

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
        final String sql = "UPDATE Adquisicion SET id_suplidor=?, fecha_emision=?, fecha_entrega=?, estado=?, monto_total=? WHERE IdCompra=?";

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
        final String sql = "DELETE FROM Adquisicion WHERE IdCompra = ?";

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
        final String sql = "SELECT * FROM Adquisicion";

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
                        resultSet.getString("IdCompra"),
                        suplidorCascaron,
                        resultSet.getObject("fecha_emision", LocalDate.class),
                        resultSet.getObject("fecha_entrega", LocalDate.class),
                        resultSet.getString("estado"),
                        resultSet.getFloat("monto_total")
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
        final String sql = "SELECT * FROM Adquisicion WHERE IdCompra = ?";

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
                            resultSet.getString("IdCompra"),
                            suplidorCascaron,
                            resultSet.getObject("fecha_emision", LocalDate.class),
                            resultSet.getObject("fecha_entrega", LocalDate.class),
                            resultSet.getString("estado"),
                            resultSet.getFloat("monto_total")
                    );
                }
            }
        } catch (SQLException e) {
            System.out.println("No se pudo obtener la adquisicion por ID: " + e.getMessage());
        }
        return adq;
    }
}