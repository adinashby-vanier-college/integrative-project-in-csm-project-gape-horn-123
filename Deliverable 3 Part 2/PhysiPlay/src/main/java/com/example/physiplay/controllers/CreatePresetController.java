package com.example.physiplay.controllers;

import com.example.physiplay.Component;
import com.example.physiplay.NumberOnlyTextField;
import com.example.physiplay.SimulationObject;
import com.example.physiplay.Vector2;
import com.example.physiplay.components.ComponentPropertyBuilder;
import com.example.physiplay.components.Rigidbody;
import com.example.physiplay.singletons.DragShapeHandler;
import com.example.physiplay.singletons.SimulationManager;
import com.example.physiplay.widgets.ComponentSelector;
import com.example.physiplay.widgets.Vector2Field;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableSet;
import javafx.collections.SetChangeListener;
import javafx.event.EventHandler;
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
            .addCheckboxProperty("isStatic", "Is Static", new CheckBox())
            .addCheckboxProperty("useGravity", "Use Gravity", new CheckBox())
            .addCheckboxProperty("useAutoMass", "Use auto mass", new CheckBox())
            .addVector2Property("initialVelocity", "Velocity", new Vector2Field())
            .addNumberInputFieldProperty("restitution", "Restitution", new TextField())
            .addNumberInputFieldProperty("mass", "Mass", new TextField())
            .addNumberInputFieldProperty("friction", "Friction", new TextField())
            .addNumberInputFieldProperty("torque", "Torque", new TextField());


    private ComponentPropertyBuilder rectanglePropertyBuilder = new ComponentPropertyBuilder()
            .addVector2Property("size", "Size", new Vector2Field());
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
        TreeItem<ComponentSelector> root = new TreeItem<>(new ComponentSelector("Components", "Components",
                null, false));
        TreeItem<ComponentSelector> rigidbodyItem = new TreeItem<>(new ComponentSelector("Rigidbody",
                "Rigidbody", rigidBodyComponentPropertyBuilder, false));
        TreeItem<ComponentSelector> shapeRoot = new TreeItem<>(new ComponentSelector("shapes", "Shapes", null, false));
        shapeRoot.getChildren().add(new TreeItem<>(new ComponentSelector("shape", "Rectangle",
                rectanglePropertyBuilder, false)));
        root.getChildren().add(rigidbodyItem);
        root.getChildren().add(shapeRoot);
        componentsTreeView.setRoot(root);
    }

    private void updateAttachedComponentVBox(VBox vbox, ObservableSet<ComponentSelector> attachedComponentsSet) {
        vbox.getChildren().clear();
        for (ComponentSelector selector : attachedComponentsSet) {
            if (selector.isInteractable())
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
            if (componentsTreeView.getSelectionModel().getSelectedItem().getValue().isInteractable())
                observableAttachedComponents.add(componentsTreeView.getSelectionModel().getSelectedItem()
                        .getValue());
        });

        NumberOnlyTextField numberOnlyTextField = new NumberOnlyTextField();
        numberOnlyTextField.numberOnly(positionYField);
        numberOnlyTextField.numberOnly(rotationField);
        numberOnlyTextField.numberOnly(scaleXField);
        numberOnlyTextField.numberOnly(scaleYField);
    }

    public void setPresetList(ArrayList<SimulationObject> list){
        this.presetList = list;
    }

    private void addComponentInSet(Set<Component> componentSet) {
        for (ComponentSelector selector : observableAttachedComponents) {
            switch (selector.getComponentName()) {
                case "Rigidbody":
                    componentSet.add(selector.convertToRigidbodyComponent());
                    break;
                case "shape":
                    componentSet.add(selector.convertToRendererComponent());
                    break;
            }
        }
    }
    public void createPreset(){
        if (presetNameField.getText().trim().isEmpty()){
            presetNameField.setPromptText("Please enter a name:");
            presetNameField.setStyle("-fx-prompt-text-fill: red;");
        }
        else {
            Set<Component> componentSet = new HashSet<>();
            addComponentInSet(componentSet);
            SimulationObject simulationObject = new SimulationObject(presetNameField.getText(), componentSet);
            VBox vBox = new VBox();
            Rectangle rectangle = new Rectangle(100,100);
            Label presetName = new Label(presetNameField.getText());
            presetName.setStyle("-fx-font-size: 12px");
            vBox.getChildren().addAll(rectangle, presetName);

            DragShapeHandler handler = new DragShapeHandler(rectangle, gc, tabPane, hierarchyView, getTextFields(), simulationObject,
                this);

            EventHandler<MouseEvent> event = mouseEvent -> {
                if (mouseEvent.getEventType() == MouseEvent.MOUSE_RELEASED) {
                    Set<Component> componentSet1 = new HashSet<>();
                    addComponentInSet(componentSet1);
                    SimulationObject copy = new SimulationObject(simulationObject.name, componentSet1,
                            new Vector2(mouseEvent.getSceneX() - 360, mouseEvent.getSceneY() - 35),
                            Integer.parseInt(rotationField.getText().isBlank() ? "0" : rotationField.getText()));
                    SimulationManager.getInstance().simulationObjectList.add(copy);

                    Tab tab = new Tab(presetNameField.getText());
                    tabPane.getTabs().add(tab);

                    hierarchyView.getRoot().getChildren().add(new TreeItem<>(simulationObject.name));
                }
            };
            rectangle.setOnMouseDragged(event);
            rectangle.addEventHandler(MouseEvent.ANY, event);

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
