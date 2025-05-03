package com.example.physiplay.components;

import com.example.physiplay.Vector2;
import com.example.physiplay.singletons.SimulationManager;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TitledPane;
import org.jbox2d.collision.shapes.PolygonShape;

public class RectangularRenderer extends Renderer {
    public Vector2 size = new Vector2(50, 50);

    @Override
    public void initializeShapeCollider() {
        PolygonShape box = new PolygonShape();
        box.setAsBox((float) size.x / SimulationManager.SCALE / 2, (float) size.y / SimulationManager.SCALE / 2);
        parent.fixtureDef.shape = box;
    }

    @Override
    public void drawShape() {
        gc.fillRect(-size.x / 2, - size.y / 2,
                size.x, size.y);
        gc.restore();
    }

    @Override
    public void drawHologram() {
        applyTransformationsForHologram();
        gc.fillRect(-size.x / 2, - size.y / 2,
                size.x, size.y);
        gc.restore();
    }

    @Override
    public void displayComponent() {
        componentTab.setText("Circle Renderer");
        Label label = new Label("In Progress");
        label.setStyle("-fx-font-size: 20px");
        componentTab.setContent(label);
    }
}
