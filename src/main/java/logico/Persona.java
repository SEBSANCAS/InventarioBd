package logico;

public abstract class Persona {
    private String NumeroIdentificacion;
    private String correo;
    private String telefono;

    public Persona(String numeroIdentificacion, String correo, String telefono) {
        NumeroIdentificacion = numeroIdentificacion;
        this.correo = correo;
        this.telefono = telefono;
    }

    protected Persona() {
    }

    public String getNumeroIdentificacion() {
        return NumeroIdentificacion;
    }

    public void setNumeroIdentificacion(String numeroIdentificacion) {
        NumeroIdentificacion = numeroIdentificacion;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
}
