package com.example.physiplay.components;

import com.example.physiplay.singletons.SimulationManager;
import javafx.scene.control.*;
import org.jbox2d.collision.shapes.CircleShape;

import java.util.Objects;

public class CircleRenderer extends Renderer {
    public double radius = 10;
    private ComponentPropertyBuilder circlePropertyBuilder = new ComponentPropertyBuilder()
            .addLabelProperty("colorLabel", "Color", new Label())
            .addColorPickerProperty("color", "Choose color:", new ColorPicker())
            .addNumberInputFieldProperty("radius", "Radius", new TextField());
    @Override
    public void initializeShapeCollider() {
        CircleShape circle = new CircleShape();
        circle.setRadius((float) radius / SimulationManager.SCALE / 2);
        parent.fixtureDef.shape = circle;
    }

    @Override
    public void drawShape() {
        gc.fillOval(-radius / 2, -radius / 2, radius, radius);
        gc.restore();
    }

    @Override
    public void drawHologram() {
        applyTransformationsForHologram();
        gc.fillOval(-radius / 2, -radius / 2, radius, radius);
        gc.restore();
    }


    @Override
    public void displayComponent() {
        componentTab.getStyleClass().add(Objects.requireNonNull(getClass().getResource("/css/tabStylesheet.css")).toExternalForm());
        componentTab.setText("Circle Renderer");

        componentTab.setContent(circlePropertyBuilder.getAllProperties());
    }
}
