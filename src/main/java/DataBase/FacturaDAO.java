package DataBase;

import logico.Cliente;
import logico.Factura;
import java.sql.*;
import java.time.LocalDate;
import java.util.*;

public class FacturaDAO {

    public static final FacturaDAO INSTANCE = new FacturaDAO();

    private FacturaDAO() {

    }

    public static FacturaDAO getInstance() {
        return INSTANCE;
    }

    public void guardar(Factura factura){
        final String sql = "INSERT INTO Factura (id_factura, numero_comprobante, id_cliente, fecha_emision, subtotal, impuestos, monto_total) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try(Connection connection = DatabaseConnection.getConnection()){
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, factura.getIdFactura());
            preparedStatement.setString(2, factura.getNumeroComprobante());
            preparedStatement.setString(3,factura.getCliente().getIdCliente());
            preparedStatement.setObject(4,factura.getFechaEmision());
            preparedStatement.setFloat(5,factura.getSubtotal());
            preparedStatement.setFloat(6,factura.getImpuestos());
            preparedStatement.setFloat(7,factura.getMontoTotal());
            preparedStatement.executeUpdate();

        }catch(SQLException e) {
            System.out.println("No se pudo guardar la factura: " + e.getMessage());
        }
    }

    public void actualizar(Factura factura){
        final String sql = "UPDATE Factura set numero_comprobante=?, id_cliente=?,fecha_emision=?,subtotal=?,impuestos=?,monto_total=? WHERE id_factura = ?";
        try(Connection connection = DatabaseConnection.getConnection()){
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, factura.getNumeroComprobante());
            preparedStatement.setString(2,factura.getCliente().getIdCliente());
            preparedStatement.setObject(3,factura.getFechaEmision());
            preparedStatement.setFloat(4,factura.getSubtotal());
            preparedStatement.setFloat(5,factura.getImpuestos());
            preparedStatement.setFloat(6,factura.getMontoTotal());
            preparedStatement.setString(7, factura.getIdFactura());
            preparedStatement.executeUpdate();

        }catch(SQLException e) {
            System.out.println("No se pudo actualizar la factura: " + e.getMessage());
        }
    }

    public void borrar(String id) {
        final String sql = "DELETE FROM Factura WHERE id_factura = ? ";
        try (Connection connection = DatabaseConnection.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setObject(1, id);
            preparedStatement.executeUpdate();

        } catch(SQLException e) {
            System.out.println("No se pudo eliminar la factura: " +  e.getMessage());
        }
    }

    public ArrayList<Factura> EncontrarTodos(){
        ArrayList<Factura> facturas = new ArrayList<>();
        final String sql = "SELECT * FROM Factura";

        try(Connection connection = DatabaseConnection.getConnection()){
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            while(resultSet.next()){
                Factura f = new Factura();
                f.setIdFactura(resultSet.getString("id_factura"));
                f.setNumeroComprobante(resultSet.getString("numero_comprobante"));
                f.setFechaEmision(resultSet.getObject("fecha_emision", LocalDate.class));
                f.setSubtotal(resultSet.getFloat("subtotal"));
                f.setImpuestos(resultSet.getFloat("impuestos"));
                f.setMontoTotal(resultSet.getFloat("monto_total"));

                Cliente c = new Cliente();
                c.setIdCliente(resultSet.getString("id_cliente"));
                f.setCliente(c);

                facturas.add(f);
            }

        }catch(SQLException e) {
            System.out.println("No se pudo obtener la lista de facturas: " + e.getMessage());
        }
        return facturas;
    }

    public Factura encontrarPorId(String id) {
        Factura factura = null;
        final String sql = "SELECT * FROM Factura WHERE id_factura = ?";
        try (Connection connection = DatabaseConnection.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, id);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    factura = new Factura();
                    factura.setIdFactura(resultSet.getString("id_factura"));
                    factura.setNumeroComprobante(resultSet.getString("numero_comprobante"));
                    factura.setFechaEmision(resultSet.getObject("fecha_emision", LocalDate.class));
                    factura.setSubtotal(resultSet.getFloat("subtotal"));
                    factura.setImpuestos(resultSet.getFloat("impuestos"));
                    factura.setMontoTotal(resultSet.getFloat("monto_total"));

                    Cliente c = new Cliente();
                    c.setIdCliente(resultSet.getString("id_cliente"));
                    factura.setCliente(c);
                }
            }

        } catch (SQLException e) {
            System.out.println("No se pudo obtener la factura por ID: " + e.getMessage());
        }
        return factura;
    }

}
