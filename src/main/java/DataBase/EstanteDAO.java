package DataBase;

import logico.Estante;
import java.sql.*;
import java.util.ArrayList;

public class EstanteDAO {

    public static final EstanteDAO INSTANCE = new EstanteDAO();

    private EstanteDAO() {}

    public static EstanteDAO getInstance() {
        return INSTANCE;
    }

    public String obtenerIdUbicacion(String codigoEstante, int nivel) {
        String idUbicacion = null;
        final String sql = "SELECT id_ubicacion FROM Ubicacion_Almacen WHERE codigo_estante = ? AND nivel_estante = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, codigoEstante);
            preparedStatement.setString(2, String.valueOf(nivel)); // En BD es varchar

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    idUbicacion = resultSet.getString("id_ubicacion");
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener ID de ubicación: " + e.getMessage());
        }
        return idUbicacion;
    }

    public void guardar(Estante estante) {
        final String sql = "INSERT INTO Ubicacion_Almacen (id_ubicacion, codigo_estante, nivel_estante, capacidad_maxima) VALUES (?, ?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            int capacidadPorNivel = estante.getCapacidad() / estante.getCantidadNiveles();
            if (capacidadPorNivel == 0) capacidadPorNivel = 1;

            for (int i = 1; i <= estante.getCantidadNiveles(); i++) {
                String idUbicacion = estante.getIdEstante() + "-N" + i;

                preparedStatement.setString(1, idUbicacion);
                preparedStatement.setString(2, estante.getIdEstante());
                preparedStatement.setString(3, String.valueOf(i));
                preparedStatement.setInt(4, capacidadPorNivel);

                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            System.out.println("No se pudo guardar el estante: " + e.getMessage());
        }
    }

    public ArrayList<Estante> EncontrarTodos() {
        ArrayList<Estante> estantes = new ArrayList<>();
        final String sql = "SELECT codigo_estante, COUNT(*) as niveles, SUM(capacidad_maxima) as cap_total FROM Ubicacion_Almacen GROUP BY codigo_estante";

        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            while (resultSet.next()) {
                String idEstante = resultSet.getString("codigo_estante");
                int niveles = resultSet.getInt("niveles");
                int capacidadTotal = resultSet.getInt("cap_total");

                Estante e = new Estante(idEstante, capacidadTotal, niveles, "Almacén Principal");
                estantes.add(e);
            }
        } catch (SQLException e) {
            System.out.println("No se pudo obtener la lista de estantes: " + e.getMessage());
        }
        return estantes;
    }

    public void borrar(String idEstante) {
        final String sql = "DELETE FROM Ubicacion_Almacen WHERE codigo_estante = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, idEstante);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            System.out.println("No se pudo eliminar el estante: " + e.getMessage());
        }
    }
}