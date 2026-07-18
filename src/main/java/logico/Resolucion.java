package logico;

import java.time.LocalDate;

public class Resolucion {
    private String idResolucion;
    private String idReclamo;
    private String idEquipoEntrante;
    private String idEquipoSaliente;
    private String accionTomada;
    private float montoCobrado;
    private float montoReembolsado;
    private LocalDate fechaResolucion;

    public Resolucion(String idResolucion, String idReclamo, String idEquipoEntrante, String idEquipoSaliente, String accionTomada, float montoCobrado, float montoReembolsado, LocalDate fechaResolucion) {
        this.idResolucion = idResolucion;
        this.idReclamo = idReclamo;
        this.idEquipoEntrante = idEquipoEntrante;
        this.idEquipoSaliente = idEquipoSaliente;
        this.accionTomada = accionTomada;
        this.montoCobrado = montoCobrado;
        this.montoReembolsado = montoReembolsado;
        this.fechaResolucion = fechaResolucion;
    }

    public Resolucion() {
    }

    public String getIdResolucion() {
        return idResolucion;
    }

    public void setIdResolucion(String idResolucion) {
        this.idResolucion = idResolucion;
    }

    public String getIdReclamo() {
        return idReclamo;
    }

    public void setIdReclamo(String idReclamo) {
        this.idReclamo = idReclamo;
    }

    public String getIdEquipoEntrante() {
        return idEquipoEntrante;
    }

    public void setIdEquipoEntrante(String idEquipoEntrante) {
        this.idEquipoEntrante = idEquipoEntrante;
    }

    public String getIdEquipoSaliente() {
        return idEquipoSaliente;
    }

    public void setIdEquipoSaliente(String idEquipoSaliente) {
        this.idEquipoSaliente = idEquipoSaliente;
    }

    public String getAccionTomada() {
        return accionTomada;
    }

    public void setAccionTomada(String accionTomada) {
        this.accionTomada = accionTomada;
    }

    public float getMontoCobrado() {
        return montoCobrado;
    }

    public void setMontoCobrado(float montoCobrado) {
        this.montoCobrado = montoCobrado;
    }

    public float getMontoReembolsado() {
        return montoReembolsado;
    }

    public void setMontoReembolsado(float montoReembolsado) {
        this.montoReembolsado = montoReembolsado;
    }

    public LocalDate getFechaResolucion() {
        return fechaResolucion;
    }

    public void setFechaResolucion(LocalDate fechaResolucion) {
        this.fechaResolucion = fechaResolucion;
    }
}