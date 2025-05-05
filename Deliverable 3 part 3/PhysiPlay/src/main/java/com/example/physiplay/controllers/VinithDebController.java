package com.example.physiplay.controllers;

import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;

import com.example.physiplay.physics.MomentumSimulation.MomentumSimulation;
import com.example.physiplay.physics.PendulumSImulation.AccelerationGraphPendulum;
import com.example.physiplay.physics.PendulumSImulation.AngleGraphPendulum;
import com.example.physiplay.physics.PendulumSImulation.Pendulum;
import com.example.physiplay.physics.PendulumSImulation.StartStopControllable;
import com.example.physiplay.physics.PendulumSImulation.VelocityGraphPendulum;
import com.example.physiplay.physics.SpringSimulation.AccelerationGraphSpring;
import com.example.physiplay.physics.SpringSimulation.PositionGraphSpring;
import com.example.physiplay.physics.SpringSimulation.Spring;
import com.example.physiplay.physics.SpringSimulation.VelocityGraphSpring;
import com.example.physiplay.singletons.SettingsSingleton;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class VinithDebController {
	
	private Stage mainWindow;
	private Scene scene;
	
	@FXML
	BorderPane borderPane;
	
	@FXML
	Button backButton;
	@FXML
	Button momentumButton;
	@FXML
	Button pendulumButton;
	@FXML
	Button springButton;
	
	//simulations
	private MomentumSimulation momentumSimulation = new MomentumSimulation();
	private Pendulum pendulumSimulation = new Pendulum();
	private Spring springSimulation = new Spring();

	//tracker
	String current_scene = "momentumSimulation";

	//Language
	String langCode;
	Locale locale;
	ResourceBundle bundle;
	
	public VinithDebController(Stage stage, Scene scene, String langCode){
        this.mainWindow = stage;
        this.scene = scene;
		this.langCode = langCode;
		this.locale = new Locale(langCode);
		this.bundle = ResourceBundle.getBundle("languages.messages", this.locale);
		momentumSimulation.setLanguage(langCode);
		pendulumSimulation.switchLanguage(langCode);
    }
	
	public void initialize() {
		//VERY IMPORTANT
		Platform.runLater(() -> {
			borderPane.setCenter(getMomentumSimulation());
        });
		
		backButton.setOnAction(goBackToMain());
		momentumButton.setOnAction(goToSimulation("momentumSimulation"));
		pendulumButton.setOnAction(goToSimulation("pendulumSimulation"));
		springButton.setOnAction(goToSimulation("springSimulation"));
	}

	private EventHandler<ActionEvent> goBackToMain() {
		// TODO Auto-generated method stub
		return event -> {
	        scene.getStylesheets().clear();
	        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/css/stylesheets.css")).toExternalForm());
	        ScreenController.getInstance().activate("mainMenu", SettingsSingleton.getInstance().language);
		};
	}

	private EventHandler<ActionEvent> goToSimulation(String simulationName) {
	    return event -> {
	        if (simulationName.equals(current_scene)) return;

	        Node simulationPane;
	        switch (simulationName) {
	            case "momentumSimulation":
	                simulationPane = getMomentumSimulation();
	                break;
	            case "pendulumSimulation":
	            	simulationPane = getPendulumSimulation();
	            	break;
	            case "springSimulation":
	            	simulationPane = getSpringSimulation();
	            	break;
	            default:
	                throw new IllegalArgumentException("Unexpected value: " + simulationName);
	        }

	        borderPane.setCenter(simulationPane);
	        current_scene = simulationName;
	    };
	}


	private Node getSpringSimulation() {
		// TODO Auto-generated method stub
		HBox hBox = new HBox();
		hBox.setSpacing(10);
		hBox.setAlignment(Pos.CENTER);
		ObservableList<Node> hBoxChildren = hBox.getChildren();
		
		VBox vBox = new VBox();
		vBox.setSpacing(10);
		vBox.setAlignment(Pos.CENTER);
		ObservableList<Node> vboxChildren = vBox.getChildren();
		
		Spring simulation = springSimulation;
		standardize(simulation);
		PositionGraphSpring positionGraphSpring = new PositionGraphSpring(springSimulation);
		standardize(positionGraphSpring);
		VelocityGraphSpring velocityGraphSpring = new VelocityGraphSpring(springSimulation);
		standardize(velocityGraphSpring);
		AccelerationGraphSpring accelerationGraphSpring = new AccelerationGraphSpring(springSimulation);
		standardize(accelerationGraphSpring);
		VBox startStopControls = makeStartStopController(simulation, positionGraphSpring, velocityGraphSpring, accelerationGraphSpring);
		standardize(startStopControls);
		
		vboxChildren.addAll(positionGraphSpring, velocityGraphSpring, accelerationGraphSpring, startStopControls);
		hBoxChildren.addAll(simulation, vBox);
		
		return hBox;
	}

	private VBox makeStartStopController(StartStopControllable... controllables) {
		// TODO Auto-generated method stub
		VBox controls = new VBox();
		controls.setSpacing(10);
		controls.setAlignment(Pos.CENTER);
		ObservableList<Node> controlsChildren = controls.getChildren();
		
		Button playButton = new Button();
		makeButton(playButton, event -> {
			for (StartStopControllable controllable : controllables) {
				controllable.play();
			}
		}, bundle.getString("button.startSimulation"));

		Button pauseButton = new Button();
		makeButton(pauseButton, event -> {
			for (StartStopControllable controllable : controllables) {
				controllable.pause();
			}
		}, bundle.getString("button.pauseSimulation"));
		
		controlsChildren.addAll(playButton, pauseButton);
		return controls;
	}

	private void makeButton(Button button, EventHandler<ActionEvent> eventHandler, String text) {
		button.setText(text);
		standardize(button);
		button.setOnAction(eventHandler);
	}

	private Node getPendulumSimulation() {
		// TODO Auto-generated method stub
		HBox hBox = new HBox();
		hBox.setSpacing(10);
		hBox.setAlignment(Pos.CENTER);
		ObservableList<Node> hBoxChildren = hBox.getChildren();
		
		VBox vBox = new VBox();
		vBox.setSpacing(10);
		vBox.setAlignment(Pos.CENTER);
		ObservableList<Node> vboxChildren = vBox.getChildren();
		
		Pendulum simulation = pendulumSimulation;
		standardize(simulation);
		AngleGraphPendulum angleGraphPendulum = new AngleGraphPendulum(simulation);
		standardize(angleGraphPendulum);
		VelocityGraphPendulum velocityGraphPendulum = new VelocityGraphPendulum(simulation);
		standardize(velocityGraphPendulum);
		AccelerationGraphPendulum accelerationGraphPendulum = new AccelerationGraphPendulum(simulation);
		standardize(accelerationGraphPendulum);
		VBox startStopControls = makeStartStopController(simulation, angleGraphPendulum, velocityGraphPendulum, accelerationGraphPendulum);
		standardize(startStopControls);
		
		vboxChildren.addAll(angleGraphPendulum, velocityGraphPendulum, accelerationGraphPendulum, startStopControls);
		hBoxChildren.addAll(simulation, vBox);
		
		return hBox;
	}

	private Node getMomentumSimulation() {
		// TODO Auto-generated method stub
		HBox box = new HBox();
		box.setSpacing(50);
		box.setAlignment(Pos.CENTER);
		ObservableList<Node> children = box.getChildren();
		
		Pane simulationPane = momentumSimulation.getSimulationPane();
		simulationPane.setPrefWidth(400);simulationPane.setPrefHeight(300);
		standardize(simulationPane);
		StackPane stackPane = new StackPane(simulationPane);
		StackPane.setAlignment(simulationPane, Pos.CENTER);
		VBox controlPane = momentumSimulation.getControlPane();
		standardize(controlPane);
		
		children.addAll(stackPane, controlPane);
		return box;
	}

	private void standardize(Region region) {
//		applyStyleToHierarchy(region, "-fx-font-size: 20;");
		region.setMinSize(region.getPrefWidth(), region.getPrefHeight());
		region.setMaxSize(region.getPrefWidth(), region.getPrefHeight());
	}

	private void applyStyleToHierarchy(Parent start, String style) {
	    for (Node node : start.getChildrenUnmodifiable()) {
	        // Skip nodes that are part of the chart's plot content (data points)
	        if (node.getStyleClass().contains("chart-line-symbol") || node.getStyleClass().contains("chart-symbol")) {
	            continue;
	        }
	        // Apply style to LineChart (for title, axes) and text-based nodes
	        if (node instanceof LineChart || node instanceof Label || node instanceof Text || node instanceof Button) {
	            node.setStyle(style);
	        }
	        if (node instanceof Parent) {
	            applyStyleToHierarchy((Parent) node, style);
	        }
	    }
	}

}
