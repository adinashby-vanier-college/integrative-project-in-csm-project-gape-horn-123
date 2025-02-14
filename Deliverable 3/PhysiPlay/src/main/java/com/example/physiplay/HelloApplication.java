package com.example.physiplay;

import com.example.physiplay.components.Collider;
import com.example.physiplay.components.RectangularCollider;
import com.example.physiplay.components.Renderer;
import com.example.physiplay.components.Rigidbody;
import com.example.physiplay.singletons.SimulationManager;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.util.HashSet;
import java.util.List;

public final class HelloApplication extends Application {

    private static final SimulationManager instance = SimulationManager.getInstance();
    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) throws Exception {
        Pane pane = new Pane(instance.canvas);
        Scene scene = new Scene(pane, 600, 600);

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

        // Add childs
        square.addChild(square1);
        square1.addChild(square2);
        // It will look like this:
        /*
        - Square:
           - Square 1
               - Square 2
        * */

        instance.simulationObjectList.add(square);

        instance.simulate();
        stage.setTitle("PhysiPlay");
        stage.setScene(scene);
        stage.show();
    }
}