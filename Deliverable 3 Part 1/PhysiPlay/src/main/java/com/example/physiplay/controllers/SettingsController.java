package com.example.physiplay.controllers;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;

import java.util.Objects;

public class SettingsController {

    Scene scene;

    @FXML
    Button backButton;

    public SettingsController(Scene scene) {
        this.scene = scene;
    }

    public void initialize(){
        backButton.setOnAction(event -> returnToMainMenu());
    }


    private void returnToMainMenu() {
        scene.getStylesheets().clear();
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/fonts/stylesheets.css")).toExternalForm());
        ScreenController.getInstance().activate("mainMenu");
    }
}
