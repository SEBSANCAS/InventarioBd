package logico;

public class DetalleFactura {
    private String IdDetalleFactura;
    private float preciounitario;
    private float descuento;
    private float SubtotalLinea;
    private int MesesGarantiaAplicados;
    private Equipo equipo;


    public DetalleFactura(String idDetalleFactura, float preciounitario, float descuento, float subtotalLinea, int mesesGarantiaAplicados,Equipo equipovendido) {
        IdDetalleFactura = idDetalleFactura;
        this.preciounitario = preciounitario;
        this.descuento = descuento;
        SubtotalLinea = subtotalLinea;
        MesesGarantiaAplicados = mesesGarantiaAplicados;
        this.equipo=equipovendido;
    }

    public String getIdDetalleFactura() {
        return IdDetalleFactura;
    }

    public void setIdDetalleFactura(String idDetalleFactura) {
        IdDetalleFactura = idDetalleFactura;
    }

    public float getPreciounitario() {
        return preciounitario;
    }

    public void setPreciounitario(float preciounitario) {
        this.preciounitario = preciounitario;
    }

    public float getDescuento() {
        return descuento;
    }

    public void setDescuento(float descuento) {
        this.descuento = descuento;
    }

    public float getSubtotalLinea() {
        return SubtotalLinea;
    }

    public void setSubtotalLinea(float subtotalLinea) {
        SubtotalLinea = subtotalLinea;
    }

    public int getMesesGarantiaAplicados() {
        return MesesGarantiaAplicados;
    }

    public void setMesesGarantiaAplicados(int mesesGarantiaAplicados) {
        MesesGarantiaAplicados = mesesGarantiaAplicados;
    }

    public Equipo getEquipoVendido() {
        return equipo;
    }

    public void setEquipoVendido(Equipo equipoVendido) {
        equipo = equipoVendido;
    }
}
