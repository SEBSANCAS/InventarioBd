package visual;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class MenuPrincipalController {

    @FXML
    private void ControlarRegistrarCliente(ActionEvent event) {
        System.out.println("Abriendo Registro de Cliente...");
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/visual/RegistroCliente.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Sistema de Inventario - Registro de Cliente");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void ControlarRegistrarMarca(ActionEvent event) {
        System.out.println("Abriendo Registro de Marca...");
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/visual/RegistroMarca.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Sistema de Inventario - Registro de Marca");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void ControlarRegistrarModelo(ActionEvent event) {
        System.out.println("Abriendo Registro de Modelo / Laptop...");
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/visual/RegistroLaptop.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Sistema de Inventario - Registro de Laptop");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void ControlarRegistrarSuplidor(ActionEvent event) {
        System.out.println("Abriendo Registro de Suplidor...");
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/visual/RegistroSuplidor.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Sistema de Inventario - Registro de Suplidor");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void ControlarRegistrarEstante(ActionEvent event) {
        System.out.println("Abriendo Registro de Estante...");
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/visual/RegistroEstante.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Sistema de Inventario - Registro de Estante");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void ControlarListados() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/visual/VentanaListados.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Panel de Listados del Sistema");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            System.out.println("Error al abrir el selector de listados: " + e.getMessage());
            e.printStackTrace();
        }
    }
    @FXML
    private void ControlarMovimientoInventario(ActionEvent event) {
        System.out.println("Abriendo Movimiento de Inventario...");
    }

    @FXML
    private void ControlarContratoSuplidor(ActionEvent event) {
        System.out.println("Abriendo Contrato de Suplidor...");
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/visual/RegistroSuplidorLaptop.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Sistema de Inventario - Registro de Acuerdo con Suplidor");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void ControlarOrdenar(ActionEvent event) {
        System.out.println("Abriendo Ordenar...");
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/visual/RegistroAdquisicion.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Sistema de Inventario - Registro de Adquisición");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void ControlarRealizarVenta(ActionEvent event) {
        System.out.println("Abriendo Realizar Venta...");
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/visual/RegistroFacturacion.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Sistema de Inventario - Registro de Facturacion");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void ControlarManejarOrdenes() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/visual/ManejarOrdenes.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Manejar Órdenes de Compra");
            stage.initModality(Modality.APPLICATION_MODAL); // Abre la ventana en modo modal
            stage.setScene(new Scene(root, 850, 600));
            stage.show();
        } catch (IOException e) {
            System.out.println("Error al abrir la vista ManejarOrdenes.fxml: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void abrirRegistroReclamo(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/visual/RegistroReclamo.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Gestión de Reclamos y Garantías");
            stage.setScene(new Scene(root));

            stage.show();

        } catch (IOException e) {
            System.out.println("No se pudo abrir la ventana de reclamos: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void ControlarResolverReclamo(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/visual/ResolverReclamo.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Resolver Reclamo / Garantía");
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            System.out.println("No se pudo abrir la ventana de resolver reclamo: " + e.getMessage());
            e.printStackTrace();
        }
    }
}