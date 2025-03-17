package com.example.physiplay.components;

import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;

public class DragShapeHandler implements EventHandler<MouseEvent> {

    private double sceneAnchorX;
    private double sceneAnchorY;

    public Node node ;
    GraphicsContext gc;

    public DragShapeHandler(Node node, GraphicsContext gc) {
        this.node = node ;
        this.gc = gc;
    }

    @Override
    public void handle(MouseEvent event) {
        if (event.getEventType() == MouseEvent.MOUSE_PRESSED) {
            sceneAnchorX = event.getSceneX();
            sceneAnchorY = event.getSceneY();
        }
        else if (event.getEventType() == MouseEvent.MOUSE_RELEASED){
            gc.fillRect(event.getSceneX() - 360, event.getSceneY() - 35, 10, 10);
            
        }
    }
}
