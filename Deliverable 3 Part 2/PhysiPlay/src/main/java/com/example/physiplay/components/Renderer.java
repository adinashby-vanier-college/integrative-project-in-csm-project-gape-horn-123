package com.example.physiplay.components;

import com.example.physiplay.Component;
import com.example.physiplay.Vector2;
import com.example.physiplay.singletons.SimulationManager;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import org.jbox2d.collision.shapes.PolygonShape;

enum Shape {
    Rectangle,
    Circle,
    Polygon
}

public class Renderer extends Component {
    public Color color = Color.rgb(255, 0, 0);
    public Vector2 size = new Vector2(50, 50);
    public double mouseX = 0, mouseY = 0;
    private GraphicsContext gc = SimulationManager.getInstance().gc;
    @Override
    public void Start() {
        PolygonShape box = new PolygonShape();
        box.setAsBox((float) size.x / SimulationManager.SCALE / 2, (float) size.y / SimulationManager.SCALE / 2);
        parent.fixtureDef.shape = box;
    }

    @Override
    public void Use() {
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
        gc.fillRect(-size.x / 2, - size.y / 2,
                size.x, size.y);
        gc.restore();
    }

    public void drawHologram() {
        gc.save();
        gc.setFill(color);
        gc.setGlobalAlpha(0.4);
        gc.translate(mouseX, mouseY);
        gc.rotate(Math.toDegrees(parent.angle));
        gc.fillRect(-size.x / 2, - size.y / 2,
                size.x, size.y);
        gc.restore();
    }
    @Override
    public void Remove() {

    }
}
