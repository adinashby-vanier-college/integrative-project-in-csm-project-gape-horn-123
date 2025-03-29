package com.example.physiplay.components;

import com.example.physiplay.Component;
import com.example.physiplay.SimulationObject;
import com.example.physiplay.Vector2;
import com.example.physiplay.singletons.SimulationManager;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.dynamics.*;

public class Rigidbody extends Component {
    public Vector2 velocity = Vector2.ZERO;
    public boolean isStatic = false;
    public boolean useGravity = false;
    public Vector2 gravity = new Vector2(0, -9.81f);

    @Override
    public void Start() {
        parent.simulationObjectBodyDef.type = isStatic ? BodyType.STATIC : BodyType.DYNAMIC;
        parent.simulationObjectBodyDef.gravityScale = 1;
        System.out.println("Rigidbody component activated!");
    }

    @Override
    public void Use() {

       //  parent.position.add(velocity);
        // System.out.println(parent.simulationObjectBody.getPosition().y);
        /*parent.position.x = parent.simulationObjectBody.getPosition().x;
        parent.position.y = parent.simulationObjectBody.getPosition().y;*/
        parent.position = new Vector2(parent.simulationObjectBody.getPosition().x, parent.simulationObjectBody.getPosition().y);
        parent.angle = parent.simulationObjectBody.getAngle();
    }

    @Override
    public void Remove() {

    }
}
