package com.example.physiplay.controllers;

import com.example.physiplay.SimulationObject;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.util.*;

public class CreatePresetController {

    // TODO: Accordion CSS styling
    @FXML
    private Accordion componentAccordion;
    @FXML
    public Button createPresetButton;
    public TextField positionXField;
    public TextField positionYField;
    public TextField rotationField;
    public TextField scaleXField;
    public TextField scaleYField;

    ArrayList<SimulationObject> presetList;
    HBox presetHBox;

    private final List<TitledPane> openPanes = new ArrayList<>();
    Stage presetWindow;

    public CreatePresetController(Stage stage, HBox presetHBox, ArrayList<SimulationObject> list){
        this.presetWindow = stage;
        this.presetHBox = presetHBox;
        this.presetList = list;
    }

    public void initialize(){
        createPresetButton.setOnAction(event -> createPreset());
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

    public void setPresetList(ArrayList<SimulationObject> list){
        this.presetList = list;
    }

    public void createPreset(){
        presetHBox.getChildren().add(new Rectangle(10,10));
        presetWindow.hide();
    }
}
