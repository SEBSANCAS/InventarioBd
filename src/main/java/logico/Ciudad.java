package logico;

public class Ciudad {
    private String idCiudad;
    private String idPais;
    private String nombreCiudad;
    private double latitud;
    private double longitud;

    public Ciudad(String idCiudad, String idPais, String nombreCiudad, double latitud,double longitud) {
        this.idCiudad = idCiudad;
        this.idPais = idPais;
        this.nombreCiudad = nombreCiudad;
        this.latitud = latitud;
        this.longitud = longitud;
    }

    public Ciudad() {}

    public String getIdCiudad() {
        return idCiudad;
    }

    public void setIdCiudad(String idCiudad) {
        this.idCiudad = idCiudad;
    }

    public String getIdPais() {
        return idPais;
    }

    public void setIdPais(String idPais) {
        this.idPais = idPais;
    }

    public String getNombreCiudad() {
        return nombreCiudad;
    }

    public void setNombreCiudad(String nombreCiudad) {
        this.nombreCiudad = nombreCiudad;
    }

    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }

    @Override
    public String toString() {
        return nombreCiudad; // Permite que el ComboBox de JavaFX muestre el nombre directamente
    }
}