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
        int siguiente = Servicio.getInstance().getGenIdEquipo();
        campoIdEquipo.setText(String.format("EQP%03d", siguiente));
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

        Equipo equipo = new Equipo(
                idEquipo,
                laptopEnMemoria,
                campoSerial.getText().trim(),
                campoColor.getText().trim(),
                "nuevo",
                "Disponible",
                0.0f,
                LocalDate.now(),
                detalleAsociado.getIdDetalleAdquisicion()
        );

        // 1. Guarda el equipo (El DAO lo asignará al primer estante disponible en BD)
        Servicio.getInstance().registrarEquipo(equipo);

        // 2. Sumamos 1 al stock de la laptop en la corrida
        laptopEnMemoria.setStockActual(laptopEnMemoria.getStockActual() + 1);

        // 3. Crear el Movimiento "Entrada por Compra"
        // Obtenemos el ID de la primera ubicación disponible para usarlo como destino
        String idUbicacionDestino = DataBase.EstanteDAO.getInstance().obtenerPrimeraUbicacionDisponible();

        // Asignamos el equipo al estante en memoria para la corrida
        if (idUbicacionDestino != null) {
            String codigoEstante = idUbicacionDestino.split("-N")[0];
            logico.Estante estante = Servicio.getInstance().getMisEstantes().get(codigoEstante);
            if (estante != null) {
                estante.getEquiposAlmacenados().add(equipo);
            }
        }

        int countMov = DataBase.MovimientoDAO.getInstance().encontrarPorEquipo(equipo.getIdEquipo()).size();
        String idMovimiento = Servicio.getInstance().generarIdDependiente(equipo.getIdEquipo(), countMov);

        logico.Movimiento mov = new logico.Movimiento();
        mov.setIdMovimiento(idMovimiento);
        mov.setIdEquipo(equipo.getIdEquipo());
        mov.setTipoMovimiento("Entrada por Compra");
        mov.setDescripcionMovimiento("Ingreso desde la orden de compra.");
        mov.setFechaHoraMovimiento(java.time.LocalDateTime.now());
        mov.setIdEstanteOrigen(null); // Viene de afuera

        // Configuramos el destino extrayendo código y nivel del id_ubicacion (Ej. "EST001-N1")
        if (idUbicacionDestino != null) {
            String[] partes = idUbicacionDestino.split("-N");
            if (partes.length == 2) {
                mov.setIdEstanteDestino(partes[0]);
                mov.setNivelDestino(Integer.parseInt(partes[1]));
            }
        }

        DataBase.MovimientoDAO.getInstance().guardar(mov);

        lblMensaje.setStyle("-fx-text-fill: #2e7d32;");
        lblMensaje.setText("Equipo registrado correctamente.");

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