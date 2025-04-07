package com.example.physiplay.controllers;

import com.example.physiplay.SimulationObject;
import com.example.physiplay.singletons.SimulationManager;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.jbox2d.dynamics.Body;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;

public class PhysiplayController {

    private final Scene scene;
    Stage mainWindow;
    ArrayList<SimulationObject> presetList = new ArrayList<>();
    ArrayList<SimulationObject> objectsList = new ArrayList<>();

    @FXML
    Button createPresetButton;
    @FXML
    Button startButton;
    @FXML
    Button pauseButton;
    @FXML
    HBox presetHBox;
    @FXML
    private MenuItem closeMenuItem;
    @FXML
    MenuItem homeScreen;
    @FXML
    MenuItem polygonVisualizerMenuItem;
    @FXML
    BorderPane borderPane;
    @FXML
    CheckMenuItem canvasViewMenuItem;
    @FXML
    private TreeView<String> hierarchyView;
    @FXML
    FlowPane presetFlowPane;
    @FXML
    ScrollPane presetScrollPane;

    @FXML
    MenuItem clearAllMenuItem;

    @FXML
    MenuItem positionCamResetMenuItem;
    @FXML
    MenuItem scaleCamResetMenuItem;
    @FXML
    Canvas canvas;
    @FXML
    TabPane tabPane;

    public PhysiplayController(Stage stage, Scene scene){
        this.mainWindow = stage;
        this.scene = scene;
    }

    private void handleMenuItems() {
        canvasViewMenuItem.selectedProperty().addListener((observableValue, oldValue, newValue) -> {
            borderPane.getLeft().setManaged(!newValue);
            borderPane.getLeft().setVisible(!newValue);
            borderPane.getRight().setManaged(!newValue);
            borderPane.getRight().setVisible(!newValue);
            System.out.println(SimulationManager.getInstance().canvas.localToScene(0, 0).getX());
        });

        clearAllMenuItem.setOnAction(event -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            stage.setAlwaysOnTop(true);
            stage.toFront();
            alert.setTitle("Clear All?");
            alert.setHeaderText("Are you sure you want to delete every object in the scene?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get().equals(ButtonType.OK)) {
                Body body = SimulationManager.getInstance().world.getBodyList();
                while (body != null) {
                    Body next = body.getNext();
                    SimulationManager.getInstance().world.destroyBody(body);
                    body = next;
                }
                SimulationManager.getInstance().simulationObjectList.clear();
            }
        });

        positionCamResetMenuItem.setOnAction(event -> {
            SimulationManager.getInstance().camX = 0;
            SimulationManager.getInstance().camY = 0;
        });

        scaleCamResetMenuItem.setOnAction(event -> {
            SimulationManager.getInstance().scaleX = 1;
            SimulationManager.getInstance().scaleY = 1;
        });
    }
    public void initialize() {
        SimulationManager.getInstance().canvas = canvas;
        SimulationManager.getInstance().gc = SimulationManager.getInstance().canvas.getGraphicsContext2D();
        presetScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        presetScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        presetFlowPane.setHgap(20);
        presetFlowPane.setVgap(20);

        createPresetButton.setOnAction(event -> createPresetWindow());
        polygonVisualizerMenuItem.setOnAction(event -> createPolygonVisualizerWindow());

        closeMenuItem.setOnAction(event -> displayClosingAlertBox());

        homeScreen.setOnAction(event -> returnToMainMenu());

        TreeItem<String> rootItem = new TreeItem<>("SampleScene");
        rootItem.setExpanded(true);
        hierarchyView.setRoot(rootItem);

        SimulationManager.getInstance().simulate();
        pauseButton.setOnAction(event -> {
            SimulationManager.getInstance().simulationPaused.setValue(!SimulationManager.getInstance().simulationPaused.getValue());
        });
//        SimulationManager.getInstance().simulationPaused.bind(mainWindow.focusedProperty().not());
        handleMenuItems();

        canvas.addEventHandler(MouseEvent.MOUSE_PRESSED, this::handleMousePressEvent);
        canvas.addEventHandler(MouseEvent.MOUSE_DRAGGED, this::handleMouseDragEvent);
        canvas.addEventHandler(ScrollEvent.SCROLL, this::handleScrollEvent);
        canvas.addEventHandler(MouseEvent.MOUSE_RELEASED, this::handleMouseReleaseEvent);

    }

    private void setUpTimer() {
        AnimationTimer animationTimer = new AnimationTimer() {
            public void handle(long l) {
                ObservableList<Node> vBoxList = presetFlowPane.getChildren();
                if (!vBoxList.isEmpty())System.out.println(vBoxList.get(0));
            }
        };
        animationTimer.start();
    }

    private void displayClosingAlertBox() {
        Alert closingAlert = new Alert(Alert.AlertType.CONFIRMATION);
        closingAlert.setTitle("Exiting");
        closingAlert.setHeaderText("Are you sure you want to exit?");
        Optional<ButtonType> result = closingAlert.showAndWait();
        if (result.isPresent() && result.get().equals(ButtonType.OK)) {
            Platform.exit();
        }
        else closingAlert.close();
    }

    public void createPresetWindow(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/createPreset.fxml"));
        Stage presetWindow = new Stage();
        CreatePresetController createPresetController = new CreatePresetController(presetWindow, presetHBox, presetList, presetFlowPane, hierarchyView,
                SimulationManager.getInstance().gc, objectsList, tabPane, scene);
        loader.setController(createPresetController);
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Scene scene = new Scene(root, 500, 980);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/css/stylesheetPresetWindow.css")).toExternalForm());
        presetWindow.setScene(scene);
        presetWindow.initModality(Modality.WINDOW_MODAL);
        presetWindow.initOwner(mainWindow);
        presetWindow.show();
    }

    public void createPolygonVisualizerWindow() {
        // TODO: Repetitive code in createPresetWindow, move save code into a method
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/polygonVisualizerPage.fxml"));
        Stage window = new Stage();
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Scene scene = new Scene(root);
        scene.getStylesheets().add(String.valueOf(getClass().getResource("/css/polygonVisualizer.css")));
        window.setTitle("Polygon visualizer");
        window.setScene(scene);
        window.initModality(Modality.WINDOW_MODAL);
        window.initOwner(mainWindow);
        window.show();
    }

    private void returnToMainMenu() {
        scene.getStylesheets().clear();
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/css/stylesheets.css")).toExternalForm());
        ScreenController.getInstance().activate("mainMenu");
    }

	private void handleScrollEvent(ScrollEvent scrollevent) {
        final double increment = .1;
        double deltaY = scrollevent.getDeltaY();
        if (deltaY > 0) {
            SimulationManager.getInstance().scaleX += increment;
            SimulationManager.getInstance().scaleY += increment;
        }
        else if (deltaY < 0) {
            SimulationManager.getInstance().scaleX -= increment;
            SimulationManager.getInstance().scaleY -= increment;
        }
        SimulationManager.getInstance().scaleX = Math.max(0.1, Math.min(SimulationManager.getInstance().scaleX, 2.0));
        SimulationManager.getInstance().scaleY = Math.max(0.1, Math.min(SimulationManager.getInstance().scaleY, 2.0));
        scrollevent.consume();
	}
	//helper vars just for this method
	private Double last_x = 0.0; //wrapper classes for unassigned checks
	private Double last_y = 0.0;
    private boolean isDragging = true;

    private void handleMousePressEvent(MouseEvent mouseEvent) {
        if (mouseEvent.getButton() == MouseButton.MIDDLE) {
            last_x = mouseEvent.getX();
            last_y = mouseEvent.getY();
            isDragging = true;
        }
    }
	private void handleMouseDragEvent(MouseEvent mouseevent) {
		if (isDragging) {
            double deltaX = mouseevent.getX() - last_x;
            double deltaY = mouseevent.getY() - last_y;
            SimulationManager.getInstance().camX += deltaX;
            SimulationManager.getInstance().camY += deltaY;
            last_x = mouseevent.getX();
            last_y = mouseevent.getY();
        }
	}

    private void handleMouseReleaseEvent(MouseEvent mouseEvent) {
        if (mouseEvent.getButton() == MouseButton.MIDDLE) {
            isDragging = false;
        }
    }
}
