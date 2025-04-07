package com.example.physiplay.models;

import com.example.physiplay.Vector2;
import javafx.animation.AnimationTimer;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

import java.util.*;

public class PolygonVisualizerModel {
    private GraphicsContext gc;
    public Color circleColor = Color.rgb(0, 0, 0);
    public Color polygonColor = Color.rgb(255, 0, 0);
    public double radius = 10;
    public SimpleBooleanProperty isDrawingFilledPolygon = new SimpleBooleanProperty();
    private Canvas canvas;
    public List<Vector2> points = new ArrayList<>();
    public PolygonVisualizerModel(Canvas canvas, GraphicsContext gc) {
        this.gc = gc;
        this.canvas = canvas;
    }

    public boolean distanceSatisfied(Vector2 point1, Vector2 point2) {
        return (point2.x - point1.x) * (point2.x - point1.x) + (point2.y - point1.y) * (point2.y - point1.y) <
                radius * radius;
    }

    public String getPositionsText() {
        StringBuilder sb = new StringBuilder();
        if (points.isEmpty()) return sb.toString();
        Vector2 minPoint = new Vector2(points.get(0).x, points.get(0).y);
        for (Vector2 point: points) {
            minPoint = new Vector2(Math.min(point.x, minPoint.x), Math.min(point.y, minPoint.y));
        }
        for (Vector2 point: points) {
            sb.append("(").append(point.x - minPoint.x).append(" ").append(point.y - minPoint.y).append(") ");
        }
        return sb.toString();
    }

    public void movePoint(Vector2 point2) {
        for (Vector2 point: points) {
            if (distanceSatisfied(point, point2)) {
                point.x = point2.x;
                point.y = point2.y;
                break;
            }
        }
    }
    public void removePoint(Vector2 point2) {
        for (Vector2 point: points) {
            if (distanceSatisfied(point, point2)) {
                points.remove(point);
                break;
            }
        }
    }
    private void drawFilledPolygon() {
        if (!isDrawingFilledPolygon.getValue()) return;
        double[] pointX = new double[points.size()], pointY = new double[points.size()];
        for (int i = 0; i < points.size(); i++) {
            pointX[i] = points.get(i).x;
            pointY[i] = points.get(i).y;
        }
        gc.setFill(polygonColor);
        gc.fillPolygon(pointX, pointY, points.size());
    }

    private void drawVertices(int index, Vector2 pos) {
        if (isDrawingFilledPolygon.getValue()) return;
        gc.setFill(Color.WHITE);
        gc.fillText((index + 1) + "", pos.x, pos.y);
    }
    public void simulate() {
        new AnimationTimer() {

            @Override
            public void handle(long l) {
                gc.setFont(new Font("Arial", 15));
                gc.setTextAlign(TextAlignment.CENTER);
                gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
                for (int i = 0; i < points.size(); i++) {
                    gc.setFill(circleColor);
                    gc.fillOval(points.get(i).x - radius, points.get(i).y - radius, radius * 2, radius * 2);
                    drawVertices(i, points.get(i));
                }
                drawFilledPolygon();
            }
        }.start();
    }
}
