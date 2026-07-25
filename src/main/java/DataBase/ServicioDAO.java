package DataBase;

import logico.Adquisicion;
import logico.Factura;
import logico.Servicio;
import java.sql.*;

public class ServicioDAO {

    public static final ServicioDAO INSTANCE = new ServicioDAO();

    private ServicioDAO() {}

    public static ServicioDAO getInstance() {
        return INSTANCE;
    }

    public void guardarContadores(Servicio servicio) {

        final String updateSql =
                "UPDATE ControlContadores SET " +
                        "id_cliente=?, id_marca=?, id_suplidor=?, id_estante=?, " +
                        "id_laptop=?, id_equipo=?, id_adquisicion=?, id_factura=? " +
                        "WHERE id=1";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(updateSql)) {

            ps.setInt(1, servicio.getGenIdCliente());
            ps.setInt(2, servicio.getGenIdMarca());
            ps.setInt(3, servicio.getGenIdSuplidor());
            ps.setInt(4, servicio.getGenIdEstante());
            ps.setInt(5, servicio.getGenIdLaptop());
            ps.setInt(6, servicio.getGenIdEquipo());
            ps.setInt(7, servicio.getGenIdAdquisicion());
            ps.setInt(8, servicio.getGenIdFactura());

            int filas = ps.executeUpdate();

            if (filas == 0) {

                final String insertSql =
                        "INSERT INTO ControlContadores (" +
                                "id, id_cliente, id_marca, id_suplidor, id_estante, " +
                                "id_laptop, id_equipo, id_adquisicion, id_factura" +
                                ") VALUES (?,?,?,?,?,?,?,?,?)";

                try (PreparedStatement insert = connection.prepareStatement(insertSql)) {
                    insert.setInt(1, 1);
                    insert.setInt(2, servicio.getGenIdCliente());
                    insert.setInt(3, servicio.getGenIdMarca());
                    insert.setInt(4, servicio.getGenIdSuplidor());
                    insert.setInt(5, servicio.getGenIdEstante());
                    insert.setInt(6, servicio.getGenIdLaptop());
                    insert.setInt(7, servicio.getGenIdEquipo());
                    insert.setInt(8, servicio.getGenIdAdquisicion());
                    insert.setInt(9, servicio.getGenIdFactura());

                    insert.executeUpdate();
                }
            }
        } catch (SQLException e) {
            System.out.println("No se pudieron guardar los contadores: " + e.getMessage());
        }
    }

    public void cargarContadores(Servicio servicio) {

        final String sql = "SELECT * FROM ControlContadores WHERE id=1";

        try(Connection connection = DatabaseConnection.getConnection();
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(sql)){

            if(rs.next()){
                servicio.setGenIdCliente(rs.getInt("id_cliente"));
                servicio.setGenIdMarca(rs.getInt("id_marca"));
                servicio.setGenIdSuplidor(rs.getInt("id_suplidor"));
                servicio.setGenIdEstante(rs.getInt("id_estante"));
                servicio.setGenIdLaptop(rs.getInt("id_laptop"));
                servicio.setGenIdEquipo(rs.getInt("id_equipo"));
                servicio.setGenIdAdquisicion(rs.getInt("id_adquisicion"));
                servicio.setGenIdFactura(rs.getInt("id_factura"));
            } else {
                guardarContadores(servicio);
            }

        }catch(SQLException e){
            System.out.println("No se pudieron cargar los contadores: " + e.getMessage());
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

        SuplidorDAO.getInstance().EncontrarTodos().forEach(s ->
                servicio.getMisSuplidores().put(s.getIdSuplidor(), s)
        );

        EstanteDAO.getInstance().EncontrarTodos().forEach(est ->
                servicio.getMisEstantes().put(est.getIdEstante(), est)
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

        FacturaDAO.getInstance().EncontrarTodos().forEach(f ->
                servicio.getMiInventarioFacturas().put(f.getIdFactura(), f)
        );

        for (Adquisicion a : servicio.getMisAdquisiciones().values()) {
            a.setDetallesAdquision(
                    DetalleAdquisicionDAO.getInstance()
                            .encontrarPorIdAdquisicion(a.getIdCompra())
            );
        }
        for (Factura f : servicio.getMiInventarioFacturas().values()) {
            f.setDetallesFactura(
                    DetalleFacturaDAO.getInstance()
                            .encontrarPorFactura(f.getIdFactura())
            );
        }

        cargarContadores(servicio);
    }
}