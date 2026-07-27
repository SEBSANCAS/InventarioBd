package DataBase;

import logico.Equipo;
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
        // CORRECCIÓN: Se cambió id_equipo por IdEquipo
        final String sql = "INSERT INTO Equipo (IdEquipo, numero_serie, id_laptop, id_ubicacion, id_detalle_orden, estado, disponibilidad, color, fecha_ingreso, descuento_por_condicion) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            String idUbicacion = EstanteDAO.getInstance().obtenerPrimeraUbicacionDisponible();
            if (idUbicacion == null) {
                throw new RuntimeException("No hay espacio disponible en el almacén.");
            }

            preparedStatement.setString(1, equipo.getIdEquipo());
            preparedStatement.setString(2, equipo.getNumeroSerie());
            preparedStatement.setString(3, equipo.getLaptop() != null ? equipo.getLaptop().getIdLaptop() : null);
            preparedStatement.setString(4, idUbicacion);
            preparedStatement.setString(5, equipo.getIdAdquisicionOrigen());
            preparedStatement.setString(6, equipo.getEstado());
            preparedStatement.setString(7, equipo.getDisponibilidad());
            preparedStatement.setString(8, equipo.getColor());

            if (equipo.getFechaIngreso() != null) {
                preparedStatement.setDate(9, Date.valueOf(equipo.getFechaIngreso()));
            } else {
                preparedStatement.setNull(9, Types.DATE);
            }

            preparedStatement.setFloat(10, equipo.getDescuentoPorCondicion());

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            System.out.println("No se pudo guardar el equipo: " + e.getMessage());
        }
    }

    public void actualizar(Equipo equipo) {
        // CORRECCIÓN: Se cambió id_equipo por IdEquipo
        final String sql =
                "UPDATE Equipo SET " +
                        "numero_serie=?, " +
                        "id_laptop=?, " +
                        "id_detalle_orden=?, " +
                        "estado=?, " +
                        "disponibilidad=?, " +
                        "color=?, " +
                        "fecha_ingreso=?, " +
                        "descuento_por_condicion=? " +
                        "WHERE IdEquipo=?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, equipo.getNumeroSerie());
            preparedStatement.setString(2, equipo.getLaptop() != null ? equipo.getLaptop().getIdLaptop() : null);
            preparedStatement.setString(3, equipo.getIdAdquisicionOrigen());
            preparedStatement.setString(4, equipo.getEstado());
            preparedStatement.setString(5, equipo.getDisponibilidad());
            preparedStatement.setString(6, equipo.getColor());

            if (equipo.getFechaIngreso() != null) {
                preparedStatement.setDate(7, Date.valueOf(equipo.getFechaIngreso()));
            } else {
                preparedStatement.setNull(7, Types.DATE);
            }

            preparedStatement.setFloat(8, equipo.getDescuentoPorCondicion());
            preparedStatement.setString(9, equipo.getIdEquipo());

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            System.out.println("No se pudo actualizar el equipo: " + e.getMessage());
        }
    }

    public void borrar(String idEquipo) {
        // CORRECCIÓN: Se cambió id_equipo por IdEquipo
        final String sql = "DELETE FROM Equipo WHERE IdEquipo = ?";

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
        final String sql = "SELECT * FROM Equipo";

        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            while (resultSet.next()) {
                String idLaptop = resultSet.getString("id_laptop");
                Laptop laptopCascaron = null;

                // Evitamos un NullPointerException al buscar la laptop
                if (idLaptop != null) {
                    laptopCascaron = LaptopDAO.getInstance().encontrarPorId(idLaptop);
                    if(laptopCascaron == null) {
                        laptopCascaron = new Laptop(idLaptop); // Cascarón si la DB no la encuentra
                    }
                }

                // Extracción segura de la fecha
                LocalDate fecha = null;
                Date sqlDate = resultSet.getDate("fecha_ingreso");
                if (sqlDate != null) {
                    fecha = sqlDate.toLocalDate();
                }

                // CORRECCIÓN: resultSet.getString("IdEquipo")
                Equipo eq = new Equipo(
                        resultSet.getString("IdEquipo"),
                        laptopCascaron,
                        resultSet.getString("numero_serie"),
                        resultSet.getString("color"),
                        resultSet.getString("estado"),
                        resultSet.getString("disponibilidad"),
                        resultSet.getFloat("descuento_por_condicion"),
                        fecha,
                        resultSet.getString("id_detalle_orden")
                );
                equipos.add(eq);
            }
        } catch (SQLException e) {
            System.out.println("No se pudo obtener la lista de equipos: " + e.getMessage());
            e.printStackTrace(); // Esto te mostrará el error exacto si vuelve a fallar
        }
        return equipos;
    }

    public Equipo encontrarPorId(String idEquipo) {
        Equipo eq = null;
        final String sql ="SELECT * FROM Equipo WHERE IdEquipo = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, idEquipo);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    String idLaptop = resultSet.getString("id_laptop");
                    Laptop laptopCascaron = null;

                    if (idLaptop != null) {
                        laptopCascaron = LaptopDAO.getInstance().encontrarPorId(idLaptop);
                        if(laptopCascaron == null) {
                            laptopCascaron = new Laptop(idLaptop);
                        }
                    }

                    LocalDate fecha = null;
                    Date sqlDate = resultSet.getDate("fecha_ingreso");
                    if (sqlDate != null) {
                        fecha = sqlDate.toLocalDate();
                    }

                    eq = new Equipo(
                            resultSet.getString("IdEquipo"),
                            laptopCascaron,
                            resultSet.getString("numero_serie"),
                            resultSet.getString("color"),
                            resultSet.getString("estado"),
                            resultSet.getString("disponibilidad"),
                            resultSet.getFloat("descuento_por_condicion"),
                            fecha,
                            resultSet.getString("id_detalle_orden")
                    );
                }
            }
        } catch (SQLException e) {
            System.out.println("No se pudo obtener el equipo por ID: " + e.getMessage());
        }
        return eq;
    }
}