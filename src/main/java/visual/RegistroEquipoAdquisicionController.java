package visual;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import logico.DetalleAdquisicion;
import logico.Equipo;
import logico.Laptop;
import logico.Servicio;

import java.time.LocalDate;

public class RegistroEquipoAdquisicionController {

    @FXML
    private Label lblContexto;

    @FXML
    private TextField campoIdEquipo;

    @FXML
    private TextField campoSerial;

    @FXML
    private TextField campoColor;

    @FXML
    private Label lblMensaje;

    private DetalleAdquisicion detalleAsociado;

    public void initData(DetalleAdquisicion detalle) {
        this.detalleAsociado = detalle;

        Laptop lap = Servicio.getInstance().getMisLaptops().get(detalle.getModeloLaptopAdquirida().getIdLaptop());
        lblContexto.setText("Modelo: " + lap.getIdLaptop() + " - " + lap.getNombreComercial() + "\nReferencia: " + detalle.getIdDetalleAdquisicion());

        actualizarIdPreview();
    }

    private void actualizarIdPreview() {
        campoIdEquipo.setText(Servicio.getInstance().generarIdEquipo());
    }

    @FXML
    private void ControlarGuardar(ActionEvent event) {
        if (campoSerial.getText().trim().isEmpty() || campoColor.getText().trim().isEmpty()) {
            lblMensaje.setStyle("-fx-text-fill: #b23b3b;");
            lblMensaje.setText("Por favor complete el Número de Serie y el Color.");
            return;
        }

        String idEquipo = Servicio.getInstance().generarIdEquipo();
        Laptop laptopEnMemoria = Servicio.getInstance().getMisLaptops().get(detalleAsociado.getModeloLaptopAdquirida().getIdLaptop());

        // El ID de Estante se asigna en el EquipoDAO automáticamente.
        // Respetando los constraints: estado="nuevo", disponibilidad="Disponible"
        Equipo equipo = new Equipo(
                idEquipo,
                laptopEnMemoria,
                campoSerial.getText().trim(),
                campoColor.getText().trim(),
                "nuevo", // Constraint: "nuevo", "open-box", "reparado"
                "Disponible", // Constraint: "Disponible", "En Reparacion", etc.
                0.0f, // Descuento obvio
                LocalDate.now(),
                detalleAsociado.getIdDetalleAdquisicion()
        );

        // 1. Guarda el equipo en la memoria y en la BD (El DAO lo pondrá en el estante disponible)
        Servicio.getInstance().registrarEquipo(equipo);

        // 2. Sumamos 1 al stock de la laptop en la corrida (La BD se maneja con su trigger)
        laptopEnMemoria.setStockActual(laptopEnMemoria.getStockActual() + 1);

        lblMensaje.setStyle("-fx-text-fill: #2e7d32;");
        lblMensaje.setText("Equipo registrado correctamente.");

        // Cerrar la ventana tras 1 segundo para que vea el mensaje
        javafx.animation.PauseTransition delay = new javafx.animation.PauseTransition(javafx.util.Duration.seconds(1));
        delay.setOnFinished(e -> ControlarCancelar(null));
        delay.play();
    }

    @FXML
    private void ControlarCancelar(ActionEvent event) {
        Stage stage = (Stage) campoSerial.getScene().getWindow();
        stage.close();
    }
}