package com.example.physiplay.singletons;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

public class SettingsSingleton {
    private static SettingsSingleton instance;
    public BooleanProperty advancedModeProperty = new SimpleBooleanProperty(false);
    public String language = "en";

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

}
