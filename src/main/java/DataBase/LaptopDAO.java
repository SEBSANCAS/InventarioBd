package DataBase;

import logico.Laptop;
import logico.Marca;
import java.sql.*;
import java.util.ArrayList;

public class LaptopDAO {

    public static final LaptopDAO INSTANCE = new LaptopDAO();

    private LaptopDAO() {}

    public static LaptopDAO getInstance() {
        return INSTANCE;
    }

    public void guardar(Laptop laptop) {
        final String sql = "INSERT INTO Laptop (id_laptop, numero_modelo, nombre_comercial, id_marca, peso, procesador, gpu, tipo_ram, cantidad_ram, tipo_almacenamiento, capacidad_almacenamiento, meses_garantia, tamano_pantalla, resolucion_pantalla, precio_venta_detalle, precio_venta_mayorista, precio_compra_promedio, cantidad_minima_mayorista, cantidad_alerta_stock, stock) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            String idMarca = null;
            if (laptop.getMarca() != null) {
                idMarca = laptop.getMarca().getIdMarca();
            }

            preparedStatement.setString(1, laptop.getIdLaptop());
            preparedStatement.setString(2, laptop.getNumeroModelo());
            preparedStatement.setString(3, laptop.getNombreComercial());
            preparedStatement.setString(4, idMarca);
            preparedStatement.setFloat(5, laptop.getPeso());
            preparedStatement.setString(6, laptop.getProcesador());
            preparedStatement.setString(7, laptop.getGpu());
            preparedStatement.setString(8, laptop.getTipoRam());
            preparedStatement.setFloat(9, laptop.getCantidadRam());
            preparedStatement.setString(10, laptop.getTipoAlmacenamiento());
            preparedStatement.setFloat(11, laptop.getCantidadAlmacenamiento());
            preparedStatement.setInt(12, laptop.getMesesGarantia());
            preparedStatement.setFloat(13, laptop.getTamanyoPantalla());
            preparedStatement.setString(14, laptop.getResolucionPantalla());
            preparedStatement.setFloat(15, laptop.getPrecioDetalle());
            preparedStatement.setFloat(16, laptop.getPrecioMayorista());
            preparedStatement.setFloat(17, laptop.getCostoPromedioCompra());
            preparedStatement.setInt(18, laptop.getCantMinMayorista());
            preparedStatement.setInt(19, laptop.getCantidadAlerta());
            preparedStatement.setInt(20, laptop.getStockActual());

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            System.out.println("No se pudo guardar la laptop: " + e.getMessage());
        }
    }

    public void actualizar(Laptop laptop) {
        final String sql = "UPDATE Laptop SET numero_modelo=?, nombre_comercial=?, id_marca=?, peso=?, procesador=?, gpu=?, tipo_ram=?, cantidad_ram=?, tipo_almacenamiento=?, capacidad_almacenamiento=?, meses_garantia=?, tamano_pantalla=?, resolucion_pantalla=?, precio_venta_detalle=?, precio_venta_mayorista=?, precio_compra_promedio=?, cantidad_minima_mayorista=?, cantidad_alerta_stock=?, stock=? WHERE id_laptop=?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            String idMarca = null;
            if (laptop.getMarca() != null) {
                idMarca = laptop.getMarca().getIdMarca();
            }

            preparedStatement.setString(1, laptop.getNumeroModelo());
            preparedStatement.setString(2, laptop.getNombreComercial());
            preparedStatement.setString(3, idMarca);
            preparedStatement.setFloat(4, laptop.getPeso());
            preparedStatement.setString(5, laptop.getProcesador());
            preparedStatement.setString(6, laptop.getGpu());
            preparedStatement.setString(7, laptop.getTipoRam());
            preparedStatement.setFloat(8, laptop.getCantidadRam());
            preparedStatement.setString(9, laptop.getTipoAlmacenamiento());
            preparedStatement.setFloat(10, laptop.getCantidadAlmacenamiento());
            preparedStatement.setInt(11, laptop.getMesesGarantia());
            preparedStatement.setFloat(12, laptop.getTamanyoPantalla());
            preparedStatement.setString(13, laptop.getResolucionPantalla());
            preparedStatement.setFloat(14, laptop.getPrecioDetalle());
            preparedStatement.setFloat(15, laptop.getPrecioMayorista());
            preparedStatement.setFloat(16, laptop.getCostoPromedioCompra());
            preparedStatement.setInt(17, laptop.getCantMinMayorista());
            preparedStatement.setInt(18, laptop.getCantidadAlerta());
            preparedStatement.setInt(19, laptop.getStockActual());
            preparedStatement.setString(20, laptop.getIdLaptop());

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            System.out.println("No se pudo actualizar la laptop: " + e.getMessage());
        }
    }

    public void borrar(String idLaptop) {
        final String sql = "DELETE FROM Laptop WHERE id_laptop = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, idLaptop);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            System.out.println("No se pudo eliminar la laptop: " + e.getMessage());
        }
    }

    public ArrayList<Laptop> EncontrarTodos() {
        ArrayList<Laptop> laptops = new ArrayList<>();
        final String sql = "SELECT * FROM Laptop";

        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            while (resultSet.next()) {
                Marca marcaCascaron = null;
                String idMarca = resultSet.getString("id_marca");
                if (idMarca != null) {
                    marcaCascaron = new Marca(idMarca, "");
                }

                Laptop laptop = new Laptop(
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
                        resultSet.getFloat("precio_compra_promedio"),
                        resultSet.getFloat("precio_venta_detalle"),
                        resultSet.getFloat("precio_venta_mayorista"),
                        resultSet.getInt("cantidad_minima_mayorista"),
                        resultSet.getInt("cantidad_alerta_stock"),
                        resultSet.getInt("stock"),
                        resultSet.getInt("meses_garantia")
                );
                laptops.add(laptop);
            }
        } catch (SQLException e) {
            System.out.println("No se pudo obtener la lista de laptops: " + e.getMessage());
        }
        return laptops;
    }

    public ArrayList<Laptop> listarPorSuplidor(String idSuplidor) {
        ArrayList<Laptop> laptops = new ArrayList<>();

        final String sql = "SELECT DISTINCT l.* FROM Laptop l " +
                "INNER JOIN Detalle_Orden_Compra doc ON l.id_laptop = doc.id_laptop " +
                "INNER JOIN Orden_Compra oc ON doc.id_orden_compra = oc.id_orden_compra " +
                "WHERE oc.id_suplidor = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, idSuplidor);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {

                while (resultSet.next()) {

                    Marca marcaCascaron = null;
                    String idMarca = resultSet.getString("id_marca");

                    if (idMarca != null) {
                        marcaCascaron = new Marca(idMarca, "");
                    }

                    Laptop laptop = new Laptop(
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
                            resultSet.getFloat("precio_compra_promedio"),
                            resultSet.getFloat("precio_venta_detalle"),
                            resultSet.getFloat("precio_venta_mayorista"),
                            resultSet.getInt("cantidad_minima_mayorista"),
                            resultSet.getInt("cantidad_alerta_stock"),
                            resultSet.getInt("stock"),
                            resultSet.getInt("meses_garantia")
                    );

                    laptops.add(laptop);
                }
            }

        } catch (SQLException e) {
            System.out.println("No se pudieron obtener las laptops del suplidor: " + e.getMessage());
        }

        return laptops;
    }

    public Laptop encontrarPorId(String idLaptop) {
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
                            resultSet.getFloat("precio_compra_promedio"),
                            resultSet.getFloat("precio_venta_detalle"),
                            resultSet.getFloat("precio_venta_mayorista"),
                            resultSet.getInt("cantidad_minima_mayorista"),
                            resultSet.getInt("cantidad_alerta_stock"),
                            resultSet.getInt("stock"),
                            resultSet.getInt("meses_garantia")
                    );
                }
            }
        } catch (SQLException e) {
            System.out.println("No se pudo obtener la laptop por ID: " + e.getMessage());
        }
        return laptop;
    }
}