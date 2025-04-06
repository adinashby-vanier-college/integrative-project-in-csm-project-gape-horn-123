package com.example.physiplay.components;

import com.example.physiplay.singletons.SimulationManager;
import org.jbox2d.collision.shapes.CircleShape;

public class CircleRenderer extends Renderer {
    public double radius = 10;

    @Override
    public void initializeShapeCollider() {
        CircleShape circle = new CircleShape();
        circle.setRadius((float) (radius / SimulationManager.getInstance().getScale() / 2));
        parent.fixtureDef.shape = circle;
    }

    @Override
    public void drawShape() {
        double scaledRadius = radius * SimulationManager.getInstance().getScale() / 50.0;
        gc.fillOval(-scaledRadius / 2, -scaledRadius / 2, scaledRadius, scaledRadius);
        gc.restore();
    }

    @Override
    public void drawHologram() {
        applyTransformationsForHologram();
        double scaledRadius = radius * SimulationManager.getInstance().getScale() / 50.0;
        gc.fillOval(-scaledRadius / 2, -scaledRadius / 2, scaledRadius, scaledRadius);
        gc.restore();
    }
}