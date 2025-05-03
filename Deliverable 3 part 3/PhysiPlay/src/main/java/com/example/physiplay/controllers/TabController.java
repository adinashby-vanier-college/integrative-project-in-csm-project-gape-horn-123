package com.example.physiplay.controllers;

import com.example.physiplay.Component;
import com.example.physiplay.NumberOnlyTextField;
import com.example.physiplay.SimulationObject;
import com.example.physiplay.singletons.SimulationManager;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;

import java.util.ArrayList;
import java.util.Objects;

public class TabController {
    @FXML
    Label gameObjectName;
    @FXML
    public TextField presetNameField;
    @FXML
    TabPane tabPane;
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
    private SimulationObject target;

    public TabController(SimulationObject target) {
        this.target = target;
    }

    public void initialize(){
        allNumberOnly();
        getOldText();
        for (Component component : target.getComponents()) {
            tabPane.getTabs().add(component.displayComponent());
        }
    }

    private void getOldText() {
        if (target == null) return;
        xValue = String.valueOf(target.position.x);
        yValue = String.valueOf(target.position.y);
        rotationValue = String.valueOf(target.angle);

        gameObjectName.setText(target.name);
        positionXField.setText(xValue);
        positionYField.setText(yValue);
        rotationField.setText(rotationValue);
    }

    public void allNumberOnly(){
        NumberOnlyTextField numberOnlyTextField = new NumberOnlyTextField();
        numberOnlyTextField.numberOnly(positionXField);
        numberOnlyTextField.numberOnly(positionYField);
        numberOnlyTextField.numberOnly(rotationField);
        numberOnlyTextField.numberOnly(scaleXField);
        numberOnlyTextField.numberOnly(scaleYField);
    }

}
