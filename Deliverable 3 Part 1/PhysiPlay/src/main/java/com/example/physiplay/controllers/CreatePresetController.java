package com.example.physiplay.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class CreatePresetController {

    @FXML
    Button addComponent;

    Stage presetWindow;

    public CreatePresetController(Stage stage){
        presetWindow = stage;
    }

    public void initialize(){
        addComponent.setOnAction(event -> System.out.println("SKfnsklfnsk"));
    }

}
