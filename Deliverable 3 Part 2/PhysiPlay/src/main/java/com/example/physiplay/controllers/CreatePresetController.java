package com.example.physiplay.controllers;

import com.example.physiplay.NumberOnlyTextField;
import com.example.physiplay.SimulationObject;
import com.example.physiplay.components.ComponentPropertyBuilder;
import com.example.physiplay.components.Rigidbody;
import com.example.physiplay.singletons.DragShapeHandler;
import com.example.physiplay.widgets.ComponentSelector;
import com.example.physiplay.widgets.Vector2Field;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableSet;
import javafx.collections.SetChangeListener;
import javafx.fxml.FXML;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
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
    public TextField positionXField;
    public TextField positionYField;
    public TextField rotationField;
    public TextField scaleXField;
    public TextField scaleYField;
    public Button createPresetButton;

    public Button addComponentButton;

    ArrayList<SimulationObject> presetList;
    ArrayList<SimulationObject> objectsList;
    HBox presetHBox;
    FlowPane presetFlowPane;
    TreeView<String> hierarchyView;
    GraphicsContext gc;
    TabPane tabPane;
    @FXML
    VBox attachedComponents;
    @FXML
    VBox componentsVBox;
    @FXML
    TreeView<ComponentSelector> componentsTreeView;
    @FXML
    Button attachComponentButton;
    Stage presetWindow;

    private boolean componentChoiceActive = false;

    // All components
    // Rigid body component property builder
    private ObservableSet<ComponentSelector> observableAttachedComponents = FXCollections.observableSet(new HashSet<>());
    private ComponentPropertyBuilder rigidBodyComponentPropertyBuilder = new ComponentPropertyBuilder()
            .addCheckboxProperty("useGravity", "Use Gravity", new CheckBox())
            .addCheckboxProperty("useAutoMass", "Use auto mass", new CheckBox())
            .addVector2Property("initialVelocity", "Velocity", new Vector2Field());


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

    private void generateComponentSelectors() {
        TreeItem<ComponentSelector> root = new TreeItem<>(new ComponentSelector("Components", null));
        TreeItem<ComponentSelector> rigidbodyItem = new TreeItem<>(new ComponentSelector("Rigidbody",
                rigidBodyComponentPropertyBuilder, false));
        root.getChildren().add(rigidbodyItem);
        componentsTreeView.setRoot(root);
    }

    private void updateAttachedComponentVBox(VBox vbox, ObservableSet<ComponentSelector> attachedComponentsSet) {
        vbox.getChildren().clear();
        for (ComponentSelector selector : attachedComponentsSet) {
            vbox.getChildren().add(selector.generateTitledPane());
        }
    }

    public void initialize() {
        componentsVBox.setVisible(componentChoiceActive);
        componentsVBox.setManaged(componentChoiceActive);
        generateComponentSelectors();

        componentsTreeView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        observableAttachedComponents.addListener((SetChangeListener<? super ComponentSelector>) change -> {
            System.out.println("Change!");
            updateAttachedComponentVBox(attachedComponents, observableAttachedComponents);
        });

        addComponentButton.setText(!componentChoiceActive ? "Add Components" : "Hide components");
        addComponentButton.setOnAction(event -> {
            componentChoiceActive = !componentChoiceActive;
            componentsVBox.setVisible(componentChoiceActive);
            componentsVBox.setManaged(componentChoiceActive);
            addComponentButton.setText(!componentChoiceActive ? "Add Components" : "Hide components");
        });
        createPresetButton.setOnAction(event -> createPreset());

        attachComponentButton.setOnAction(event -> {
            observableAttachedComponents.add(componentsTreeView.getSelectionModel().getSelectedItem()
                    .getValue());
        });



    }
    

    public void setPresetList(ArrayList<SimulationObject> list){
        this.presetList = list;
    }

    public void createPreset(){
        if (presetNameField.getText().trim().isEmpty()){
            presetNameField.setPromptText("Please enter a name:");
            presetNameField.setStyle("-fx-prompt-text-fill: red;");
        }
        else {
            SimulationObject simulationObject = new SimulationObject(presetNameField.getText(), new HashSet<>());
            VBox vBox = new VBox();
            Rectangle rectangle = new Rectangle(100,100);
            Label presetName = new Label(presetNameField.getText());
            presetName.setStyle("-fx-font-size: 12px");
            vBox.getChildren().addAll(rectangle, presetName);

            DragShapeHandler handler = new DragShapeHandler(rectangle, gc, tabPane, hierarchyView, getTextFields(), simulationObject);
            //rectangle.setOnMousePressed(handler);
            rectangle.setOnMouseDragged(handler);

            rectangle.addEventHandler(MouseEvent.ANY, new DragShapeHandler(rectangle, gc, tabPane, hierarchyView, getTextFields(), simulationObject));

            presetFlowPane.getChildren().add(vBox);

            TreeItem<String> preset = new TreeItem<>(presetName.getText());
            hierarchyView.getRoot().getChildren().add(preset);

            presetWindow.hide();
        }
    }

    public ArrayList<TextField> getTextFields(){
        ArrayList<TextField> arrayList = new ArrayList<TextField>();
        arrayList.add(presetNameField);
        arrayList.add(positionXField);
        arrayList.add(positionYField);
        arrayList.add(rotationField);
        arrayList.add(scaleXField);
        arrayList.add(scaleYField);
        return arrayList;
    }
}
