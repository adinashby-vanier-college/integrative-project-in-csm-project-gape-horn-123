package com.example.physiplay.controllers;

import com.example.physiplay.physics.MomentumSimulation.MomentumSimulation;
import com.example.physiplay.physics.PendulumSImulation.AccelerationGraphPendulum;
import com.example.physiplay.physics.PendulumSImulation.AngleGraphPendulum;
import com.example.physiplay.physics.PendulumSImulation.Pendulum;
import com.example.physiplay.physics.PendulumSImulation.VelocityGraphPendulum;
import com.example.physiplay.physics.SpringSimulation.AccelerationGraphSpring;
import com.example.physiplay.physics.SpringSimulation.PositionGraphSpring;
import com.example.physiplay.physics.SpringSimulation.Spring;
import com.example.physiplay.physics.SpringSimulation.VelocityGraphSpring;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
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
	
	public VinithDebController(Stage stage, Scene scene){
        this.mainWindow = stage;
        this.scene = scene;
    }
	
	public void initialize() {
		Platform.runLater(() -> {// 🥀🥀 ts pmo
			borderPane.setCenter(getMomentumSimulation());
        });
		
		backButton.setOnAction(event -> ScreenController.getInstance().activate("mainMenu", "/css/stylesheets.css"));
		momentumButton.setOnAction(goToSimulation("momentumSimulation"));
		pendulumButton.setOnAction(goToSimulation("pendulumSimulation"));
		springButton.setOnAction(goToSimulation("springSimulation"));
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
		
		Pane simulation = springSimulation;
		standardize(simulation);
		Pane accelerationGraphSpring = new AccelerationGraphSpring(springSimulation);
		standardize(accelerationGraphSpring);
		Pane positionGraphSpring = new PositionGraphSpring(springSimulation);
		standardize(positionGraphSpring);
		Pane velocityGraphSpring = new VelocityGraphSpring(springSimulation);
		standardize(velocityGraphSpring);
		
		vboxChildren.addAll(positionGraphSpring, velocityGraphSpring, accelerationGraphSpring);
		hBoxChildren.addAll(simulation, vBox);
		
		return hBox;
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
		
		vboxChildren.addAll(angleGraphPendulum, velocityGraphPendulum, accelerationGraphPendulum);
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
		applyStyleToHierarchy(region, "-fx-font-size: 20;");
		region.setMinSize(region.getPrefWidth(), region.getPrefHeight());
		region.setMaxSize(region.getPrefWidth(), region.getPrefHeight());
	}

	private void applyStyleToHierarchy(Parent start, String style) {
		// TODO Auto-generated method stub
		for (Node node : start.getChildrenUnmodifiable()) {
			node.setStyle(style);
			if (node instanceof Parent) {
				applyStyleToHierarchy((Parent) node, style);
			}
		}
	}

}
