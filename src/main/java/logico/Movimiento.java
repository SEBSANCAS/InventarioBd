package logico;

import java.time.LocalDateTime;

public class Movimiento {
    private String idMovimiento;
    private String idEquipo;

    private String idEstanteOrigen;
    private int nivelOrigen;

    private String idEstanteDestino;
    private int nivelDestino;

    private String tipoMovimiento;
    private String descripcionMovimiento;
    private LocalDateTime fechaHoraMovimiento;

    public Movimiento(String idMovimiento, String idEquipo, String idEstanteOrigen, int nivelOrigen, String idEstanteDestino, int nivelDestino, String tipoMovimiento, String descripcionMovimiento, LocalDateTime fechaHoraMovimiento) {
        this.idMovimiento = idMovimiento;
        this.idEquipo = idEquipo;
        this.idEstanteOrigen = idEstanteOrigen;
        this.nivelOrigen = nivelOrigen;
        this.idEstanteDestino = idEstanteDestino;
        this.nivelDestino = nivelDestino;
        this.tipoMovimiento = tipoMovimiento;
        this.descripcionMovimiento = descripcionMovimiento;
        this.fechaHoraMovimiento = fechaHoraMovimiento;
    }

    public Movimiento() {
    }

    public String getIdMovimiento() {
        return idMovimiento;
    }

    public void setIdMovimiento(String idMovimiento) {
        this.idMovimiento = idMovimiento;
    }

    public String getIdEquipo() {
        return idEquipo;
    }

    public void setIdEquipo(String idEquipo) {
        this.idEquipo = idEquipo;
    }

    public String getIdEstanteOrigen() {
        return idEstanteOrigen;
    }

    public void setIdEstanteOrigen(String idEstanteOrigen) {
        this.idEstanteOrigen = idEstanteOrigen;
    }

    public int getNivelOrigen() {
        return nivelOrigen;
    }

    public void setNivelOrigen(int nivelOrigen) {
        this.nivelOrigen = nivelOrigen;
    }

    public String getIdEstanteDestino() {
        return idEstanteDestino;
    }

    public void setIdEstanteDestino(String idEstanteDestino) {
        this.idEstanteDestino = idEstanteDestino;
    }

    public int getNivelDestino() {
        return nivelDestino;
    }

    public void setNivelDestino(int nivelDestino) {
        this.nivelDestino = nivelDestino;
    }

    public String getTipoMovimiento() {
        return tipoMovimiento;
    }

    public void setTipoMovimiento(String tipoMovimiento) {
        this.tipoMovimiento = tipoMovimiento;
    }

    public String getDescripcionMovimiento() {
        return descripcionMovimiento;
    }

    public void setDescripcionMovimiento(String descripcionMovimiento) {
        this.descripcionMovimiento = descripcionMovimiento;
    }

    public LocalDateTime getFechaHoraMovimiento() {
        return fechaHoraMovimiento;
    }

    public void setFechaHoraMovimiento(LocalDateTime fechaHoraMovimiento) {
        this.fechaHoraMovimiento = fechaHoraMovimiento;
    }
}