package com.example.physiplay.components;

import com.example.physiplay.Component;
import com.example.physiplay.Vector2;
import com.example.physiplay.singletons.SimulationManager;
import com.google.gson.annotations.Expose;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import org.jbox2d.collision.shapes.PolygonShape;

/**
 * Abstract base class for all renderable components in the simulation.
 * Provides transformation utilities and rendering structure for subclasses.
 */
public abstract class Renderer extends Component {

    /** Fill color of the rendered shape. */
    @Expose
    public Color color = Color.rgb(255, 0, 0);

    /** Mouse X and Y coordinates, used for hologram rendering. */
    public double mouseX = 0, mouseY = 0;

    /** Reference to the shared JavaFX graphics context. */
    protected GraphicsContext gc = SimulationManager.getInstance().gc;

    /**
     * Called once when the component is initialized.
     * Subclasses must set up their physics collider here.
     */
    @Override
    public void Start() {
        initializeShapeCollider();
    }

    /**
     * Initializes the physics collider shape (Box2D) for the object.
     * This must be implemented by all concrete subclasses.
     */
    public abstract void initializeShapeCollider();

    /**
     * Draws the object’s main shape with full opacity.
     * This is called automatically during rendering.
     */
    public abstract void drawShape();

    /**
     * Draws a semi-transparent version of the object at the mouse position,
     * used to preview placement (e.g., during creation).
     */
    public abstract void drawHologram();

    /**
     * Applies transformations and styling before rendering a normal shape.
     * Called automatically by {@link #Use()}.
     */
    private void applyTransformations() {
        gc.save();
        gc.setFill(color);
        gc.setGlobalAlpha(1);
        if (parent.getComponent(Rigidbody.class) == null) {
            gc.translate(parent.getWorldPosition().x, parent.getWorldPosition().y);
        } else {
            gc.translate(parent.getWorldPosition().x * SimulationManager.SCALE,
                    parent.getWorldPosition().y * SimulationManager.SCALE);
        }
        gc.rotate(Math.toDegrees(parent.angle));
    }

    /**
     * Applies transformations and reduced opacity for hologram rendering.
     * Called before {@link #drawHologram()}.
     */
    protected void applyTransformationsForHologram() {
        gc.save();
        gc.setFill(color);
        gc.setGlobalAlpha(0.4);
        gc.translate(mouseX, mouseY);
        gc.rotate(Math.toDegrees(parent.angle));
    }

    /**
     * Called every frame when the component is active.
     * Applies transformations and calls {@link #drawShape()}.
     */
    @Override
    public void Use() {
        applyTransformations();
        drawShape();
    }

    /**
     * Called when the component is removed. Default implementation is empty.
     * Override if cleanup is needed.
     */
    @Override
    public void Remove() {
        // Optional override
    }
}
