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
    private MenuItem clearAllMenuItem;
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

//        SimulationManager.getInstance().simulationPaused.bind(mainWindow.focusedProperty().not());
        handleMenuItems();
        
        canvas.setOnScroll(this::handleScrollEvent);
        canvas.setOnMouseDragged(this::handleMouseDragEvent);
        canvas.setOnMouseMoved(this::handleMouseMovingInsideCanvasEvent);
        
        pauseButton.setOnAction(this::handlePauseButtonEvent);
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
		GraphicsContext gc = SimulationManager.getInstance().gc;

		double px = scrollevent.getX();
		double py = scrollevent.getY();
		double scale = 1 + scrollevent.getDeltaY()*0.001;

		gc.translate(px, py);
		gc.scale(scale, scale);
		gc.translate(-px, -py);
		
		SimulationManager.getInstance().zoomFactor *= scale;
		System.out.println(SimulationManager.getInstance().zoomFactor);
	}
	
	//helper vars just for this method
	private Double last_x; //wrapper classes for unassigned checks
	private Double last_y;

	private void handleMouseDragEvent(MouseEvent mouseevent) {
		GraphicsContext gc = SimulationManager.getInstance().gc;
		double zoomFactor = SimulationManager.getInstance().zoomFactor;
		
		if (last_x == null) {
			last_x = Double.valueOf(mouseevent.getX());
		}
		if (last_y == null) {
			last_y = Double.valueOf(mouseevent.getY());
		}
		
		double delta_x = (mouseevent.getX() - last_x) / zoomFactor;
		double delta_y = (mouseevent.getY() - last_y) / zoomFactor;
		
		gc.translate(delta_x, delta_y);
		
		last_x = Double.valueOf(mouseevent.getX());
		last_y = Double.valueOf(mouseevent.getY());
	}

	private void handleMouseMovingInsideCanvasEvent(MouseEvent mouseevent) {
		last_x = Double.valueOf(mouseevent.getX());
		last_y = Double.valueOf(mouseevent.getY());
	}

	private void handlePauseButtonEvent(ActionEvent actionevent) {
		SimpleBooleanProperty simulationPaused = SimulationManager.getInstance().simulationPaused;
		simulationPaused.setValue(!simulationPaused.getValue());
	}
}
