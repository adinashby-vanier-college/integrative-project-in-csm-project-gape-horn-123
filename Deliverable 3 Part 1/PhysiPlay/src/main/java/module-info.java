module com.example.physiplay {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.physiplay to javafx.fxml;
    exports com.example.physiplay;
    exports com.example.physiplay.controllers;
    opens com.example.physiplay.controllers to javafx.fxml;
}