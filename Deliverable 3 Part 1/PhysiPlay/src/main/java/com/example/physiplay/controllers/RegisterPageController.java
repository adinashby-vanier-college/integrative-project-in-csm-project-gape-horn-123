package com.example.physiplay.controllers;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class RegisterPageController {

    Stage stage;
    Scene scene;

    @FXML
    Button registerButton;
    @FXML
    Button loginButton;

    private void loadScenes(Stage stage, Scene scene) {
        ScreenController.getInstance()
                .addScreen("mainMenu", ScreenController.getInstance().getRootPane(getClass().getResource("/fxml/mainMenu.fxml"), new MainMenuController(stage, scene)))
                .addScreen("login", ScreenController.getInstance().getRootPane(getClass().getResource("/fxml/loginPage.fxml"), new LoginController(stage, scene)));
    }

    public void initialize(){
        loadScenes(stage, scene);
        loginButton.setOnAction(event -> loadLoginPage());
        registerButton.setOnAction(event -> loadMainMenu());

    }

    public RegisterPageController(){}

    public RegisterPageController(Stage stage, Scene scene) {
        this.stage = stage;
        this.scene = scene;
    }

    public void loadMainMenu(){
        ScreenController.getInstance()
                .setMainScene(scene)
                .activate("mainMenu")
                .printCurrentSceneName();
    }

    public void loadLoginPage(){
        ScreenController.getInstance()
                .setMainScene(scene)
                .activate("login")
                .printCurrentSceneName();
    }
}
