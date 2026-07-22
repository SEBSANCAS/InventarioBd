package logico;

import java.util.Objects;

public class Laptop {
    private String IdLaptop;
    private String NumeroModelo;
    private String NombreComercial;
    private Marca marca;
    private float peso;
    private String procesador;
    private String gpu;
    private String TipoRam;
    private float CantidadRam;
    private String TipoAlmacenamiento;
    private float CantidadAlmacenamiento;
    private float TamanyoPantalla;
    private String ResolucionPantalla;
    private float CostoPromedioCompra;
    private float PrecioDetalle;
    private float PrecioMayorista;
    private int CantMinMayorista;
    private int CantidadAlerta;
    private int StockActual;
    private int MesesGarantia;

    public Laptop(String idLaptop, String numeroModelo, String nombreComercial,Marca marca,float peso, String procesador, String gpu, String tipoRam, float cantidadRam, String tipoAlmacenamiento, float cantidadAlmacenamiento, float tamanyoPantalla, String resolucionPantalla, float costoPromedioCompra, float precioDetalle, float precioMayorista, int cantMinMayorista, int cantidadAlerta, int stockActual,int MesesGarantia) {
        IdLaptop = idLaptop;
        NumeroModelo = numeroModelo;
        NombreComercial = nombreComercial;
        this.marca = marca;
        this.peso = peso;
        this.procesador = procesador;
        this.gpu = gpu;
        TipoRam = tipoRam;
        CantidadRam = cantidadRam;
        TipoAlmacenamiento = tipoAlmacenamiento;
        CantidadAlmacenamiento = cantidadAlmacenamiento;
        TamanyoPantalla = tamanyoPantalla;
        ResolucionPantalla = resolucionPantalla;
        CostoPromedioCompra = costoPromedioCompra;
        PrecioDetalle = precioDetalle;
        PrecioMayorista = precioMayorista;
        CantMinMayorista = cantMinMayorista;
        CantidadAlerta = cantidadAlerta;
        StockActual = stockActual;
        this.MesesGarantia = MesesGarantia;
    }

    public Laptop() {
    }

    public Laptop(String idLaptop){this.IdLaptop=idLaptop;}

    public String getIdLaptop() {
        return IdLaptop;
    }

    public void setIdLaptop(String idLaptop) {
        IdLaptop = idLaptop;
    }

    public String getNumeroModelo() {
        return NumeroModelo;
    }

    public void setNumeroModelo(String numeroModelo) {
        NumeroModelo = numeroModelo;
    }

    public String getNombreComercial() {
        return NombreComercial;
    }

    public void setNombreComercial(String nombreComercial) {
        NombreComercial = nombreComercial;
    }

    public String getProcesador() {
        return procesador;
    }

    public void setProcesador(String procesador) {
        this.procesador = procesador;
    }

    public String getGpu() {
        return gpu;
    }

    public void setGpu(String gpu) {
        this.gpu = gpu;
    }

    public String getTipoRam() {
        return TipoRam;
    }

    public void setTipoRam(String tipoRam) {
        TipoRam = tipoRam;
    }

    public float getCantidadRam() {
        return CantidadRam;
    }

    public void setCantidadRam(float cantidadRam) {
        CantidadRam = cantidadRam;
    }

    public String getTipoAlmacenamiento() {
        return TipoAlmacenamiento;
    }

    public void setTipoAlmacenamiento(String tipoAlmacenamiento) {
        TipoAlmacenamiento = tipoAlmacenamiento;
    }

    public float getCantidadAlmacenamiento() {
        return CantidadAlmacenamiento;
    }

    public void setCantidadAlmacenamiento(float cantidadAlmacenamiento) {
        CantidadAlmacenamiento = cantidadAlmacenamiento;
    }

    public float getTamanyoPantalla() {
        return TamanyoPantalla;
    }

    public void setTamanyoPantalla(float tamanyoPantalla) {
        TamanyoPantalla = tamanyoPantalla;
    }

    public String getResolucionPantalla() {
        return ResolucionPantalla;
    }

    public void setResolucionPantalla(String resolucionPantalla) {
        ResolucionPantalla = resolucionPantalla;
    }

    public float getCostoPromedioCompra() {
        return CostoPromedioCompra;
    }

    public void setCostoPromedioCompra(float costoPromedioCompra) {
        CostoPromedioCompra = costoPromedioCompra;
    }

    public float getPrecioDetalle() {
        return PrecioDetalle;
    }

    public void setPrecioDetalle(float precioDetalle) {
        PrecioDetalle = precioDetalle;
    }

    public float getPrecioMayorista() {
        return PrecioMayorista;
    }

    public void setPrecioMayorista(float precioMayorista) {
        PrecioMayorista = precioMayorista;
    }

    public int getCantMinMayorista() {
        return CantMinMayorista;
    }

    public void setCantMinMayorista(int cantMinMayorista) {
        CantMinMayorista = cantMinMayorista;
    }

    public int getCantidadAlerta() {
        return CantidadAlerta;
    }

    public void setCantidadAlerta(int cantidadAlerta) {
        CantidadAlerta = cantidadAlerta;
    }

    public int getStockActual() {
        return StockActual;
    }

    public void setStockActual(int stockActual) {
        StockActual = stockActual;
    }

    public int getMesesGarantia() {
        return MesesGarantia;
    }

    public void setMesesGarantia(int mesesGarantia) {
        MesesGarantia = mesesGarantia;
    }

    public Marca getMarca() {
        return marca;
    }

    public void setMarca(Marca marca) {
        this.marca = marca;
    }

    public float getPeso() {
        return peso;
    }

    public void setPeso(float peso) {
        this.peso = peso;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Laptop laptop = (Laptop) o;
        return Objects.equals(IdLaptop, laptop.IdLaptop);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(IdLaptop);
    }
}