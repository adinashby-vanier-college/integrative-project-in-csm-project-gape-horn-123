package com.example.physiplay;

import com.example.physiplay.Component;
import com.example.physiplay.singletons.SimulationManager;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.FixtureDef;

import java.util.*;

public class SimulationObject {
    public String name;
    public String uuid;
    public boolean isActive = true;
    public Body simulationObjectBody;
    public BodyDef simulationObjectBodyDef = new BodyDef();
    public FixtureDef fixtureDef = new FixtureDef();
    private SimulationObject parent = null;
    public Vector2 position = new Vector2(0, 0);
    public float angle = 0;
    private Set<Component> components = new HashSet<>();
    private List<SimulationObject> children = new ArrayList<>();

    public SimulationObject(String name, Set<Component> createdComponents) {
        this.name = name;
        components.addAll(createdComponents);
        activateComponentRecursive(children);
    }

    public SimulationObject(String name, Set<Component> createdComponents, Vector2 initialPosition, float angle) {

        setPosition(initialPosition);
        setRotation(angle);
        this.name = name;
        components.addAll(createdComponents);
        activateComponentRecursive(children);
    }

    public void setRotation(float angle) {
        this.angle = angle;
        this.simulationObjectBodyDef.angle = this.angle;
    }
    public void setPosition(Vector2 pos) {
        position = new Vector2(pos.x, pos.y);
        this.simulationObjectBodyDef.position.set(new Vec2((float) position.x / SimulationManager.SCALE,
                (float) position.y / SimulationManager.SCALE));
    }

    public Set<Component> getComponents() {
        return components;
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
        simulationObjectBody = SimulationManager.getInstance().world.createBody(simulationObjectBodyDef);
        simulationObjectBody.setFixedRotation(false);
        simulationObjectBody.createFixture(fixtureDef);

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
