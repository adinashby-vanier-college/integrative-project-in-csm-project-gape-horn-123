package com.example.physiplay.controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

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
        playButton.setOnAction(event -> {switchScene("start");});
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
