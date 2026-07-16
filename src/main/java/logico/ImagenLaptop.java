package logico;

public class ImagenLaptop {

    private int idImagen;
    private Laptop laptop;
    private byte[] imagen;

    public ImagenLaptop(int idImagen, Laptop laptop, byte[] imagen) {
        this.idImagen = idImagen;
        this.laptop = laptop;
        this.imagen = imagen;
    }

    public int getIdImagen() {
        return idImagen;
    }

    public void setIdImagen(int idImagen) {
        this.idImagen = idImagen;
    }

    public Laptop getLaptop() {
        return laptop;
    }

    public void setLaptop(Laptop laptop) {
        this.laptop = laptop;
    }

    public byte[] getImagen() {
        return imagen;
    }

    public void setImagen(byte[] imagen) {
        this.imagen = imagen;
    }
}
