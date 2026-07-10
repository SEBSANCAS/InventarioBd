package logico;

import java.util.ArrayList;

public class Estante {
    private String IdEstante;
    private int capacidad;
    private int CantidadNiveles;
    private String Ubicacion;
    private ArrayList<Equipo> EquiposAlmacenados;

    public Estante(String idEstante, int capacidad, int cantidadNiveles, String ubicacion) {
        IdEstante = idEstante;
        this.capacidad = capacidad;
        CantidadNiveles = cantidadNiveles;
        Ubicacion = ubicacion;
        EquiposAlmacenados = new ArrayList<>();
    }

    public String getIdEstante() {
        return IdEstante;
    }

    public void setIdEstante(String idEstante) {
        IdEstante = idEstante;
    }

    public int getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(int capacidad) {
        this.capacidad = capacidad;
    }

    public int getCantidadNiveles() {
        return CantidadNiveles;
    }

    public void setCantidadNiveles(int cantidadNiveles) {
        CantidadNiveles = cantidadNiveles;
    }

    public String getUbicacion() {
        return Ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        Ubicacion = ubicacion;
    }

    public ArrayList<Equipo> getEquiposAlmacenados() {
        return EquiposAlmacenados;
    }

    public void setEquiposAlmacenados(ArrayList<Equipo> equiposAlmacenados) {
        EquiposAlmacenados = equiposAlmacenados;
    }
}
