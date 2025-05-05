package com.example.physiplay.components;

import com.example.physiplay.Vector2;
import com.example.physiplay.singletons.SimulationManager;
import com.example.physiplay.widgets.Vector2Field;
import com.google.gson.annotations.Expose;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TitledPane;
import org.jbox2d.collision.shapes.PolygonShape;

import java.util.Objects;

/**
 * A renderer for rectangular shapes that can be simulated and rendered in the simulation.
 * Provides editable properties such as color and size via a component UI.
 */
public class RectangularRenderer extends Renderer {

    /** Width and height of the rectangle in screen units (pixels). */
    @Expose
    public Vector2 size = new Vector2(50, 50);

    /** UI builder for customizing rectangle properties. */
    private ComponentPropertyBuilder rectanglePropertyBuilder = new ComponentPropertyBuilder()
            .addLabelProperty("colorLabel", "Color", new Label())
            .addColorPickerProperty("color", "Choose color:", new ColorPicker())
            .addVector2Property("size", "Size", new Vector2Field());

    /**
     * Initializes the physics shape (Box2D polygon shape) for the rectangle.
     * Called after deserialization or initial setup.
     */
    @Override
    public void initializeShapeCollider() {
        resizeRectangle();
        updateValues();
    }

    /**
     * Sets the Box2D shape to match the current size of the rectangle.
     */
    private void resizeRectangle() {
        PolygonShape box = new PolygonShape();
        box.setAsBox((float) size.x / SimulationManager.SCALE / 2, (float) size.y / SimulationManager.SCALE / 2);
        parent.fixtureDef.shape = box;
    }

    /**
     * Updates the property fields (color picker, size fields) and synchronizes their listeners with this component.
     */
    private void updateValues() {
        ColorPicker picker = rectanglePropertyBuilder.getColorPicker("color");
        Vector2Field sizeField = rectanglePropertyBuilder.getVector2Field("size");

        picker.valueProperty().setValue(color);
        sizeField.x.setText((int) size.x + "");
        sizeField.y.setText((int) size.y + "");

        sizeField.x.setOnAction(event -> {
            if (!sizeField.x.getText().isBlank()) {
                size.x = Float.parseFloat(sizeField.x.getText());
                PolygonShape box = new PolygonShape();
                box.setAsBox((float) size.x / SimulationManager.SCALE / 2, (float) size.y / SimulationManager.SCALE / 2);
                parent.simulationObjectBody.getFixtureList().m_shape = box;
            }
        });

        sizeField.y.setOnAction(event -> {
            if (!sizeField.y.getText().isBlank()) {
                size.y = Float.parseFloat(sizeField.y.getText());
                PolygonShape box = new PolygonShape();
                box.setAsBox((float) size.x / SimulationManager.SCALE / 2, (float) size.y / SimulationManager.SCALE / 2);
                parent.simulationObjectBody.getFixtureList().m_shape = box;
            }
        });

        picker.valueProperty().addListener((obs, oldVal, newVal) -> {
            color = newVal;
        });
    }

    /**
     * Draws the rectangle using the JavaFX graphics context.
     * Called during each render cycle for the active scene.
     */
    @Override
    public void drawShape() {
        gc.fillRect(-size.x / 2, - size.y / 2,
                size.x, size.y);
        gc.restore();
    }

    /**
     * Draws a translucent/holographic version of the rectangle, typically used during placement or editing.
     */
    @Override
    public void drawHologram() {
        applyTransformationsForHologram();
        gc.fillRect(-size.x / 2, - size.y / 2,
                size.x, size.y);
        gc.restore();
    }

    /**
     * Displays this component’s editable properties in a tabbed UI pane.
     */
    @Override
    public void displayComponent() {
        componentTab.getStyleClass().add(Objects.requireNonNull(getClass().getResource("/css/tabStylesheet.css")).toExternalForm());
        componentTab.setText("Rectangular Renderer");
        componentTab.setContent(rectanglePropertyBuilder.getAllProperties());
    }
}
