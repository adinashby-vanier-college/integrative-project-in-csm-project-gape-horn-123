package com.example.physiplay.controllers;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class CreatePresetController {

    @FXML
    public Button createComponentButton;
    public TextField positionXField;
    public TextField positionYField;
    public TextField rotationField;
    public TextField scaleXField;
    public TextField scaleYField;


    Stage presetWindow;

    public CreatePresetController(Stage stage){
        this.presetWindow = stage;
    }

    public void initialize(){
        createComponentButton.setOnAction(event -> {
            presetWindow.hide();
        });
        numberOnly(positionXField);
        numberOnly(positionYField);
        numberOnly(rotationField);
        numberOnly(scaleXField);
        numberOnly(scaleYField);
    }

    public void numberOnly(TextField textField){
        ChangeListener<String> numbersOnly = (observableValue, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) textField.setText(newValue.replaceAll("[^\\d]", ""));
        };
        textField.textProperty().addListener(numbersOnly);
    }
}
