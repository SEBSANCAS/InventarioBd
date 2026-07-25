package visual;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

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
    private void ControlarReclamo(ActionEvent event) {
        System.out.println("Abriendo Reclamo...");
    }

    @FXML
    private void ControlarResolverReclamo(ActionEvent event) {
        System.out.println("Abriendo Resolver Reclamo...");
    }
}