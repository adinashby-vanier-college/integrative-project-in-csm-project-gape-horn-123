package com.example.physiplay.components;

import com.example.physiplay.Vector2;
import javafx.scene.paint.Color;

public class RectangularCollider extends Collider {
    public Vector2 colliderSize = new Vector2(0, 0);
    public RectangularCollider() {}
    public RectangularCollider(Vector2 size) {
        colliderSize.x = size.x;
        colliderSize.y = size.y;
    }

    @Override
    public void showColliderBounds() {
        gc.setStroke(Color.rgb(0, 0, 0));
        gc.setLineWidth(2);
        gc.strokeRect(position.x, position.y, colliderSize.x, colliderSize.y);
    }

    @Override
    public void onScreenEdgeCollision() {
        // I will change with width/height customization, set it to 600 for now
        float x = position.x, y = position.y;
        if (x + colliderSize.x > 600 || x < 0) {
            parent.getComponent(Rigidbody.class).velocity.x *= -1;
        }
        if (y + colliderSize.y > 600 || y < 0) {
            parent.getComponent(Rigidbody.class).velocity.y *= -1;
        }
    }

    @Override
    public void startCollider() {
        System.out.println("Rectangle collider activated!");
    }

    @Override
    public void onCollisionEnter() {

    }

    @Override
    public void onTriggerEnter() {

    }


}
