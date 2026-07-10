package logico;

public class Cliente extends Persona {
    private String IdCliente;
    private String nombres;
    private String apellidos;
    private String TipoCLiente;

    public Cliente(String numeroIdentificacion, String correo, String telefono, String idCliente, String nombres, String apellidos, String tipoCLiente) {
        super(numeroIdentificacion, correo, telefono);
        IdCliente = idCliente;
        this.nombres = nombres;
        this.apellidos = apellidos;
        TipoCLiente = tipoCLiente;
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
}
