package com.example.physiplay;

import com.example.physiplay.controllers.*;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import java.util.Objects;

public final class MainApp extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    // These methods load all scenes
    private void loadScenes(Stage stage, Scene scene) {
        ScreenController.getInstance()
                .addScreen("mainMenu", ScreenController.getInstance().getRootPane(getClass().getResource("/fxml/mainMenu.fxml"), new MainMenuController(stage, scene)))
                .addScreen("instructions", ScreenController.getInstance().getRootPane(getClass().getResource("/fxml/instructionsPage.fxml"), new InstructionsController(scene)))
                .addScreen("settings", ScreenController.getInstance().getRootPane(getClass().getResource("/fxml/settingPage.fxml"), new SettingsController(stage, scene)))
                .addScreen("loginPage", ScreenController.getInstance().getRootPane(getClass().getResource("/fxml/loginPage.fxml"), new LoginController(stage, scene) ))
                .addScreen("play", ScreenController.getInstance().getRootPane(getClass().getResource("/fxml/physiplay.fxml"), new PhysiplayController(stage, scene)));
    }

    @Override
    public void start(Stage stage) throws Exception {

        //For Main Menu

        Scene scene = new Scene(new Pane(), 1920, 1080);
        // CSS File
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/css/stylesheets.css")).toExternalForm());

        loadScenes(stage, scene);

        ScreenController.getInstance()
                .setMainScene(scene)
                .activate("loginPage")
                .printCurrentSceneName();


        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.F && !stage.isFullScreen()) stage.setFullScreen(true);
            else stage.setFullScreen(false);
        });

        stage.setTitle("PhysiPlay");
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.show();
    }
}