module com.example.inventarioproyectobd {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires mysql.connector.j;


    opens com.example.inventarioproyectobd to javafx.fxml;
    exports com.example.inventarioproyectobd;

    opens visual to javafx.fxml;
    exports visual;
}