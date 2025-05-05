package com.example.physiplay.components;

import com.example.physiplay.Component;
import com.example.physiplay.Vector2;
import com.example.physiplay.singletons.SimulationManager;
import javafx.scene.canvas.GraphicsContext;

public abstract class Collider extends Component {
    public boolean isTrigger = false;
    public boolean screenEdgeCollision = false;
    public boolean showColliderBounds = true;

    protected final GraphicsContext gc = SimulationManager.getInstance().gc;

    protected Vector2 position = new Vector2(0, 0);
    @Override
    public void Start() {
        allowMultipleInstances = false;
        startCollider();
    }

    @Override
    public void Use() {
        bindCollisionPositionWithObject();
        if (showColliderBounds) showColliderBounds();
        if (isTrigger) onTriggerEnter();
        else onCollisionEnter();

        if (screenEdgeCollision) onScreenEdgeCollision();
    }

    @Override
    public void Remove() {

    }

    public abstract void showColliderBounds();

    private void bindCollisionPositionWithObject() {
        position.x = this.parent.getWorldPosition().x;
        position.y = this.parent.getWorldPosition().y;
    }
    public abstract void onScreenEdgeCollision();
    public abstract void startCollider();
    public abstract void onCollisionEnter();

    public abstract void onTriggerEnter();

}
