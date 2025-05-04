package com.example.physiplay.controllers;

import com.example.physiplay.physicsconcepts.MomentumSimulation;
import com.example.physiplay.physicsconcepts.PendulumSimulation;
import com.example.physiplay.physicsconcepts.SpringSimulation;

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
import javafx.scene.layout.Pane;
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
	private PendulumSimulation pendulumSimulation = new PendulumSimulation();
	private SpringSimulation springSimulation = new SpringSimulation();

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
		applyStyleToHierarchy(springSimulation, "-fx-font-size: 20;");
		springSimulation.setMinSize(springSimulation.getPrefWidth(), springSimulation.getPrefHeight());
		springSimulation.setMaxSize(springSimulation.getPrefWidth(), springSimulation.getPrefHeight());
		return springSimulation;
	}

	private Node getPendulumSimulation() {
		// TODO Auto-generated method stub
		applyStyleToHierarchy(pendulumSimulation, "-fx-font-size: 20;");
		pendulumSimulation.setMinSize(pendulumSimulation.getPrefWidth(), pendulumSimulation.getPrefHeight());
		pendulumSimulation.setMaxSize(pendulumSimulation.getPrefWidth(), pendulumSimulation.getPrefHeight());
		return pendulumSimulation; //?????? why is this a pane and the other one isnt
	}

	private Node getMomentumSimulation() {
		// TODO Auto-generated method stub
		VBox box = new VBox();
		box.setSpacing(20);
		box.setAlignment(Pos.CENTER);
		ObservableList<Node> children = box.getChildren();
		
		Pane simulationPane = momentumSimulation.getSimulationPane();
		simulationPane.setMaxSize(400, 400);
		VBox controlPane = momentumSimulation.getControlPane();
		applyStyleToHierarchy(controlPane, "-fx-font-size: 20;");
		
		children.addAll(simulationPane, controlPane);
		return box;
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
