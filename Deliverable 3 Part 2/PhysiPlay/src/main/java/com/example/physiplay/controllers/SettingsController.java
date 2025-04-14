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


        frenchRadioButton.setOnAction(event -> {
            SettingsSingleton.getInstance().setLanguage("fr");
            ScreenController.getInstance().clearMap();
            ScreenController.getInstance()
                    .addScreen("mainMenu", ScreenController.getInstance().getRootPane(getClass().getResource("/fxml/mainMenu.fxml"), new MainMenuController(stage, scene), SettingsSingleton.getInstance().language))
                    .addScreen("instructions", ScreenController.getInstance().getRootPane(getClass().getResource("/fxml/instructionsPage.fxml"), new InstructionsController(scene), SettingsSingleton.getInstance().language))
                    .addScreen("settings", ScreenController.getInstance().getRootPane(getClass().getResource("/fxml/settingPage.fxml"), new SettingsController(stage, scene), SettingsSingleton.getInstance().language))
                    .addScreen("play", ScreenController.getInstance().getRootPane(getClass().getResource("/fxml/physiplay.fxml"), new PhysiplayController(stage, scene), SettingsSingleton.getInstance().language));
            ScreenController.getInstance()
                    .activate("settings");
        });

        englishRadioButton.setOnAction(event -> {
            SettingsSingleton.getInstance().setLanguage("en");
            ScreenController.getInstance().clearMap();
            ScreenController.getInstance()
                    .addScreen("mainMenu", ScreenController.getInstance().getRootPane(getClass().getResource("/fxml/mainMenu.fxml"), new MainMenuController(stage, scene), SettingsSingleton.getInstance().language))
                    .addScreen("instructions", ScreenController.getInstance().getRootPane(getClass().getResource("/fxml/instructionsPage.fxml"), new InstructionsController(scene), SettingsSingleton.getInstance().language))
                    .addScreen("settings", ScreenController.getInstance().getRootPane(getClass().getResource("/fxml/settingPage.fxml"), new SettingsController(stage, scene), SettingsSingleton.getInstance().language))
                    .addScreen("play", ScreenController.getInstance().getRootPane(getClass().getResource("/fxml/physiplay.fxml"), new PhysiplayController(stage, scene), SettingsSingleton.getInstance().language));
            ScreenController.getInstance()
                    .activate("settings");
        });

        if (Objects.equals(SettingsSingleton.getInstance().language, "fr")) frenchRadioButton.setSelected(true);
        else englishRadioButton.setSelected(true);

    }

    private void returnToMainMenu() {
        scene.getStylesheets().clear();
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/css/stylesheets.css")).toExternalForm());

        ScreenController.getInstance().activate("mainMenu");
    }
}
