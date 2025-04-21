package com.example.physiplay.controllers;

import com.example.physiplay.singletons.SettingsSingleton;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.util.Locale;
import java.util.Objects;
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
        //loadScenes(stage, scene);
        setUpButtons();
    }

    public void loadMainMenu(){
        ScreenController.getInstance().addScreen("mainMenu", ScreenController.getInstance().getRootPane(getClass().getResource("/fxml/mainMenu.fxml"), new MainMenuController(stage, scene), SettingsSingleton.getInstance().language));
        ScreenController.getInstance()
                .activate("mainMenu");
    }

    public void loadRegisterPage(){
        if (Objects.equals(SettingsSingleton.getInstance().language, "fr")) {
            ScreenController.getInstance().activate("registerFR");
        }
        else ScreenController.getInstance().activate("register");
    }

    public void setUpButtons(){
        loginButton.setOnAction(event -> {
            loadMainMenu();
        });
        registerButton.setOnAction(event -> {
            loadRegisterPage();
        });
        languageButton.setOnAction(event -> {
            changeLabel();
        });

    }

    public void changeLabel(){
        SettingsSingleton.getInstance().switchLanguage();
        Locale locale = new Locale(SettingsSingleton.getInstance().language);
        ResourceBundle bundle = ResourceBundle.getBundle("languages.messages", locale);
        loginLabel.setText(bundle.getString("label.loginPage"));
        usernameLabel.setText(bundle.getString("label.username"));
        passwordLabel.setText(bundle.getString("label.password"));
        loginButton.setText(bundle.getString("button.login"));
        registerButton.setText(bundle.getString("button.register"));
        languageButton.setText(bundle.getString("button.language"));

        System.out.println(SettingsSingleton.getInstance().language);
    }

}
