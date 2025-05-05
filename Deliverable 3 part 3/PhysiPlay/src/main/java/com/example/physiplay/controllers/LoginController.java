package com.example.physiplay.controllers;

import com.example.physiplay.Account;
import com.example.physiplay.singletons.LoginRegisterSingleton;
import com.example.physiplay.singletons.SettingsSingleton;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.File;
import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;

/**
 * Controller class for the login page.
 * Handles authentication, navigation to registration or main menu,
 * and language switching.
 */
public class LoginController {

    /** JavaFX stage associated with this controller. */
    Stage stage;

    /** JavaFX scene associated with this controller. */
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

    @FXML
    TextField usernameField;

    @FXML
    PasswordField passwordField;

    /**
     * Constructs a LoginController with the specified stage and scene.
     *
     * @param stage the window stage
     * @param scene the current scene
     */
    public LoginController(Stage stage, Scene scene){
        this.stage = stage;
        this.scene = scene;
    }

    /**
     * Initializes the controller by updating the account list and
     * setting up button behaviors.
     */
    public void initialize(){
        LoginRegisterSingleton.getInstance().updateAccountList();
        setUpButtons();
    }

    /**
     * Loads the main menu screen if the login information file exists.
     */
    public void loadMainMenu(){
        File file = new File("login.info");
        if (!file.exists() || file.isDirectory()) {
            return;
        }
        ScreenController.getInstance().activate("mainMenu", SettingsSingleton.getInstance().language);
    }

    /**
     * Navigates to the registration page.
     */
    public void loadRegisterPage(){
        ScreenController.getInstance().activate("register", SettingsSingleton.getInstance().language);
    }

    /**
     * Sets up button event handlers for login, registration, and language toggle.
     */
    public void setUpButtons(){
        loginButton.setOnAction(event -> {
            LoginRegisterSingleton.getInstance().updateAccountList();
            if (LoginRegisterSingleton.getInstance().accountList.contains(
                    new Account(usernameField.getText(), passwordField.getText())))
                loadMainMenu();
            else {
                SettingsSingleton.getInstance().displayAlertErrorMessage("Either username or password is incorrect. Please try again.");
            }
        });

        registerButton.setOnAction(event -> {
            loadRegisterPage();
        });

        languageButton.setOnAction(event -> {
            changeLabel();
        });
    }

    /**
     * Updates the text of UI labels and buttons based on the current language setting.
     */
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
