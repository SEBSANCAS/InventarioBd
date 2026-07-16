package DataBase;

import logico.Marca;
import java.sql.*;
import java.util.ArrayList;

public class MarcaDAO {

    public static final MarcaDAO INSTANCE = new MarcaDAO();

    private MarcaDAO() {}

    public static MarcaDAO getInstance() {
        return INSTANCE;
    }

    public void guardar(Marca marca) {
        final String sql = "INSERT INTO Marca (id_marca, nombre_marca) VALUES (?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, marca.getIdMarca());
            preparedStatement.setString(2, marca.getNombreMarca());

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            System.out.println("No se pudo guardar la marca: " + e.getMessage());
        }
    }

    public void actualizar(Marca marca) {
        final String sql = "UPDATE Marca SET nombre_marca=? WHERE id_marca=?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, marca.getNombreMarca());
            preparedStatement.setString(2, marca.getIdMarca());

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            System.out.println("No se pudo actualizar la marca: " + e.getMessage());
        }
    }

    public void borrar(String idMarca) {
        final String sql = "DELETE FROM Marca WHERE id_marca = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, idMarca);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            System.out.println("No se pudo eliminar la marca: " + e.getMessage());
        }
    }

    public ArrayList<Marca> EncontrarTodos() {
        ArrayList<Marca> marcas = new ArrayList<>();
        final String sql = "SELECT * FROM Marca";

        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            while (resultSet.next()) {
                Marca marca = new Marca(
                        resultSet.getString("id_marca"),
                        resultSet.getString("nombre_marca")
                );
                marcas.add(marca);
            }
        } catch (SQLException e) {
            System.out.println("No se pudo obtener la lista de marcas: " + e.getMessage());
        }
        return marcas;
    }

    public Marca encontrarPorId(String idMarca) {
        Marca marca = null;
        final String sql = "SELECT * FROM Marca WHERE id_marca = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, idMarca);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    marca = new Marca(
                            resultSet.getString("id_marca"),
                            resultSet.getString("nombre_marca")
                    );
                }
            }
        } catch (SQLException e) {
            System.out.println("No se pudo obtener la marca por ID: " + e.getMessage());
        }
        return marca;
    }
}