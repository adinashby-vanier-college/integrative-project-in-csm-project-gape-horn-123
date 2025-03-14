package com.example.physiplay.controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class PhysiplayController {

    @FXML
    Button createPresetButton;
    @FXML
    Button startButton;

    public void initialize() {
        createPresetButton.setOnAction(event -> {
            Parent root = new Pane();
            Stage presetWindow = new Stage();
            Scene scene = new Scene(root, 500, 900);
            presetWindow.setScene(scene);
            presetWindow.show();
        });
    }
}
