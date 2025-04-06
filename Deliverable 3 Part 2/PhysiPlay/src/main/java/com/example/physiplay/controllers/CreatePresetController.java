package com.example.physiplay.controllers;

import com.example.physiplay.Component;
import com.example.physiplay.NumberOnlyTextField;
import com.example.physiplay.SimulationObject;
import com.example.physiplay.Vector2;
import com.example.physiplay.components.ComponentPropertyBuilder;
import com.example.physiplay.components.Renderer;
import com.example.physiplay.singletons.SimulationManager;
import com.example.physiplay.widgets.ComponentSelector;
import com.example.physiplay.widgets.Vector2Field;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableSet;
import javafx.collections.SetChangeListener;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.effect.InnerShadow;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.*;

public class CreatePresetController {
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
    TabPane componentTabPane;
    @FXML
    VBox shapeVBox;
    @FXML
    TreeView<ComponentSelector> componentsTreeView;
    @FXML
    Button attachComponentButton;
    Stage presetWindow;
    Scene scene;
    private SimpleBooleanProperty componentChoiceActiveProperty = new SimpleBooleanProperty(false);

    private ContextMenu contextMenu = new ContextMenu();
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

    private ComponentPropertyBuilder circlePropertyBuilder = new ComponentPropertyBuilder()
            .addNumberInputFieldProperty("radius", "Radius", new TextField());

    private ComponentPropertyBuilder regularPolygonPropertyBuilder = new ComponentPropertyBuilder()
            .addNumberInputFieldProperty("sides", "Sides", new TextField())
            .addNumberInputFieldProperty("size", "Size", new TextField());

    public CreatePresetController(Stage stage, HBox presetHBox, ArrayList<SimulationObject> list, FlowPane presetFlowPane, TreeView<String> treeView, GraphicsContext gc, ArrayList<SimulationObject> objectsList, TabPane tabPane, Scene scene){
        this.presetWindow = stage;
        this.presetHBox = presetHBox;
        this.presetList = list;
        this.presetFlowPane = presetFlowPane;
        this.hierarchyView = treeView;
        this.gc = gc;
        this.objectsList = objectsList;
        this.tabPane = tabPane;
        this.scene = scene;
    }

    private ComponentSelector getComponentSelectorByName(String componentName) {
        for (ComponentSelector cs : observableAttachedComponents) {
            if (cs.getComponentName().equals(componentName)) return cs;
        }
        return null;
    }

    private void generateComponentSelectors() {
        TreeItem<ComponentSelector> root = new TreeItem<>(new ComponentSelector("Components", "Components",
                null, false));
        TreeItem<ComponentSelector> rigidbodyItem = new TreeItem<>(new ComponentSelector("Rigidbody",
                "Rigidbody", rigidBodyComponentPropertyBuilder, false));
        TreeItem<ComponentSelector> shapeRoot = new TreeItem<>(new ComponentSelector("shapes", "Shapes", null, false));
        shapeRoot.getChildren().add(new TreeItem<>(new ComponentSelector("shape", "Rectangle",
                rectanglePropertyBuilder, false)));
        shapeRoot.getChildren().add(new TreeItem<>(new ComponentSelector("shape", "Circle",
                circlePropertyBuilder, false)));
        shapeRoot.getChildren().add(new TreeItem<>(new ComponentSelector("shape", "Regular Polygon",
                regularPolygonPropertyBuilder, false)));
        root.getChildren().add(rigidbodyItem);
        root.getChildren().add(shapeRoot);
        componentsTreeView.setRoot(root);
    }

    private void handleContextMenu() {
        contextMenu.getItems().add(new MenuItem("Rename"));
        contextMenu.getItems().add(new MenuItem("Delete"));
    }

    public void initialize() {
        this.presetWindow.titleProperty().bind(presetNameField.textProperty());
        handleContextMenu();
        generateComponentSelectors();
        componentsTreeView.visibleProperty().bind(componentChoiceActiveProperty);
        componentsTreeView.managedProperty().bind(componentChoiceActiveProperty);
        attachComponentButton.disableProperty().bind(componentChoiceActiveProperty.not());
        componentsTreeView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        observableAttachedComponents.addListener((SetChangeListener<ComponentSelector>) change -> {
            if (change.wasAdded()) {
                ComponentSelector selector = change.getElementAdded();
                Tab tab = new Tab(selector.getTitle());
                tab.setClosable(false);
                tab.setId(selector.getComponentName());
                ScrollPane pane = new ScrollPane(selector.getComponentProperties());
                pane.setEffect(new InnerShadow(5, Color.BLACK));
                tab.setContent(pane);

                tab.setOnClosed(event -> {
                    if (getComponentSelectorByName(tab.getId()) != null) {
                        observableAttachedComponents.remove(getComponentSelectorByName(tab.getId()));
                    }
                });
                componentTabPane.getTabs().add(tab);
            }

        });

        addComponentButton.setText(!componentChoiceActiveProperty.getValue() ? "Add Components" : "Hide Components");
        addComponentButton.setOnAction(event -> {
            componentChoiceActiveProperty.setValue(!componentChoiceActiveProperty.getValue());
            addComponentButton.setText(!componentChoiceActiveProperty.getValue() ? "Add Components" : "Hide Components");
        });
        createPresetButton.setOnAction(event -> createPreset());

        attachComponentButton.setOnAction(event -> {
            if (componentsTreeView.getSelectionModel().getSelectedItem().getValue().isInteractable()) {
                observableAttachedComponents.add(componentsTreeView.getSelectionModel().getSelectedItem().getValue());
            }
        });

        setNumberOnly();

    }

    public void setNumberOnly(){
        NumberOnlyTextField numberOnlyTextField = new NumberOnlyTextField();
        numberOnlyTextField.numberOnly(positionXField);
        numberOnlyTextField.numberOnly(positionYField);
        numberOnlyTextField.numberOnly(rotationField);
        numberOnlyTextField.numberOnly(scaleXField);
        numberOnlyTextField.numberOnly(scaleYField);
    }

    private void addComponentInSet(Set<Component> componentSet) {
        for (ComponentSelector selector : observableAttachedComponents) {
            switch (selector.getComponentName()) {
                case "Rigidbody":
                    componentSet.add(selector.convertToRigidbodyComponent());
                    break;
                case "shape":
                    switch (selector.getTitle()) {
                        case "Rectangle" -> componentSet.add(selector.convertToRectangularRendererComponent());
                        case "Circle" -> componentSet.add(selector.convertToCircleRendererComponent());
                        case "Regular Polygon" -> componentSet.add(selector.convertToRegularPolygonRendererComponent());
                    }
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
            SimulationObject simulationObject = new SimulationObject(presetNameField.getText(), componentSet, Vector2.ZERO,
                    Float.parseFloat(rotationField.getText().isBlank() ? "0" : rotationField.getText()));
            VBox vBox = new VBox();
            Rectangle rectangle = new Rectangle(100,100);
            rectangle.setRotate(Math.toDegrees(simulationObject.angle));
            Label presetName = new Label(presetNameField.getText());
            presetName.setStyle("-fx-font-size: 12px");
            vBox.getChildren().addAll(rectangle, presetName);

            EventHandler<MouseEvent> event = mouseEvent -> {
                if (mouseEvent.getEventType() == MouseEvent.MOUSE_DRAGGED) {
                    if (simulationObject.getComponent(Renderer.class) != null) {

                        Point2D localPoint = SimulationManager.getInstance().canvas.sceneToLocal(mouseEvent.getSceneX(),
                                mouseEvent.getSceneY());
                        simulationObject.getComponent(Renderer.class).mouseX = localPoint.getX();
                        simulationObject.getComponent(Renderer.class).mouseY = localPoint.getY();
                    }
                    SimulationManager.getInstance().hologramSimulationObject = simulationObject;
                }
                if (mouseEvent.getEventType() == MouseEvent.MOUSE_RELEASED) {
                    if (mouseEvent.getButton() == MouseButton.SECONDARY) {
                        contextMenu.show(rectangle, mouseEvent.getScreenX(), mouseEvent.getScreenY());
                    }
                    else contextMenu.hide();
                    Point2D localPoint = SimulationManager.getInstance().canvas.sceneToLocal(mouseEvent.getSceneX(),
                            mouseEvent.getSceneY());
                    SimulationManager.getInstance().hologramSimulationObject = null;
                    if (!SimulationManager.getInstance().isCoordinateInCanvas(new Vector2(
                            localPoint.getX(), localPoint.getY()))) {
                        return;
                    }
                    Set<Component> componentSet1 = new HashSet<>();
                    addComponentInSet(componentSet1);
                    SimulationObject copy = new SimulationObject(simulationObject.name, componentSet1,
                            new Vector2(
                                    localPoint.getX(), localPoint.getY()),
                            Float.parseFloat(rotationField.getText().isBlank() ? "0" : rotationField.getText()));
                    SimulationManager.getInstance().simulationObjectList.add(copy);

                    Tab tab = new Tab(presetNameField.getText());
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/gameObjectTab.fxml"));
                    loader.setController(new TabController(getAllTextFields()));
                    try {
                        tab.setContent(loader.load());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    tabPane.getTabs().add(tab);

                    hierarchyView.getRoot().getChildren().add(new TreeItem<>(simulationObject.name));
                }

            };
            rectangle.setOnMouseDragged(event);
            rectangle.addEventHandler(MouseEvent.ANY, event);

            presetFlowPane.getChildren().addAll(vBox);

            TreeItem<String> preset = new TreeItem<>(presetName.getText());
            hierarchyView.getRoot().getChildren().add(preset);
            
            presetWindow.hide();
        }
    }

    public ArrayList<TextField> getAllTextFields() {
        ArrayList<TextField> textFields = new ArrayList<>();
        textFields.add(presetNameField);
        textFields.add(positionXField);
        textFields.add(positionYField);
        textFields.add(rotationField);
        textFields.add(scaleXField);
        textFields.add(scaleYField);
        return textFields;
    }

}
