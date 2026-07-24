package visual;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class RegistroView extends BorderPane {

    private TabPane mainTabs;

    // Componentes - Marca
    private TextField txtIdMarca, txtNombreMarca, txtPaisMarca;
    private TextArea txtDescMarca;
    private Button btnGuardarMarca, btnLimpiarMarca, btnCancelarMarca;
    private Label lblStatusMarca;

    // Componentes - Cliente
    private TextField txtIdCliente, txtNombreCliente, txtDocCliente, txtTelCliente, txtEmailCliente;
    private TextArea txtDireccionCliente;
    private Button btnGuardarCliente, btnLimpiarCliente, btnCancelarCliente;
    private Label lblStatusCliente;

    public RegistroView() {
        this.setPrefSize(1100, 720);
        this.setStyle("-fx-background-color: #F4F6F9;");

        // 1. Encabezado Azul
        this.setTop(crearHeader());

        // 2. TabPane Central con las Pestañas
        mainTabs = new TabPane();
        mainTabs.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        // Agregar las pestañas principales
        mainTabs.getTabs().addAll(
                crearTabCliente(),
                crearTabMarca(),
                crearTabProximamente("Suplidor"),
                crearTabProximamente("Estante"),
                crearTabProximamente("Laptop"),
                crearTabProximamente("Equipo Técnico"),
                crearTabProximamente("Adquisición"),
                crearTabProximamente("Factura")
        );

        this.setCenter(mainTabs);
    }

    public void seleccionarPestana(int indice) {
        if (mainTabs != null && indice >= 0 && indice < mainTabs.getTabs().size()) {
            mainTabs.getSelectionModel().select(indice);
        }
    }
    // --- ENCABEZADO ---
    private VBox crearHeader() {
        VBox header = new VBox(4);
        header.setPadding(new Insets(18, 30, 18, 30));
        header.setStyle("-fx-background-color: #1B4F72;");

        Label title = new Label("Gestión de Registros del Sistema");
        title.setTextFill(Color.WHITE);
        title.setFont(Font.font("System", FontWeight.BOLD, 20));

        Label subtitle = new Label("Selecciona el módulo para agregar nuevas entidades al inventario");
        subtitle.setTextFill(Color.web("#ECF0F1"));
        subtitle.setFont(Font.font("System", 13));

        header.getChildren().addAll(title, subtitle);
        return header;
    }

    // --- FORMULARIO CLIENTE ---
    private Tab crearTabCliente() {
        Tab tab = new Tab("Cliente");

        VBox card = crearTarjetaBase("Registro de Cliente", "Complete los datos del nuevo cliente.");
        GridPane grid = crearGridBase();

        txtIdCliente = crearTextField("Auto-generado por el sistema", true);
        txtNombreCliente = crearTextField("Nombre completo", false);
        txtDocCliente = crearTextField("000-0000000-0", false);
        txtTelCliente = crearTextField("(809) 000-0000", false);
        txtEmailCliente = crearTextField("cliente@correo.com", false);

        txtDireccionCliente = new TextArea();
        txtDireccionCliente.setPromptText("Dirección física");
        txtDireccionCliente.setPrefRowCount(2);
        txtDireccionCliente.setWrapText(true);
        aplicarEstiloInput(txtDireccionCliente);

        grid.add(crearLabel("ID Cliente:"), 0, 0);
        grid.add(txtIdCliente, 1, 0);

        grid.add(crearLabel("Nombre:"), 0, 1);
        grid.add(txtNombreCliente, 1, 1);

        grid.add(crearLabel("Cédula / RNC:"), 0, 2);
        grid.add(txtDocCliente, 1, 2);

        grid.add(crearLabel("Teléfono:"), 0, 3);
        grid.add(txtTelCliente, 1, 3);

        grid.add(crearLabel("Correo:"), 0, 4);
        grid.add(txtEmailCliente, 1, 4);

        grid.add(crearLabel("Dirección:"), 0, 5);
        grid.add(txtDireccionCliente, 1, 5);

        lblStatusCliente = new Label();
        lblStatusCliente.setVisible(false);

        btnCancelarCliente = crearBoton("Cancelar", "#transparent", "#C0392B");
        btnLimpiarCliente = crearBoton("Limpiar Campos", "#E0E0E0", "#2C3E50");
        btnGuardarCliente = crearBoton("Guardar / Registrar", "#1B4F72", "#FFFFFF");

        HBox acciones = new HBox(12, btnCancelarCliente, btnLimpiarCliente, btnGuardarCliente);
        acciones.setAlignment(Pos.CENTER_RIGHT);

        card.getChildren().addAll(grid, lblStatusCliente, acciones);

        StackPane container = new StackPane(card);
        container.setPadding(new Insets(30));
        tab.setContent(container);

        return tab;
    }

    // --- FORMULARIO MARCA ---
    private Tab crearTabMarca() {
        Tab tab = new Tab("Marca");

        VBox card = crearTarjetaBase("Registro de Marca", "Añada una nueva marca al catálogo.");
        GridPane grid = crearGridBase();

        txtIdMarca = crearTextField("Auto-generado por el sistema", true);
        txtNombreMarca = crearTextField("Nombre de la marca", false);
        txtPaisMarca = crearTextField("País de origen", false);

        txtDescMarca = new TextArea();
        txtDescMarca.setPromptText("Notas o descripción");
        txtDescMarca.setPrefRowCount(3);
        txtDescMarca.setWrapText(true);
        aplicarEstiloInput(txtDescMarca);

        grid.add(crearLabel("ID Marca:"), 0, 0);
        grid.add(txtIdMarca, 1, 0);

        grid.add(crearLabel("Nombre:"), 0, 1);
        grid.add(txtNombreMarca, 1, 1);

        grid.add(crearLabel("País de Origen:"), 0, 2);
        grid.add(txtPaisMarca, 1, 2);

        grid.add(crearLabel("Descripción:"), 0, 3);
        grid.add(txtDescMarca, 1, 3);

        lblStatusMarca = new Label();
        lblStatusMarca.setVisible(false);

        btnCancelarMarca = crearBoton("Cancelar", "#transparent", "#C0392B");
        btnLimpiarMarca = crearBoton("Limpiar Campos", "#E0E0E0", "#2C3E50");
        btnGuardarMarca = crearBoton("Guardar / Registrar", "#1B4F72", "#FFFFFF");

        HBox acciones = new HBox(12, btnCancelarMarca, btnLimpiarMarca, btnGuardarMarca);
        acciones.setAlignment(Pos.CENTER_RIGHT);

        card.getChildren().addAll(grid, lblStatusMarca, acciones);

        StackPane container = new StackPane(card);
        container.setPadding(new Insets(30));
        tab.setContent(container);

        return tab;
    }

    // --- COMPONENTES Y ESTILOS AUXILIARES ---
    private VBox crearTarjetaBase(String titulo, String subtitulo) {
        VBox card = new VBox(15);
        card.setMaxWidth(640);
        card.setPadding(new Insets(25));
        card.setStyle(
                "-fx-background-color: #FFFFFF;" +
                        "-fx-background-radius: 8px;" +
                        "-fx-border-radius: 8px;" +
                        "-fx-border-color: #E0E0E0;" +
                        "-fx-border-width: 1px;"
        );

        Label lblTitle = new Label(titulo);
        lblTitle.setFont(Font.font("System", FontWeight.BOLD, 18));
        lblTitle.setTextFill(Color.web("#1B4F72"));

        Label lblSubtitle = new Label(subtitulo);
        lblSubtitle.setTextFill(Color.web("#7F8C8D"));

        card.getChildren().addAll(lblTitle, lblSubtitle);
        return card;
    }

    private GridPane crearGridBase() {
        GridPane grid = new GridPane();
        grid.setHgap(15);
        grid.setVgap(12);

        ColumnConstraints col1 = new ColumnConstraints(150);
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setHgrow(Priority.ALWAYS);

        grid.getColumnConstraints().addAll(col1, col2);
        return grid;
    }

    private Label crearLabel(String texto) {
        Label lbl = new Label(texto);
        lbl.setFont(Font.font("System", FontWeight.BOLD, 13));
        lbl.setTextFill(Color.web("#2C3E50"));
        return lbl;
    }

    private TextField crearTextField(String placeholder, boolean deshabilitado) {
        TextField tf = new TextField();
        tf.setPromptText(placeholder);
        tf.setDisable(deshabilitado);
        aplicarEstiloInput(tf);
        if (deshabilitado) {
            tf.setStyle("-fx-background-color: #EAEDED; -fx-border-color: #BDC3C7; -fx-border-radius: 4px;");
        }
        return tf;
    }

    private void aplicarEstiloInput(Control c) {
        c.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #BDC3C7; -fx-border-radius: 4px; -fx-padding: 6px;");
    }

    private Button crearBoton(String texto, String bgHex, String textHex) {
        Button btn = new Button(texto);
        btn.setStyle(
                String.format("-fx-background-color: %s; -fx-text-fill: %s; -fx-font-weight: bold; -fx-background-radius: 4px; -fx-cursor: hand; -fx-padding: 8 16 8 16;", bgHex, textHex)
        );
        return btn;
    }

    private Tab crearTabProximamente(String nombre) {
        Tab tab = new Tab(nombre);
        StackPane pane = new StackPane(new Label("Formulario de " + nombre));
        tab.setContent(pane);
        return tab;
    }

    // Getters para conectar con tus controladores o eventos
    public Button getBtnGuardarMarca() { return btnGuardarMarca; }
    public Button getBtnLimpiarMarca() { return btnLimpiarMarca; }
    public TextField getTxtNombreMarca() { return txtNombreMarca; }
    public Label getLblStatusMarca() { return lblStatusMarca; }
}