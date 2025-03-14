package com.example.physiplay.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

public class PhysiplayController {

    @FXML
    Button createPresetButton;
    @FXML
    Button startButton;

    public void initialize() {
        createPresetButton.setOnAction(event -> {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/createPreset.fxml"));
            Parent root = null;
            try {
                root = loader.load();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            Stage presetWindow = new Stage();
            Scene scene = new Scene(root, 500, 900);
            presetWindow.setScene(scene);
            presetWindow.show();
        });
    }
}
