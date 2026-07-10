package logico;

import java.time.LocalDate;
import java.util.Date;

public class Equipo extends Laptop{
    private String IdEquipo;
    private String NumeroSerie;
    private Estante estante;
    private int NivelEstante;
    private String estado;
    LocalDate FechaIngreso;
    private String IdAdquisicionOrigen;

    public Equipo(String idLaptop, String numeroModelo, String nombreComercial, Marca marca,String procesador, String gpu, String tipoRam, float cantidadRam, String tipoAlmacenamiento, float cantidadAlmacenamiento, float tamanyoPantalla, String resolucionPantalla, float costoPromedioCompra, float precioDetalle, float precioMayorista, int cantMinMayorista, int cantidadAlerta, int stockActual, int MesesGarantia, String idEquipo, String numeroSerie,Estante estante, int nivelEstante, String estado, LocalDate fechaIngreso, String idAdquisicionOrigen) {
        super(idLaptop, numeroModelo, nombreComercial,marca, procesador, gpu, tipoRam, cantidadRam, tipoAlmacenamiento, cantidadAlmacenamiento, tamanyoPantalla, resolucionPantalla, costoPromedioCompra, precioDetalle, precioMayorista, cantMinMayorista, cantidadAlerta, stockActual, MesesGarantia);
        IdEquipo = idEquipo;
        NumeroSerie = numeroSerie;
        this.estante=estante;
        NivelEstante = nivelEstante;
        this.estado = estado;
        FechaIngreso = fechaIngreso;
        IdAdquisicionOrigen = idAdquisicionOrigen;
    }

    public String getIdEquipo() {
        return IdEquipo;
    }

    public void setIdEquipo(String idEquipo) {
        IdEquipo = idEquipo;
    }

    public String getNumeroSerie() {
        return NumeroSerie;
    }

    public void setNumeroSerie(String numeroSerie) {
        NumeroSerie = numeroSerie;
    }

    public int getNivelEstante() {
        return NivelEstante;
    }

    public void setNivelEstante(int nivelEstante) {
        NivelEstante = nivelEstante;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public LocalDate getFechaIngreso() {
        return FechaIngreso;
    }

    public void setFechaIngreso(LocalDate fechaIngreso) {
        FechaIngreso = fechaIngreso;
    }

    public String getIdAdquisicionOrigen() {
        return IdAdquisicionOrigen;
    }

    public void setIdAdquisicionOrigen(String idAdquisicionOrigen) {
        IdAdquisicionOrigen = idAdquisicionOrigen;
    }

    public Estante getEstante() {
        return estante;
    }

    public void setEstante(Estante estante) {
        this.estante = estante;
    }
}
