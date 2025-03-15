package com.example.physiplay.controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class CreatePresetController {

    @FXML
    Button addComponentButton;

    Stage presetWindow;

    public CreatePresetController(Stage stage){
        this.presetWindow = stage;
    }

    public void initialize(){
        addComponentButton.setOnAction(event -> {
            presetWindow.hide();
        });
    }
}
