package com.example.physiplay.controllers;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class LoginController {

    Stage stage;
    Scene scene;

    @FXML
    Button loginButton;
    @FXML
    Button registerButton;

    public LoginController(Stage stage, Scene scene){
        this.stage = stage;
        this.scene = scene;
    }

    private void loadScenes(Stage stage, Scene scene) {
        ScreenController.getInstance()
                .addScreen("mainMenu", ScreenController.getInstance().getRootPane(getClass().getResource("/fxml/mainMenu.fxml"), new MainMenuController(stage, scene)))
                .addScreen("register", ScreenController.getInstance().getRootPane(getClass().getResource("/fxml/registerPage.fxml"), new RegisterPageController(stage, scene)));
    }

    public void initialize(){
        loadScenes(stage, scene);

        loginButton.setOnAction(event -> {
            loadMainMenu();
        });
        registerButton.setOnAction(event -> {
            loadRegisterPage();
        });
    }

    public void loadMainMenu(){
        ScreenController.getInstance()
                .setMainScene(scene)
                .activate("mainMenu")
                .printCurrentSceneName();
    }

    public void loadRegisterPage(){
        ScreenController.getInstance()
                .setMainScene(scene)
                .activate("register")
                .printCurrentSceneName();
    }

}
