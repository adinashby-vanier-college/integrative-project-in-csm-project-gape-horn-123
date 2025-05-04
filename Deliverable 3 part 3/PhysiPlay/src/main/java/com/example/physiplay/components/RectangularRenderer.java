package com.example.physiplay.components;

import com.example.physiplay.Vector2;
import com.example.physiplay.singletons.SimulationManager;
import com.example.physiplay.widgets.Vector2Field;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TitledPane;
import org.jbox2d.collision.shapes.PolygonShape;

import java.util.Objects;

public class RectangularRenderer extends Renderer {
    public Vector2 size = new Vector2(50, 50);
    private ComponentPropertyBuilder rectanglePropertyBuilder = new ComponentPropertyBuilder()
            .addLabelProperty("colorLabel", "Color", new Label())
            .addColorPickerProperty("color", "Choose color:", new ColorPicker())
            .addVector2Property("size", "Size", new Vector2Field());
    @Override
    public void initializeShapeCollider() {
        resizeRectangle();
        updateValues();
    }

    private void resizeRectangle() {
        PolygonShape box = new PolygonShape();
        box.setAsBox((float) size.x / SimulationManager.SCALE / 2, (float) size.y / SimulationManager.SCALE / 2);
        parent.fixtureDef.shape = box;
    }
    private void updateValues() {
        ColorPicker picker = rectanglePropertyBuilder.getColorPicker("color");
        Vector2Field sizeField = rectanglePropertyBuilder.getVector2Field("size");

        picker.valueProperty().setValue(color);
        sizeField.x.setText((int) size.x + "");
        sizeField.y.setText((int) size.y + "");

        sizeField.x.setOnAction(event -> {
            if (!sizeField.x.getText().isBlank()) {
                size.x = Float.parseFloat(sizeField.x.getText());
                resizeRectangle();
            }
        });

        sizeField.y.setOnAction(event -> {
            if (!sizeField.y.getText().isBlank()) {
                size.y = Float.parseFloat(sizeField.y.getText());
                resizeRectangle();
            }
        });

        picker.valueProperty().addListener((obs, oldVal, newVal) -> {
            color = newVal;
        });
    }
    @Override
    public void drawShape() {
        gc.fillRect(-size.x / 2, - size.y / 2,
                size.x, size.y);
        gc.restore();
    }

    @Override
    public void drawHologram() {
        applyTransformationsForHologram();
        gc.fillRect(-size.x / 2, - size.y / 2,
                size.x, size.y);
        gc.restore();
    }

    @Override
    public void displayComponent() {
        componentTab.getStyleClass().add(Objects.requireNonNull(getClass().getResource("/css/tabStylesheet.css")).toExternalForm());
        componentTab.setText("Rectangular Renderer");
        componentTab.setContent(rectanglePropertyBuilder.getAllProperties());
    }
}
