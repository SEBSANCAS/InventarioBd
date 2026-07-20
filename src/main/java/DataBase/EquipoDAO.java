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
        final String sql = "INSERT INTO Equipo (id_equipo, numero_serie, id_laptop, id_ubicacion, id_detalle_orden, estado, disponibilidad, color, fecha_ingreso, descuento_por_condicion) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            String idUbicacion = EstanteDAO.getInstance().obtenerPrimeraUbicacionDisponible();
            if (idUbicacion == null) {
                throw new RuntimeException("No hay espacio disponible en el almacén.");
            }

            preparedStatement.setString(1, equipo.getIdEquipo());
            preparedStatement.setString(2, equipo.getNumeroSerie());
            preparedStatement.setString(3, equipo.getLaptop().getIdLaptop());
            preparedStatement.setString(4, idUbicacion);
            preparedStatement.setString(5, equipo.getIdAdquisicionOrigen());
            preparedStatement.setString(6, equipo.getEstado());
            preparedStatement.setString(7, equipo.getDisponibilidad());
            preparedStatement.setString(8, equipo.getColor());
            preparedStatement.setObject(9, equipo.getFechaIngreso());
            preparedStatement.setFloat(10, equipo.getDescuentoPorCondicion());

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            System.out.println("No se pudo guardar el equipo: " + e.getMessage());
        }
    }

    public void actualizar(Equipo equipo) {

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
                        "WHERE id_equipo=?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, equipo.getNumeroSerie());
            preparedStatement.setString(2, equipo.getLaptop().getIdLaptop());
            preparedStatement.setString(3, equipo.getIdAdquisicionOrigen());
            preparedStatement.setString(4, equipo.getEstado());
            preparedStatement.setString(5, equipo.getDisponibilidad());
            preparedStatement.setString(6, equipo.getColor());
            preparedStatement.setObject(7, equipo.getFechaIngreso());
            preparedStatement.setFloat(8, equipo.getDescuentoPorCondicion());
            preparedStatement.setString(9, equipo.getIdEquipo());

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

    public ArrayList<Equipo> encontrarPorEstado(String estado) {

        ArrayList<Equipo> equipos = new ArrayList<>();

        final String sql = "SELECT * FROM Equipo WHERE estado = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, estado);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {

                while (resultSet.next()) {

                    Laptop laptop = LaptopDAO.getInstance()
                            .encontrarPorId(resultSet.getString("id_laptop"));

                    Equipo equipo = new Equipo(
                            resultSet.getString("id_equipo"),
                            laptop,
                            resultSet.getString("numero_serie"),
                            resultSet.getString("color"),
                            resultSet.getString("estado"),
                            resultSet.getString("disponibilidad"),
                            resultSet.getFloat("descuento_por_condicion"),
                            resultSet.getObject("fecha_ingreso", LocalDate.class),
                            resultSet.getString("id_detalle_orden")
                    );

                    equipos.add(equipo);
                }
            }

        } catch (SQLException e) {
            System.out.println("No se pudieron obtener los equipos: " + e.getMessage());
        }

        return equipos;
    }
    public ArrayList<Equipo> encontrarPorDisponibilidad(String disponibilidad) {

        ArrayList<Equipo> equipos = new ArrayList<>();

        final String sql = "SELECT * FROM Equipo WHERE disponibilidad = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, disponibilidad);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {

                while (resultSet.next()) {

                    Laptop laptop = LaptopDAO.getInstance()
                            .encontrarPorId(resultSet.getString("id_laptop"));

                    Equipo equipo = new Equipo(
                            resultSet.getString("id_equipo"),
                            laptop,
                            resultSet.getString("numero_serie"),
                            resultSet.getString("color"),
                            resultSet.getString("estado"),
                            resultSet.getString("disponibilidad"),
                            resultSet.getFloat("descuento_por_condicion"),
                            resultSet.getObject("fecha_ingreso", LocalDate.class),
                            resultSet.getString("id_detalle_orden")
                    );

                    equipos.add(equipo);
                }
            }

        } catch (SQLException e) {
            System.out.println("No se pudieron obtener los equipos: " + e.getMessage());
        }

        return equipos;
    }
    public ArrayList<Equipo> EncontrarTodos() {
        ArrayList<Equipo> equipos = new ArrayList<>();

        final String sql = "SELECT * FROM Equipo";

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
                        resultSet.getString("estado"),
                        resultSet.getString("disponibilidad"),
                        resultSet.getFloat("descuento_por_condicion"),
                        resultSet.getObject("fecha_ingreso", LocalDate.class),
                        resultSet.getString("id_detalle_orden")
                );
                equipos.add(eq);
            }
        } catch (SQLException e) {
            System.out.println("No se pudo obtener la lista de equipos: " + e.getMessage());
        }
        return equipos;
    }

    public Equipo encontrarPorId(String idEquipo) {
        Equipo eq = null;
        final String sql ="SELECT * FROM Equipo WHERE id_equipo = ?";

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
                            resultSet.getString("estado"),
                            resultSet.getString("disponibilidad"),
                            resultSet.getFloat("descuento_por_condicion"),
                            resultSet.getObject("fecha_ingreso", LocalDate.class),
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