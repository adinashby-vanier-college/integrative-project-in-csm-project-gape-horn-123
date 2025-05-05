/**
 * A JavaFX simulation component representing a spring-mass system.
 * This class visualizes harmonic motion and allows control over amplitude,
 * spring constant, and mass using sliders. It supports multilingual label updates
 * and conforms to the StartStopControllable interface.
 */
package com.example.physiplay.physics.SpringSimulation;

import com.example.physiplay.physics.PendulumSImulation.StartStopControllable;

import javafx.animation.AnimationTimer;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polyline;

import java.util.Locale;
import java.util.ResourceBundle;

public class Spring extends Pane implements StartStopControllable {
    private static final double WIDTH = 600;
    private static final double HEIGHT = 500;
    private static final double ANCHOR_X = WIDTH / 2;
    private static final double ANCHOR_Y = 60;
    private static final double SPRING_LENGTH = 200;
    private static final double MASS_RADIUS = 20;

    public static final double PIXELS_PER_METER = 100.0;
    public static double MAX_DISPLACEMENT = 1.0 * PIXELS_PER_METER;
    private static double springConstant = 20;
    private static double massValue = 2;
    public static double OMEGA = Math.sqrt(springConstant / massValue);

    private double time = 0;
    private Circle mass;
    private Polyline spring;
    private AnimationTimer timer;
    private boolean running = true;
    private Slider amplitudeSlider, springConstantSlider, massSlider;
    private long lastTime = 0;
    private String langCode = "en";

    private Label amplitudeLabel;
    private Label springLabel;
    private Label massLabel;

    /**
     * Constructs the spring simulation with default size and physics properties.
     */
    public Spring() {
        this.setPrefSize(WIDTH, HEIGHT);
        initializeSpring();
        initializeSliders();
        startAnimation();
    }

    /**
     * Initializes the visual components of the spring and mass.
     */
    private void initializeSpring() {
        mass = new Circle(MASS_RADIUS, Color.BLUE);
        spring = new Polyline();
        spring.setStroke(Color.BLACK);
        spring.setStrokeWidth(3);

        double startY = ANCHOR_Y + SPRING_LENGTH + MAX_DISPLACEMENT;

        mass.setCenterX(ANCHOR_X);
        mass.setCenterY(startY);
        updateSpringShape(startY);

        this.getChildren().addAll(spring, mass);
    }

    /**
     * Returns the current displacement of the mass in the spring system.
     * @return the current displacement in pixels
     */
    public double getCurrentDisplacement() {
        return MAX_DISPLACEMENT * Math.cos(OMEGA * time);
    }

    /**
     * Gets the current simulation time.
     * @return the time in seconds
     */
    public double getTime() {
        return time;
    }

    /**
     * Initializes control sliders for amplitude, spring constant, and mass.
     */
    private void initializeSliders() {
        amplitudeLabel = new Label("Amplitude (m)");
        amplitudeSlider = new Slider(0.1, 2.0, MAX_DISPLACEMENT / PIXELS_PER_METER);
        amplitudeSlider.setShowTickMarks(true);
        amplitudeSlider.setShowTickLabels(true);
        amplitudeSlider.setMajorTickUnit(0.5);

        springLabel = new Label("Spring Constant (N/m)");
        springConstantSlider = new Slider(5, 100, springConstant);
        springConstantSlider.setShowTickMarks(true);
        springConstantSlider.setShowTickLabels(true);
        springConstantSlider.setMajorTickUnit(20);

        massLabel = new Label("Mass (kg)");
        massSlider = new Slider(0.5, 10, massValue);
        massSlider.setShowTickMarks(true);
        massSlider.setShowTickLabels(true);
        massSlider.setMajorTickUnit(2);

        amplitudeSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            MAX_DISPLACEMENT = newVal.doubleValue() * PIXELS_PER_METER;
        });
        springConstantSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            updateOmega(newVal.doubleValue(), massValue);
        });
        massSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            updateOmega(springConstant, newVal.doubleValue());
        });

        VBox sliderBox = new VBox(10, amplitudeLabel, amplitudeSlider, springLabel, springConstantSlider, massLabel, massSlider);
        sliderBox.setLayoutX(10);
        sliderBox.setLayoutY(10);

        this.getChildren().add(sliderBox);
    }

    /**
     * Updates the angular frequency OMEGA based on the spring constant and mass.
     * @param k spring constant
     * @param m mass
     */
    private void updateOmega(double k, double m) {
        springConstant = k;
        massValue = m;
        OMEGA = Math.sqrt(springConstant / massValue);
    }

    /**
     * Redraws the spring shape based on the end Y-coordinate of the mass.
     * @param endY the Y position of the mass
     */
    private void updateSpringShape(double endY) {
        spring.getPoints().clear();
        spring.getPoints().addAll(ANCHOR_X, ANCHOR_Y);

        int coilCount = 15;
        double coilWidth = 20;
        double dynamicSpacing = (endY - ANCHOR_Y) / coilCount;

        for (int i = 1; i <= coilCount; i++) {
            double xOffset = (i % 2 == 0) ? coilWidth : -coilWidth;
            double y = ANCHOR_Y + i * dynamicSpacing;
            spring.getPoints().addAll(ANCHOR_X + xOffset, y);
        }
        spring.getPoints().addAll(ANCHOR_X, endY);
    }

    @Override
    public void pause() {
        timer.stop();
        running = false;
    }

    @Override
    public void play() {
        lastTime = 0;
        timer.start();
        running = true;
    }

    /**
     * Returns whether the simulation is currently running.
     * @return true if running, false if paused
     */
    public boolean isRunning() {
        return running;
    }

    /**
     * Starts the animation timer that updates the mass position and spring shape in real time.
     */
    private void startAnimation() {
        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (lastTime == 0) {
                    lastTime = now;
                    return;
                }

                double deltaTime = (now - lastTime) / 1_000_000_000.0;
                lastTime = now;
                time += deltaTime;

                double y = ANCHOR_Y + SPRING_LENGTH + MAX_DISPLACEMENT * Math.cos(OMEGA * time);

                mass.setCenterY(y);
                updateSpringShape(y);
            }
        };

        timer.start();
    }

    /**
     * Switches the language used for UI labels.
     * @param langCode the language code to switch to (e.g., "en", "fr")
     */
    public void switchLanguage(String langCode) {
        this.langCode = langCode;
        Locale locale = new Locale(langCode);
        ResourceBundle bundle = ResourceBundle.getBundle("languages.messages", locale);
        amplitudeLabel.setText(bundle.getString("label.amplitude"));
        springLabel.setText(bundle.getString("label.spring"));
        massLabel.setText(bundle.getString("label.mass"));
    }
}
