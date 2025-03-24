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

    public TabController(ArrayList<TextField> textFields) {
        presetNameField = textFields.getFirst();
        positionXField = textFields.get(1);
        positionYField = textFields.get(2);
        rotationField = textFields.get(3);
        scaleXField = textFields.get(4);
        scaleYField = textFields.get(5);

    }

    public void initialize(){
        gameObjectName.setText(presetNameField.getText());
        positionXField.setText(positionXField.getText());
        positionYField.setText(positionXField.getText());
        rotationField.setText(rotationField.getText());
        scaleXField.setText(scaleXField.getText());
        scaleYField.setText(scaleYField.getText());
    }

}
