package logico;

import DataBase.*;

import java.util.HashMap;
import java.util.*;


public class Servicio {
    private HashMap<String, Cliente> misClientes;
    private HashMap<String, Marca> misMarcas;
    private HashMap<String, Suplidor> misSuplidores;
    private HashMap<String, Estante> misEstantes;
    private HashMap<String, Laptop> misLaptops;
    private HashMap<String, Equipo> misEquipos;
    private HashMap<String, Adquisicion> misAdquisiciones;
    private HashMap<String, Factura> miInventarioFacturas;

    private int genIdCliente;
    private int genIdMarca;
    private int genIdSuplidor;
    private int genIdEstante;
    private int genIdLaptop;
    private int genIdEquipo;
    private int genIdAdquisicion;
    private int genIdFactura;

    private int genIdDetalleAdquisicion;
    private int genIdDetalleFactura;

    private static Servicio miServicio = null;

    private Servicio() {
        this.misClientes = new HashMap<>();
        this.misMarcas = new HashMap<>();
        this.misSuplidores = new HashMap<>();
        this.misEstantes = new HashMap<>();
        this.misLaptops = new HashMap<>();
        this.misEquipos = new HashMap<>();
        this.misAdquisiciones = new HashMap<>();
        this.miInventarioFacturas = new HashMap<>();
        this.genIdCliente = 1;
        this.genIdMarca = 1;
        this.genIdSuplidor = 1;
        this.genIdEstante = 1;
        this.genIdLaptop = 1;
        this.genIdEquipo = 1;
        this.genIdAdquisicion = 1;
        this.genIdFactura = 1;
        this.genIdDetalleAdquisicion = 1;
        this.genIdDetalleFactura = 1;
    }

    public static Servicio getInstance() {
        if (miServicio == null) {
            miServicio = new Servicio();
        }
        return miServicio;
    }

    public HashMap<String, Cliente> getMisClientes() {
        return misClientes;
    }

    public HashMap<String, Marca> getMisMarcas() {
        return misMarcas;
    }

    public HashMap<String, Suplidor> getMisSuplidores() {
        return misSuplidores;
    }

    public HashMap<String, Estante> getMisEstantes() {
        return misEstantes;
    }

    public HashMap<String, Laptop> getMisLaptops() {
        return misLaptops;
    }

    public HashMap<String, Equipo> getMisEquipos() {
        return misEquipos;
    }

    public HashMap<String, Adquisicion> getMisAdquisiciones() {
        return misAdquisiciones;
    }

    public HashMap<String, Factura> getMiInventarioFacturas() {
        return miInventarioFacturas;
    }

    public int getSiguienteIdCliente() {
        return genIdCliente++;
    }

    public int getSiguienteIdMarca() {
        return genIdMarca++;
    }

    public int getSiguienteIdSuplidor() {
        return genIdSuplidor++;
    }

    public int getSiguienteIdEstante() {
        return genIdEstante++;
    }

    public int getSiguienteIdLaptop() {
        return genIdLaptop++;
    }

    public int getSiguienteIdEquipo() {
        return genIdEquipo++;
    }

    public int getSiguienteIdAdquisicion() {
        return genIdAdquisicion++;
    }

    public int getSiguienteIdFactura() {
        return genIdFactura++;
    }

    public int getSiguienteIdDetalleAdquisicion() {
        return genIdDetalleAdquisicion++;
    }

    public int getSiguienteIdDetalleFactura() {
        return genIdDetalleFactura++;
    }
    public int getGenIdCliente() { return genIdCliente; }
    public int getGenIdMarca() { return genIdMarca; }
    public int getGenIdSuplidor() { return genIdSuplidor; }
    public int getGenIdEstante() { return genIdEstante; }
    public int getGenIdLaptop() { return genIdLaptop; }
    public int getGenIdEquipo() { return genIdEquipo; }
    public int getGenIdAdquisicion() { return genIdAdquisicion; }
    public int getGenIdFactura() { return genIdFactura; }
    public int getGenIdDetalleAdquisicion() { return genIdDetalleAdquisicion; }
    public int getGenIdDetalleFactura() { return genIdDetalleFactura; }
    public void setGenIdCliente(int genIdCliente) {
        this.genIdCliente = genIdCliente;
    }

    public void setGenIdMarca(int genIdMarca) {
        this.genIdMarca = genIdMarca;
    }

    public void setGenIdSuplidor(int genIdSuplidor) {
        this.genIdSuplidor = genIdSuplidor;
    }

    public void setGenIdEstante(int genIdEstante) {
        this.genIdEstante = genIdEstante;
    }

    public void setGenIdLaptop(int genIdLaptop) {
        this.genIdLaptop = genIdLaptop;
    }

    public void setGenIdEquipo(int genIdEquipo) {
        this.genIdEquipo = genIdEquipo;
    }

    public void setGenIdAdquisicion(int genIdAdquisicion) {
        this.genIdAdquisicion = genIdAdquisicion;
    }

    public void setGenIdFactura(int genIdFactura) {
        this.genIdFactura = genIdFactura;
    }

    public void setGenIdDetalleAdquisicion(int genIdDetalleAdquisicion) {
        this.genIdDetalleAdquisicion = genIdDetalleAdquisicion;
    }

    public void setGenIdDetalleFactura(int genIdDetalleFactura) {
        this.genIdDetalleFactura = genIdDetalleFactura;
    }

    public void registrarNuevaAdquisicion(Adquisicion adquisicion, ArrayList<DetalleAdquisicion> detalles) {
        adquisicion.setDetallesAdquision(detalles);
        AdquisicionDAO.getInstance().guardar(adquisicion);
        misAdquisiciones.put(adquisicion.getIdCompra(), adquisicion);
        for (DetalleAdquisicion detalle : detalles) {
            DetalleAdquisicionDAO.getInstance().guardar(detalle, adquisicion.getIdCompra());
            Laptop laptop = detalle.getModeloLaptopAdquirida();
            if (laptop != null) {
                laptop.setStockActual(laptop.getStockActual() + detalle.getCantidad());
                LaptopDAO.getInstance().actualizar(laptop);
            }
        }
        ServicioDAO.getInstance().guardarContadores(this);
    }
    public void registrarNuevaFactura(Factura factura, ArrayList<DetalleFactura> detalles) {

        factura.setDetallesFactura(detalles);
        FacturaDAO.getInstance().guardar(factura);
        miInventarioFacturas.put(factura.getIdFactura(), factura);
        for (DetalleFactura detalle : detalles) {
            DetalleFacturaDAO.getInstance().guardar(detalle, factura.getIdFactura());
            Equipo equipo = detalle.getEquipoVendido();
            if (equipo != null) {
                equipo.setEstado("Vendido");
                EquipoDAO.getInstance().actualizar(equipo);

                Laptop laptop = equipo.getLaptop();

                if (laptop != null) {
                    laptop.setStockActual(laptop.getStockActual() - 1);
                    LaptopDAO.getInstance().actualizar(laptop);
                }
            }
        }
        ServicioDAO.getInstance().guardarContadores(this);
    }
    void registrarMarca(Marca marca)
    {
        misMarcas.put(marca.getIdMarca(),marca);
        MarcaDAO.getInstance().guardar(marca);
    }
    void registrarSuplidor(Suplidor suplidor)
    {
        misSuplidores.put(suplidor.getIdSuplidor(),suplidor);
        SuplidorDAO.getInstance().guardar(suplidor);
    }
    public void registrarCliente(Cliente cliente)
    {
        misClientes.put(cliente.getIdCliente(), cliente);
        ClienteDAO.getInstance().guardar(cliente);
    }
    public void registrarEstante(Estante estante)
    {
        misEstantes.put(estante.getIdEstante(), estante);
        EstanteDAO.getInstance().guardar(estante);
    }
    public void registrarLaptop(Laptop laptop)
    {
        misLaptops.put(laptop.getIdLaptop(), laptop);
        LaptopDAO.getInstance().guardar(laptop);
    }
    public void registrarEquipo(Equipo equipo)
    {
        misEquipos.put(equipo.getIdEquipo(), equipo);
        EquipoDAO.getInstance().guardar(equipo);
    }
}