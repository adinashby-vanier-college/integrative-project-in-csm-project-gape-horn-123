package com.example.physiplay.widgets;

import com.example.physiplay.Vector2;
import com.example.physiplay.components.CircleRenderer;
import com.example.physiplay.components.ComponentPropertyBuilder;
import com.example.physiplay.components.RectangularRenderer;
import com.example.physiplay.components.Rigidbody;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.util.Objects;

public class ComponentSelector {
    private ComponentPropertyBuilder builder;
    private boolean interactable = true;
    private String componentName;
    private String title;
    private boolean allowMultipleInstances = false;

    public String getComponentName() {
        return componentName;
    }

    public ComponentSelector(String componentName, String title, ComponentPropertyBuilder builder, boolean allowMultipleInstances) {
        this.builder = builder;
        if (this.builder == null) this.interactable = false;
        this.title = title;
        this.componentName = componentName;
        this.allowMultipleInstances = allowMultipleInstances;
    }

    @Override
    public boolean equals(Object o) {
        if (this.allowMultipleInstances) return true;
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ComponentSelector that = (ComponentSelector) o;
        return allowMultipleInstances == that.allowMultipleInstances && Objects.equals(componentName, that.componentName);
    }

    public String getTitle() {
        return this.title;
    }

    private float parseFloat(String s) {
        return Float.parseFloat(s.isBlank() ? "0" : s);
    }
    public Rigidbody convertToRigidbodyComponent() {
        if (builder == null) return null;
        Rigidbody rb = new Rigidbody();
        rb.velocity = new Vector2(builder.getVector2Field("initialVelocity").getX(),
                builder.getVector2Field("initialVelocity").getY());
        rb.useGravity = builder.getCheckBox("useGravity").isSelected();
        rb.torque = parseFloat(builder.getTextField("torque").getText());
        rb.isStatic = builder.getCheckBox("isStatic").isSelected();
        rb.mass = Float.parseFloat(builder.getTextField("mass").getText());
        rb.restitution = Float.parseFloat(builder.getTextField("restitution").getText());
        rb.friction = Float.parseFloat(builder.getTextField("friction").getText());
        return rb;
    }

    public RectangularRenderer convertToRectangularRendererComponent() {
        if (builder == null) return null;
        RectangularRenderer renderer = new RectangularRenderer();
        renderer.size = new Vector2(builder.getVector2Field("size").getX(),
                builder.getVector2Field("size").getY());
        return renderer;
    }

    public CircleRenderer convertToCircleRendererComponent() {
        if (builder == null) return null;
        CircleRenderer renderer = new CircleRenderer();
        renderer.radius = parseFloat(builder.getTextField("radius").getText());
        return renderer;
    }

    @Override
    public int hashCode() {
        return Objects.hash(componentName, allowMultipleInstances);
    }

    public boolean isInteractable() {
        return interactable;
    }
    public TitledPane generateTitledPane() {
        VBox properties = builder.getAllProperties();
        return new TitledPane(this.title, properties);
    }

    public VBox getComponentProperties() {
        return builder.getAllProperties();
    }

    @Override
    public String toString() {
        return this.title;
    }
}
