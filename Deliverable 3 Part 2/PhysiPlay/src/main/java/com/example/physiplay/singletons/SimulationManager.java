package com.example.physiplay.singletons;

import com.example.physiplay.SimulationObject;
import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;

import java.util.ArrayList;

import java.util.List;

public final class SimulationManager {
    private static final int WIDTH = 600, HEIGHT = 600;
    private static final SimulationManager instance = new SimulationManager();
    public final Canvas canvas = new Canvas(WIDTH, HEIGHT);
    public final GraphicsContext gc = canvas.getGraphicsContext2D();

    public final World world = new World(new Vec2(0, 9.8f));

    public List<SimulationObject> simulationObjectList = new ArrayList<>();
    private SimulationManager() {}

    public void simulate() {
        new AnimationTimer() {

            @Override
            public void handle(long l) {
                gc.clearRect(0, 0, WIDTH, HEIGHT);
                for (SimulationObject object : simulationObjectList) {
                    object.simulateObject();
                }
            }
        }.start();
    }
    public static SimulationManager getInstance() {
        return instance;
    }
}
