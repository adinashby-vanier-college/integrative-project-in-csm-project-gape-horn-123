package com.example.physiplay.components;

import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;

public class DragShapeHandler implements EventHandler<MouseEvent> {

    private double sceneAnchorX;
    private double sceneAnchorY;

    public Node node ;

    public DragShapeHandler(Node node) {
        this.node = node ;
    }

    @Override
    public void handle(MouseEvent event) {
        if (event.getEventType() == MouseEvent.MOUSE_PRESSED) {
            sceneAnchorX = event.getSceneX();
            sceneAnchorY = event.getSceneY();
        } else if (event.getEventType() == MouseEvent.MOUSE_DRAGGED) {
            double x = event.getSceneX();
            double y = event.getSceneY();
            node.setTranslateX(node.getTranslateX() + x - sceneAnchorX);
            node.setTranslateY(node.getTranslateY() + y - sceneAnchorY);
            sceneAnchorX = x;
            sceneAnchorY = y;
            System.out.println("Hello");
        }
    }
}
