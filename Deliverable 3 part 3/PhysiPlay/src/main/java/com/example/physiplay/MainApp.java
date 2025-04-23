package com.example.physiplay;

import com.example.physiplay.controllers.*;
import com.example.physiplay.controllers.ScreenController;
import com.example.physiplay.singletons.SettingsSingleton;
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
                .addScreen("mainMenuEN", ScreenController.getInstance().getRootPane(getClass().getResource("/fxml/mainMenu.fxml"), new MainMenuController(stage, scene), SettingsSingleton.getInstance().language))
                .addScreen("mainMenuFR", ScreenController.getInstance().getRootPane(getClass().getResource("/fxml/mainMenu.fxml"), new MainMenuController(stage, scene), "fr"))
                .addScreen("instructionsEN", ScreenController.getInstance().getRootPane(getClass().getResource("/fxml/instructionsPage.fxml"), new InstructionsController(scene), SettingsSingleton.getInstance().language))
                .addScreen("instructionsFR", ScreenController.getInstance().getRootPane(getClass().getResource("/fxml/instructionsPage.fxml"), new InstructionsController(scene), "fr"))
                .addScreen("settingsEN", ScreenController.getInstance().getRootPane(getClass().getResource("/fxml/settingPage.fxml"), new SettingsController(stage, scene, "en"), SettingsSingleton.getInstance().language))
                .addScreen("settingsFR", ScreenController.getInstance().getRootPane(getClass().getResource("/fxml/settingPage.fxml"), new SettingsController(stage, scene, "fr"), "fr"))
                .addScreen("loginEN", ScreenController.getInstance().getRootPane(getClass().getResource("/fxml/loginPage.fxml"), new LoginController(stage, scene), SettingsSingleton.getInstance().language))
                .addScreen("loginFR", ScreenController.getInstance().getRootPane(getClass().getResource("/fxml/loginPage.fxml"), new LoginController(stage, scene), "fr"))
                .addScreen("registerEN", ScreenController.getInstance().getRootPane(getClass().getResource("/fxml/registerPage.fxml"), new RegisterPageController(stage, scene), SettingsSingleton.getInstance().language))
                .addScreen("registerFR", ScreenController.getInstance().getRootPane(getClass().getResource("/fxml/registerPage.fxml"), new RegisterPageController(stage, scene), "fr"))
                .addScreen("playEN", ScreenController.getInstance().getRootPane(getClass().getResource("/fxml/physiplay.fxml"), new PhysiplayController(stage, scene), SettingsSingleton.getInstance().language))
                .addScreen("playFR", ScreenController.getInstance().getRootPane(getClass().getResource("/fxml/physiplay.fxml"), new PhysiplayController(stage, scene), "fr"));

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
                .activate("login", SettingsSingleton.getInstance().language)
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