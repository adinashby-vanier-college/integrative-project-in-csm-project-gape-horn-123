package com.example.physiplay.controllers;

import com.example.physiplay.Account;
import com.example.physiplay.singletons.LoginRegisterSingleton;
import com.example.physiplay.singletons.SettingsSingleton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.lang.reflect.Type;
import java.util.*;
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
    TextField usernameTextField;
    @FXML
    PasswordField passwordField;
    @FXML
    PasswordField confirmPasswordField;
    private Gson gson = new Gson();

    public void initialize(){
        returnLoginButton.setOnAction(event -> loadLoginPage());
        languageButton.setOnAction(event -> setUpLanguageButton());

        registerButton.disableProperty().bind(
                usernameTextField.textProperty().isEmpty().or(passwordField.textProperty().isEmpty())
                        .or(passwordField.textProperty().isNotEqualTo(confirmPasswordField.textProperty()))
        );
        registerButton.setOnAction(event -> {
            register();
            loadLoginPage();
        });
    }

    private void displayAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.setAlwaysOnTop(true);
        stage.toFront();
        alert.setTitle("Error");
        alert.setHeaderText(message);
        alert.showAndWait();
    }

    private void register() {
        File file = new File("login.info");
        List<Account> accounts = new ArrayList<>();
        if (file.exists()) {
            try (Reader reader = new FileReader(file)) {
                Type accountType = new TypeToken<List<Account>>() {}.getType();
                accounts = gson.fromJson(reader, accountType);
                if (accounts == null) accounts = new ArrayList<>();
            }
            catch (IOException e) {
                displayAlert("Something went wrong, please try again");
                return;
            }
        }
        accounts.add(new Account(usernameTextField.getText(), passwordField.getText()));
        try (Writer writer = new FileWriter(file)) {
            gson.toJson(accounts, writer);
            LoginRegisterSingleton.getInstance().updateAccountList();
        }
        catch (IOException e) {
            displayAlert("Something went wrong, please try again");
        }

    }

    public RegisterPageController(){}

    public RegisterPageController(Stage stage, Scene scene) {
        this.stage = stage;
        this.scene = scene;
    }

    public void loadLoginPage(){
        ScreenController.getInstance().activate("login", SettingsSingleton.getInstance().language);
    }

    public void setUpLanguageButton(){
        SettingsSingleton.getInstance().switchLanguage();
        ScreenController.getInstance().activate("register", SettingsSingleton.getInstance().language);
    }

}
