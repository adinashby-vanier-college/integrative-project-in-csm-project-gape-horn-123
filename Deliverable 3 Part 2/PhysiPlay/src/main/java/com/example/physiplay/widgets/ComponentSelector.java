package com.example.physiplay.widgets;

import com.example.physiplay.components.ComponentPropertyBuilder;
import javafx.scene.control.TitledPane;

import java.util.Objects;

public class ComponentSelector {
    private ComponentPropertyBuilder builder;
    private boolean interactable = true;
    private String componentName;
    private boolean allowMultipleInstances = false;
    public ComponentSelector(String componentName, ComponentPropertyBuilder builder) {
        this.builder = builder;
        if (this.builder == null) this.interactable = false;
        this.componentName = componentName;
    }

    public ComponentSelector(String componentName, ComponentPropertyBuilder builder, boolean allowMultipleInstances) {
        this(componentName, builder);
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

    @Override
    public int hashCode() {
        return Objects.hash(componentName, allowMultipleInstances);
    }

    public boolean isInteractable() {
        return interactable;
    }
    public TitledPane generateTitledPane() {
        return new TitledPane(componentName, builder.getAllProperties());
    }

    @Override
    public String toString() {
        return this.componentName;
    }
}
