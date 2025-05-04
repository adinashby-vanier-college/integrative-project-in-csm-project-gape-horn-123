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

    private Label velocityLabel = rigidbodyComponentPropertyBuilder.getLabelField("velocity");
    private void resetParameters() {
        parent.simulationObjectBody.setAwake(true);
        parent.simulationObjectBody.setSleepingAllowed(false);
        parent.simulationObjectBody.applyAngularImpulse(torque * parent.simulationObjectBody.getInertia());
        parent.simulationObjectBody.setLinearVelocity(new Vec2((float) velocity.x, (float) velocity.y));
    }

    private void updateValues() {
        CheckBox isStaticCheckbox = rigidbodyComponentPropertyBuilder.getCheckBox("isStatic"),
        useGravityCheckbox = rigidbodyComponentPropertyBuilder.getCheckBox("useGravity");
        Vector2Field initialVelocityField = rigidbodyComponentPropertyBuilder.getVector2Field("initialVelocity");

        TextField restitutionField = rigidbodyComponentPropertyBuilder.getTextField("restitution"),
        massField = rigidbodyComponentPropertyBuilder.getTextField("mass"),
                frictionField = rigidbodyComponentPropertyBuilder.getTextField("friction"),
        torqueField = rigidbodyComponentPropertyBuilder.getTextField("torque");

        // Initialize values
        isStaticCheckbox.setSelected(parent.simulationObjectBody.getType() == BodyType.STATIC);
        initialVelocityField.x.setText(velocity.x + "");
        initialVelocityField.y.setText(velocity.y + "");
        useGravityCheckbox.setSelected(useGravity);
        restitutionField.setText(restitution + "");
        massField.setText(mass + "");
        frictionField.setText(friction + "");
        torqueField.setText(torque + "");

        isStaticCheckbox.selectedProperty().addListener((obs, oldValue, newValue) -> {
            parent.simulationObjectBody.setType(newValue ? BodyType.STATIC : BodyType.DYNAMIC);
            resetParameters();
        });
        useGravityCheckbox.selectedProperty().addListener((obs, oldValue, newValue) -> {
            useGravity = newValue;
            parent.simulationObjectBody.setGravityScale(useGravity ? 1 : 0);
            if (Math.abs(velocity.y) < 1e-6) velocity.y = 0;
            if (Math.abs(velocity.x) < 1e-6) velocity.x = 0;
            resetParameters();
        });
        restitutionField.setOnAction(event -> {
            if (!restitutionField.getText().isBlank()) {
                restitution = Float.parseFloat(restitutionField.getText());
                parent.simulationObjectBody.getFixtureList().setRestitution(restitution);
            }
        });

        massField.setOnAction(event -> {
            if (!massField.getText().isBlank()) {
                mass = Float.parseFloat(massField.getText());
                parent.simulationObjectBody.getFixtureList().setDensity(mass);
            }
        });

        frictionField.setOnAction(event -> {
            if (!frictionField.getText().isBlank()) {
                friction = Float.parseFloat(massField.getText());
                if (friction >= 0 && friction <= 1) {
                    parent.simulationObjectBody.getFixtureList().setFriction(friction);
                }
            }
        });

        torqueField.setOnAction(event -> {
            if (!torqueField.getText().isBlank()) {
                parent.simulationObjectBody.setAngularVelocity(0);
                torque = Float.parseFloat(torqueField.getText());
                parent.simulationObjectBody.applyAngularImpulse(torque
                        * parent.simulationObjectBody.getInertia());
            }
        });
        initialVelocityField.x.setOnAction(event -> {
            if (!initialVelocityField.x.getText().isBlank()) {
                velocity.x = Float.parseFloat(initialVelocityField.x.getText());
                parent.simulationObjectBody.setLinearVelocity(new Vec2(
                        (float) velocity.x, parent.simulationObjectBody.getLinearVelocity().y));
            }
        });
        initialVelocityField.y.setOnAction(event -> {
            if (!initialVelocityField.y.getText().isBlank()) {
                velocity.y = Float.parseFloat(initialVelocityField.y.getText());
                parent.simulationObjectBody.setLinearVelocity(new Vec2(parent.simulationObjectBody.getLinearVelocity().x,
                        (float) velocity.y));
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
        System.out.println("Rigidbody component activated!");
    }
    @Override
    public void Use() {
        if (!firstFrame) {
            resetParameters();
            updateValues();
            firstFrame = true;
        }
        parent.simulationObjectBody.setLinearDamping(0);
        parent.position = new Vector2(parent.simulationObjectBody.getPosition().x, parent.simulationObjectBody.getPosition().y);
        parent.simulationObjectBody.setAngularDamping(0f);
        parent.angle = parent.simulationObjectBody.getAngle();
        velocityLabel.setText("Velocity: (" + parent.simulationObjectBody.getLinearVelocity().x + ", " + parent.simulationObjectBody.getLinearVelocity().y + ")");
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
        SimulationManager.getInstance().world.destroyBody(parent.simulationObjectBody);
    }
}
