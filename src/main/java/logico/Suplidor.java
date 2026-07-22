package logico;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

public class Suplidor extends Persona {
    private String IdSuplidor;
    private String RazonComercial;
    private String NombreComercial;
    private String idCalle;
    private String tipoIdentificacion;
    private LocalDate FechaRegistro;
    private HashMap<Laptop, DetalleLaptopSuplidor> LaptopsSuplidor;
    private ArrayList<Telefono> telefonos;

    public Suplidor(String numeroIdentificacion, String correo, String nombreComercial, String idSuplidor, String razonComercial, String idCalle, LocalDate FechaRegistro, String tipoIdentificacion) {
        super(numeroIdentificacion, correo);
        this.NombreComercial = nombreComercial;
        this.IdSuplidor = idSuplidor;
        this.RazonComercial = razonComercial;
        this.idCalle = idCalle;
        this.FechaRegistro = FechaRegistro;
        this.tipoIdentificacion = tipoIdentificacion;
        this.LaptopsSuplidor = new HashMap<>();
        this.telefonos = new ArrayList<>();
    }

    public Suplidor(String numeroIdentificacion, String correo, String nombreComercial, String idSuplidor, String razonComercial, String idCalle, LocalDate FechaRegistro) {
        this(numeroIdentificacion, correo, nombreComercial, idSuplidor, razonComercial, idCalle, FechaRegistro, "Rnc");
    }

    public Suplidor() {
        this.LaptopsSuplidor = new HashMap<>();
        this.telefonos = new ArrayList<>();
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

    public String getIdCalle() {
        return idCalle;
    }

    public void setIdCalle(String idCalle) {
        this.idCalle = idCalle;
    }

    public String getTipoIdentificacion() {
        return tipoIdentificacion;
    }

    public void setTipoIdentificacion(String tipoIdentificacion) {
        this.tipoIdentificacion = tipoIdentificacion;
    }

    public HashMap<Laptop, DetalleLaptopSuplidor> getLaptopsSuplidor() {
        return LaptopsSuplidor;
    }

    public void setLaptopsSuplidor(HashMap<Laptop, DetalleLaptopSuplidor> laptopsSuplidor) {
        LaptopsSuplidor = laptopsSuplidor;
    }

    public LocalDate getFechaRegistro() {
        return FechaRegistro;
    }

    public void setFechaRegistro(LocalDate fechaRegistro) {
        FechaRegistro = fechaRegistro;
    }

    public ArrayList<Telefono> getTelefonos() {
        return telefonos;
    }

    public void setTelefonos(ArrayList<Telefono> telefonos) {
        this.telefonos = telefonos;
    }

    public void agregarTelefono(Telefono telefono) {
        telefonos.add(telefono);
    }
}