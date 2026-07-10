package logico;

import java.time.LocalDate;
import java.util.ArrayList;

public class Adquisicion {
    private String IdCompra;
    private Suplidor suplidor;
    private LocalDate FechaEmision;
    private LocalDate FechaEntrega;
    private String estado;
    private float MontoTotal;
    private ArrayList<DetalleAdquisicion> DetallesAdquision;

    public Adquisicion(String idCompra, Suplidor suplidor, LocalDate fechaEmision, LocalDate fechaEntrega, String estado, float montoTotal) {
        IdCompra = idCompra;
        this.suplidor = suplidor;
        FechaEmision = fechaEmision;
        FechaEntrega = fechaEntrega;
        this.estado = estado;
        MontoTotal = montoTotal;
        DetallesAdquision = new ArrayList<>();
    }

    public String getIdCompra() {
        return IdCompra;
    }

    public void setIdCompra(String idCompra) {
        IdCompra = idCompra;
    }

    public Suplidor getSuplidor() {
        return suplidor;
    }

    public void setSuplidor(Suplidor suplidor) {
        this.suplidor = suplidor;
    }

    public LocalDate getFechaEmision() {
        return FechaEmision;
    }

    public void setFechaEmision(LocalDate fechaEmision) {
        FechaEmision = fechaEmision;
    }

    public LocalDate getFechaEntrega() {
        return FechaEntrega;
    }

    public void setFechaEntrega(LocalDate fechaEntrega) {
        FechaEntrega = fechaEntrega;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public float getMontoTotal() {
        return MontoTotal;
    }

    public void setMontoTotal(float montoTotal) {
        MontoTotal = montoTotal;
    }

    public ArrayList<DetalleAdquisicion> getDetallesAdquision() {
        return DetallesAdquision;
    }

    public void setDetallesAdquision(ArrayList<DetalleAdquisicion> detallesAdquision) {
        DetallesAdquision = detallesAdquision;
    }
}
