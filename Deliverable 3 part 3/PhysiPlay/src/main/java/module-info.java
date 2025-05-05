module com.example.physiplay {
    requires javafx.controls;
    requires javafx.fxml;
    requires jbox2d.library;
	requires javafx.graphics;
    requires com.google.gson;
    requires org.junit.jupiter.api;
    requires org.junit.jupiter.params;

    opens com.example.physiplay to javafx.fxml, org.junit.platform.commons;
    exports com.example.physiplay;
    exports com.example.physiplay.widgets;
    exports com.example.physiplay.components;
    opens com.example.physiplay.components to com.google.gson;
    exports com.example.physiplay.controllers;
    opens com.example.physiplay.controllers to javafx.fxml;
}