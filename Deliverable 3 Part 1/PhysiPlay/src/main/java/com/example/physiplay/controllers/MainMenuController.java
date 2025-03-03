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

    public void initialize(){
        playButton.setOnAction(event -> {switchScene("play");});
        instructionsButton.setOnAction(event -> switchScene("instructions"));
        exitButton.setOnAction(event -> Platform.exit());
    }

    public void switchScene(String sceneType) {
        try {
            Parent root;
            switch (sceneType){
                case "instructions":
                    FXMLLoader instructionsLoader = new FXMLLoader(getClass().getResource("/fxml/instructionsPage.fxml"));
                    root = instructionsLoader.load();
                    break;
                default:
                    FXMLLoader mainLoader = new FXMLLoader(getClass().getResource("/fxml/physiplay.fxml"));
                    root = mainLoader.load();
            }
            Scene scene = new Scene(root, 1920, 1080);
            scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/fonts/stylesheets.css")).toExternalForm());
            stage.setScene(scene);
            stage.show();
        } catch (Exception e){
            System.out.println("Error while switching to " + sceneType + " scene");
        }

    }
}
