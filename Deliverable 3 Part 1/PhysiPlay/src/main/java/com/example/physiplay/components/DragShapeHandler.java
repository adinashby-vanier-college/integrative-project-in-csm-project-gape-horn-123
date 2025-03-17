package com.example.physiplay.components;

import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;

public class DragShapeHandler implements EventHandler<MouseEvent> {

    public Node node ;
    GraphicsContext gc;

    public DragShapeHandler(Node node, GraphicsContext gc) {
        this.node = node ;
        this.gc = gc;
    }

    @Override
    public void handle(MouseEvent event) {
        if (event.getEventType() == MouseEvent.MOUSE_PRESSED) {

        }
        else if (event.getEventType() == MouseEvent.MOUSE_RELEASED){
            gc.fillRect(event.getSceneX() - 360, event.getSceneY() - 35, 10, 10);

        }
    }
}
