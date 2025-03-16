package com.example.physiplay.controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;


public class MainMenuController {
    @FXML
    Button playButton;
    @FXML
    Button instructionsButton;
    @FXML
    Button exitButton;

    Stage stage;

    public MainMenuController(Stage stage){
        this.stage = stage;
    }

    public void initialize() {
        playButton.setOnAction(event -> switchScene("play"));
        instructionsButton.setOnAction(event -> switchScene("instructions"));
        exitButton.setOnAction(event -> Platform.exit());
    }

    public void switchScene(String sceneType) {
        try {
            ScreenController.getInstance().activate(sceneType);
        } catch (Exception e){
            System.out.println("Error while switching to " + sceneType + " scene");
        }

    }
}
