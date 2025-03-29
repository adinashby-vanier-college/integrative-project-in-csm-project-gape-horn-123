package com.example.physiplay.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.util.ArrayList;

public class TabController {
    @FXML
    Label gameObjectName;
    @FXML
    public TextField presetNameField;
    public TextField positionXField;
    public TextField positionYField;
    public TextField rotationField;
    public TextField scaleXField;
    public TextField scaleYField;

    ArrayList<TextField> textFields;

    String xValue;
    String yValue;
    String rotationValue;
    String scaleXValue;
    String scaleYValue;

    public TabController(ArrayList<TextField> textFields) {

        this.textFields = textFields;

        presetNameField = textFields.getFirst();
        xValue = textFields.get(1).getText();
        yValue = textFields.get(2).getText();
        rotationValue = textFields.get(3).getText();
        scaleXValue = textFields.get(4).getText();
        scaleYValue = textFields.get(5).getText();

    }

    public void initialize(){
        gameObjectName.setText(presetNameField.getText());
        positionXField.setText(textFields.get(1).getText());
        positionYField.setText(textFields.get(2).getText());
        rotationField.setText(textFields.get(3).getText());
        scaleXField.setText(textFields.get(4).getText());
        scaleYField.setText(textFields.get(5).getText());
    }

}
