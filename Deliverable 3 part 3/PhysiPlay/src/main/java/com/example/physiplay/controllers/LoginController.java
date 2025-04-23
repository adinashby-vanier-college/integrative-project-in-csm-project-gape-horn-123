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

    public void initialize(){
        setUpButtons();
    }

    public void loadMainMenu(){
        ScreenController.getInstance().activate("mainMenu", SettingsSingleton.getInstance().language);
    }

    public void loadRegisterPage(){
        ScreenController.getInstance().activate("register", SettingsSingleton.getInstance().language);
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
    }

}
