package DataBase;

import logico.Equipo;
import logico.Estante;
import logico.Laptop;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class EquipoDAO {

    public static final EquipoDAO INSTANCE = new EquipoDAO();

    private EquipoDAO() {}

    public static EquipoDAO getInstance() {
        return INSTANCE;
    }

    public void guardar(Equipo equipo) {
        final String sql = "INSERT INTO Equipo (id_equipo, numero_serie, id_laptop, id_ubicacion, id_detalle_orden, estado, color, fecha_ingreso) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            String idUbicacion = null;
            if (equipo.getEstante() != null) {
                idUbicacion = EstanteDAO.getInstance().obtenerIdUbicacion(
                        equipo.getEstante().getIdEstante(),
                        equipo.getNivelEstante()
                );
            }

            preparedStatement.setString(1, equipo.getIdEquipo());
            preparedStatement.setString(2, equipo.getNumeroSerie());
            preparedStatement.setString(3, equipo.getLaptop().getIdLaptop());
            preparedStatement.setString(4, idUbicacion);
            preparedStatement.setString(5, equipo.getIdAdquisicionOrigen());
            preparedStatement.setString(6, equipo.getEstado());
            preparedStatement.setString(7, equipo.getColor());
            preparedStatement.setObject(8, equipo.getFechaIngreso());

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            System.out.println("No se pudo guardar el equipo: " + e.getMessage());
        }
    }

    public void actualizar(Equipo equipo) {
        final String sql = "UPDATE Equipo SET numero_serie=?, id_laptop=?, id_ubicacion=?, id_detalle_orden=?, estado=?, color=?, fecha_ingreso=? WHERE id_equipo=?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            String idUbicacion = null;
            if (equipo.getEstante() != null) {
                idUbicacion = EstanteDAO.getInstance().obtenerIdUbicacion(
                        equipo.getEstante().getIdEstante(),
                        equipo.getNivelEstante()
                );
            }

            preparedStatement.setString(1, equipo.getNumeroSerie());
            preparedStatement.setString(2, equipo.getLaptop().getIdLaptop());
            preparedStatement.setString(3, idUbicacion);
            preparedStatement.setString(4, equipo.getIdAdquisicionOrigen());
            preparedStatement.setString(5, equipo.getEstado());
            preparedStatement.setString(6, equipo.getColor());
            preparedStatement.setObject(7, equipo.getFechaIngreso());
            preparedStatement.setString(8, equipo.getIdEquipo());

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            System.out.println("No se pudo actualizar el equipo: " + e.getMessage());
        }
    }

    public void borrar(String idEquipo) {
        final String sql = "DELETE FROM Equipo WHERE id_equipo = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, idEquipo);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            System.out.println("No se pudo eliminar el equipo: " + e.getMessage());
        }
    }

    public ArrayList<Equipo> EncontrarTodos() {
        ArrayList<Equipo> equipos = new ArrayList<>();

        final String sql = "SELECT e.*, u.codigo_estante, u.nivel_estante " +
                "FROM Equipo e " +
                "LEFT JOIN Ubicacion_Almacen u ON e.id_ubicacion = u.id_ubicacion";

        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            while (resultSet.next()) {
                String idLaptop = resultSet.getString("id_laptop");

                Laptop laptop = LaptopDAO
                        .getInstance()
                        .encontrarPorId(idLaptop);

                Equipo eq = new Equipo(
                        resultSet.getString("id_equipo"),
                        laptop,
                        resultSet.getString("numero_serie"),
                        resultSet.getString("color"),
                        null,
                        0,
                        resultSet.getString("estado"),
                        resultSet.getObject("fecha_ingreso", LocalDate.class),
                        resultSet.getString("id_detalle_orden")
                );

                String codigoEstante = resultSet.getString("codigo_estante");
                String nivelStr = resultSet.getString("nivel_estante");

                if (codigoEstante != null) {
                    Estante estanteCascaron = new Estante(codigoEstante, 0, 0, "");
                    eq.setEstante(estanteCascaron);

                    if (nivelStr != null && !nivelStr.isEmpty()) {
                        try {
                            eq.setNivelEstante(Integer.parseInt(nivelStr));
                        } catch (NumberFormatException ex) {
                            eq.setNivelEstante(1);
                        }
                    }
                }

                equipos.add(eq);
            }
        } catch (SQLException e) {
            System.out.println("No se pudo obtener la lista de equipos: " + e.getMessage());
        }
        return equipos;
    }

    public Equipo encontrarPorId(String idEquipo) {
        Equipo eq = null;
        final String sql = "SELECT e.*, u.codigo_estante, u.nivel_estante " +
                "FROM Equipo e " +
                "LEFT JOIN Ubicacion_Almacen u ON e.id_ubicacion = u.id_ubicacion " +
                "WHERE e.id_equipo = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, idEquipo);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    String idLaptop = resultSet.getString("id_laptop");

                    Laptop laptop = LaptopDAO
                            .getInstance()
                            .encontrarPorId(idLaptop);

                    eq = new Equipo(
                            resultSet.getString("id_equipo"),
                            laptop,
                            resultSet.getString("numero_serie"),
                            resultSet.getString("color"),
                            null,
                            0,
                            resultSet.getString("estado"),
                            resultSet.getObject("fecha_ingreso", LocalDate.class),
                            resultSet.getString("id_detalle_orden")
                    );

                    String codigoEstante = resultSet.getString("codigo_estante");
                    String nivelStr = resultSet.getString("nivel_estante");

                    if (codigoEstante != null) {
                        Estante estanteCascaron = new Estante(codigoEstante, 0, 0, "");
                        eq.setEstante(estanteCascaron);
                        if (nivelStr != null && !nivelStr.isEmpty()) {
                            try {
                                eq.setNivelEstante(Integer.parseInt(nivelStr));
                            } catch (NumberFormatException ex) {
                                eq.setNivelEstante(1);
                            }
                        }
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("No se pudo obtener el equipo por ID: " + e.getMessage());
        }
        return eq;
    }
}