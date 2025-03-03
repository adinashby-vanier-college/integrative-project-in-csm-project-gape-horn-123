package com.example.physiplay.controllers;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class InstructionsController {
    @FXML
    public Button backButton;

    public void initialize() {
        backButton.setOnAction(event -> {
            returnToMainMenu();
        });
    }

    private void returnToMainMenu() {
        ScreenController.getInstance().activate("mainMenu");
    }
}
