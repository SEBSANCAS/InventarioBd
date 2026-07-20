package logico;

import java.time.LocalDate;

public class Equipo {

    private String idEquipo;
    private String numeroSerie;
    private String color;
    private Laptop laptop;
    private String estado;
    private String disponibilidad;
    private float descuentoPorCondicion;
    private LocalDate fechaIngreso;
    private String idAdquisicionOrigen;

    public Equipo(String idEquipo,
                  Laptop laptop,
                  String numeroSerie,
                  String color,
                  String estado,
                  String disponibilidad,
                  float descuentoPorCondicion,
                  LocalDate fechaIngreso,
                  String idAdquisicionOrigen) {

        this.idEquipo = idEquipo;
        this.laptop = laptop;
        this.numeroSerie = numeroSerie;
        this.color = color;
        this.estado = estado;
        this.disponibilidad = disponibilidad;
        this.descuentoPorCondicion = descuentoPorCondicion;
        this.fechaIngreso = fechaIngreso;
        this.idAdquisicionOrigen = idAdquisicionOrigen;
    }

    public Equipo(String idEquipo) {
        this.idEquipo = idEquipo;
    }

    public String getIdEquipo() {
        return idEquipo;
    }

    public void setIdEquipo(String idEquipo) {
        this.idEquipo = idEquipo;
    }

    public Laptop getLaptop() {
        return laptop;
    }

    public void setLaptop(Laptop laptop) {
        this.laptop = laptop;
    }

    public String getNumeroSerie() {
        return numeroSerie;
    }

    public void setNumeroSerie(String numeroSerie) {
        this.numeroSerie = numeroSerie;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getDisponibilidad() {
        return disponibilidad;
    }

    public void setDisponibilidad(String disponibilidad) {
        this.disponibilidad = disponibilidad;
    }

    public float getDescuentoPorCondicion() {
        return descuentoPorCondicion;
    }

    public void setDescuentoPorCondicion(float descuentoPorCondicion) {
        this.descuentoPorCondicion = descuentoPorCondicion;
    }

    public LocalDate getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(LocalDate fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public String getIdAdquisicionOrigen() {
        return idAdquisicionOrigen;
    }

    public void setIdAdquisicionOrigen(String idAdquisicionOrigen) {
        this.idAdquisicionOrigen = idAdquisicionOrigen;
    }
}