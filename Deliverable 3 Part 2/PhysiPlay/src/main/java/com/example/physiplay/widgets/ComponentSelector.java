package com.example.physiplay.widgets;

import com.example.physiplay.Vector2;
import com.example.physiplay.components.ComponentPropertyBuilder;
import com.example.physiplay.components.Renderer;
import com.example.physiplay.components.Rigidbody;
import javafx.scene.control.TitledPane;

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

    public Rigidbody convertToRigidbodyComponent() {
        if (builder == null) return null;
        Rigidbody rb = new Rigidbody();
        rb.velocity = new Vector2(builder.getVector2Field("initialVelocity").getX(),
                builder.getVector2Field("initialVelocity").getY());
        rb.isStatic = builder.getCheckBox("isStatic").isSelected();
        return rb;
    }

    public Renderer convertToRendererComponent() {
        if (builder == null) return null;
        Renderer renderer = new Renderer();
        renderer.size = new Vector2(builder.getVector2Field("size").getX(),
                builder.getVector2Field("size").getY());
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
        return new TitledPane(this.title, builder.getAllProperties());
    }

    @Override
    public String toString() {
        return this.title;
    }
}
