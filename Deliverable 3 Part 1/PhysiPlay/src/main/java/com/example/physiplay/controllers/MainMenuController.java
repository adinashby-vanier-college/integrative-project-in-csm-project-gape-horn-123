package com.example.physiplay.controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.util.Objects;


public class MainMenuController {
    @FXML
    Button playButton;
    @FXML
    Button instructionsButton;
    @FXML
    Button exitButton;

    Stage stage;
    Scene scene;

    public MainMenuController(Stage stage, Scene scene){
        this.stage = stage;
        this.scene = scene;
    }

    public void initialize() {
        stage.setAlwaysOnTop(true);
        playButton.setOnAction(event -> switchScene("play", scene));
        instructionsButton.setOnAction(event -> switchScene("instructions", scene));
        exitButton.setOnAction(event -> Platform.exit());
    }

    public void switchScene(String sceneType, Scene Scene) {
        try {
            if (sceneType.equals("instructions")) scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/fonts/instructionsStylesheet.css")).toExternalForm());
            else scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/fonts/stylesheets.css")).toExternalForm());
            ScreenController.getInstance().activate(sceneType);
        } catch (Exception e){
            System.out.println("Error while switching to " + sceneType + " scene");
        }

    }
}
