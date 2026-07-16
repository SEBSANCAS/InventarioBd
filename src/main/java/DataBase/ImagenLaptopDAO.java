package DataBase;

import logico.ImagenLaptop;
import logico.Laptop;
import java.sql.*;
import java.util.ArrayList;

public class ImagenLaptopDAO {

    public static final ImagenLaptopDAO INSTANCE = new ImagenLaptopDAO();

    private ImagenLaptopDAO() {}

    public static ImagenLaptopDAO getInstance() {
        return INSTANCE;
    }

    public void guardar(ImagenLaptop imagenLaptop) {
        final String sql = "INSERT INTO ImagenLaptop (IdLaptop, Imagen) VALUES (?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, imagenLaptop.getLaptop().getIdLaptop());
            preparedStatement.setBytes(2, imagenLaptop.getImagen());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("No se pudo guardar la imagen: " + e.getMessage());
        }
    }

    public void borrar(int idImagen) {
        final String sql = "DELETE FROM ImagenLaptop WHERE IdImagen=?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, idImagen);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("No se pudo eliminar la imagen: " + e.getMessage());
        }
    }

    public ArrayList<ImagenLaptop> encontrarPorLaptop(String idLaptop) {
        ArrayList<ImagenLaptop> imagenes = new ArrayList<>();
        final String sql = "SELECT * FROM ImagenLaptop WHERE IdLaptop=?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, idLaptop);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                Laptop laptop = LaptopDAO.getInstance().encontrarPorId(idLaptop);
                while (resultSet.next()) {
                    imagenes.add(new ImagenLaptop(
                            resultSet.getInt("IdImagen"),
                            laptop,
                            resultSet.getBytes("Imagen")
                    ));
                }
            }

        } catch (SQLException e) {
            System.out.println("No se pudieron obtener las imágenes: " + e.getMessage());
        }
        return imagenes;
    }
}
