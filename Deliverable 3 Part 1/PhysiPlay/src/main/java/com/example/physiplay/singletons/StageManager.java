package com.example.physiplay.singletons;

import javafx.stage.Stage;

public class StageManager {
    Stage mainWindow;
    Stage presetWindow;

    public StageManager(){

    }

    public void setMainWindow(Stage mainWindow) {
        this.mainWindow = mainWindow;
    }

    public void setPresetWindow(Stage presetWindow) {
        this.presetWindow = presetWindow;
    }

    public Stage getMainWindow() {
        return mainWindow;
    }

    public Stage getPresetWindow() {
        return presetWindow;
    }
}
