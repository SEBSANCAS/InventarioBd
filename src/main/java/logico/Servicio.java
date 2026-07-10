package logico;

import java.util.HashMap;

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
}