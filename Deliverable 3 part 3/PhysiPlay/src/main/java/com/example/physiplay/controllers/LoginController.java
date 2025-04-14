package com.example.physiplay.controllers;

import com.example.physiplay.singletons.SettingsSingleton;
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
    @FXML
    Button languageButton;

    public LoginController(Stage stage, Scene scene){
        this.stage = stage;
        this.scene = scene;
    }

    private void loadScenes(Stage stage, Scene scene) {
        ScreenController.getInstance()
                .addScreen("mainMenu", ScreenController.getInstance().getRootPane(getClass().getResource("/fxml/mainMenu.fxml"), new MainMenuController(stage, scene), SettingsSingleton.getInstance().language))
                .addScreen("register", ScreenController.getInstance().getRootPane(getClass().getResource("/fxml/registerPage.fxml"), new RegisterPageController(stage, scene), SettingsSingleton.getInstance().language));
    }

    public void initialize(){
        loadScenes(stage, scene);
        setUpButtons();
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

    public void setUpButtons(){
        loginButton.setOnAction(event -> {
            loadMainMenu();
        });
        registerButton.setOnAction(event -> {
            loadRegisterPage();
        });
        languageButton.setOnAction(event -> {
            SettingsSingleton.getInstance().switchLanguage();
            ScreenController.getInstance().clearMap();
            ScreenController.getInstance()
                    .addScreen("loginPage", ScreenController.getInstance().getRootPane(getClass().getResource("/fxml/loginPage.fxml"), new LoginController(stage, scene), SettingsSingleton.getInstance().language))
                    .addScreen("mainMenu", ScreenController.getInstance().getRootPane(getClass().getResource("/fxml/mainMenu.fxml"), new MainMenuController(stage, scene), SettingsSingleton.getInstance().language))
                    .addScreen("register", ScreenController.getInstance().getRootPane(getClass().getResource("/fxml/registerPage.fxml"), new RegisterPageController(stage, scene), SettingsSingleton.getInstance().language));
            ScreenController.getInstance().activate("loginPage");
        });

    }

}
