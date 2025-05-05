/**
 * Controller class for handling the simulation UI in the VinithDeb scene.
 * Supports multiple physics simulations including momentum, pendulum, and spring.
 * Allows switching between simulations and localized language support.
 */
package com.example.physiplay.controllers;

import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;

import com.example.physiplay.physics.MomentumSimulation.MomentumSimulation;
import com.example.physiplay.physics.PendulumSImulation.*;
import com.example.physiplay.physics.SpringSimulation.*;
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
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class VinithDebController {

	private Stage mainWindow;
	private Scene scene;

	@FXML
	private BorderPane borderPane;

	@FXML
	private Button backButton, momentumButton, pendulumButton, springButton;

	private MomentumSimulation momentumSimulation = new MomentumSimulation();
	private Pendulum pendulumSimulation = new Pendulum();
	private Spring springSimulation = new Spring();

	private String current_scene = "momentumSimulation";
	private String langCode;
	private Locale locale;
	private ResourceBundle bundle;

	/**
	 * Constructs the controller and sets the default language.
	 */
	public VinithDebController(Stage stage, Scene scene, String langCode) {
		this.mainWindow = stage;
		this.scene = scene;
		this.langCode = langCode;
		this.locale = new Locale(langCode);
		this.bundle = ResourceBundle.getBundle("languages.messages", this.locale);
		momentumSimulation.setLanguage(langCode);
		pendulumSimulation.switchLanguage(langCode);
		springSimulation.switchLanguage(langCode);
	}

	/**
	 * Initializes the scene and sets the event handlers for buttons.
	 */
	public void initialize() {
		Platform.runLater(() -> borderPane.setCenter(getMomentumSimulation()));

		backButton.setOnAction(goBackToMain());
		momentumButton.setOnAction(goToSimulation("momentumSimulation"));
		pendulumButton.setOnAction(goToSimulation("pendulumSimulation"));
		springButton.setOnAction(goToSimulation("springSimulation"));
	}

	private EventHandler<ActionEvent> goBackToMain() {
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
				case "momentumSimulation" -> simulationPane = getMomentumSimulation();
				case "pendulumSimulation" -> simulationPane = getPendulumSimulation();
				case "springSimulation" -> simulationPane = getSpringSimulation();
				default -> throw new IllegalArgumentException("Unexpected value: " + simulationName);
			}

			borderPane.setCenter(simulationPane);
			current_scene = simulationName;
		};
	}

	private Node getSpringSimulation() {
		HBox hBox = new HBox(10);
		hBox.setAlignment(Pos.CENTER);
		VBox vBox = new VBox(10);
		vBox.setAlignment(Pos.CENTER);

		Spring simulation = springSimulation;
		standardize(simulation);

		PositionGraphSpring position = new PositionGraphSpring(springSimulation, langCode);
		VelocityGraphSpring velocity = new VelocityGraphSpring(springSimulation, langCode);
		AccelerationGraphSpring acceleration = new AccelerationGraphSpring(springSimulation, langCode);
		VBox controls = makeStartStopController(simulation, position, velocity, acceleration);

		standardize(position);
		standardize(velocity);
		standardize(acceleration);
		standardize(controls);

		vBox.getChildren().addAll(position, velocity, acceleration, controls);
		hBox.getChildren().addAll(simulation, vBox);
		return hBox;
	}

	private Node getPendulumSimulation() {
		HBox hBox = new HBox(10);
		hBox.setAlignment(Pos.CENTER);
		VBox vBox = new VBox(10);
		vBox.setAlignment(Pos.CENTER);

		Pendulum simulation = pendulumSimulation;
		AngleGraphPendulum angle = new AngleGraphPendulum(simulation, langCode);
		VelocityGraphPendulum velocity = new VelocityGraphPendulum(simulation, langCode);
		AccelerationGraphPendulum acceleration = new AccelerationGraphPendulum(simulation, langCode);
		VBox controls = makeStartStopController(simulation, angle, velocity, acceleration);

		standardize(simulation);
		standardize(angle);
		standardize(velocity);
		standardize(acceleration);
		standardize(controls);

		vBox.getChildren().addAll(angle, velocity, acceleration, controls);
		hBox.getChildren().addAll(simulation, vBox);
		return hBox;
	}

	private Node getMomentumSimulation() {
		HBox box = new HBox(50);
		box.setAlignment(Pos.CENTER);

		Pane simPane = momentumSimulation.getSimulationPane();
		simPane.setPrefSize(400, 300);
		VBox controlPane = momentumSimulation.getControlPane();

		standardize(simPane);
		standardize(controlPane);

		box.getChildren().addAll(new StackPane(simPane), controlPane);
		return box;
	}

	private VBox makeStartStopController(StartStopControllable... controllables) {
		VBox controls = new VBox(10);
		controls.setAlignment(Pos.CENTER);

		Button play = new Button(bundle.getString("button.startSimulation"));
		play.setOnAction(e -> {
			for (StartStopControllable c : controllables) c.play();
		});

		Button pause = new Button(bundle.getString("button.pauseSimulation"));
		pause.setOnAction(e -> {
			for (StartStopControllable c : controllables) c.pause();
		});

		standardize(play);
		standardize(pause);

		controls.getChildren().addAll(play, pause);
		return controls;
	}

	private void makeButton(Button button, EventHandler<ActionEvent> eventHandler, String text) {
		button.setText(text);
		standardize(button);
		button.setOnAction(eventHandler);
	}

	private void standardize(Region region) {
		region.setMinSize(region.getPrefWidth(), region.getPrefHeight());
		region.setMaxSize(region.getPrefWidth(), region.getPrefHeight());
	}

	private void applyStyleToHierarchy(Parent start, String style) {
		for (Node node : start.getChildrenUnmodifiable()) {
			if (node.getStyleClass().contains("chart-line-symbol") || node.getStyleClass().contains("chart-symbol")) continue;
			if (node instanceof LineChart || node instanceof Label || node instanceof Text || node instanceof Button) {
				node.setStyle(style);
			}
			if (node instanceof Parent) {
				applyStyleToHierarchy((Parent) node, style);
			}
		}
	}
}
