package DataBase;

import logico.Reclamo;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class ReclamoDAO {

    public static final ReclamoDAO INSTANCE = new ReclamoDAO();

    private ReclamoDAO() {}

    public static ReclamoDAO getInstance() {
        return INSTANCE;
    }

    public void guardar(Reclamo reclamo) {
        // Se agregó 'fecha' al INSERT y un '?' adicional (son 9 parámetros ahora)
        final String sql = "INSERT INTO Reclamo (id_reclamo, id_factura, id_detalle, id_cliente, fecha, en_garantia, tipo_solicitud, diagnostico_tecnico, estado_caso) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, reclamo.getIdReclamo());
            preparedStatement.setString(2, reclamo.getIdFactura());
            preparedStatement.setString(3, reclamo.getIdDetalleFactura());
            preparedStatement.setString(4, reclamo.getIdCliente());

            // Si la fecha es null, se guarda la fecha del sistema actual por defecto
            preparedStatement.setObject(5, reclamo.getFecha() != null ? reclamo.getFecha() : LocalDate.now());

            preparedStatement.setBoolean(6, reclamo.isEnGarantia());
            preparedStatement.setString(7, reclamo.getTipoSolicitud());
            preparedStatement.setString(8, reclamo.getDiagnosticoTecnico());
            preparedStatement.setString(9, reclamo.getEstadoCaso());

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            System.out.println("No se pudo guardar el reclamo: " + e.getMessage());
        }
    }

    public void actualizar(Reclamo reclamo) {
        // Se agregó 'fecha=?' al UPDATE (son 9 parámetros ahora)
        final String sql = "UPDATE Reclamo SET id_factura=?, id_detalle=?, id_cliente=?, fecha=?, en_garantia=?, tipo_solicitud=?, diagnostico_tecnico=?, estado_caso=? WHERE id_reclamo=?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, reclamo.getIdFactura());
            preparedStatement.setString(2, reclamo.getIdDetalleFactura());
            preparedStatement.setString(3, reclamo.getIdCliente());
            preparedStatement.setObject(4, reclamo.getFecha());
            preparedStatement.setBoolean(5, reclamo.isEnGarantia());
            preparedStatement.setString(6, reclamo.getTipoSolicitud());
            preparedStatement.setString(7, reclamo.getDiagnosticoTecnico());
            preparedStatement.setString(8, reclamo.getEstadoCaso());
            preparedStatement.setString(9, reclamo.getIdReclamo());

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            System.out.println("No se pudo actualizar el reclamo: " + e.getMessage());
        }
    }

    public void borrar(String idReclamo) {
        final String sql = "DELETE FROM Reclamo WHERE id_reclamo = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, idReclamo);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            System.out.println("No se pudo eliminar el reclamo: " + e.getMessage());
        }
    }

    public ArrayList<Reclamo> EncontrarTodos() {
        ArrayList<Reclamo> reclamos = new ArrayList<>();
        final String sql = "SELECT * FROM Reclamo";

        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            while (resultSet.next()) {
                reclamos.add(mapearReclamo(resultSet));
            }
        } catch (SQLException e) {
            System.out.println("No se pudo obtener la lista de reclamos: " + e.getMessage());
        }
        return reclamos;
    }

    public Reclamo encontrarPorId(String idReclamo) {
        Reclamo reclamo = null;
        final String sql = "SELECT * FROM Reclamo WHERE id_reclamo = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, idReclamo);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    reclamo = mapearReclamo(resultSet);
                }
            }
        } catch (SQLException e) {
            System.out.println("No se pudo obtener el reclamo por ID: " + e.getMessage());
        }
        return reclamo;
    }

    public ArrayList<Reclamo> encontrarPorEquipo(String idEquipo) {
        ArrayList<Reclamo> reclamos = new ArrayList<>();

        final String sql = "SELECT r.* FROM Reclamo r " +
                "JOIN Detalle_Factura df ON r.id_detalle = df.id_detalle " +
                "WHERE df.IdEquipo = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, idEquipo);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    reclamos.add(mapearReclamo(resultSet));
                }
            }
        } catch (SQLException e) {
            System.out.println("No se pudo obtener los reclamos del equipo: " + e.getMessage());
        }
        return reclamos;
    }

    // Se actualizó este método para incluir la extracción de la fecha desde la BD
    private Reclamo mapearReclamo(ResultSet resultSet) throws SQLException {
        return new Reclamo(
                resultSet.getString("id_reclamo"),
                resultSet.getString("id_factura"),
                resultSet.getString("id_detalle"),
                resultSet.getString("id_cliente"),
                resultSet.getObject("fecha", LocalDate.class), // <-- Se extrae la fecha
                resultSet.getBoolean("en_garantia"),
                resultSet.getString("tipo_solicitud"),
                resultSet.getString("diagnostico_tecnico"),
                resultSet.getString("estado_caso")
        );
    }
}