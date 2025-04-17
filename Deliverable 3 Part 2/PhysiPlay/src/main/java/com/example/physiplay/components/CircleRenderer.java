package com.example.physiplay.components;

import com.example.physiplay.singletons.SimulationManager;
import org.jbox2d.collision.shapes.CircleShape;

public class CircleRenderer extends Renderer {
    public double radius = 10;
    @Override
    public void initializeShapeCollider() {
        CircleShape circle = new CircleShape();
        circle.setRadius((float) radius / SimulationManager.SCALE / 2);
        parent.fixtureDef.shape = circle;
    }

    @Override
    public void drawShape() {
        gc.fillOval(-radius / 2, -radius / 2, radius, radius);
        gc.restore();
    }

    @Override
    public void drawHologram() {
        applyTransformationsForHologram();
        gc.fillOval(-radius / 2, -radius / 2, radius, radius);
        gc.restore();
    }
}
