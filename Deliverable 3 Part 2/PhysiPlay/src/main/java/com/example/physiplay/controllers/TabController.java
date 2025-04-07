package com.example.physiplay.controllers;

import com.example.physiplay.NumberOnlyTextField;
import com.example.physiplay.singletons.SimulationManager;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.util.ArrayList;
import java.util.Objects;

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
    }

    public void initialize(){
        allNumberOnly();
        getOldText();
        System.out.println(SimulationManager.getInstance().simulationObjectList.getFirst().position.x);
    }

    private void getOldText() {
        int index;
        if (!SimulationManager.getInstance().simulationObjectList.isEmpty()) {
            index = 0;
            for (int i = 0; i < SimulationManager.getInstance().simulationObjectList.size(); i++) {
                if (Objects.equals(SimulationManager.getInstance().simulationObjectList.get(i).name, textFields.getFirst().getText())){
                    index = i;
                }
            }
            presetNameField = textFields.getFirst();
            xValue = String.valueOf(SimulationManager.getInstance().simulationObjectList.get(index).position.x);
            yValue = String.valueOf(SimulationManager.getInstance().simulationObjectList.get(index).position.y);
            rotationValue = String.valueOf(SimulationManager.getInstance().simulationObjectList.get(index).angle);

            gameObjectName.setText(presetNameField.getText());
            positionXField.setText(xValue);
            positionYField.setText(yValue);
            rotationField.setText(rotationValue);
        }
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
