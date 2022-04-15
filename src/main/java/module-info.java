module com.example.demo {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.application.main to javafx.fxml;
    exports com.application.main;
}