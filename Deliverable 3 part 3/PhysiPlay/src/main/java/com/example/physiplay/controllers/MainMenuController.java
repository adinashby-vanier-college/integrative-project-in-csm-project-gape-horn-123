package com.example.physiplay.controllers;

import com.example.physiplay.singletons.SettingsSingleton;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;


public class MainMenuController {
    @FXML
    Button playButton;
    @FXML
    Button instructionsButton;
    @FXML
    Button exitButton;
    @FXML
    Button settingsButton;

    Stage stage;
    Scene scene;
    PhysiplayController physiplayController;

    public MainMenuController(Stage stage, Scene scene, PhysiplayController physiplayController){
        this.stage = stage;
        this.scene = scene;
        this.physiplayController = physiplayController;
    }

    public void initialize() {
        stage.setAlwaysOnTop(true);
        playButton.setOnAction(event -> switchScene("play", "/css/stylesheets.css"));
        instructionsButton.setOnAction(event -> switchScene("instructions", "/css/instructionsStylesheet.css"));
        settingsButton.setOnAction(event -> switchScene("settings", "/css/settingsStylesheet.css"));
        exitButton.setOnAction(event -> Platform.exit());
    }

    public void switchScene(String sceneType, String cssUrl) {
        try {
            if (sceneType == "play") physiplayController.translation();
            ScreenController.getInstance().activate(sceneType, cssUrl, SettingsSingleton.getInstance().language);
        } catch (Exception e){
            System.out.println("Error while switching to " + sceneType + " scene");
        }

    }
}
