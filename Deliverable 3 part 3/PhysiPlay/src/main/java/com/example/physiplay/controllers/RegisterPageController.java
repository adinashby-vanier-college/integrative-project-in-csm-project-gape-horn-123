package com.example.physiplay.controllers;

import com.example.physiplay.singletons.SettingsSingleton;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.util.Locale;
import java.util.ResourceBundle;

public class RegisterPageController {

    Stage stage;
    Scene scene;

    @FXML
    Button registerButton;
    @FXML
    Button returnLoginButton;
    @FXML
    Button languageButton;
    @FXML
    Label registerPageLabel;
    @FXML
    Label usernameLabel;
    @FXML
    Label passwordLabel;
    @FXML
    Label confirmPasswordLabel;


    private void loadScenes(Stage stage, Scene scene) {
        ScreenController.getInstance()
                .addScreen("mainMenu", ScreenController.getInstance().getRootPane(getClass().getResource("/fxml/mainMenu.fxml"), new MainMenuController(stage, scene), SettingsSingleton.getInstance().language))
                .addScreen("login", ScreenController.getInstance().getRootPane(getClass().getResource("/fxml/loginPage.fxml"), new LoginController(stage, scene), SettingsSingleton.getInstance().language));
    }

    public void initialize(){
        loadScenes(stage, scene);
        returnLoginButton.setOnAction(event -> loadLoginPage());
        registerButton.setOnAction(event -> loadMainMenu());
        languageButton.setOnAction(event -> {
            SettingsSingleton.getInstance().switchLanguage();
            Locale locale = new Locale(SettingsSingleton.getInstance().language);
            ResourceBundle bundle = ResourceBundle.getBundle("languages.messages", locale);
            registerPageLabel.setText(bundle.getString("label.registerPage"));
            usernameLabel.setText(bundle.getString("label.username"));
            passwordLabel.setText(bundle.getString("label.password"));
            confirmPasswordLabel.setText(bundle.getString("button.confirmPassword"));
            returnLoginButton.setText(bundle.getString("button.returnLogin"));
            registerButton.setText(bundle.getString("button.register"));
            languageButton.setText(bundle.getString("button.language"));

            /*SettingsSingleton.getInstance().switchLanguage();
            ScreenController.getInstance().clearMap();
            ScreenController.getInstance()
                    .addScreen("loginPage", ScreenController.getInstance().getRootPane(getClass().getResource("/fxml/loginPage.fxml"), new LoginController(stage, scene), SettingsSingleton.getInstance().language))
                    .addScreen("mainMenu", ScreenController.getInstance().getRootPane(getClass().getResource("/fxml/mainMenu.fxml"), new MainMenuController(stage, scene), SettingsSingleton.getInstance().language))
                    .addScreen("register", ScreenController.getInstance().getRootPane(getClass().getResource("/fxml/registerPage.fxml"), new RegisterPageController(stage, scene), SettingsSingleton.getInstance().language));
            ScreenController.getInstance().activate("loginPage");*/
        });
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
