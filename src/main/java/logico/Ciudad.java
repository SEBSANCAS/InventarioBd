package logico;

public class Ciudad {
    private String idCiudad;
    private String idPais;
    private String nombreCiudad;
    private double latMin;
    private double latMax;
    private double lonMin;
    private double lonMax;

    public Ciudad(String idCiudad, String idPais, String nombreCiudad, double latMin, double latMax, double lonMin, double lonMax) {
        this.idCiudad = idCiudad;
        this.idPais = idPais;
        this.nombreCiudad = nombreCiudad;
        this.latMin = latMin;
        this.latMax = latMax;
        this.lonMin = lonMin;
        this.lonMax = lonMax;
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

    public double getLatMin() {
        return latMin;
    }

    public void setLatMin(double latMin) {
        this.latMin = latMin;
    }

    public double getLatMax() {
        return latMax;
    }

    public void setLatMax(double latMax) {
        this.latMax = latMax;
    }

    public double getLonMin() {
        return lonMin;
    }

    public void setLonMin(double lonMin) {
        this.lonMin = lonMin;
    }

    public double getLonMax() {
        return lonMax;
    }

    public void setLonMax(double lonMax) {
        this.lonMax = lonMax;
    }

    @Override
    public String toString() {
        return nombreCiudad; // Permite que el ComboBox de JavaFX muestre el nombre directamente
    }
}