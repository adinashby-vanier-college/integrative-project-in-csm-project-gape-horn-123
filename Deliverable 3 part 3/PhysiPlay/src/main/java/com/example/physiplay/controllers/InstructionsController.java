package com.example.physiplay.controllers;

import com.example.physiplay.singletons.SettingsSingleton;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;

import java.util.Objects;

/**
 * Controller for the Instructions page.
 * Handles navigation back to the main menu.
 */
public class InstructionsController {

    /** Button to navigate back to the main menu. */
    @FXML
    public Button backButton;

    /** The current JavaFX scene. */
    Scene scene;

    /**
     * Constructs the InstructionsController with the given scene.
     *
     * @param scene the current scene to apply style changes and handle navigation
     */
    public InstructionsController(Scene scene){
        this.scene = scene;
    }

    /**
     * Initializes the back button behavior.
     * Clicking it returns the user to the main menu.
     */
    public void initialize() {
        backButton.setOnAction(event -> {
            returnToMainMenu();
        });
    }

    /**
     * Navigates back to the main menu and resets the scene stylesheet.
     */
    private void returnToMainMenu() {
        scene.getStylesheets().clear();
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/css/stylesheets.css")).toExternalForm());
        ScreenController.getInstance().activate("mainMenu", SettingsSingleton.getInstance().language);
    }
}
