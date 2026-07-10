module com.example.inventarioproyectobd {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.inventarioproyectobd to javafx.fxml;
    exports com.example.inventarioproyectobd;
}