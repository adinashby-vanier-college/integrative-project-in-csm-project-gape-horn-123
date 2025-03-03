package com.example.physiplay;

import com.example.physiplay.controllers.MainMenuController;
import com.example.physiplay.singletons.SimulationManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.Objects;

public final class MainApp extends Application {

    private static final SimulationManager instance = SimulationManager.getInstance();
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        //For Main Menu
        Font.loadFont(Objects.requireNonNull(getClass().getResource("/fonts/madimi.ttf")).toExternalForm(), 50);
        FXMLLoader loader = new FXMLLoader((getClass().getResource("/fxml/mainMenu.fxml")));
        MainMenuController mainMenuController = new MainMenuController(stage);
        loader.setController(mainMenuController);
        Parent root = loader.load();
        Scene scene = new Scene(root, 1920, 1080);
        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.F && !stage.isFullScreen()) stage.setFullScreen(true);
            else stage.setFullScreen(false);
        });
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/fonts/stylesheets.css")).toExternalForm());
        //Pane pane = new Pane(instance.canvas);

/*
        // Every class you see here is a specific component, all inherited from the Component class
        RectangularCollider collider = new RectangularCollider(new Vector2(160, 160));
        Renderer renderer = new Renderer();
        Rigidbody rb = new Rigidbody();

        // Attribute setting
        collider.screenEdgeCollision = true;
        rb.velocity = new Vector2(1f, 1.2f);
        // Creation of a simulation object
        SimulationObject square = new SimulationObject(new HashSet<>(List.of(renderer, rb, collider)));
        square.position = new Vector2(200, 200);

        Renderer rendere1 = new Renderer();
        SimulationObject square1 = new SimulationObject(new HashSet<>(List.of(rendere1)));
        square1.position = new Vector2(50, 50);

        Renderer renderer2 = new Renderer();
        SimulationObject square2 = new SimulationObject(new HashSet<>(List.of(renderer2)));
        square2.position = new Vector2(60, 60);

        // Add children
        square.addChild(square1);
        square1.addChild(square2);
        // It will look like this:
        *//*
        - Square:
           - Square 1
               - Square 2
        * *//*

        instance.simulationObjectList.add(square);

        instance.simulate();*/
        stage.setTitle("PhysiPlay");
        stage.setScene(scene);
        stage.show();
        scene.getWindow().sizeToScene();
    }
}