package com.example.physiplay.controllers;

import com.example.physiplay.Component;
import com.example.physiplay.NumberOnlyTextField;
import com.example.physiplay.SimulationObject;
import com.example.physiplay.Vector2;
import com.example.physiplay.components.Rigidbody;
import com.example.physiplay.singletons.SimulationManager;
import javafx.animation.AnimationTimer;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableStringValue;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;

import java.util.ArrayList;
import java.util.Objects;

public class TabController {
    @FXML
    Label gameObjectName;
    @FXML
    public TextField presetNameField;
    @FXML
    TabPane tabPane;
    @FXML
    private Label rotationLabel;
    @FXML
    private Label positionLabel;
    @FXML
    public TextField positionXField;
    @FXML
    public TextField positionYField;
    @FXML
    public TextField rotationField;
    @FXML
    public TextField scaleXField;
    @FXML
    public TextField scaleYField;


    String xValue;
    String yValue;
    String rotationValue;
    String scaleXValue;
    String scaleYValue;
    private SimulationObject target;

    public TabController(SimulationObject target) {
        this.target = target;
    }

    public void initialize() {
        allNumberOnly();
        new AnimationTimer() {

            @Override
            public void handle(long l) {
                updateTransformValues();
            }
        }.start();
        updateValuesThroughInputs();
        getOldText();
        for (Component component : target.getComponents()) {
            component.displayComponent();
            tabPane.getTabs().add(component.componentTab);
        }
    }

    private void getOldText() {
        if (target == null) return;
        if (target.getComponent(Rigidbody.class) != null) {
            xValue = String.valueOf(target.position.x * SimulationManager.SCALE);
            yValue = String.valueOf(target.position.y * SimulationManager.SCALE);
        }
        else {
            xValue = String.valueOf(target.position.x);
            yValue = String.valueOf(target.position.y);
        }
        rotationValue = String.valueOf(target.angle);

        gameObjectName.setText(target.name);
        positionXField.setText(xValue);
        positionYField.setText(yValue);
        rotationField.setText(rotationValue);
    }

    private void updateValuesThroughInputs() {
        positionXField.setOnAction(event -> {
            if (!positionXField.getText().isBlank())  {
                if (target.getComponent(Rigidbody.class) != null) {
                    target.getComponent(Rigidbody.class).parent.simulationObjectBodyDef.position.set(
                            new Vec2(Float.parseFloat(positionXField.getText()) / SimulationManager.SCALE,
                                    target.getComponent(Rigidbody.class).parent.simulationObjectBodyDef.position.y)
                    );
                    Vec2 pos = target.getComponent(Rigidbody.class).parent.simulationObjectBody.getPosition();
                    pos.set(target.getComponent(Rigidbody.class).parent.simulationObjectBodyDef.position);
                    target.position = new Vector2(pos.x, pos.y);
                }
                else {
                    target.position.x = Float.parseFloat(positionXField.getText());
                }
            }
        });
        positionYField.setOnAction(event -> {
            if (!positionYField.getText().isBlank())  {
                if (target.getComponent(Rigidbody.class) != null) {
                    target.getComponent(Rigidbody.class).parent.simulationObjectBodyDef.position.set(
                            new Vec2(target.getComponent(Rigidbody.class).parent.simulationObjectBodyDef.position.x,
                                    Float.parseFloat(positionYField.getText()) / SimulationManager.SCALE)
                    );
                    Vec2 pos = target.getComponent(Rigidbody.class).parent.simulationObjectBody.getPosition();
                    pos.set(target.getComponent(Rigidbody.class).parent.simulationObjectBodyDef.position);
                    target.position = new Vector2(pos.x, pos.y);
                }
                else {
                    target.position.y = Float.parseFloat(positionYField.getText());
                }
            }
        });
        rotationField.setOnAction(event -> {
            if (!rotationField.getText().isBlank()) {
                if (target.getComponent(Rigidbody.class) != null) {
                    Body body = target.getComponent(Rigidbody.class).parent.simulationObjectBody;
                    body.setTransform(
                        body.getPosition(), (float) Math.toRadians(Float.parseFloat(rotationField.getText()))
                    );
                    target.angle = Float.parseFloat(rotationField.getText());
                }
                else target.angle = Float.parseFloat(rotationField.getText());
            }
        });
    }

    private void updateTransformValues() {
        if (target.getComponent(Rigidbody.class) != null) {
            positionLabel.setText("Position: x: " + (int) (target.position.x * SimulationManager.SCALE)
                    + ", y: " + (int) (target.position.y * SimulationManager.SCALE));
        }
        else {
            positionLabel.setText("Position: x: " + (int) target.position.x + ", y: " + (int) target.position.y);
        }
        rotationLabel.setText("Rotation: " + (int) Math.toDegrees(target.angle) % 360);
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
