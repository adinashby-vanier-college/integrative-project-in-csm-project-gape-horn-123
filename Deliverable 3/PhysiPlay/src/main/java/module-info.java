module com.example.physiplay {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.physiplay to javafx.fxml;
    exports com.example.physiplay;
}