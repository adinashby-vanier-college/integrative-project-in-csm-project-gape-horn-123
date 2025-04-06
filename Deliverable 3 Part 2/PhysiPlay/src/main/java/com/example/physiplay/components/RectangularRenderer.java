package com.example.physiplay.components;

import com.example.physiplay.Vector2;
import com.example.physiplay.singletons.SimulationManager;
import org.jbox2d.collision.shapes.PolygonShape;

public class RectangularRenderer extends Renderer {
    public Vector2 size = new Vector2(50, 50);

    @Override
    public void initializeShapeCollider() {
        PolygonShape box = new PolygonShape();
        box.setAsBox((float) (size.x / SimulationManager.getInstance().getScale() / 2), 
                     (float) (size.y / SimulationManager.getInstance().getScale() / 2));
        parent.fixtureDef.shape = box;
    }

    @Override
    public void drawShape() {
        double scaleFactor = SimulationManager.getInstance().getScale() / 50.0;
        double scaledWidth = size.x * scaleFactor;
        double scaledHeight = size.y * scaleFactor;
        
        gc.fillRect(-scaledWidth / 2, -scaledHeight / 2, scaledWidth, scaledHeight);
        gc.restore();
    }

    @Override
    public void drawHologram() {
        applyTransformationsForHologram();
        double scaleFactor = SimulationManager.getInstance().getScale() / 50.0;
        double scaledWidth = size.x * scaleFactor;
        double scaledHeight = size.y * scaleFactor;
        
        gc.fillRect(-scaledWidth / 2, -scaledHeight / 2, scaledWidth, scaledHeight);
        gc.restore();
    }
}