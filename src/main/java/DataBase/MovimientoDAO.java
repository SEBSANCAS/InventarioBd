package DataBase;

import logico.Movimiento;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class MovimientoDAO {

    public static final MovimientoDAO INSTANCE = new MovimientoDAO();

    private MovimientoDAO() {}

    public static MovimientoDAO getInstance() {
        return INSTANCE;
    }

    public void guardar(Movimiento movimiento) {
        final String sql = "INSERT INTO Movimiento (Id_movimiento, idEquipo, id_ubicacion_origen, id_ubicacion_destino, tipo_movimiento, descripcion_movimiento, fecha_hora_movimiento) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            String idUbicacionOrigen = null;
            if (movimiento.getIdEstanteOrigen() != null) {
                idUbicacionOrigen = EstanteDAO.getInstance().obtenerIdUbicacion(
                        movimiento.getIdEstanteOrigen(),
                        movimiento.getNivelOrigen()
                );
            }

            String idUbicacionDestino = null;
            if (movimiento.getIdEstanteDestino() != null) {
                idUbicacionDestino = EstanteDAO.getInstance().obtenerIdUbicacion(
                        movimiento.getIdEstanteDestino(),
                        movimiento.getNivelDestino()
                );
            }

            preparedStatement.setString(1, movimiento.getIdMovimiento());
            preparedStatement.setString(2, movimiento.getIdEquipo());
            preparedStatement.setString(3, idUbicacionOrigen);
            preparedStatement.setString(4, idUbicacionDestino);
            preparedStatement.setString(5, movimiento.getTipoMovimiento());
            preparedStatement.setString(6, movimiento.getDescripcionMovimiento());
            preparedStatement.setObject(7, movimiento.getFechaHoraMovimiento());

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            System.out.println("No se pudo guardar el movimiento: " + e.getMessage());
        }
    }

    public void borrar(String idMovimiento) {
        final String sql = "DELETE FROM Movimiento WHERE Id_movimiento = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, idMovimiento);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            System.out.println("No se pudo eliminar el movimiento: " + e.getMessage());
        }
    }

    public ArrayList<Movimiento> EncontrarTodos() {
        ArrayList<Movimiento> movimientos = new ArrayList<>();

        final String sql = "SELECT m.*, " +
                "uo.codigo_estante AS estante_origen, uo.nivel_estante AS nivel_origen, " +
                "ud.codigo_estante AS estante_destino, ud.nivel_estante AS nivel_destino " +
                "FROM Movimiento m " +
                "LEFT JOIN Ubicacion_Almacen uo ON m.id_ubicacion_origen = uo.id_ubicacion " +
                "LEFT JOIN Ubicacion_Almacen ud ON m.id_ubicacion_destino = ud.id_ubicacion";

        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            while (resultSet.next()) {
                movimientos.add(mapearMovimiento(resultSet));
            }
        } catch (SQLException e) {
            System.out.println("No se pudo obtener la lista de movimientos: " + e.getMessage());
        }
        return movimientos;
    }

    public Movimiento encontrarPorId(String idMovimiento) {
        Movimiento movimiento = null;

        final String sql = "SELECT m.*, " +
                "uo.codigo_estante AS estante_origen, uo.nivel_estante AS nivel_origen, " +
                "ud.codigo_estante AS estante_destino, ud.nivel_estante AS nivel_destino " +
                "FROM Movimiento m " +
                "LEFT JOIN Ubicacion_Almacen uo ON m.id_ubicacion_origen = uo.id_ubicacion " +
                "LEFT JOIN Ubicacion_Almacen ud ON m.id_ubicacion_destino = ud.id_ubicacion " +
                "WHERE m.Id_movimiento = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, idMovimiento);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    movimiento = mapearMovimiento(resultSet);
                }
            }
        } catch (SQLException e) {
            System.out.println("No se pudo obtener el movimiento por ID: " + e.getMessage());
        }
        return movimiento;
    }

    public ArrayList<Movimiento> encontrarPorEquipo(String idEquipo) {
        ArrayList<Movimiento> movimientos = new ArrayList<>();

        final String sql = "SELECT m.*, " +
                "uo.codigo_estante AS estante_origen, uo.nivel_estante AS nivel_origen, " +
                "ud.codigo_estante AS estante_destino, ud.nivel_estante AS nivel_destino " +
                "FROM Movimiento m " +
                "LEFT JOIN Ubicacion_Almacen uo ON m.id_ubicacion_origen = uo.id_ubicacion " +
                "LEFT JOIN Ubicacion_Almacen ud ON m.id_ubicacion_destino = ud.id_ubicacion " +
                "WHERE m.idEquipo = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, idEquipo);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    movimientos.add(mapearMovimiento(resultSet));
                }
            }
        } catch (SQLException e) {
            System.out.println("No se pudo obtener los movimientos del equipo: " + e.getMessage());
        }
        return movimientos;
    }

    private Movimiento mapearMovimiento(ResultSet resultSet) throws SQLException {
        Movimiento movimiento = new Movimiento();
        movimiento.setIdMovimiento(resultSet.getString("Id_movimiento"));
        movimiento.setIdEquipo(resultSet.getString("idEquipo"));
        movimiento.setTipoMovimiento(resultSet.getString("tipo_movimiento"));
        movimiento.setDescripcionMovimiento(resultSet.getString("descripcion_movimiento"));
        movimiento.setFechaHoraMovimiento(resultSet.getObject("fecha_hora_movimiento", LocalDateTime.class));

        movimiento.setIdEstanteOrigen(resultSet.getString("estante_origen"));
        String nivelOrigenStr = resultSet.getString("nivel_origen");
        if (nivelOrigenStr != null && !nivelOrigenStr.isEmpty()) {
            try {
                movimiento.setNivelOrigen(Integer.parseInt(nivelOrigenStr));
            } catch (NumberFormatException ex) {
                movimiento.setNivelOrigen(0);
            }
        }

        movimiento.setIdEstanteDestino(resultSet.getString("estante_destino"));
        String nivelDestinoStr = resultSet.getString("nivel_destino");
        if (nivelDestinoStr != null && !nivelDestinoStr.isEmpty()) {
            try {
                movimiento.setNivelDestino(Integer.parseInt(nivelDestinoStr));
            } catch (NumberFormatException ex) {
                movimiento.setNivelDestino(0);
            }
        }

        return movimiento;
    }
}