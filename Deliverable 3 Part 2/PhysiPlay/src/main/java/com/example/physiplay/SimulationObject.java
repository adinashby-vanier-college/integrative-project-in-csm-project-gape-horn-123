package com.example.physiplay;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SimulationObject {
    public String name;
    public boolean isActive = true;

    private SimulationObject parent = null;
    public Vector2 position = new Vector2(0, 0);
    private Set<Component> components = new HashSet<>();
    private List<SimulationObject> children = new ArrayList<>();

    public SimulationObject(String name, Set<Component> createdComponents) {
        this.name = name;
        components.addAll(createdComponents);
        activateComponentRecursive(children);
    }

    public void simulateObject() {
        simulateObjectRecursive(children, this);
    }

    private void simulateObjectRecursive(List<SimulationObject> _children, SimulationObject object) {
        if (!this.isActive) return;
        for (Component c: object.components) {
            c.Use();
        }
        if (_children.isEmpty()) return;
        for (SimulationObject obj: _children) simulateObjectRecursive(obj.getChildren(), obj);
    }

    public Vector2 getWorldPosition() {
        if (parent != null) {
            Vector2 parentWorld = parent.getWorldPosition();
            return new Vector2(parentWorld.x + this.position.x,
                    parentWorld.y + this.position.y);
        }
        return new Vector2(this.position.x, this.position.y);
    }

    public SimulationObject getParent() {
        return parent;
    }

    public void activateComponentRecursive(List<SimulationObject> _children) {
        for (Component c: components) {
            c.parent = this;
            c.Start();
        }
        if (_children.isEmpty()) return;
        for (SimulationObject obj: _children) {
            activateComponentRecursive(obj.getChildren());
        }
    }
    public void addChild(SimulationObject childObject) {
        childObject.parent = this;
        children.add(childObject);
    }

    public List<SimulationObject> getChildren() {
        return children;
    }

    // Simple way to get a specific component in this object
    public <T extends Component> T getComponent(Class<T> type) {
        for (Component c: components) {
            if (type.isInstance(c)) {
                return type.cast(c);
            }
        }
        return null;
    }
}
