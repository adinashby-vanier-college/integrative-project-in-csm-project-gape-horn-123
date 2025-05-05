package com.example.physiplay.controllers;

import com.example.physiplay.singletons.SettingsSingleton;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

/**
 * Controller for the main menu screen. Handles navigation to other screens such as
 * play mode, instructions, settings, and physics concepts. Also manages application exit.
 */
public class MainMenuController {

    @FXML
    Button playButton;

    @FXML
    Button instructionsButton;

    @FXML
    Button exitButton;

    @FXML
    Button settingsButton;

    @FXML
    Button physicsConceptsButton;

    /** The main application stage */
    Stage stage;

    /** The current scene */
    Scene scene;

    /** The controller for the Physiplay simulation screen */
    PhysiplayController physiplayController;

    /**
     * Constructs the MainMenuController with references to the main stage, scene, and Physiplay controller.
     *
     * @param stage the primary application window
     * @param scene the current scene object
     * @param physiplayController the simulation controller used for play mode
     */
    public MainMenuController(Stage stage, Scene scene, PhysiplayController physiplayController){
        this.stage = stage;
        this.scene = scene;
        this.physiplayController = physiplayController;
    }

    /**
     * Initializes button actions for switching to different views or exiting the application.
     */
    public void initialize() {
        stage.setAlwaysOnTop(true);
        playButton.setOnAction(event -> switchScene("play", "/css/stylesheets.css"));
        instructionsButton.setOnAction(event -> switchScene("instructions", "/css/instructionsStylesheet.css"));
        settingsButton.setOnAction(event -> switchScene("settings", "/css/settingsStylesheet.css"));
        physicsConceptsButton.setOnAction(event -> switchScene("physicsConcepts", "/css/sahon_saha.css"));
        exitButton.setOnAction(event -> Platform.exit());
    }

    /**
     * Switches the application to a new scene using the provided scene type and CSS file.
     *
     * @param sceneType the name of the target screen
     * @param cssUrl the path to the associated stylesheet
     */
    public void switchScene(String sceneType, String cssUrl) {
        try {
            if (sceneType == "play") physiplayController.translation();
            if (SettingsSingleton.getInstance().getTheme()=="baw"){
                String css = switch (sceneType) {
                    case "instructions" -> "/css/instructionsStylesheet.css";
                    case "settings" -> "/css/settingsStylesheet.css";
                    case "physicsConcepts" -> "/css/sahon_saha.css";
                    default -> "/css/stylesheetsBAW.css";
                };
                ScreenController.getInstance().activate(sceneType, css, SettingsSingleton.getInstance().language);
            }
            else {
                ScreenController.getInstance().activate(sceneType, cssUrl, SettingsSingleton.getInstance().language);

            }
        } catch (Exception e){
            System.out.println("Error while switching to " + sceneType + " scene");
        }
    }
}
