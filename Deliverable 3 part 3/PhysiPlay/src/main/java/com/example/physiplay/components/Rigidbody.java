package com.example.physiplay.components;

import com.example.physiplay.Component;
import com.example.physiplay.SimulationObject;
import com.example.physiplay.Vector2;
import com.example.physiplay.singletons.SimulationManager;
import com.example.physiplay.widgets.Vector2Field;
import javafx.scene.control.*;
import org.jbox2d.collision.shapes.MassData;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.*;

import java.util.Objects;

public class Rigidbody extends Component {
    public Vector2 velocity = Vector2.ZERO;
    public boolean isStatic = false;
    public boolean useGravity = false;
    public float torque = 0f;
    public float mass;
    public float restitution;
    public float friction;
    private boolean firstFrame = false;

    private ComponentPropertyBuilder rigidbodyComponentPropertyBuilder = new ComponentPropertyBuilder()
            .addCheckboxProperty("isStatic", "Static", new CheckBox())
            .addCheckboxProperty("useGravity", "Gravity", new CheckBox())
            .addCheckboxProperty("useAutoMass", "Auto Mass", new CheckBox())
            .addVector2Property("initialVelocity", "Velocity", new Vector2Field())
            .addLabelProperty("velocity", "Velocity", new Label())
            .addNumberInputFieldProperty("restitution", "Restitution", new TextField())
            .addNumberInputFieldProperty("mass", "Mass", new TextField())
            .addNumberInputFieldProperty("friction", "Friction", new TextField())
            .addNumberInputFieldProperty("torque", "Torque", new TextField());

    private void updateValues() {
        Vector2Field initialVelocityField = rigidbodyComponentPropertyBuilder.getVector2Field("initialVelocity");
        initialVelocityField.y.setOnAction(event -> {
            if (!initialVelocityField.y.getText().isBlank()) {
                parent.simulationObjectBodyDef.linearVelocity.y = Float.parseFloat(initialVelocityField.y.getText());
            }
        });
    }
    @Override
    public void Start() {
        parent.simulationObjectBodyDef.type = isStatic ? BodyType.STATIC : BodyType.DYNAMIC;
        parent.simulationObjectBodyDef.gravityScale = useGravity ? 1 : 0;
        parent.fixtureDef.density = mass;

        parent.fixtureDef.restitution = restitution;
        parent.fixtureDef.friction = friction;
        updateValues();
        System.out.println("Rigidbody component activated!");
    }

    @Override
    public void Use() {
        if (!firstFrame) {
            parent.simulationObjectBody.setAwake(true);
            parent.simulationObjectBody.setSleepingAllowed(false);
            parent.simulationObjectBody.applyAngularImpulse(torque * parent.simulationObjectBody.getInertia());
            parent.simulationObjectBody.setLinearVelocity(new Vec2((float) velocity.x, (float) velocity.y));
            firstFrame = true;
        }
        parent.simulationObjectBody.setLinearDamping(0);
        parent.position = new Vector2(parent.simulationObjectBody.getPosition().x, parent.simulationObjectBody.getPosition().y);
        parent.simulationObjectBody.setAngularDamping(0f);
        parent.angle = parent.simulationObjectBody.getAngle();
    }

    @Override
    public void displayComponent() {
        componentTab.getStyleClass().add(Objects.requireNonNull(getClass().getResource("/css/tabStylesheet.css")).toExternalForm());
        System.out.println(componentTab.getStyleClass().size());
        componentTab.setText("Rigid Body");
        componentTab.setContent(rigidbodyComponentPropertyBuilder.getAllProperties());
    }

    @Override
    public void Remove() {

    }
}
