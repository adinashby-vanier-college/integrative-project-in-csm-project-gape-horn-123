package com.example.physiplay.components;

import com.example.physiplay.Vector2;
import com.example.physiplay.singletons.SimulationManager;
import com.example.physiplay.widgets.ComponentSelector;
import com.google.gson.annotations.Expose;
import javafx.scene.control.*;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;

import java.util.*;

/**
 * A renderer component for drawing and simulating custom polygon shapes in the simulation.
 * Uses user-provided vertices and supports live editing of the shape and color via GUI.
 */
public class PolygonRenderer extends Renderer {

    /** List of vertex positions making up the polygon. */
    @Expose
    public List<Vector2> positions = new ArrayList<>();

    /** UI builder for editing polygon properties like color and vertices. */
    private ComponentPropertyBuilder polygonPropertyBuilder = new ComponentPropertyBuilder()
            .addLabelProperty("colorLabel", "Color", new Label())
            .addColorPickerProperty("color", "Choose color:", new ColorPicker())
            .addTextFieldProperty("positions", "Enter positions:", new TextField())
            .addLabelProperty("notsure", "Not sure? Go to Window -> Polygon Visualizer", new Label());

    /**
     * Formats the list of vertex positions into a user-readable string.
     *
     * @return a formatted string of vertices like "(x y) (x y) ..."
     */
    private String getPositionString() {
        StringBuilder sb = new StringBuilder();
        for (Vector2 position : positions) {
            sb.append("(").append(position.x).append(" ").append(position.y).append(") ");
        }
        return sb.toString();
    }

    /**
     * Initializes the polygon shape collider using the current list of vertices.
     */
    @Override
    public void initializeShapeCollider() {
        PolygonShape shape = new PolygonShape();
        shape.set(getVerticesOfPolygon(), positions.size());
        parent.fixtureDef.shape = shape;
        updateValues();
    }

    /**
     * Converts the list of {@link Vector2} positions into an array of JBox2D {@link Vec2} objects,
     * scaled appropriately for the physics simulation.
     *
     * @return an array of vertices in physics engine units
     */
    private Vec2[] getVerticesOfPolygon() {
        Vec2[] points = new Vec2[positions.size()];
        for (int i = 0; i < positions.size(); i++) {
            points[i] = new Vec2((float) positions.get(i).x / SimulationManager.SCALE,
                    (float) positions.get(i).y / SimulationManager.SCALE);
        }
        return points;
    }

    /**
     * Updates the UI fields with the current color and vertex list, and adds listeners for live editing.
     */
    private void updateValues() {
        ColorPicker picker = polygonPropertyBuilder.getColorPicker("color");
        TextField positionsTextField = polygonPropertyBuilder.getTextField("positions");

        positionsTextField.setText(getPositionString());
        picker.valueProperty().setValue(color);

        positionsTextField.setOnAction(event -> {
            List<Vector2> list = ComponentSelector.parseVector2List(positionsTextField.getText());
            if (!list.isEmpty()) {
                positions = new ArrayList<>(list);
                PolygonShape shape = new PolygonShape();
                shape.set(getVerticesOfPolygon(), positions.size());
                parent.simulationObjectBody.getFixtureList().m_shape = shape;
            }
        });

        picker.valueProperty().addListener((obs, oldVal, newVal) -> color = newVal);
    }

    /**
     * Gets either the x or y coordinates of all vertices for rendering in JavaFX.
     *
     * @param index 0 for x-values, 1 for y-values
     * @return array of x or y coordinates scaled to screen
     */
    private double[] getVerticesIndividualCoordinates(int index) {
        if (index != 0 && index != 1) index = 0;
        double[] points = new double[positions.size()];
        for (int i = 0; i < points.length; i++) {
            points[i] = index == 0
                    ? getVerticesOfPolygon()[i].x * SimulationManager.SCALE
                    : getVerticesOfPolygon()[i].y * SimulationManager.SCALE;
        }
        return points;
    }

    /**
     * Draws the polygon on the canvas using the current graphics context.
     */
    @Override
    public void drawShape() {
        gc.fillPolygon(getVerticesIndividualCoordinates(0),
                getVerticesIndividualCoordinates(1),
                positions.size());
        gc.restore();
    }

    /**
     * Draws the polygon as a hologram, usually for preview purposes before final placement.
     */
    @Override
    public void drawHologram() {
        applyTransformationsForHologram();
        gc.fillPolygon(getVerticesIndividualCoordinates(0),
                getVerticesIndividualCoordinates(1),
                positions.size());
        gc.restore();
    }

    /**
     * Configures and shows the component’s tab content using a custom stylesheet and editor.
     */
    @Override
    public void displayComponent() {
        componentTab.getStyleClass().add(Objects.requireNonNull(getClass().getResource("/css/tabStylesheet.css")).toExternalForm());
        componentTab.setText("Polygon Renderer");
        componentTab.setContent(polygonPropertyBuilder.getAllProperties());
    }
}
