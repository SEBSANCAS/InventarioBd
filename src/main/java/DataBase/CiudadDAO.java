package DataBase;

import logico.Ciudad;
import java.sql.*;
import java.util.ArrayList;

public class CiudadDAO {

    public static final CiudadDAO INSTANCE = new CiudadDAO();

    private CiudadDAO() {}

    public static CiudadDAO getInstance() {
        return INSTANCE;
    }

    public ArrayList<Ciudad> EncontrarTodas() {
        ArrayList<Ciudad> ciudades = new ArrayList<>();
        final String sql = "SELECT * FROM Ciudad ORDER BY nombre_ciudad ASC";

        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            while (resultSet.next()) {
                Ciudad ciudad = new Ciudad(
                        resultSet.getString("id_ciudad"),
                        resultSet.getString("id_pais"),
                        resultSet.getString("nombre_ciudad"),
                        resultSet.getDouble("latitud"),
                        resultSet.getDouble("longitud")
                );
                ciudades.add(ciudad);
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener ciudades de la base de datos: " + e.getMessage());
        }
        return ciudades;
    }

    public Ciudad encontrarPorId(String idCiudad) {
        Ciudad ciudad = null;
        final String sql = "SELECT * FROM Ciudad WHERE id_ciudad = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, idCiudad);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    ciudad = new Ciudad(
                            resultSet.getString("id_ciudad"),
                            resultSet.getString("id_pais"),
                            resultSet.getString("nombre_ciudad"),
                            resultSet.getDouble("latitud"),
                            resultSet.getDouble("longitud")
                    );
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener la ciudad por ID: " + e.getMessage());
        }
        return ciudad;
    }
}