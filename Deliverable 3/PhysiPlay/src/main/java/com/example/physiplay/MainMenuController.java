package com.example.physiplay;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class MainMenuController {
    @FXML
    Button playButton;
    @FXML
    Button instructionsButton;
    @FXML
    Button exitButton;

    public void initialize(){
        playButton.setOnAction(event -> System.out.println("Something"));
        instructionsButton.setOnAction(event -> System.out.println());
        exitButton.setOnAction(event -> Platform.exit());
    }
}
