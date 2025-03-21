package com.example.physiplay.controllers;

import com.example.physiplay.SimulationObject;
import com.example.physiplay.components.DragShapeHandler;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.util.*;

public class CreatePresetController {

    @FXML
    private Accordion componentAccordion;
    @FXML
    public TextField presetNameField;
    public Button createPresetButton;
    public TextField positionXField;
    public TextField positionYField;
    public TextField rotationField;
    public TextField scaleXField;
    public TextField scaleYField;

    ArrayList<SimulationObject> presetList;
    ArrayList<SimulationObject> objectsList;
    HBox presetHBox;
    FlowPane presetFlowPane;
    TreeView<String> hierarchyView;
    GraphicsContext gc;
    TabPane tabPane;

    private final List<TitledPane> openPanes = new ArrayList<>();
    Stage presetWindow;

    public CreatePresetController(Stage stage, HBox presetHBox, ArrayList<SimulationObject> list, FlowPane presetFlowPane, TreeView<String> treeView, GraphicsContext gc, ArrayList<SimulationObject> objectsList, TabPane tabPane){
        this.presetWindow = stage;
        this.presetHBox = presetHBox;
        this.presetList = list;
        this.presetFlowPane = presetFlowPane;
        this.hierarchyView = treeView;
        this.gc = gc;
        this.objectsList = objectsList;
        this.tabPane = tabPane;
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
        VBox vBox = new VBox();
        Rectangle rectangle = new Rectangle(100,100);
        Label presetName = new Label(presetNameField.getText());
        presetName.setStyle("-fx-font-size: 12px");
        vBox.getChildren().addAll(rectangle, presetName);
        /*vBox.setOnMouseDragged(mouseEvent -> {
            rectangle.setX(mouseEvent.getX());
            System.out.println(mouseEvent.getScreenX());
            gc.fillRect(10,10,10,10);
        });*/

        DragShapeHandler handler = new DragShapeHandler(rectangle, gc, tabPane, hierarchyView);
        //rectangle.setOnMousePressed(handler);
        rectangle.setOnMouseDragged(handler);

        rectangle.addEventHandler(MouseEvent.ANY, new DragShapeHandler(rectangle, gc, tabPane, hierarchyView));

        presetFlowPane.getChildren().add(vBox);

        TreeItem<String> preset = new TreeItem<>(presetName.getText());
        hierarchyView.getRoot().getChildren().add(preset);

        presetWindow.hide();
    }
}
