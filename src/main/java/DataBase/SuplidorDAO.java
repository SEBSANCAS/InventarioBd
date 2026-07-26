package DataBase;

import logico.Suplidor;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class SuplidorDAO {

    public static final SuplidorDAO INSTANCE = new SuplidorDAO();

    private SuplidorDAO() {}

    public static SuplidorDAO getInstance() {
        return INSTANCE;
    }

    public void guardar(Suplidor suplidor) {
        // Se corrigió id_calle -> id_ciudad
        final String sql = "INSERT INTO Suplidor (id_suplidor, numero_identificacion, razon_social, nombre_comercial, correo_electronico, id_ciudad, fecha_registro, tipo_identificacion) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, suplidor.getIdSuplidor());
            preparedStatement.setString(2, suplidor.getNumeroIdentificacion());
            preparedStatement.setString(3, suplidor.getRazonComercial());
            preparedStatement.setString(4, suplidor.getNombreComercial());
            preparedStatement.setString(5, suplidor.getCorreo());
            preparedStatement.setString(6, suplidor.getIdCalle()); // Pasa el id_ciudad guardado en el objeto
            preparedStatement.setObject(7, LocalDate.now());
            preparedStatement.setString(8, suplidor.getTipoIdentificacion());

            preparedStatement.executeUpdate();

            if (suplidor.getTelefonos() != null && !suplidor.getTelefonos().isEmpty()) {
                TelefonoDAO.getInstance().guardarTodosPorSuplidor(suplidor.getTelefonos(), suplidor.getIdSuplidor());
            }

            if (suplidor.getLaptopsSuplidor() != null && !suplidor.getLaptopsSuplidor().isEmpty()) {
                SuplidorLaptopDAO.getInstance().guardarLaptopsPorSuplidor(suplidor.getIdSuplidor(), suplidor.getLaptopsSuplidor());
            }

        } catch (SQLException e) {
            System.out.println("No se pudo guardar el suplidor: " + e.getMessage());
        }
    }

    public void actualizar(Suplidor suplidor) {
        // Se corrigió id_calle -> id_ciudad
        final String sql = "UPDATE Suplidor SET numero_identificacion=?, razon_social=?, nombre_comercial=?, correo_electronico=?, id_ciudad=?, tipo_identificacion=? WHERE id_suplidor=?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, suplidor.getNumeroIdentificacion());
            preparedStatement.setString(2, suplidor.getRazonComercial());
            preparedStatement.setString(3, suplidor.getNombreComercial());
            preparedStatement.setString(4, suplidor.getCorreo());
            preparedStatement.setString(5, suplidor.getIdCalle()); // Pasa el id_ciudad
            preparedStatement.setString(6, suplidor.getTipoIdentificacion());
            preparedStatement.setString(7, suplidor.getIdSuplidor());

            preparedStatement.executeUpdate();

            if (suplidor.getLaptopsSuplidor() != null) {
                SuplidorLaptopDAO.getInstance().actualizarLaptopsPorSuplidor(suplidor.getIdSuplidor(), suplidor.getLaptopsSuplidor());
            }

        } catch (SQLException e) {
            System.out.println("No se pudo actualizar el suplidor: " + e.getMessage());
        }
    }

    public void borrar(String idSuplidor) {
        final String sql = "DELETE FROM Suplidor WHERE id_suplidor = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, idSuplidor);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            System.out.println("No se pudo eliminar el suplidor: " + e.getMessage());
        }
    }

    public ArrayList<Suplidor> EncontrarTodos() {
        ArrayList<Suplidor> suplidores = new ArrayList<>();
        final String sql = "SELECT * FROM Suplidor";

        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            while (resultSet.next()) {
                Suplidor suplidor = new Suplidor(
                        resultSet.getString("numero_identificacion"),
                        resultSet.getString("correo_electronico"),
                        resultSet.getString("nombre_comercial"),
                        resultSet.getString("id_suplidor"),
                        resultSet.getString("razon_social"),
                        resultSet.getString("id_ciudad"), // Se lee desde la columna id_ciudad de la BD
                        resultSet.getObject("fecha_registro", LocalDate.class),
                        resultSet.getString("tipo_identificacion")
                );
                suplidor.setTelefonos(TelefonoDAO.getInstance().encontrarPorSuplidor(suplidor.getIdSuplidor()));
                suplidor.setLaptopsSuplidor(SuplidorLaptopDAO.getInstance().encontrarPorSuplidor(suplidor.getIdSuplidor()));
                suplidores.add(suplidor);
            }
        } catch (SQLException e) {
            System.out.println("No se pudo obtener la lista de suplidores: " + e.getMessage());
        }
        return suplidores;
    }

    public Suplidor encontrarPorId(String idSuplidor) {
        Suplidor suplidor = null;
        final String sql = "SELECT * FROM Suplidor WHERE id_suplidor = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, idSuplidor);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    suplidor = new Suplidor(
                            resultSet.getString("numero_identificacion"),
                            resultSet.getString("correo_electronico"),
                            resultSet.getString("nombre_comercial"),
                            resultSet.getString("id_suplidor"),
                            resultSet.getString("razon_social"),
                            resultSet.getString("id_ciudad"), // Se lee desde la columna id_ciudad de la BD
                            resultSet.getObject("fecha_registro", LocalDate.class),
                            resultSet.getString("tipo_identificacion")
                    );
                    suplidor.setTelefonos(TelefonoDAO.getInstance().encontrarPorSuplidor(idSuplidor));
                    suplidor.setLaptopsSuplidor(SuplidorLaptopDAO.getInstance().encontrarPorSuplidor(idSuplidor));
                }
            }
        } catch (SQLException e) {
            System.out.println("No se pudo obtener el suplidor por ID: " + e.getMessage());
        }
        return suplidor;
    }
}