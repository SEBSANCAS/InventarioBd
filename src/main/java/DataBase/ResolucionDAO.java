package DataBase;

import logico.Resolucion;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class ResolucionDAO {

    public static final ResolucionDAO INSTANCE = new ResolucionDAO();

    private ResolucionDAO() {}

    public static ResolucionDAO getInstance() {
        return INSTANCE;
    }

    public void guardar(Resolucion resolucion) {
        final String sql = "INSERT INTO Resolucion (id_resolucion, id_reclamo, id_equipo_entrante, id_equipo_saliente, accion_tomada, monto_cobrado, monto_reembolsado, fecha_resolucion) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, resolucion.getIdResolucion());
            preparedStatement.setString(2, resolucion.getIdReclamo());
            preparedStatement.setString(3, resolucion.getIdEquipoEntrante());
            preparedStatement.setString(4, resolucion.getIdEquipoSaliente());
            preparedStatement.setString(5, resolucion.getAccionTomada());
            preparedStatement.setFloat(6, resolucion.getMontoCobrado());
            preparedStatement.setFloat(7, resolucion.getMontoReembolsado());
            preparedStatement.setObject(8, resolucion.getFechaResolucion());

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            System.out.println("No se pudo guardar la resolucion: " + e.getMessage());
        }
    }

    public void actualizar(Resolucion resolucion) {
        final String sql = "UPDATE Resolucion SET id_reclamo=?, id_equipo_entrante=?, id_equipo_saliente=?, accion_tomada=?, monto_cobrado=?, monto_reembolsado=?, fecha_resolucion=? WHERE id_resolucion=?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, resolucion.getIdReclamo());
            preparedStatement.setString(2, resolucion.getIdEquipoEntrante());
            preparedStatement.setString(3, resolucion.getIdEquipoSaliente());
            preparedStatement.setString(4, resolucion.getAccionTomada());
            preparedStatement.setFloat(5, resolucion.getMontoCobrado());
            preparedStatement.setFloat(6, resolucion.getMontoReembolsado());
            preparedStatement.setObject(7, resolucion.getFechaResolucion());
            preparedStatement.setString(8, resolucion.getIdResolucion());

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            System.out.println("No se pudo actualizar la resolucion: " + e.getMessage());
        }
    }

    public void borrar(String idResolucion) {
        final String sql = "DELETE FROM Resolucion WHERE id_resolucion = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, idResolucion);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            System.out.println("No se pudo eliminar la resolucion: " + e.getMessage());
        }
    }

    public ArrayList<Resolucion> EncontrarTodos() {
        ArrayList<Resolucion> resoluciones = new ArrayList<>();
        final String sql = "SELECT * FROM Resolucion";

        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            while (resultSet.next()) {
                resoluciones.add(mapearResolucion(resultSet));
            }
        } catch (SQLException e) {
            System.out.println("No se pudo obtener la lista de resoluciones: " + e.getMessage());
        }
        return resoluciones;
    }

    public Resolucion encontrarPorId(String idResolucion) {
        Resolucion resolucion = null;
        final String sql = "SELECT * FROM Resolucion WHERE id_resolucion = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, idResolucion);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    resolucion = mapearResolucion(resultSet);
                }
            }
        } catch (SQLException e) {
            System.out.println("No se pudo obtener la resolucion por ID: " + e.getMessage());
        }
        return resolucion;
    }

    public ArrayList<Resolucion> encontrarPorEquipo(String idEquipo) {
        ArrayList<Resolucion> resoluciones = new ArrayList<>();
        final String sql = "SELECT * FROM Resolucion WHERE id_equipo_entrante = ? OR id_equipo_saliente = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, idEquipo);
            preparedStatement.setString(2, idEquipo);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    resoluciones.add(mapearResolucion(resultSet));
                }
            }
        } catch (SQLException e) {
            System.out.println("No se pudo obtener las resoluciones del equipo: " + e.getMessage());
        }
        return resoluciones;
    }

    private Resolucion mapearResolucion(ResultSet resultSet) throws SQLException {
        return new Resolucion(
                resultSet.getString("id_resolucion"),
                resultSet.getString("id_reclamo"),
                resultSet.getString("id_equipo_entrante"),
                resultSet.getString("id_equipo_saliente"),
                resultSet.getString("accion_tomada"),
                resultSet.getFloat("monto_cobrado"),
                resultSet.getFloat("monto_reembolsado"),
                resultSet.getObject("fecha_resolucion", LocalDate.class)
        );
    }
}