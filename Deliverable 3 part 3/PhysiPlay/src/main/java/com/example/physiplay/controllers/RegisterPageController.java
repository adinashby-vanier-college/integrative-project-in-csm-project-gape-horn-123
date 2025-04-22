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

    public void initialize(){
        returnLoginButton.setOnAction(event -> loadLoginPage());
        registerButton.setOnAction(event -> loadMainMenu());
        languageButton.setOnAction(event -> setUpLanguageButton());
    }

    public RegisterPageController(){}

    public RegisterPageController(Stage stage, Scene scene) {
        this.stage = stage;
        this.scene = scene;
    }

    public void loadMainMenu(){
        if (SettingsSingleton.getInstance().language == "fr") ScreenController.getInstance().activate("mainMenuFR");
        else ScreenController.getInstance().activate("mainMenu");

    }

    public void loadLoginPage(){
        if (SettingsSingleton.getInstance().language == "fr") ScreenController.getInstance().activate("loginFR");
        else ScreenController.getInstance().activate("login");

    }

    public void setUpLanguageButton(){
        SettingsSingleton.getInstance().switchLanguage();
        if (SettingsSingleton.getInstance().language == "fr") ScreenController.getInstance().activate("registerFR");
        else ScreenController.getInstance().activate("register");
    }

}
