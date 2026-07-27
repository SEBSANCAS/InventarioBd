package visual;

import DataBase.AdquisicionDAO;
import DataBase.EquipoDAO;
import logico.Equipo;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class MenuPrincipalController {

    @FXML
    private VBox listaAlertas;

    @FXML
    public void initialize() {
        cargarNotificaciones();
    }

    public void cargarNotificaciones() {
        listaAlertas.getChildren().clear();
        List<Equipo> equiposEnBD = EquipoDAO.getInstance().EncontrarTodos();
        List<logico.Adquisicion> adquisicionesEnBD = AdquisicionDAO.getInstance().EncontrarTodos();
        System.out.println("Adquisiciones encontradas en BD: " + (adquisicionesEnBD != null ? adquisicionesEnBD.size() : 0));
        if (adquisicionesEnBD != null) {
            for (logico.Adquisicion adq : adquisicionesEnBD) {

                if ("Recibida".equalsIgnoreCase(adq.getEstado())) {

                    for (logico.DetalleAdquisicion detalle : adq.getDetallesAdquision()) {

                        long registrados = equiposEnBD.stream()
                                .filter(e -> e.getIdAdquisicionOrigen() != null && e.getIdAdquisicionOrigen().equals(detalle.getIdDetalleAdquisicion()))
                                .count();

                        long faltantes = detalle.getCantidad() - registrados;

                        if (faltantes > 0) {
                            crearTarjetaNotificacion(adq.getIdCompra(), detalle, faltantes);
                        }
                    }
                }
            }
        }

        if (listaAlertas.getChildren().isEmpty()) {
            javafx.scene.control.Label lblVacio = new javafx.scene.control.Label("No hay equipos pendientes por registrar.");
            lblVacio.setStyle("-fx-text-fill: #7f8c8d; -fx-font-style: italic;");
            listaAlertas.getChildren().add(lblVacio);
        }
    }

    private void crearTarjetaNotificacion(String idAdq, logico.DetalleAdquisicion detalle, long faltantes) {
        javafx.scene.layout.VBox tarjeta = new javafx.scene.layout.VBox(8);
        tarjeta.setStyle("-fx-background-color: #f8f9fa; -fx-border-color: #e0e0e0; -fx-border-radius: 5; -fx-background-radius: 5; -fx-padding: 10;");

        String nombreLaptop = detalle.getModeloLaptopAdquirida().getNombreComercial();
        javafx.scene.control.Label lblInfo = new javafx.scene.control.Label("Orden: " + idAdq + "\nModelo: " + nombreLaptop + "\nFaltan: " + faltantes + " equipos.");
        lblInfo.setStyle("-fx-font-size: 13px; -fx-text-fill: #2c3e50;");
        lblInfo.setWrapText(true);

        javafx.scene.control.Button btnRegistrar = new javafx.scene.control.Button("Registrar Equipo");
        btnRegistrar.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; -fx-font-weight: bold; -fx-cursor: hand;");
        btnRegistrar.setMaxWidth(Double.MAX_VALUE);

        btnRegistrar.setOnAction(e -> {
            abrirRegistroEquipo(detalle);
            cargarNotificaciones();
        });

        tarjeta.getChildren().addAll(lblInfo, btnRegistrar);
        listaAlertas.getChildren().add(tarjeta);
    }

    private void abrirRegistroEquipo(logico.DetalleAdquisicion detalle) {
        try {
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource("/visual/RegistroEquipoAdquisicion.fxml"));
            javafx.scene.Parent root = loader.load();

            RegistroEquipoAdquisicionController controller = loader.getController();
            controller.initData(detalle);

            Stage stage = new Stage();
            stage.setTitle("Registrar Ingreso de Equipo");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    private void ControlarRegistrarCliente(ActionEvent event) {
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
            e.printStackTrace();
        }
    }

    @FXML
    private void ControlarMovimientoInventario(ActionEvent event) {
    }

    @FXML
    private void ControlarContratoSuplidor(ActionEvent event) {
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

            ManejarOrdenesController controller = loader.getController();
            controller.setMenuPrincipalController(this);

            Stage stage = new Stage();
            stage.setTitle("Manejar Órdenes de Compra");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root, 850, 600));
            stage.show();
        } catch (IOException e) {
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
            e.printStackTrace();
        }
    }
}