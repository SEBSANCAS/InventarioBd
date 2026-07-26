package visual;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class VentanaListadosController {

    @FXML private ComboBox<String> cbListados;

    @FXML
    public void initialize() {
        cbListados.setItems(FXCollections.observableArrayList(
                "Listado de Clientes",
                "Listado de Laptops",
                "Listado de Estantes",
                "Listado de Suplidores",
                "Listado de Marcas",
                "Listado de Adquisiciones",
                "Listado de Facturas",
                "Listado de Reclamos"
        ));
        cbListados.getSelectionModel().selectFirst();
    }

    @FXML
    private void handleAbrirListado() {
        String seleccion = cbListados.getValue();
        if (seleccion == null) return;

        String fxmlPath = "";
        String titulo = "";

        switch (seleccion) {
            case "Listado de Clientes":
                fxmlPath = "/visual/ListaClientes.fxml";
                titulo = "Gestión y Listado de Clientes";
                break;
            case "Listado de Laptops":
                fxmlPath = "/visual/ListaLaptop.fxml";
                titulo = "Gestión y Listado de Laptops";
                break;
            case "Listado de Estantes":
                fxmlPath = "/visual/ListaEstante.fxml";
                titulo = "Gestión y Listado de Estantes";
                break;
            case "Listado de Suplidores":
                fxmlPath = "/visual/ListaSuplidor.fxml";
                titulo = "Gestión y Listado de Suplidores";
                break;
            case "Listado de Marcas":
                fxmlPath = "/visual/ListaMarca.fxml";
                titulo = "Gestión y Listado de Marcas";
                break;
            case "Listado de Adquisiciones":
                fxmlPath = "/visual/ListaAdquisicion.fxml";
                titulo = "Historial de Adquisiciones";
                break;
            case "Listado de Facturas":
                fxmlPath = "/visual/ListaFactura.fxml";
                titulo = "Historial de Facturación";
                break;
            case "Listado de Reclamos":
                fxmlPath = "/visual/ListaReclamo.fxml";
                titulo = "Gestión de Reclamos";
                break;
        }

        abrirVentanaListado(fxmlPath, titulo);

        // Cerrar selector tras abrir la vista
        Stage stage = (Stage) cbListados.getScene().getWindow();
        stage.close();
    }

    private void abrirVentanaListado(String fxmlPath, String titulo) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle(titulo);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            System.out.println("Error al cargar " + fxmlPath + ": " + e.getMessage());
            e.printStackTrace();

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error de Carga");
            alert.setHeaderText(null);
            alert.setContentText("No se pudo cargar el FXML: " + fxmlPath);
            alert.showAndWait();
        }
    }

    @FXML
    private void handleCancelar() {
        Stage stage = (Stage) cbListados.getScene().getWindow();
        stage.close();
    }
}