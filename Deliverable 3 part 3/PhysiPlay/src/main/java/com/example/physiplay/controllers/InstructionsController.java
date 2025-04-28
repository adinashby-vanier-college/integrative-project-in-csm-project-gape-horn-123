package com.example.physiplay.controllers;


import com.example.physiplay.singletons.SettingsSingleton;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;

import java.util.Objects;

public class InstructionsController {
    @FXML
    public Button backButton;
    Scene scene;

    public InstructionsController(Scene scene){
        this.scene = scene;
    }

    public void initialize() {
        backButton.setOnAction(event -> {
            returnToMainMenu();
        });
    }

    private void returnToMainMenu() {
        scene.getStylesheets().clear();
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/css/stylesheets.css")).toExternalForm());
        ScreenController.getInstance().activate("mainMenu", SettingsSingleton.getInstance().language);
    }
}
