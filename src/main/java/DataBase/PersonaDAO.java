package DataBase;

import logico.Persona;
import java.sql.*;
import java.util.ArrayList;

public class PersonaDAO {

    public static final PersonaDAO INSTANCE = new PersonaDAO();

    private PersonaDAO() {}

    public static PersonaDAO getInstance() {
        return INSTANCE;
    }

    public void guardar(Persona persona) {
        final String sql = "INSERT INTO Persona (numero_identificacion, correo, telefono) VALUES (?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, persona.getNumeroIdentificacion());
            preparedStatement.setString(2, persona.getCorreo());
            preparedStatement.setString(3, persona.getTelefono());

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            System.out.println("No se pudo guardar la persona: " + e.getMessage());
        }
    }

    public void actualizar(Persona persona) {
        final String sql = "UPDATE Persona SET correo=?, telefono=? WHERE numero_identificacion=?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, persona.getCorreo());
            preparedStatement.setString(2, persona.getTelefono());
            preparedStatement.setString(3, persona.getNumeroIdentificacion());

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            System.out.println("No se pudo actualizar la persona: " + e.getMessage());
        }
    }

    public void borrar(String numeroIdentificacion) {
        final String sql = "DELETE FROM Persona WHERE numero_identificacion = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, numeroIdentificacion);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            System.out.println("No se pudo eliminar la persona: " + e.getMessage());
        }
    }

    public ArrayList<Persona> EncontrarTodos() {
        ArrayList<Persona> personas = new ArrayList<>();
        final String sql = "SELECT * FROM Persona";

        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            while (resultSet.next()) {
              Persona persona = new Persona(
                        resultSet.getString("numero_identificacion"),
                        resultSet.getString("correo"),
                        resultSet.getString("telefono")
                ) {};
                personas.add(persona);
            }
        } catch (SQLException e) {
            System.out.println("No se pudo obtener la lista de personas: " + e.getMessage());
        }
        return personas;
    }

    public Persona encontrarPorId(String numeroIdentificacion) {
        Persona persona = null;
        final String sql = "SELECT * FROM Persona WHERE numero_identificacion = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, numeroIdentificacion);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    persona = new Persona(
                            resultSet.getString("numero_identificacion"),
                            resultSet.getString("correo"),
                            resultSet.getString("telefono")
                    ) {};
                }
            }
        } catch (SQLException e) {
            System.out.println("No se pudo obtener la persona por ID: " + e.getMessage());
        }
        return persona;
    }
}