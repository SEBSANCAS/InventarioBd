package logico;

public class DetalleLaptopSuplidor {
    private int DiasEntrega;
    private float PrecioAcordado;

    public DetalleLaptopSuplidor(int diasEntrega, float precioAcordado) {
        DiasEntrega = diasEntrega;
        PrecioAcordado = precioAcordado;
    }

    public int getDiasEntrega() {
        return DiasEntrega;
    }

    public void setDiasEntrega(int diasEntrega) {
        DiasEntrega = diasEntrega;
    }

    public float getPrecioAcordado() {
        return PrecioAcordado;
    }

    public void setPrecioAcordado(float precioAcordado) {
        PrecioAcordado = precioAcordado;
    }
}
