package visual;

import DataBase.ServicioDAO;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import logico.Servicio;
import org.kordamp.ikonli.fontawesome6.FontAwesomeSolid;

public class Main extends Application {


    @Override
    public void start(Stage primaryStage) throws Exception {
        DataBase.ServicioDAO.getInstance().cargarTodoElSistema();

        Parent root = FXMLLoader.load(getClass().getResource("/visual/Principal.fxml"));
        primaryStage.setTitle("Sistema de Inventario");
        primaryStage.setScene(new Scene(root, 1300, 800));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}