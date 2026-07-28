package visual;

import javafx.animation.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MenuPrincipalController {

    @FXML
    private VBox listaAlertas;
    @FXML
    private GridPane panelPrincipal;
    @FXML
    private VBox panelNotificaciones;

    @FXML
    public void initialize() {
        cargarNotificaciones();
        panelPrincipal.lookupAll(".card")
                .forEach(this::animarTarjeta);
        FadeTransition fade =
                new FadeTransition(Duration.seconds(.8), panelPrincipal);

        fade.setFromValue(0);

        fade.setToValue(1);

        fade.play();
        TranslateTransition tt =
                new TranslateTransition(Duration.seconds(.5),
                        panelNotificaciones);

        tt.setFromX(250);

        tt.setToX(0);

        tt.play();
        Timeline reloj =
                new Timeline(
                        new KeyFrame(
                                Duration.seconds(1),
                                e->lblHora.setText(
                                        LocalDateTime.now()
                                                .format(DateTimeFormatter.ofPattern("HH:mm:ss"))
                                )
                        )
                );

        reloj.setCycleCount(Animation.INDEFINITE);

        reloj.play();

    }
    @FXML
    private Label lblHora;
    public void cargarNotificaciones() {
        listaAlertas.getChildren().clear();

        for (logico.Adquisicion adq : logico.Servicio.getInstance().getMisAdquisiciones().values()) {
            if ("Recibida".equalsIgnoreCase(adq.getEstado())) {

                for (logico.DetalleAdquisicion detalle : adq.getDetallesAdquision()) {
                    long registrados = logico.Servicio.getInstance().getMisEquipos().values().stream()
                            .filter(e -> e.getIdAdquisicionOrigen() != null && e.getIdAdquisicionOrigen().equals(detalle.getIdDetalleAdquisicion()))
                            .count();

                    long faltantes = detalle.getCantidad() - registrados;

                    if (faltantes > 0) {
                        crearTarjetaNotificacion(adq.getIdCompra(), detalle, faltantes);
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
        VBox tarjeta = new VBox(12);
        tarjeta.setStyle(
                "-fx-background-color:white;" +
                        "-fx-background-radius:18;" +
                        "-fx-border-radius:18;" +
                        "-fx-border-color:#D7E3F5;" +
                        "-fx-border-width:1;" +
                        "-fx-padding:16;" +
                        "-fx-effect:dropshadow(gaussian,rgba(0,0,0,.12),12,0,0,3);");
        String nombreLaptop = detalle.getModeloLaptopAdquirida().getNombreComercial();
        Label lblInfo = new Label(

                "📦 Orden: " + idAdq +

                        "\n\nLaptop: " + nombreLaptop +

                        "\n\nPendientes: " + faltantes

        );
        lblInfo.setStyle(
                "-fx-font-size:14;" +
                        "-fx-font-weight:bold;" +
                        "-fx-text-fill:#2F3A56;");
        lblInfo.setWrapText(true);

        javafx.scene.control.Button btnRegistrar = new javafx.scene.control.Button("Registrar Equipo");
        btnRegistrar.setStyle(

                "-fx-background-color:#1565C0;" +

                        "-fx-text-fill:white;" +

                        "-fx-font-weight:bold;" +

                        "-fx-font-size:14;" +

                        "-fx-background-radius:10;" +

                        "-fx-cursor:hand;"

        );
        btnRegistrar.setPrefHeight(38);
        btnRegistrar.setMaxWidth(Double.MAX_VALUE);

        btnRegistrar.setOnAction(e -> {
            abrirRegistroEquipo(detalle);
            cargarNotificaciones();
        });

        tarjeta.getChildren().addAll(lblInfo, btnRegistrar);
        listaAlertas.getChildren().add(tarjeta);
    }
    private void animarTarjeta(Node nodo){

        ScaleTransition entrar =
                new ScaleTransition(Duration.millis(180), nodo);

        entrar.setToX(1.03);
        entrar.setToY(1.03);

        ScaleTransition salir =
                new ScaleTransition(Duration.millis(180), nodo);

        salir.setToX(1);
        salir.setToY(1);

        nodo.setOnMouseEntered(e->entrar.playFromStart());

        nodo.setOnMouseExited(e->salir.playFromStart());

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