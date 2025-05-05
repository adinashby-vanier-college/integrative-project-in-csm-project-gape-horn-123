package com.example.physiplay.components;

import com.example.physiplay.singletons.SimulationManager;
import com.google.gson.annotations.Expose;
import javafx.scene.control.*;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;

import java.util.Objects;

/**
 * A component responsible for rendering a regular polygon shape and defining its physics collider.
 * Users can customize the number of sides, size, and color through a GUI.
 */
public class RegularPolygonRenderer extends Renderer {

    /** Number of sides for the regular polygon (minimum 3). */
    @Expose
    public int sides = 6;

    /** Radius (size) of the regular polygon in pixels. */
    @Expose
    public float size = 80;

    /** UI builder for exposing component properties to the user. */
    private ComponentPropertyBuilder regularPolygonPropertyBuilder = new ComponentPropertyBuilder()
            .addLabelProperty("colorLabel", "Color", new Label())
            .addColorPickerProperty("color", "Choose color:", new ColorPicker())
            .addNumberInputFieldProperty("sides", "Sides", new TextField())
            .addNumberInputFieldProperty("size", "Size", new TextField());

    /**
     * Initializes the Box2D shape collider for this regular polygon.
     * This is typically called after deserialization or on creation.
     */
    @Override
    public void initializeShapeCollider() {
        PolygonShape polygon = new PolygonShape();
        polygon.set(getVerticesOfRegularPolygon(), sides);
        parent.fixtureDef.shape = polygon;
        updateValues();
    }

    /**
     * Resets the shape collider to reflect any updates to size or sides.
     */
    private void resetRegularPolygonData() {
        PolygonShape newRegularPolygon = new PolygonShape();
        newRegularPolygon.set(getVerticesOfRegularPolygon(), sides);
        parent.simulationObjectBody.getFixtureList().m_shape = newRegularPolygon;
    }

    /**
     * Syncs property fields (color, size, sides) with the current component state
     * and adds listeners to update internal values on user input.
     */
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

    /**
     * Computes the vertices of a regular polygon based on the current side count and radius.
     *
     * @return an array of Box2D {@link Vec2} representing the polygon vertices
     */
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

    /**
     * Converts the polygon vertices into x or y coordinate arrays for rendering.
     *
     * @param index 0 for x-coordinates, 1 for y-coordinates
     * @return array of coordinate values
     */
    private double[] getVerticesIndividualCoordinates(int index) {
        if (index != 0 && index != 1) index = 0;
        double[] points = new double[sides];
        for (int i = 0; i < points.length; i++) {
            points[i] = index == 0 ? getVerticesOfRegularPolygon()[i].x * SimulationManager.SCALE :
                    getVerticesOfRegularPolygon()[i].y * SimulationManager.SCALE;
        }
        return points;
    }

    /**
     * Draws the polygon shape to the JavaFX canvas.
     */
    @Override
    public void drawShape() {
        gc.fillPolygon(getVerticesIndividualCoordinates(0), getVerticesIndividualCoordinates(1), sides);
        gc.restore();
    }

    /**
     * Draws a translucent/holographic version of the polygon shape.
     */
    @Override
    public void drawHologram() {
        applyTransformationsForHologram();
        gc.fillPolygon(getVerticesIndividualCoordinates(0), getVerticesIndividualCoordinates(1), sides);
        gc.restore();
    }

    /**
     * Displays the editable GUI tab for this renderer in the inspector.
     */
    @Override
    public void displayComponent() {
        componentTab.getStyleClass().add(Objects.requireNonNull(getClass().getResource("/css/tabStylesheet.css")).toExternalForm());
        componentTab.setText("Regular Polygon Renderer");
        componentTab.setContent(regularPolygonPropertyBuilder.getAllProperties());
    }
}
