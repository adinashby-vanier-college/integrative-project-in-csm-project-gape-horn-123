package com.example.physiplay.singletons;

import com.example.physiplay.SimulationObject;
import com.example.physiplay.Vector2;
import com.example.physiplay.components.Renderer;
import javafx.animation.AnimationTimer;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;

import java.util.*;

public final class SimulationManager {
    private static final int WIDTH = 600, HEIGHT = 600;
    private static final SimulationManager instance = new SimulationManager();

    public Canvas canvas = new Canvas(WIDTH, HEIGHT);

    public Map<String, SimulationObject> dataMap = new HashMap<>();
    public SimpleBooleanProperty simulationPaused = new SimpleBooleanProperty(false);
    public GraphicsContext gc = canvas.getGraphicsContext2D();
    public static final float SCALE = 50f;
    public final World world = new World(new Vec2(0, 9.8f));

    public double camX = 0, camY = 0;
    public double scaleX = 1, scaleY = 1;
    public boolean isCoordinateInCanvas(Vector2 coords) {
        return coords.x > canvas.getLayoutX() && coords.x < canvas.getLayoutX() + canvas.getWidth() &&
                coords.y > canvas.getLayoutY() && coords.y < canvas.getLayoutY() + canvas.getHeight();
    }
    public List<SimulationObject> simulationObjectList = new ArrayList<>();
    public SimulationObject hologramSimulationObject = null;
    private SimulationManager() {}

    public void simulate() {
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long l) {
                gc.clearRect(0, 0, scaleX * (canvas.getWidth() + camX), scaleY * (canvas.getHeight() + camY));
                gc.save();
                gc.translate(camX, camY);
                gc.scale(scaleX, scaleY);
                world.step(1.0f / 60.0f, 8, 3);
                if (hologramSimulationObject != null && hologramSimulationObject.getComponent(Renderer.class) != null) {
                    hologramSimulationObject.getComponent(Renderer.class).drawHologram();
                }
                for (SimulationObject object : simulationObjectList) {
                    object.simulateObject();
                }
                gc.restore();
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
