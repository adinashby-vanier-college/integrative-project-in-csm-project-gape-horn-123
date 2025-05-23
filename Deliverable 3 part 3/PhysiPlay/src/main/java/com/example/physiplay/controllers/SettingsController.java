package com.example.physiplay.controllers;

import com.example.physiplay.singletons.SettingsSingleton;
import javafx.beans.property.StringProperty;
import javafx.css.Stylesheet;
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
    String stylesheet = getClass().getResource("/css/stylesheets.css").toExternalForm();

    final String defaultKey = "string.default";
    final String bwKey = "string.blackAndWhite";

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
        String defaultLabel = bundle.getString(defaultKey);
        String bwLabel = bundle.getString(bwKey);
        comboBoxThemes.getItems().addAll(defaultLabel, bwLabel);
        comboBoxThemes.setValue(bundle.getString(defaultKey));

        comboBoxThemes.setOnAction(event -> {
            String selected = comboBoxThemes.getValue();
            Scene scene = comboBoxThemes.getScene();

            scene.getStylesheets().clear();

            if (bwLabel.equals(selected)) {
                stylesheet = getClass().getResource("/css/stylesheetsBAW.css").toExternalForm();
                scene.getStylesheets().add(stylesheet);
                SettingsSingleton.getInstance().setTheme("baw");
            } else {
                stylesheet = getClass().getResource("/css/stylesheets.css").toExternalForm();
                scene.getStylesheets().add(stylesheet);
                SettingsSingleton.getInstance().setTheme("default");
            }
        });

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
        scene.getStylesheets().add(Objects.requireNonNull(stylesheet));
        ScreenController.getInstance().activate("mainMenu", SettingsSingleton.getInstance().language);
    }

    public void switchEnglish(){
        SettingsSingleton.getInstance().setLanguage("en");
        ScreenController.getInstance().activate("settings", "en");
    }

    public void switchFrench(){
        SettingsSingleton.getInstance().setLanguage("fr");
        ScreenController.getInstance().activate("settings", "fr");
    }
}
