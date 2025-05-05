package com.example.physiplay.components;

import com.example.physiplay.singletons.SimulationManager;
import com.google.gson.annotations.Expose;
import javafx.scene.control.*;
import org.jbox2d.collision.shapes.CircleShape;

import java.util.Objects;

/**
 * A component responsible for rendering a circle shape and handling its physical properties
 * when attached to a simulation object.
 */
public class CircleRenderer extends Renderer {

    /** The radius of the circle to be rendered. */
    @Expose
    public double radius = 10;

    /** Property builder for creating editable UI fields for the circle renderer. */
    private ComponentPropertyBuilder circlePropertyBuilder = new ComponentPropertyBuilder()
            .addLabelProperty("colorLabel", "Color", new Label())
            .addColorPickerProperty("color", "Choose color:", new ColorPicker())
            .addNumberInputFieldProperty("radius", "Radius", new TextField());

    /**
     * Initializes the shape collider using a JBox2D {@link CircleShape} with the specified radius.
     */
    @Override
    public void initializeShapeCollider() {
        CircleShape circle = new CircleShape();
        circle.setRadius((float) radius / SimulationManager.SCALE / 2);
        parent.fixtureDef.shape = circle;
        updateValues();
    }

    /**
     * Resets the circle collider shape with the current radius value.
     * Typically used when properties are updated.
     */
    private void resetCircleData() {
        CircleShape circle = new CircleShape();
        circle.setRadius((float) radius / SimulationManager.SCALE / 2);
        parent.fixtureDef.shape = circle;
    }

    /**
     * Updates the UI values for the radius and color, and binds property listeners
     * to reflect live changes on the rendered object.
     */
    private void updateValues() {
        ColorPicker picker = circlePropertyBuilder.getColorPicker("color");
        TextField radiusTextField = circlePropertyBuilder.getTextField("radius");

        picker.valueProperty().setValue(color);
        radiusTextField.setText(radius + "");

        radiusTextField.setOnAction(event -> {
            if (!radiusTextField.getText().isBlank()) {
                radius = Float.parseFloat(radiusTextField.getText());
                CircleShape circle = new CircleShape();
                circle.setRadius((float) radius / SimulationManager.SCALE / 2);
                parent.simulationObjectBody.getFixtureList().m_shape = circle;
            }
        });
        picker.valueProperty().addListener((obs, oldVal, newVal) -> {
            color = newVal;
        });
    }

    /**
     * Draws the circle on the canvas using the GraphicsContext of the parent.
     */
    @Override
    public void drawShape() {
        gc.fillOval(-radius / 2, -radius / 2, radius, radius);
        gc.restore();
    }

    /**
     * Draws the circle as a hologram (preview or ghosted shape).
     */
    @Override
    public void drawHologram() {
        applyTransformationsForHologram();
        gc.fillOval(-radius / 2, -radius / 2, radius, radius);
        gc.restore();
    }

    /**
     * Displays the editable component tab in the UI with appropriate styling and properties.
     */
    @Override
    public void displayComponent() {
        componentTab.getStyleClass().add(Objects.requireNonNull(getClass().getResource("/css/tabStylesheet.css")).toExternalForm());
        componentTab.setText("Circle Renderer");
        componentTab.setContent(circlePropertyBuilder.getAllProperties());
    }
}
