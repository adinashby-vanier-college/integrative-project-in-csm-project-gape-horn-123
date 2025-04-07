package com.example.physiplay.controllers;

import com.example.physiplay.Vector2;
import com.example.physiplay.models.PolygonVisualizerModel;
import javafx.animation.AnimationTimer;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;

import java.util.*;

public class PolygonVisualizerController {
    @FXML
    private Canvas polygonCanvas;
    @FXML
    private Button generatePolygonButton;
    @FXML
    private TextArea positionsTextArea;
    @FXML
    private Button clearAllButton;
    private GraphicsContext gc;
    private PolygonVisualizerModel model;
    private Point2D pressPoint = null;

    public void initialize() {
        gc = polygonCanvas.getGraphicsContext2D();
        model = new PolygonVisualizerModel(polygonCanvas, gc);
        model.simulate();

        generatePolygonButton.setOnAction(event -> {
            model.isDrawingFilledPolygon.setValue(model.isDrawingFilledPolygon.not().getValue());
            generatePolygonButton.setText(model.isDrawingFilledPolygon.getValue() ? "Hide filled polygon" : "Generate polygon");
        });
        clearAllButton.setOnAction(event -> model.points.clear());
        handleMouseEvents();
    }

    public void getPositions(ActionEvent event) {
        positionsTextArea.clear();
        positionsTextArea.setText(model.getPositionsText());
    }

    private void handleMouseEvents() {
        polygonCanvas.setOnMousePressed(event -> {
            pressPoint = polygonCanvas.sceneToLocal(new Point2D(event.getSceneX(), event.getSceneY()));
        });
        polygonCanvas.setOnMouseDragged(event -> {
            Point2D mouseCoords = polygonCanvas.sceneToLocal(new Point2D(event.getSceneX(), event.getSceneY()));
            model.movePoint(new Vector2(mouseCoords.getX(), mouseCoords.getY()));
        });
        polygonCanvas.setOnMouseReleased(event -> {
            Point2D mouseCoords = polygonCanvas.sceneToLocal(new Point2D(event.getSceneX(), event.getSceneY()));
            if (pressPoint == null || pressPoint.distance(mouseCoords) >= 5) return;
            if (event.getButton() == MouseButton.PRIMARY) {
                model.points.add(new Vector2(mouseCoords.getX(), mouseCoords.getY()));
            }
            else if (event.getButton() == MouseButton.SECONDARY) {
                model.removePoint(new Vector2(mouseCoords.getX(), mouseCoords.getY()));
            }

        });
    }
}
