package logico;

import java.util.ArrayList;

public class DetalleAdquisicion {
    private String IdDetalleAdquisicion;
    private Laptop ModeloLaptopAdquirida;
    private int cantidad;
    private float CostoUnitario;
    private float SubtotalLinea;
    private ArrayList<Equipo> equipos;

    public DetalleAdquisicion(String idDetalleAdquisicion, Laptop modeloLaptopAdquirida, int cantidad, float costoUnitario, float subtotalLinea) {
        IdDetalleAdquisicion = idDetalleAdquisicion;
        ModeloLaptopAdquirida = modeloLaptopAdquirida;
        this.cantidad = cantidad;
        CostoUnitario = costoUnitario;
        SubtotalLinea = subtotalLinea;
        equipos = new ArrayList<>();
    }
    public ArrayList<Equipo> getEquipos() {
        return equipos;
    }

    public void agregarEquipo(Equipo equipo) {
        equipos.add(equipo);
    }
    public String getIdDetalleAdquisicion() {
        return IdDetalleAdquisicion;
    }

    public void setIdDetalleAdquisicion(String idDetalleAdquisicion) {
        IdDetalleAdquisicion = idDetalleAdquisicion;
    }

    public Laptop getModeloLaptopAdquirida() {
        return ModeloLaptopAdquirida;
    }

    public void setModeloLaptopAdquirida(Laptop modeloLaptopAdquirida) {
        ModeloLaptopAdquirida = modeloLaptopAdquirida;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public float getCostoUnitario() {
        return CostoUnitario;
    }

    public void setCostoUnitario(float costoUnitario) {
        CostoUnitario = costoUnitario;
    }

    public float getSubtotalLinea() {
        return SubtotalLinea;
    }

    public void setSubtotalLinea(float subtotalLinea) {
        SubtotalLinea = subtotalLinea;
    }
}
