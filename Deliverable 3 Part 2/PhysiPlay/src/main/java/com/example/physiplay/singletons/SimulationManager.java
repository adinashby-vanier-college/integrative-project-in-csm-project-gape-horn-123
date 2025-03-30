package com.example.physiplay.singletons;

import com.example.physiplay.SimulationObject;
import javafx.animation.AnimationTimer;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;

import java.util.ArrayList;

import java.util.List;

public final class SimulationManager {
    private static final int WIDTH = 600, HEIGHT = 600;
    private static final SimulationManager instance = new SimulationManager();
    public Canvas canvas = new Canvas(WIDTH, HEIGHT);

    public SimpleBooleanProperty simulationPaused = new SimpleBooleanProperty(false);
    public GraphicsContext gc = canvas.getGraphicsContext2D();

    public final World world = new World(new Vec2(0, 20.8f));

    public List<SimulationObject> simulationObjectList = new ArrayList<>();
    private SimulationManager() {}

    public void simulate() {
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long l) {
                gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
                world.step(1.0f / 60.0f, 8, 3);
                for (SimulationObject object : simulationObjectList) {
                    object.simulateObject();
                }
            }
        };
        timer.start();
        simulationPaused.addListener(((observableValue, oldValue, newValue) ->  {
            if (newValue) timer.stop();
            else timer.start();
        }));
    }
    public static SimulationManager getInstance() {
        return instance;
    }
}
