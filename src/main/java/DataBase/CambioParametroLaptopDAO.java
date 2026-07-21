package DataBase;

import logico.Laptop;
import logico.Marca;

import java.sql.*;
import java.time.LocalDate;

public class CambioParametroLaptopDAO {

    public static final CambioParametroLaptopDAO INSTANCE = new CambioParametroLaptopDAO();

    private CambioParametroLaptopDAO() {}

    public static CambioParametroLaptopDAO getInstance() {
        return INSTANCE;
    }

    public void guardar(String idCambio, String idLaptop, String campoModificado, String valorAnterior, String valorNuevo, String descripcionCambio) {
        final String sql = "INSERT INTO Cambio_Parametro_Laptop (id_cambio, id_laptop, campo_modificado, valor_anterior, valor_nuevo, fecha_cambio, descripcion_cambio) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, idCambio);
            preparedStatement.setString(2, idLaptop);
            preparedStatement.setString(3, campoModificado);
            preparedStatement.setString(4, valorAnterior);
            preparedStatement.setString(5, valorNuevo);
            preparedStatement.setObject(6, LocalDate.now());
            preparedStatement.setString(7, descripcionCambio);

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            System.out.println("No se pudo guardar el cambio de parametro: " + e.getMessage());
        }
    }

    public void actualizar(String idCambio, String campoModificado, String valorAnterior, String valorNuevo, String descripcionCambio) {
        final String sql = "UPDATE Cambio_Parametro_Laptop SET campo_modificado=?, valor_anterior=?, valor_nuevo=?, descripcion_cambio=? WHERE id_cambio=?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, campoModificado);
            preparedStatement.setString(2, valorAnterior);
            preparedStatement.setString(3, valorNuevo);
            preparedStatement.setString(4, descripcionCambio);
            preparedStatement.setString(5, idCambio);

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            System.out.println("No se pudo actualizar el cambio de parametro: " + e.getMessage());
        }
    }
    public Laptop buscarLaptop(String idLaptop) {
        Laptop laptop = null;

        final String sql = "SELECT * FROM Laptop WHERE id_laptop = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, idLaptop);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {

                if (resultSet.next()) {

                    Marca marcaCascaron = null;
                    String idMarca = resultSet.getString("id_marca");

                    if (idMarca != null) {
                        marcaCascaron = new Marca(idMarca, "");
                    }

                    laptop = new Laptop(
                            resultSet.getString("id_laptop"),
                            resultSet.getString("numero_modelo"),
                            resultSet.getString("nombre_comercial"),
                            marcaCascaron,
                            resultSet.getFloat("peso"),
                            resultSet.getString("procesador"),
                            resultSet.getString("gpu"),
                            resultSet.getString("tipo_ram"),
                            resultSet.getFloat("cantidad_ram"),
                            resultSet.getString("tipo_almacenamiento"),
                            resultSet.getFloat("capacidad_almacenamiento"),
                            resultSet.getFloat("tamano_pantalla"),
                            resultSet.getString("resolucion_pantalla"),
                            0f,
                            resultSet.getFloat("precio_venta_detalle"),
                            resultSet.getFloat("precio_venta_mayorista"),
                            resultSet.getInt("cantidad_minima_mayorista"),
                            resultSet.getInt("cantidad_alerta_stock"),
                            resultSet.getInt("stock_actual"),
                            resultSet.getInt("meses_garantia")
                    );
                }

            }

        } catch (SQLException e) {
            System.out.println("No se pudo obtener la laptop: " + e.getMessage());
        }

        return laptop;
    }
    public void borrar(String idCambio) {
        final String sql = "DELETE FROM Cambio_Parametro_Laptop WHERE id_cambio = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, idCambio);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            System.out.println("No se pudo eliminar el cambio de parametro: " + e.getMessage());
        }
    }
}