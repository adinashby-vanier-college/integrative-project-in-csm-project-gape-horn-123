package com.example.physiplay.controllers;

import com.example.physiplay.singletons.SettingsSingleton;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.stage.Stage;

import java.util.Objects;

public class SettingsController {

    Stage stage;
    Scene scene;

    @FXML
    Button backButton;

    @FXML
    CheckBox debugCheckbox;

    public SettingsController(Stage stage, Scene scene) {
        this.stage = stage;
        this.scene = scene;
    }

    public void initialize() {
        backButton.setOnAction(event -> returnToMainMenu());
        debugCheckbox.selectedProperty().bindBidirectional(SettingsSingleton.getInstance().debugModeProperty);

        debugCheckbox.selectedProperty().addListener(observable -> {
            stage.setAlwaysOnTop(!SettingsSingleton.getInstance().debugModeProperty.getValue());
        });
    }


    private void returnToMainMenu() {
        scene.getStylesheets().clear();
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/fonts/stylesheets.css")).toExternalForm());
        ScreenController.getInstance().activate("mainMenu");
    }
}
