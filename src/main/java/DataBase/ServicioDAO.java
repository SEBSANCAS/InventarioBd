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

        final String updateSql =
                "UPDATE ControlContadores SET " +
                        "gen_cliente=?, gen_marca=?, gen_suplidor=?, gen_estante=?, " +
                        "gen_laptop=?, gen_equipo=?, gen_adquisicion=?, gen_factura=?, " +
                        "gen_detalle_adquisicion=?, gen_detalle_factura=? " +
                        "WHERE id=1";

        try(Connection connection = DatabaseConnection.getConnection()){

            PreparedStatement ps = connection.prepareStatement(updateSql);

            ps.setInt(1, servicio.getGenIdCliente());
            ps.setInt(2, servicio.getGenIdMarca());
            ps.setInt(3, servicio.getGenIdSuplidor());
            ps.setInt(4, servicio.getGenIdEstante());
            ps.setInt(5, servicio.getGenIdLaptop());
            ps.setInt(6, servicio.getGenIdEquipo());
            ps.setInt(7, servicio.getGenIdAdquisicion());
            ps.setInt(8, servicio.getGenIdFactura());
            ps.setInt(9, servicio.getGenIdDetalleAdquisicion());
            ps.setInt(10, servicio.getGenIdDetalleFactura());

            int filas = ps.executeUpdate();

            if(filas == 0){

                final String insertSql =
                        "INSERT INTO ControlContadores VALUES (?,?,?,?,?,?,?,?,?,?,?)";

                PreparedStatement insert = connection.prepareStatement(insertSql);

                insert.setInt(1,1);
                insert.setInt(2, servicio.getGenIdCliente());
                insert.setInt(3, servicio.getGenIdMarca());
                insert.setInt(4, servicio.getGenIdSuplidor());
                insert.setInt(5, servicio.getGenIdEstante());
                insert.setInt(6, servicio.getGenIdLaptop());
                insert.setInt(7, servicio.getGenIdEquipo());
                insert.setInt(8, servicio.getGenIdAdquisicion());
                insert.setInt(9, servicio.getGenIdFactura());
                insert.setInt(10, servicio.getGenIdDetalleAdquisicion());
                insert.setInt(11, servicio.getGenIdDetalleFactura());

                insert.executeUpdate();
            }

        }catch(SQLException e){

            System.out.println("No se pudieron guardar los contadores: " + e.getMessage());

        }

    }

    public void cargarContadores(Servicio servicio) {

        final String sql = "SELECT * FROM ControlContadores WHERE id=1";

        try(Connection connection = DatabaseConnection.getConnection();
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(sql)){

            if(rs.next()){

                servicio.setGenIdCliente(rs.getInt("gen_cliente"));
                servicio.setGenIdMarca(rs.getInt("gen_marca"));
                servicio.setGenIdSuplidor(rs.getInt("gen_suplidor"));
                servicio.setGenIdEstante(rs.getInt("gen_estante"));
                servicio.setGenIdLaptop(rs.getInt("gen_laptop"));
                servicio.setGenIdEquipo(rs.getInt("gen_equipo"));
                servicio.setGenIdAdquisicion(rs.getInt("gen_adquisicion"));
                servicio.setGenIdFactura(rs.getInt("gen_factura"));
                servicio.setGenIdDetalleAdquisicion(rs.getInt("gen_detalle_adquisicion"));
                servicio.setGenIdDetalleFactura(rs.getInt("gen_detalle_factura"));

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

        cargarContadores(servicio);
    }
}