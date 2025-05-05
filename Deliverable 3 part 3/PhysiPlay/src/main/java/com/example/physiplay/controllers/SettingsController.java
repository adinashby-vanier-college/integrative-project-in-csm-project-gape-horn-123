package com.example.physiplay.controllers;

import com.example.physiplay.singletons.SettingsSingleton;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
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
    @FXML
    ComboBox<String> comboBoxThemes;

    String lang;

    public SettingsController(Stage stage, Scene scene, String lang) {
        this.stage = stage;
        this.scene = scene;
        this.lang = lang;
    }

    public void initialize() {
        setUpRadioButtons();

        if (!englishRadioButton.isSelected() && !frenchRadioButton.isSelected() && SettingsSingleton.getInstance().getLanguageRadioInitialized() < 2) {
            if (lang == "fr") frenchRadioButton.setSelected(true);
            else englishRadioButton.setSelected(true);
            SettingsSingleton.getInstance().addLanguageRadioInitialized();
            System.out.println(SettingsSingleton.getInstance().getLanguageRadioInitialized());
        }
        if (lang == "en" && SettingsSingleton.getInstance().language == "en") englishRadioButton.setSelected(true);
        else if (lang == "fr" && SettingsSingleton.getInstance().language == "fr") frenchRadioButton.setSelected(true);
        backButton.setOnAction(event -> returnToMainMenu());
        advancedModeCheckbox.selectedProperty().bindBidirectional(SettingsSingleton.getInstance().advancedModeProperty);
        advancedModeCheckbox.selectedProperty().addListener(observable -> {
            stage.setAlwaysOnTop(!SettingsSingleton.getInstance().advancedModeProperty.getValue());
        });

        setUpComboBox();
    }

    private void setUpComboBox(){
        Locale locale = new Locale(lang);
        ResourceBundle bundle = ResourceBundle.getBundle("languages.messages", locale);
        comboBoxThemes.getItems().add(bundle.getString("string.default"));
        comboBoxThemes.getItems().add(bundle.getString("string.blackAndWhite"));
        comboBoxThemes.setValue(bundle.getString("string.default"));
    }

    private void setUpRadioButtons(){
        ToggleGroup languageGroup = new ToggleGroup();
        englishRadioButton.setToggleGroup(languageGroup);
        frenchRadioButton.setToggleGroup(languageGroup);

        frenchRadioButton.setOnAction(event -> {
            switchFrench();
        });

        englishRadioButton.setOnAction(event -> {
            switchEnglish();
        });

    }

    private void returnToMainMenu() {
        scene.getStylesheets().clear();
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/css/stylesheets.css")).toExternalForm());
        ScreenController.getInstance().activate("mainMenu", SettingsSingleton.getInstance().language);
    }

    public void switchEnglish(){
        SettingsSingleton.getInstance().setLanguage("en");
        ScreenController.getInstance().activate("settings", "en");
        System.out.println(SettingsSingleton.getInstance().language);
    }

    public void switchFrench(){
        SettingsSingleton.getInstance().setLanguage("fr");
        ScreenController.getInstance().activate("settings", "fr");
        System.out.println(SettingsSingleton.getInstance().language);
    }
}
