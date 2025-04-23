package com.example.physiplay.controllers;

import javafx.animation.FadeTransition;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
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

    private String currentSceneName = null;
    private Map<String, Pane> screenMap = new HashMap<>();
    private Scene mainScene;

    public Pane getRootPane(URL url, Object controller, String langCode)  {
        try {
            Locale locale = new Locale(langCode);
            ResourceBundle bundle = ResourceBundle.getBundle("languages.messages", locale);
            FXMLLoader loader = new FXMLLoader(url, bundle);
            if (controller != null) {
                loader.setController(controller);
            }
            return loader.load();
        }
        catch (IllegalStateException | IOException e) {
            return new FlowPane(new Label("Unable to find scene"));
        }
    }

    public Pane getRootPane(URL url) {
        try {
            FXMLLoader loader = new FXMLLoader(url);
            return loader.load();
        }
        catch (IllegalStateException | IOException e) {
            return new FlowPane(new Label("Unable to find scene"));
        }
    }

    public ScreenController setMainScene(Scene scene) {
        this.mainScene = scene;
        return this;
    }

    public ScreenController addScreen(String name, Pane pane) {
        screenMap.put(name, pane);
        return this;
    }

    public ScreenController removeScreen(String name) {
        screenMap.remove(name);
        return this;
    }

    private void applyTransition(Parent parent) {

        FadeTransition ft = new FadeTransition(Duration.millis(3000), parent);
        ft.setFromValue(0.0);
        ft.setToValue(1.0);
        ft.play();
    }

    public void printCurrentSceneName() {
        if (currentSceneName == null) System.out.println("No scenes are defined yet!");
        else System.out.println("Scene name: " + currentSceneName);
    }
    public ScreenController activate(String name) {
        Parent parent = screenMap.get(name);
        currentSceneName = name;
        // applyTransition(parent);
        mainScene.setRoot(parent);
        mainScene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/css/stylesheets.css")).toExternalForm());
        return this;
    }

    public ScreenController activate(String name, String cssLink, String language) {
        language = language.toUpperCase();
        Parent parent = screenMap.get(name+language);
        currentSceneName = name;
        // applyTransition(parent);
        mainScene.setRoot(parent);
        mainScene.getStylesheets().add(Objects.requireNonNull(getClass().getResource(cssLink)).toExternalForm());
        return this;
    }

    public void clearMap(){
        screenMap.clear();
    }

    public ScreenController activate(String name, String language) {
        language = language.toUpperCase();
        Parent parent = screenMap.get(name+language);
        currentSceneName = name;
        mainScene.setRoot(parent);
        //mainScene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/css/stylesheets.css")).toExternalForm());
        return this;
    }
}
