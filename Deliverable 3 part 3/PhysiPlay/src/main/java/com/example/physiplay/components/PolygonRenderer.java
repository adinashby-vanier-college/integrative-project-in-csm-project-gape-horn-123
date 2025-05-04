package com.example.physiplay.components;

import com.example.physiplay.Vector2;
import com.example.physiplay.singletons.SimulationManager;
import javafx.scene.control.*;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;

import java.util.*;

public class PolygonRenderer extends Renderer {
    public List<Vector2> positions = new ArrayList<>();

    private ComponentPropertyBuilder polygonPropertyBuilder = new ComponentPropertyBuilder()
            .addLabelProperty("colorLabel", "Color", new Label())
            .addColorPickerProperty("color", "Choose color:", new ColorPicker());
            /*.addTextFieldProperty("positions", "Enter positions:", new TextField())
            .addLabelProperty("notsure", "Not sure? Go to Window -> Polygon Visualizer", new Label());*/
    @Override
    public void initializeShapeCollider() {
        PolygonShape shape = new PolygonShape();
        shape.set(getVerticesOfPolygon(), positions.size());
        parent.fixtureDef.shape = shape;
        updateValues();
    }
    private Vec2[] getVerticesOfPolygon() {
        Vec2[] points = new Vec2[positions.size()];
        for (int i = 0 ; i < positions.size(); i++) {
            points[i] = new Vec2((float) positions.get(i).x / SimulationManager.SCALE,
                    (float) positions.get(i).y / SimulationManager.SCALE);
        }
        return points;
    }

    private void updateValues() {
        ColorPicker picker = polygonPropertyBuilder.getColorPicker("color");
        picker.valueProperty().setValue(color);
        picker.valueProperty().addListener((obs, oldVal, newVal) -> {
            color = newVal;
        });
    }

    private double[] getVerticesIndividualCoordinates(int index) {
        if (index != 0 && index != 1) index = 0;
        double[] points = new double[positions.size()];
        for (int i = 0; i < points.length; i++) {
            points[i] = index == 0 ? getVerticesOfPolygon()[i].x * SimulationManager.SCALE :
                    getVerticesOfPolygon()[i].y * SimulationManager.SCALE;
        }
        return points;
    }
    @Override
    public void drawShape() {
        gc.fillPolygon(getVerticesIndividualCoordinates(0), getVerticesIndividualCoordinates(1),
                positions.size());
        gc.restore();
    }

    @Override
    public void drawHologram() {
        applyTransformationsForHologram();
        gc.fillPolygon(getVerticesIndividualCoordinates(0), getVerticesIndividualCoordinates(1),
                positions.size());
        gc.restore();
    }

    @Override
    public void displayComponent() {
        componentTab.getStyleClass().add(Objects.requireNonNull(getClass().getResource("/css/tabStylesheet.css")).toExternalForm());
        componentTab.setText("Polygon Renderer");
        componentTab.setContent(polygonPropertyBuilder.getAllProperties());
    }


}
