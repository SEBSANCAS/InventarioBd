package logico;

import java.util.ArrayList;

public class Estante {
    private String IdEstante;
    private int capacidad;
    private int CantidadNiveles;
    private ArrayList<Equipo> EquiposAlmacenados;

    public Estante(String idEstante, int capacidad, int cantidadNiveles) {
        IdEstante = idEstante;
        this.capacidad = capacidad;
        CantidadNiveles = cantidadNiveles;
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

    public ArrayList<Equipo> getEquiposAlmacenados() {
        return EquiposAlmacenados;
    }

    public void setEquiposAlmacenados(ArrayList<Equipo> equiposAlmacenados) {
        EquiposAlmacenados = equiposAlmacenados;
    }
}
