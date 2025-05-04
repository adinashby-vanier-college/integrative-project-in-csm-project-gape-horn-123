package com.example.physiplay.components;

import com.example.physiplay.singletons.SimulationManager;
import javafx.scene.control.*;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;

import java.util.Objects;

public class RegularPolygonRenderer extends Renderer {
    public int sides = 6;
    public float size = 80;

    private ComponentPropertyBuilder regularPolygonPropertyBuilder = new ComponentPropertyBuilder()
            .addLabelProperty("colorLabel", "Color", new Label())
            .addColorPickerProperty("color", "Choose color:", new ColorPicker())
            .addNumberInputFieldProperty("sides", "Sides", new TextField())
            .addNumberInputFieldProperty("size", "Size", new TextField());
    @Override
    public void initializeShapeCollider() {
        PolygonShape polygon = new PolygonShape();
        polygon.set(getVerticesOfRegularPolygon(), sides);
        parent.fixtureDef.shape = polygon;
        updateValues();
    }

    private void resetRegularPolygonData() {
        PolygonShape newRegularPolygon = new PolygonShape();
        newRegularPolygon.set(getVerticesOfRegularPolygon(), sides);
        parent.simulationObjectBody.getFixtureList().m_shape = newRegularPolygon;
    }

    private void updateValues() {
        ColorPicker picker = regularPolygonPropertyBuilder.getColorPicker("color");
        TextField sizeTextField = regularPolygonPropertyBuilder.getTextField("size"),
            sidesTextField = regularPolygonPropertyBuilder.getTextField("sides");

        picker.valueProperty().setValue(color);
        sizeTextField.setText(size + "");
        sidesTextField.setText(sides + "");

        sizeTextField.setOnAction(event -> {
            if (!sizeTextField.getText().isBlank()) {
                size = Integer.parseInt(sizeTextField.getText());
                resetRegularPolygonData();
            }
        });
        sidesTextField.setOnAction(event -> {
            if (!sidesTextField.getText().isBlank()) {
                int a = Integer.parseInt(sidesTextField.getText());
                if (a >= 3 && a <= 100) {
                    sides = Integer.parseInt(sidesTextField.getText());
                    resetRegularPolygonData();
                }
            }
        });

        picker.valueProperty().addListener((obs, oldVal, newVal) -> {
            color = newVal;
        });
    }
    private Vec2[] getVerticesOfRegularPolygon() {
        Vec2[] points = new Vec2[sides];
        for (int i = 0; i < sides; i++) {
            double angle = 2 * Math.PI * i / sides - Math.PI / 2;
            double x = size * Math.cos(angle) / SimulationManager.SCALE;
            double y = size * Math.sin(angle) / SimulationManager.SCALE;
            points[i] = new Vec2((float) x, (float) y);
        }
        return points;
    }

    private double[] getVerticesIndividualCoordinates(int index) {
        if (index != 0 && index != 1) index = 0;
        double[] points = new double[sides];
        for (int i = 0; i < points.length; i++) {
            points[i] = index == 0 ? getVerticesOfRegularPolygon()[i].x * SimulationManager.SCALE :
                    getVerticesOfRegularPolygon()[i].y * SimulationManager.SCALE;
        }
        return points;
    }
    @Override
    public void drawShape() {
        gc.fillPolygon(getVerticesIndividualCoordinates(0), getVerticesIndividualCoordinates(1), sides);
        gc.restore();
    }

    @Override
    public void drawHologram() {
        applyTransformationsForHologram();
        gc.fillPolygon(getVerticesIndividualCoordinates(0), getVerticesIndividualCoordinates(1), sides);
        gc.restore();
    }

    @Override
    public void displayComponent() {
        componentTab.getStyleClass().add(Objects.requireNonNull(getClass().getResource("/css/tabStylesheet.css")).toExternalForm());
        componentTab.setText("Regular Polygon Renderer");
        componentTab.setContent(regularPolygonPropertyBuilder.getAllProperties());
    }
}
