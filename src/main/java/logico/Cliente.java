package logico;

import java.util.ArrayList;

public class Cliente extends Persona {
    private String IdCliente;
    private String nombres;
    private String apellidos;
    private String TipoCLiente;
    private String tipoIdentificacion;
    private ArrayList<Telefono> telefonos;

    public Cliente(String numeroIdentificacion, String correo, String idCliente, String nombres, String apellidos, String tipoCliente, String tipoIdentificacion) {
        super(numeroIdentificacion, correo);
        this.IdCliente = idCliente;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.TipoCLiente = tipoCliente;
        this.tipoIdentificacion = tipoIdentificacion;
        this.telefonos = new ArrayList<>();
    }

    public Cliente(String numeroIdentificacion, String correo, String idCliente, String nombres, String apellidos) {
        super(numeroIdentificacion, correo);
        this.IdCliente = idCliente;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.TipoCLiente = (apellidos == null || apellidos.trim().isEmpty()) ? "Empresa" : "Persona";
        this.tipoIdentificacion = (this.TipoCLiente.equals("Empresa")) ? "Rnc" : "Cedula";
        this.telefonos = new ArrayList<>();
    }

    public Cliente() {
        this.telefonos = new ArrayList<>();
    }

    public String getIdCliente() {
        return IdCliente;
    }

    public void setIdCliente(String idCliente) {
        IdCliente = idCliente;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getTipoCLiente() {
        return TipoCLiente;
    }

    public void setTipoCLiente(String tipoCLiente) {
        TipoCLiente = tipoCLiente;
    }

    public String getTipoIdentificacion() {
        return tipoIdentificacion;
    }

    public void setTipoIdentificacion(String tipoIdentificacion) {
        this.tipoIdentificacion = tipoIdentificacion;
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