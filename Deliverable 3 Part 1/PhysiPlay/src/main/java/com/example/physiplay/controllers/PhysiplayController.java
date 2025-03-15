package com.example.physiplay.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.EventListener;
import java.util.Objects;

public class PhysiplayController {

    Stage mainWindow;
    @FXML
    Button createPresetButton;
    @FXML
    Button startButton;

    public PhysiplayController(Stage stage){
        this.mainWindow = stage;
    }

    public void initialize() {
        System.out.println("Hello");
        createPresetButton.setOnAction(event -> {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/createPreset.fxml"));
            Stage presetWindow = new Stage();
            Parent root = null;
            try {
                root = loader.load();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            Scene scene = new Scene(root, 500, 900);
            scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/fonts/stylesheetPresetWindow.css")).toExternalForm());
            loader.setController(new CreatePresetController(presetWindow));
            presetWindow.setScene(scene);
            presetWindow.initModality(Modality.WINDOW_MODAL);
            presetWindow.initOwner(mainWindow);
            presetWindow.show();
        });
    }
}
