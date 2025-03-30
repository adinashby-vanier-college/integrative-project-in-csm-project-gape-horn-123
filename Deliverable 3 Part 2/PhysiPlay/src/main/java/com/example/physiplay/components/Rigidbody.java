package com.example.physiplay.components;

import com.example.physiplay.Component;
import com.example.physiplay.SimulationObject;
import com.example.physiplay.Vector2;
import com.example.physiplay.singletons.SimulationManager;
import org.jbox2d.collision.shapes.MassData;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.dynamics.*;

public class Rigidbody extends Component {
    public Vector2 velocity = Vector2.ZERO;
    public boolean isStatic = false;
    public boolean useGravity = false;
    public float torque = 0f;
    public float mass;
    public float restitution;
    public float friction;
    private boolean firstFrame = false;

    @Override
    public void Start() {
        parent.simulationObjectBodyDef.type = isStatic ? BodyType.STATIC : BodyType.DYNAMIC;
        parent.simulationObjectBodyDef.gravityScale = useGravity ? 1 : 0;

        parent.fixtureDef.density = mass;
        parent.fixtureDef.restitution = restitution;
        parent.fixtureDef.friction = friction;
        System.out.println("Rigidbody component activated!");
    }

    @Override
    public void Use() {
        if (!firstFrame) {
            parent.simulationObjectBody.applyAngularImpulse(torque * parent.simulationObjectBody.getInertia());
            firstFrame = true;
        }

        parent.position = new Vector2(parent.simulationObjectBody.getPosition().x, parent.simulationObjectBody.getPosition().y);
        parent.simulationObjectBody.setAngularDamping(0f);
        parent.angle = parent.simulationObjectBody.getAngle();
    }

    @Override
    public void Remove() {

    }
}
