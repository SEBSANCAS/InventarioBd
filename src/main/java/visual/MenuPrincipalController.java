package visual;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class MenuPrincipalController {

    // --- MÉTODOS DE REGISTRO ---

    @FXML
    private void ControlarRegistrarCliente(ActionEvent event) {
        // Abre la ventana individual de Cliente
        ClienteRegistroView view = new ClienteRegistroView();
        mostrarVentana(view, "Registrar Nuevo Cliente");
    }

    @FXML
    private void ControlarRegistrarMarca(ActionEvent event) {
        System.out.println("Abriendo Registro de Marca...");
        // Cuando tengas MarcaRegistroView la conectas así:
        // MarcaRegistroView view = new MarcaRegistroView();
        // mostrarVentana(view, "Registrar Nueva Marca");
    }

    @FXML
    private void ControlarRegistrarModelo(ActionEvent event) {
        System.out.println("Abriendo Registro de Modelo / Laptop...");
        // Cuando tengas LaptopRegistroView la conectas así:
        // LaptopRegistroView view = new LaptopRegistroView();
        // mostrarVentana(view, "Registrar Nuevo Modelo de Laptop");
    }

    @FXML
    private void ControlarRegistrarSuplidor(ActionEvent event) {
        System.out.println("Abriendo Registro de Suplidor...");
        // SuplidorRegistroView view = new SuplidorRegistroView();
        // mostrarVentana(view, "Registrar Nuevo Suplidor");
    }

    @FXML
    private void ControlarRegistrarEstante(ActionEvent event) {
        System.out.println("Abriendo Registro de Estante...");
        // EstanteRegistroView view = new EstanteRegistroView();
        // mostrarVentana(view, "Registrar Nuevo Estante");
    }

    // --- MÉTODOS DE MENÚ SECUNDARIOS ---

    @FXML
    private void ControlarListados(ActionEvent event) {
        System.out.println("Abriendo Listados...");
    }

    @FXML
    private void ControlarMovimientoInventario(ActionEvent event) {
        System.out.println("Abriendo Movimiento de Inventario...");
    }

    @FXML
    private void ControlarContratoSuplidor(ActionEvent event) {
        System.out.println("Abriendo Contrato de Suplidor...");
    }

    @FXML
    private void ControlarOrdenar(ActionEvent event) {
        System.out.println("Abriendo Ordenar...");
    }

    @FXML
    private void ControlarRealizarVenta(ActionEvent event) {
        System.out.println("Abriendo Realizar Venta...");
    }

    @FXML
    private void ControlarReclamo(ActionEvent event) {
        System.out.println("Abriendo Reclamo...");
    }

    @FXML
    private void ControlarResolverReclamo(ActionEvent event) {
        System.out.println("Abriendo Resolver Reclamo...");
    }

    // --- MÉTODO REUTILIZABLE PARA ABRIR VENTANAS ---

    private void mostrarVentana(Pane vista, String titulo) {
        Stage stage = new Stage();
        stage.setTitle("Sistema de Inventario - " + titulo);
        stage.setScene(new Scene(vista));

        // Impide interactuar con el menú principal hasta cerrar la ventana de registro
        stage.initModality(Modality.APPLICATION_MODAL);

        stage.show();
    }
}