package com.example.physiplay.components;

import com.example.physiplay.Component;
import com.example.physiplay.Vector2;
import com.example.physiplay.singletons.SimulationManager;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import org.jbox2d.collision.shapes.PolygonShape;


public class Renderer extends Component {
    public Color color = Color.rgb(255, 0, 0);
    public Vector2 size = new Vector2(50, 50);

    private GraphicsContext gc = SimulationManager.getInstance().gc;
    @Override
    public void Start() {
        PolygonShape box = new PolygonShape();
        box.setAsBox((float) size.x / 2, (float) size.y / 2);
        parent.fixtureDef.shape = box;
        parent.fixtureDef.density = 1f;
        parent.fixtureDef.restitution = 0.4f;
        parent.fixtureDef.friction = 0.3f;
    }

    @Override
    public void Use() {
        gc.save();
        gc.setFill(color);
        gc.translate(parent.getWorldPosition().x, parent.getWorldPosition().y);
        gc.rotate(Math.toDegrees(parent.angle));
        gc.fillRect(-size.x / 2, - size.y / 2, size.x, size.y);
        gc.restore();
    }

    @Override
    public void Remove() {

    }
}
