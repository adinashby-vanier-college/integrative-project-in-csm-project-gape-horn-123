package com.example.physiplay.components;

import com.example.physiplay.Component;
import com.example.physiplay.Vector2;
import com.example.physiplay.singletons.SimulationManager;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import org.jbox2d.collision.shapes.PolygonShape;

public abstract class Renderer extends Component {
    public Color color = Color.rgb(255, 0, 0);
    public double mouseX = 0, mouseY = 0;
    protected GraphicsContext gc = SimulationManager.getInstance().gc;
    @Override
    public void Start() {
        initializeShapeCollider();
        /*PolygonShape box = new PolygonShape();
        box.setAsBox((float) size.x / SimulationManager.SCALE / 2, (float) size.y / SimulationManager.SCALE / 2);
        parent.fixtureDef.shape = box;*/
    }

    public abstract void initializeShapeCollider();
    public abstract void drawShape();
    public abstract void drawHologram();

    private void applyTransformations() {
        gc.save();
        gc.setFill(color);
        gc.setGlobalAlpha(1);
        if (parent.getComponent(Rigidbody.class) == null) {
            gc.translate(parent.getWorldPosition().x, parent.getWorldPosition().y);
        }
        else {
            gc.translate(parent.getWorldPosition().x * SimulationManager.SCALE, parent.getWorldPosition().y * SimulationManager.SCALE);
        }
        gc.rotate(Math.toDegrees(parent.angle));
    }

    protected void applyTransformationsForHologram() {
        gc.save();
        gc.setFill(color);
        gc.setGlobalAlpha(0.4);
        gc.translate(mouseX, mouseY);
        gc.rotate(Math.toDegrees(parent.angle));
    }
    @Override
    public void Use() {
        applyTransformations();
        drawShape();
    }
    @Override
    public void Remove() {

    }
}
