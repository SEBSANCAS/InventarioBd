package DataBase;

import logico.Servicio;
import java.sql.*;

public class ServicioDAO {

    public static final ServicioDAO INSTANCE = new ServicioDAO();

    private ServicioDAO() {}

    public static ServicioDAO getInstance() {
        return INSTANCE;
    }

    public void guardarContadores(Servicio servicio) {
        final String sql = "UPDATE ControlContadores SET " +
                "gen_cliente = ?, gen_marca = ?, gen_suplidor = ?, gen_estante = ?, " +
                "gen_laptop = ?, gen_equipo = ?, gen_adquisicion = ?, gen_factura = ?, " +
                "gen_detalle_adquisicion = ?, gen_detalle_factura = ? WHERE id = 1";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, servicio.getSiguienteIdCliente() - 1);
            preparedStatement.setInt(2, servicio.getSiguienteIdMarca() - 1);
            preparedStatement.setInt(3, servicio.getSiguienteIdSuplidor() - 1);
            preparedStatement.setInt(4, servicio.getSiguienteIdEstante() - 1);
            preparedStatement.setInt(5, servicio.getSiguienteIdLaptop() - 1);
            preparedStatement.setInt(6, servicio.getSiguienteIdEquipo() - 1);
            preparedStatement.setInt(7, servicio.getSiguienteIdAdquisicion() - 1);
            preparedStatement.setInt(8, servicio.getSiguienteIdFactura() - 1);
            preparedStatement.setInt(9, servicio.getSiguienteIdDetalleAdquisicion() - 1);
            preparedStatement.setInt(10, servicio.getSiguienteIdDetalleFactura() - 1);

            int filasAfectadas = preparedStatement.executeUpdate();

            if (filasAfectadas == 0) {
                final String insertSql = "INSERT INTO ControlContadores (id, gen_cliente, gen_marca, gen_suplidor, gen_estante, gen_laptop, gen_equipo, gen_adquisicion, gen_factura, gen_detalle_adquisicion, gen_detalle_factura) VALUES (1, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                try (PreparedStatement insertStatement = connection.prepareStatement(insertSql)) {
                    insertStatement.setInt(1, 1);
                    insertStatement.setInt(2, 1);
                    insertStatement.setInt(3, 1);
                    insertStatement.setInt(4, 1);
                    insertStatement.setInt(5, 1);
                    insertStatement.setInt(6, 1);
                    insertStatement.setInt(7, 1);
                    insertStatement.setInt(8, 1);
                    insertStatement.setInt(9, 1);
                    insertStatement.setInt(10, 1);
                    insertStatement.executeUpdate();
                }
            }

        } catch (SQLException e) {
            System.out.println("No se pudieron guardar los contadores del servicio: " + e.getMessage());
        }
    }

    public void cargarContadores(Servicio servicio) {
        final String sql = "SELECT * FROM ControlContadores WHERE id = 1";

        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            if (resultSet.next()) {

            }
        } catch (SQLException e) {
            System.out.println("No se pudieron cargar los contadores del servicio: " + e.getMessage());
        }
    }

    public void borrarContadores() {
        final String sql = "DELETE FROM ControlContadores WHERE id = 1";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("No se pudieron eliminar los contadores: " + e.getMessage());
        }
    }

    public void cargarTodoElSistema() {
        Servicio servicio = Servicio.getInstance();

        MarcaDAO.getInstance().EncontrarTodos().forEach(m ->
                servicio.getMisMarcas().put(m.getIdMarca(), m)
        );

        ClienteDAO.getInstance().EncontrarTodos().forEach(c ->
                servicio.getMisClientes().put(c.getIdCliente(), c)
        );

        LaptopDAO.getInstance().EncontrarTodos().forEach(l ->
                servicio.getMisLaptops().put(l.getIdLaptop(), l)
        );

        EquipoDAO.getInstance().EncontrarTodos().forEach(e ->
                servicio.getMisEquipos().put(e.getIdEquipo(), e)
        );

        AdquisicionDAO.getInstance().EncontrarTodos().forEach(a ->
                servicio.getMisAdquisiciones().put(a.getIdCompra(), a)
        );

        SuplidorDAO.getInstance().EncontrarTodos().forEach(s ->
                servicio.getMisSuplidores().put(s.getIdSuplidor(), s)
        );

        cargarContadores(servicio);
    }
}