package com.example.physiplay.controllers;

import com.example.physiplay.singletons.SettingsSingleton;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;

import java.net.URL;
import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;

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

    String lang = "en";

    public SettingsController(Stage stage, Scene scene, String lang) {
        this.stage = stage;
        this.scene = scene;
        this.lang = lang;
    }

    public void initialize() {
        backButton.setOnAction(event -> returnToMainMenu());
        advancedModeCheckbox.selectedProperty().bindBidirectional(SettingsSingleton.getInstance().advancedModeProperty);
        advancedModeCheckbox.selectedProperty().addListener(observable -> {
            stage.setAlwaysOnTop(!SettingsSingleton.getInstance().advancedModeProperty.getValue());
        });
        setUpRadioButtons();
    }

    private void setUpRadioButtons(){
        ToggleGroup languageGroup = new ToggleGroup();
        englishRadioButton.setToggleGroup(languageGroup);
        frenchRadioButton.setToggleGroup(languageGroup);

        frenchRadioButton.setOnAction(event -> {
            switchLanguage();
        });

        englishRadioButton.setOnAction(event -> {
            switchLanguage();
        });

        if (lang == "fr") {
            frenchRadioButton.setSelected(true);
            System.out.println(lang);
        }
        else englishRadioButton.setSelected(true);

    }

    private void returnToMainMenu() {
        scene.getStylesheets().clear();
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/css/stylesheets.css")).toExternalForm());
        if (SettingsSingleton.getInstance().language == "fr") ScreenController.getInstance().activate("mainMenuFR");
        else ScreenController.getInstance().activate("mainMenu");
    }

    public void switchLanguage(){
        SettingsSingleton.getInstance().switchLanguage();
        if (SettingsSingleton.getInstance().language == "fr") ScreenController.getInstance().activate("settingsFR");
        else ScreenController.getInstance().activate("settings");
    }
}
