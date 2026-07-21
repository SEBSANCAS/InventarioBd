package logico;

import java.util.Objects;

public class Auditoria {

    private String idAuditoria;
    private DetalleFactura detalleFactura;
    private Equipo equipo;
    private float precioVentaFinal;
    private float costoCompra;

    public Auditoria(String idAuditoria, DetalleFactura detalleFactura,
                     Equipo equipo, float precioVentaFinal,
                     float costoCompra) {
        this.idAuditoria = idAuditoria;
        this.detalleFactura = detalleFactura;
        this.equipo = equipo;
        this.precioVentaFinal = precioVentaFinal;
        this.costoCompra = costoCompra;
    }

    public Auditoria() {
    }

    public String getIdAuditoria() {
        return idAuditoria;
    }

    public void setIdAuditoria(String idAuditoria) {
        this.idAuditoria = idAuditoria;
    }

    public DetalleFactura getDetalleFactura() {
        return detalleFactura;
    }

    public void setDetalleFactura(DetalleFactura detalleFactura) {
        this.detalleFactura = detalleFactura;
    }

    public Equipo getEquipo() {
        return equipo;
    }

    public void setEquipo(Equipo equipo) {
        this.equipo = equipo;
    }

    public float getPrecioVentaFinal() {
        return precioVentaFinal;
    }

    public void setPrecioVentaFinal(float precioVentaFinal) {
        this.precioVentaFinal = precioVentaFinal;
    }

    public float getCostoCompra() {
        return costoCompra;
    }

    public void setCostoCompra(float costoCompra) {
        this.costoCompra = costoCompra;
    }

    public float calcularGanancia() {
        return precioVentaFinal - costoCompra;
    }

    public float calcularPorcentajeRentabilidad() {
        if (costoCompra == 0) {
            return 0;
        }
        return ((precioVentaFinal - costoCompra) / costoCompra) * 100;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Auditoria)) return false;
        Auditoria auditoria = (Auditoria) o;
        return Objects.equals(idAuditoria, auditoria.idAuditoria);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idAuditoria);
    }
}