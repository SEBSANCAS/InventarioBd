package logico;

import java.time.LocalDate;
import java.util.ArrayList;

public class Factura {
    private String IdFactura;
    private String NumeroComprobante;
    private Cliente cliente;
    private LocalDate FechaEmision;
    private float subtotal;
    private float impuestos;
    private float MontoTotal;
    private ArrayList<DetalleFactura> DetallesFactura;

    public Factura(String idFactura, String numeroComprobante, Cliente cliente, LocalDate fechaEmision, float subtotal, float impuestos, float montoTotal) {
        IdFactura = idFactura;
        NumeroComprobante = numeroComprobante;
        this.cliente = cliente;
        FechaEmision = fechaEmision;
        this.subtotal = subtotal;
        this.impuestos = impuestos;
        MontoTotal = montoTotal;
        DetallesFactura = new ArrayList<>();
    }

    public String getIdFactura() {
        return IdFactura;
    }

    public void setIdFactura(String idFactura) {
        IdFactura = idFactura;
    }

    public String getNumeroComprobante() {
        return NumeroComprobante;
    }

    public void setNumeroComprobante(String numeroComprobante) {
        NumeroComprobante = numeroComprobante;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public LocalDate getFechaEmision() {
        return FechaEmision;
    }

    public void setFechaEmision(LocalDate fechaEmision) {
        FechaEmision = fechaEmision;
    }

    public float getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(float subtotal) {
        this.subtotal = subtotal;
    }

    public float getImpuestos() {
        return impuestos;
    }

    public void setImpuestos(float impuestos) {
        this.impuestos = impuestos;
    }

    public float getMontoTotal() {
        return MontoTotal;
    }

    public void setMontoTotal(float montoTotal) {
        MontoTotal = montoTotal;
    }

    public ArrayList<DetalleFactura> getDetallesFactura() {
        return DetallesFactura;
    }

    public void setDetallesFactura(ArrayList<DetalleFactura> detallesFactura) {
        DetallesFactura = detallesFactura;
    }
}
