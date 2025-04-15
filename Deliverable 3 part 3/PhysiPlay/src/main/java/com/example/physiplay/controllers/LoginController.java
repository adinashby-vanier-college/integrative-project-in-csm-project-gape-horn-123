package com.example.physiplay.controllers;

import com.example.physiplay.singletons.SettingsSingleton;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.util.Locale;
import java.util.ResourceBundle;

public class LoginController {

    Stage stage;
    Scene scene;

    @FXML
    Button loginButton;
    @FXML
    Button registerButton;
    @FXML
    Button languageButton;
    @FXML
    BorderPane rootPane;
    @FXML
    Label loginLabel;
    @FXML
    Label usernameLabel;
    @FXML
    Label passwordLabel;



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
            Locale locale = new Locale(SettingsSingleton.getInstance().language);
            ResourceBundle bundle = ResourceBundle.getBundle("languages.messages", locale);
            loginLabel.setText(bundle.getString("label.loginPage"));
            usernameLabel.setText(bundle.getString("label.username"));
            passwordLabel.setText(bundle.getString("label.password"));
            loginButton.setText(bundle.getString("button.login"));
            registerButton.setText(bundle.getString("button.register"));

            /*SettingsSingleton.getInstance().switchLanguage();
            ScreenController.getInstance().clearMap();
            ScreenController.getInstance()
                    .addScreen("loginPage", ScreenController.getInstance().getRootPane(getClass().getResource("/fxml/loginPage.fxml"), new LoginController(stage, scene), SettingsSingleton.getInstance().language))
                    .addScreen("mainMenu", ScreenController.getInstance().getRootPane(getClass().getResource("/fxml/mainMenu.fxml"), new MainMenuController(stage, scene), SettingsSingleton.getInstance().language))
                    .addScreen("register", ScreenController.getInstance().getRootPane(getClass().getResource("/fxml/registerPage.fxml"), new RegisterPageController(stage, scene), SettingsSingleton.getInstance().language));
            ScreenController.getInstance().activate("loginPage");*/
        });

    }

    public void loadPages(){
        rootPane.getChildren().setAll(ScreenController.getInstance().getRootPane(getClass().getResource("/fxml/loginPage.fxml"), new LoginController(stage, scene), SettingsSingleton.getInstance().language));

    }

}
