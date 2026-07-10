package logico;

public class Marca {
    private String IdMarca;
    private String NombreMarca;

    public Marca(String idMarca, String nombreMarca) {
        IdMarca = idMarca;
        NombreMarca = nombreMarca;
    }

    public String getIdMarca() {
        return IdMarca;
    }

    public void setIdMarca(String idMarca) {
        IdMarca = idMarca;
    }

    public String getNombreMarca() {
        return NombreMarca;
    }

    public void setNombreMarca(String nombreMarca) {
        NombreMarca = nombreMarca;
    }
}
