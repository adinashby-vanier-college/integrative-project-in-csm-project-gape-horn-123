package com.example.physiplay.controllers;

import com.example.physiplay.singletons.SettingsSingleton;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;

import java.util.Objects;

public class SettingsController {

    Stage stage;
    Scene scene;

    @FXML
    Button backButton;

    @FXML
    CheckBox advancedModeCheckbox;

    @FXML
    RadioButton englishRadioButton;
    @FXML
    RadioButton frenchRadioButton;

    public SettingsController(Stage stage, Scene scene) {
        this.stage = stage;
        this.scene = scene;
    }

    public void initialize() {
        backButton.setOnAction(event -> returnToMainMenu());
        advancedModeCheckbox.selectedProperty().bindBidirectional(SettingsSingleton.getInstance().advancedModeProperty);
        setUpRadioButtons();

        advancedModeCheckbox.selectedProperty().addListener(observable -> {
            stage.setAlwaysOnTop(!SettingsSingleton.getInstance().advancedModeProperty.getValue());
        });
    }

    private void setUpRadioButtons(){
        ToggleGroup languageGroup = new ToggleGroup();
        englishRadioButton.setToggleGroup(languageGroup);
        frenchRadioButton.setToggleGroup(languageGroup);

        if (frenchRadioButton.isSelected()) SettingsSingleton.getInstance().language = "French";
        else SettingsSingleton.getInstance().language = "English";
    }

    private void returnToMainMenu() {
        scene.getStylesheets().clear();
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/fonts/stylesheets.css")).toExternalForm());
        ScreenController.getInstance().activate("mainMenu");
    }
}
