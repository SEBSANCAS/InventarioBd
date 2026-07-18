package logico;

public class Telefono {
    private String idTelefono;
    private String numeroTelefono;
    private boolean esPrincipal;

    public Telefono(String idTelefono, String numeroTelefono, boolean esPrincipal) {
        this.idTelefono = idTelefono;
        this.numeroTelefono = numeroTelefono;
        this.esPrincipal = esPrincipal;
    }

    public Telefono() {
    }

    public String getIdTelefono() {
        return idTelefono;
    }

    public void setIdTelefono(String idTelefono) {
        this.idTelefono = idTelefono;
    }

    public String getNumeroTelefono() {
        return numeroTelefono;
    }

    public void setNumeroTelefono(String numeroTelefono) {
        this.numeroTelefono = numeroTelefono;
    }

    public boolean isEsPrincipal() {
        return esPrincipal;
    }

    public void setEsPrincipal(boolean esPrincipal) {
        this.esPrincipal = esPrincipal;
    }
}