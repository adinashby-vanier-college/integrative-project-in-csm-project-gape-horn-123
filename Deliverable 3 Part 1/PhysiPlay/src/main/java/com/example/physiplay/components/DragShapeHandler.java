package com.example.physiplay.components;

import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;

public class DragShapeHandler implements EventHandler<MouseEvent> {

    public Node node ;
    GraphicsContext gc;
    TabPane tabPane;

    public DragShapeHandler(Node node, GraphicsContext gc, TabPane tabPane) {
        this.node = node;
        this.gc = gc;
        this.tabPane = tabPane;
    }

    @Override
    public void handle(MouseEvent event) {
        if (event.getEventType() == MouseEvent.MOUSE_PRESSED) {

        }
        else if (event.getEventType() == MouseEvent.MOUSE_RELEASED){
            gc.fillRect(event.getSceneX() - 360, event.getSceneY() - 35, 10, 10);
            tabPane.getTabs().add(new Tab("GameObject 1"));
        }
    }

    public void setTabPane(TabPane tabPane){
        this.tabPane = tabPane;
    }
}
