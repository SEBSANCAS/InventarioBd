package logico;

import DataBase.*;
import java.util.HashMap;
import java.util.ArrayList;

public class Servicio {
    private HashMap<String, Cliente> misClientes;
    private HashMap<String, Marca> misMarcas;
    private HashMap<String, Suplidor> misSuplidores;
    private HashMap<String, Estante> misEstantes;
    private HashMap<String, Laptop> misLaptops;
    private HashMap<String, Equipo> misEquipos;
    private HashMap<String, Adquisicion> misAdquisiciones;
    private HashMap<String, Factura> miInventarioFacturas;
    private HashMap<String, Resolucion> misResoluciones;
    private HashMap<String, Reclamo> misReclamos;

    private int genIdCliente;
    private int genIdMarca;
    private int genIdSuplidor;
    private int genIdEstante;
    private int genIdLaptop;
    private int genIdEquipo;
    private int genIdAdquisicion;
    private int genIdFactura;
    private int genIdResolucion;
    private int genIdReclamo;

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
        this.misReclamos = new HashMap<>();
        this.misResoluciones = new HashMap<>();

        this.genIdCliente = 1;
        this.genIdMarca = 1;
        this.genIdSuplidor = 1;
        this.genIdEstante = 1;
        this.genIdLaptop = 1;
        this.genIdEquipo = 1;
        this.genIdAdquisicion = 1;
        this.genIdFactura = 1;
        this.genIdResolucion = 1;
        this.genIdReclamo = 1;
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

    public HashMap<String, Resolucion> getMisResoluciones() {
        return misResoluciones;
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

    public HashMap<String, Reclamo> getMisReclamos() {
        return misReclamos;
    }

    public int getSiguienteIdCliente() {
        return genIdCliente++;
    }

    public int getSiguienteIdResolucion() {
        return genIdResolucion++;
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

    public int getSiguienteIdReclamo() {
        return genIdReclamo++;
    }

    public int getGenIdCliente() { return genIdCliente; }
    public int getGenIdMarca() { return genIdMarca; }
    public int getGenIdSuplidor() { return genIdSuplidor; }
    public int getGenIdEstante() { return genIdEstante; }
    public int getGenIdLaptop() { return genIdLaptop; }
    public int getGenIdEquipo() { return genIdEquipo; }
    public int getGenIdAdquisicion() { return genIdAdquisicion; }
    public int getGenIdFactura() { return genIdFactura; }
    public int getGenIdResolucion() { return genIdResolucion; }
    public int getGenIdReclamo() { return genIdReclamo; }

    public void setGenIdCliente(int genIdCliente) { this.genIdCliente = genIdCliente; }
    public void setGenIdMarca(int genIdMarca) { this.genIdMarca = genIdMarca; }
    public void setGenIdSuplidor(int genIdSuplidor) { this.genIdSuplidor = genIdSuplidor; }
    public void setGenIdEstante(int genIdEstante) { this.genIdEstante = genIdEstante; }
    public void setGenIdLaptop(int genIdLaptop) { this.genIdLaptop = genIdLaptop; }
    public void setGenIdEquipo(int genIdEquipo) { this.genIdEquipo = genIdEquipo; }
    public void setGenIdAdquisicion(int genIdAdquisicion) { this.genIdAdquisicion = genIdAdquisicion; }
    public void setGenIdFactura(int genIdFactura) { this.genIdFactura = genIdFactura; }
    public void setGenIdResolucion(int genIdResolucion) { this.genIdResolucion = genIdResolucion; }
    public void setGenIdReclamo(int genIdReclamo) { this.genIdReclamo = genIdReclamo; }

    public void registrarNuevaAdquisicion(Adquisicion adquisicion, ArrayList<DetalleAdquisicion> detalles) {
        adquisicion.setDetallesAdquision(detalles);
        AdquisicionDAO.getInstance().guardar(adquisicion);
        misAdquisiciones.put(adquisicion.getIdCompra(), adquisicion);
        for (DetalleAdquisicion detalle : detalles) {
            DetalleAdquisicionDAO.getInstance().guardar(detalle, adquisicion.getIdCompra());
        }
        ServicioDAO.getInstance().guardarContadores(this);
    }

    public void registrarResolucion(Resolucion resolucion) {
        misResoluciones.put(resolucion.getIdResolucion(), resolucion);
        ResolucionDAO.getInstance().guardar(resolucion);
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
                equipo.setDisponibilidad("Vendido");
                EquipoDAO.getInstance().actualizar(equipo);

                Laptop laptop = equipo.getLaptop();

                if (laptop != null) {
                    laptop.setStockActual(laptop.getStockActual() - 1);
                }
            }
        }
        ServicioDAO.getInstance().guardarContadores(this);
    }

    public String generarIdCliente() { return String.format("CLI%03d", getSiguienteIdCliente()); }
    public String generarIdMarca() { return String.format("MAR%03d", getSiguienteIdMarca()); }
    public String generarIdSuplidor() { return String.format("SUP%03d", getSiguienteIdSuplidor()); }
    public String generarIdEstante() { return String.format("EST%03d", getSiguienteIdEstante()); }
    public String generarIdLaptop() { return String.format("LAP%03d", getSiguienteIdLaptop()); }
    public String generarIdEquipo() { return String.format("EQP%03d", getSiguienteIdEquipo()); }
    public String generarIdAdquisicion() { return String.format("ADQ%03d", getSiguienteIdAdquisicion()); }
    public String generarIdFactura() { return String.format("FAC%03d", getSiguienteIdFactura()); }
    public String generarIdReclamo() { return String.format("REC%03d", getSiguienteIdReclamo()); }
    public String generarIdResolucion() { return String.format("RES%03d", getSiguienteIdResolucion()); }

    public void registrarMarca(Marca marca) {
        misMarcas.put(marca.getIdMarca(), marca);
        MarcaDAO.getInstance().guardar(marca);
        ServicioDAO.getInstance().guardarContadores(this);
    }

    public void registrarSuplidor(Suplidor suplidor) {
        misSuplidores.put(suplidor.getIdSuplidor(), suplidor);
        SuplidorDAO.getInstance().guardar(suplidor);
        ServicioDAO.getInstance().guardarContadores(this);
    }

    public void registrarCliente(Cliente cliente) {
        misClientes.put(cliente.getIdCliente(), cliente);
        ClienteDAO.getInstance().guardar(cliente);
        ServicioDAO.getInstance().guardarContadores(this);
    }

    public void registrarEstante(Estante estante) {
        misEstantes.put(estante.getIdEstante(), estante);
        EstanteDAO.getInstance().guardar(estante);
        ServicioDAO.getInstance().guardarContadores(this);
    }

    public void registrarLaptop(Laptop laptop) {
        misLaptops.put(laptop.getIdLaptop(), laptop);
        LaptopDAO.getInstance().guardar(laptop);
        ServicioDAO.getInstance().guardarContadores(this);
    }

    public void registrarEquipo(Equipo equipo) {
        misEquipos.put(equipo.getIdEquipo(), equipo);
        EquipoDAO.getInstance().guardar(equipo);
        ServicioDAO.getInstance().guardarContadores(this);
    }

    public void registrarReclamo(Reclamo reclamo) {
        misReclamos.put(reclamo.getIdReclamo(), reclamo);
        ReclamoDAO.getInstance().guardar(reclamo);
        ServicioDAO.getInstance().guardarContadores(this);
    }

    public String generarIdDependiente(String idPadre, int cantidadActual) {
        return String.format("%s-%02d", idPadre, cantidadActual + 1);
    }

    public float calcularRentabilidadHistoricaLaptop(String idLaptop) {
        ArrayList<Auditoria> auditorias = AuditoriaRentabilidadDAO.getInstance().busca(idLaptop);
        float totalVentas = 0;
        float totalCostos = 0;
        for (Auditoria a : auditorias) {
            totalVentas += a.getPrecioVentaFinal();
            totalCostos += a.getCostoCompra();
        }
        if (totalCostos == 0) {
            return 0;
        }
        return ((totalVentas - totalCostos) / totalCostos) * 100;
    }
}