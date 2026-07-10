package logico;


import java.time.LocalDate;
import java.util.HashMap;

public class Suplidor extends Persona{
    private String IdSuplidor;
    private String RazonComercial;
    private String NombreComercial;
    private String  pais;
    private String  ciudad;
    private String  calle;
    private LocalDate FechaRegistro;
    private HashMap<Laptop,Float> LaptopsSuplidor;

    public Suplidor(String numeroIdentificacion, String correo, String telefono, String nombreComercial, String idSuplidor, String razonComercial, String pais, String ciudad, String calle,LocalDate FechaRegistro) {
        super(numeroIdentificacion, correo, telefono);
        NombreComercial = nombreComercial;
        IdSuplidor = idSuplidor;
        RazonComercial = razonComercial;
        this.pais = pais;
        this.ciudad = ciudad;
        this.calle = calle;
        this.FechaRegistro=FechaRegistro;
        LaptopsSuplidor = new HashMap<>();
    }

    public String getIdSuplidor() {
        return IdSuplidor;
    }

    public void setIdSuplidor(String idSuplidor) {
        IdSuplidor = idSuplidor;
    }

    public String getRazonComercial() {
        return RazonComercial;
    }

    public void setRazonComercial(String razonComercial) {
        RazonComercial = razonComercial;
    }

    public String getNombreComercial() {
        return NombreComercial;
    }

    public void setNombreComercial(String nombreComercial) {
        NombreComercial = nombreComercial;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getCalle() {
        return calle;
    }

    public void setCalle(String calle) {
        this.calle = calle;
    }

    public HashMap<Laptop, Float> getLaptopsSuplidor() {
        return LaptopsSuplidor;
    }

    public void setLaptopsSuplidor(HashMap<Laptop, Float> laptopsSuplidor) {
        LaptopsSuplidor = laptopsSuplidor;
    }

    public LocalDate getFechaRegistro() {
        return FechaRegistro;
    }

    public void setFechaRegistro(LocalDate fechaRegistro) {
        FechaRegistro = fechaRegistro;
    }
}
