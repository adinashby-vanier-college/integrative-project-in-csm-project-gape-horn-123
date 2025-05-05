package com.example.physiplay.singletons;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.util.Objects;

public class SettingsSingleton {
    private static SettingsSingleton instance;
    public BooleanProperty advancedModeProperty = new SimpleBooleanProperty(false);
    public String language = "en";
    private int languageRadioInitialized = 0;

    public static SettingsSingleton getInstance() {
        if (instance == null) {
            instance = new SettingsSingleton();
        }
        return instance;
    }
    private SettingsSingleton() {}

    public void setLanguage(String language){
        this.language = language;
    }

    public void displayAlertErrorMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.setAlwaysOnTop(true);
        stage.toFront();
        alert.setTitle("Error");
        alert.setHeaderText(message);
        alert.showAndWait();
    }
    public void switchLanguage(){
        if (Objects.equals(this.language, "fr")) {
            this.language = "en";
        }
        else {
            this.language = "fr";
        }
    }

    public void addLanguageRadioInitialized() {
        this.languageRadioInitialized = languageRadioInitialized+1;
    }

    public int getLanguageRadioInitialized(){
        return languageRadioInitialized;
    }
}
