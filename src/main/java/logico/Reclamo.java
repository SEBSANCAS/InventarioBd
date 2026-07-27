package logico;

import java.time.LocalDate;

public class Reclamo {
    private String idReclamo;
    private String idFactura;
    private String idDetalleFactura;
    private String idCliente;
    private LocalDate fecha; // <-- NUEVO CAMPO AÑADIDO
    private boolean enGarantia;
    private String tipoSolicitud;
    private String diagnosticoTecnico;
    private String estadoCaso;

    public Reclamo(String idReclamo, String idFactura, String idDetalleFactura, String idCliente, LocalDate fecha, boolean enGarantia, String tipoSolicitud, String diagnosticoTecnico, String estadoCaso) {
        this.idReclamo = idReclamo;
        this.idFactura = idFactura;
        this.idDetalleFactura = idDetalleFactura;
        this.idCliente = idCliente;
        this.fecha = fecha; // <-- ASIGNACIÓN
        this.enGarantia = enGarantia;
        this.tipoSolicitud = tipoSolicitud;
        this.diagnosticoTecnico = diagnosticoTecnico;
        this.estadoCaso = estadoCaso;
    }

    public Reclamo() {
    }

    public String getIdReclamo() {
        return idReclamo;
    }

    public void setIdReclamo(String idReclamo) {
        this.idReclamo = idReclamo;
    }

    public String getIdFactura() {
        return idFactura;
    }

    public void setIdFactura(String idFactura) {
        this.idFactura = idFactura;
    }

    public String getIdDetalleFactura() {
        return idDetalleFactura;
    }

    public void setIdDetalleFactura(String idDetalleFactura) {
        this.idDetalleFactura = idDetalleFactura;
    }

    public String getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(String idCliente) {
        this.idCliente = idCliente;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public boolean isEnGarantia() {
        return enGarantia;
    }

    public void setEnGarantia(boolean enGarantia) {
        this.enGarantia = enGarantia;
    }

    public String getTipoSolicitud() {
        return tipoSolicitud;
    }

    public void setTipoSolicitud(String tipoSolicitud) {
        this.tipoSolicitud = tipoSolicitud;
    }

    public String getDiagnosticoTecnico() {
        return diagnosticoTecnico;
    }

    public void setDiagnosticoTecnico(String diagnosticoTecnico) {
        this.diagnosticoTecnico = diagnosticoTecnico;
    }

    public String getEstadoCaso() {
        return estadoCaso;
    }

    public void setEstadoCaso(String estadoCaso) {
        this.estadoCaso = estadoCaso;
    }
}