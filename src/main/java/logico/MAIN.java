package logico;

import java.time.LocalDate;

public class MAIN {
    public static void main(String[] args) {

        Servicio servicio = Servicio.getInstance();


        Marca marca = new Marca(
                "MAR-001",
                "Dell"
        );

        servicio.registrarMarca(marca);


        Suplidor suplidor = new Suplidor(
                "131234567",
                "ventas@tech.com",
                "8095551234",
                "Tech Supplier",
                "SUP-001",
                "Tech Supplier SRL",
                "República Dominicana",
                "Santiago",
                "Calle Duarte #25",
                LocalDate.now()
        );

        servicio.registrarSuplidor(suplidor);

        Cliente cliente = new Cliente(
                "40212345678",
                "junior@gmail.com",
                "8095551234",
                "CLI-001",
                "Junior",
                "Espinal",
                "Regular"
        );


        servicio.registrarCliente(cliente);

        Estante estante = new Estante(
                "EST-001",
                5,
                4,
                "Almacén Principal"
        );

        servicio.registrarEstante(estante);

        Laptop laptop = new Laptop(
                "LAP-001",
                "Inspiron 15",
                "Dell Inspiron",
                marca,
                2.3f,
                "Core i7",
                "RTX 3050",
                "DDR5",
                16,
                "SSD",
                512,
                15.6f,
                "1920x1080",
                50000,
                65000,
                62000,
                5,
                3,
                10,
                24
        );

        servicio.registrarLaptop(laptop);

        System.out.println("====================================");
        System.out.println("Todos los registros fueron creados.");
        System.out.println("Clientes : " + servicio.getMisClientes().size());
        System.out.println("Marcas   : " + servicio.getMisMarcas().size());
        System.out.println("Suplidores : " + servicio.getMisSuplidores().size());
        System.out.println("Estantes : " + servicio.getMisEstantes().size());
        System.out.println("Laptops  : " + servicio.getMisLaptops().size());
        System.out.println("====================================");
    }
}
