package logico;

import java.time.LocalDate;
import java.util.Objects;

public class CambioParametroLaptop {

    private String idCambio;
    private Laptop laptop;
    private String campoModificado;
    private String valorAnterior;
    private String valorNuevo;
    private LocalDate fechaCambio;
    private String descripcionCambio;

    public CambioParametroLaptop(String idCambio, Laptop laptop,
                                 String campoModificado,
                                 String valorAnterior,
                                 String valorNuevo,
                                 LocalDate fechaCambio,
                                 String descripcionCambio) {
        this.idCambio = idCambio;
        this.laptop = laptop;
        this.campoModificado = campoModificado;
        this.valorAnterior = valorAnterior;
        this.valorNuevo = valorNuevo;
        this.fechaCambio = fechaCambio;
        this.descripcionCambio = descripcionCambio;
    }

    public CambioParametroLaptop() {
    }

    public String getIdCambio() {
        return idCambio;
    }

    public void setIdCambio(String idCambio) {
        this.idCambio = idCambio;
    }

    public Laptop getLaptop() {
        return laptop;
    }

    public void setLaptop(Laptop laptop) {
        this.laptop = laptop;
    }

    public String getCampoModificado() {
        return campoModificado;
    }

    public void setCampoModificado(String campoModificado) {
        this.campoModificado = campoModificado;
    }

    public String getValorAnterior() {
        return valorAnterior;
    }

    public void setValorAnterior(String valorAnterior) {
        this.valorAnterior = valorAnterior;
    }

    public String getValorNuevo() {
        return valorNuevo;
    }

    public void setValorNuevo(String valorNuevo) {
        this.valorNuevo = valorNuevo;
    }

    public LocalDate getFechaCambio() {
        return fechaCambio;
    }

    public void setFechaCambio(LocalDate fechaCambio) {
        this.fechaCambio = fechaCambio;
    }

    public String getDescripcionCambio() {
        return descripcionCambio;
    }

    public void setDescripcionCambio(String descripcionCambio) {
        this.descripcionCambio = descripcionCambio;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CambioParametroLaptop)) return false;
        CambioParametroLaptop cambio = (CambioParametroLaptop) o;
        return Objects.equals(idCambio, cambio.idCambio);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idCambio);
    }
}