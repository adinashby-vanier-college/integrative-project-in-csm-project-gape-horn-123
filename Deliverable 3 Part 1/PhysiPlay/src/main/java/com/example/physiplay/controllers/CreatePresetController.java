package com.example.physiplay.controllers;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.stage.Stage;

import java.util.*;

public class CreatePresetController {

    // TODO: Accordion CSS styling
    @FXML
    private Accordion componentAccordion;
    @FXML
    public Button createComponentButton;
    public TextField positionXField;
    public TextField positionYField;
    public TextField rotationField;
    public TextField scaleXField;
    public TextField scaleYField;

    private final List<TitledPane> openPanes = new ArrayList<>();
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
