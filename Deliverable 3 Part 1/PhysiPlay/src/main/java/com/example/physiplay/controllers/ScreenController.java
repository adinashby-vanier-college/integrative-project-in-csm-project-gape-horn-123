package com.example.physiplay.controllers;

import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class ScreenController {
    private static ScreenController instance;

    public static ScreenController getInstance() {
        if (instance == null) {
            instance = new ScreenController();
        }
        return instance;
    }

    private ScreenController() {}

    private Map<String, Pane> screenMap = new HashMap<>();
    private Scene mainScene;

    public Pane getRootPane(URL url, Object controller)  {
        try {
            FXMLLoader loader = new FXMLLoader(url);
            if (controller != null) {
                loader.setController(controller);
            }
            return loader.load();
        }
        catch (IllegalStateException | IOException e) {
            return new FlowPane(new Label("Unable to find scene"));
        }
    }

    public void setMainScene(Scene scene) {
        this.mainScene = scene;
    }

    public void addScreen(String name, Pane pane) {
        screenMap.put(name, pane);
    }

    public void removeScreen(String name) {
        screenMap.remove(name);
    }

    private void applyTransition(Parent parent) {

        FadeTransition ft = new FadeTransition(Duration.millis(3000), parent);
        ft.setFromValue(0.0);
        ft.setToValue(1.0);
        ft.play();
    }

    public void activate(String name) {
        Parent parent = screenMap.get(name);
        // applyTransition(parent);
        mainScene.setRoot(parent);
    }
}
