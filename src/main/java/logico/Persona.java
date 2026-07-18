package logico;

public abstract class Persona {
    private String NumeroIdentificacion;
    private String correo;

    public Persona(String numeroIdentificacion, String correo) {
        NumeroIdentificacion = numeroIdentificacion;
        this.correo = correo;
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
}
