package main.java.com.example.physiplay.physicsconcepts;

import javafx.animation.AnimationTimer;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.layout.VBox;

public class PendulumSimulation extends Pane {
    private static final double WIDTH = 600; // Increased width to fit everything
    private static final double HEIGHT = 400;
    private static final double PIVOT_X = WIDTH * 0.75; // Moved pendulum to center-right
    private static final double PIVOT_Y = 50;
    private static final double BALL_RADIUS = 20;

    public static double MAX_ANGLE = Math.toRadians(30);
    public static double LENGTH = 150;
    private static int MULTIPLIER = 100;
    public static double GRAVITY = 9.81 * MULTIPLIER;

    private double time = 0;
    private Circle ball;
    private Line rod;
    private AnimationTimer timer;

    private Slider angleSlider;
    private Slider lengthSlider;
    private Slider gravitySlider;

    public PendulumSimulation() {
        this.setPrefSize(WIDTH, HEIGHT);
        initializePendulum();
        initializeSliders();
        startAnimation();
    }

    private void initializePendulum() {
        ball = new Circle(BALL_RADIUS, Color.RED);
        rod = new Line();

        updatePendulumPosition();
        this.getChildren().addAll(rod, ball);
    }

    private void initializeSliders() {
        Label angleLabel = new Label("Max Angle (°)");
        angleSlider = new Slider(5, 90, Math.toDegrees(MAX_ANGLE));
        angleSlider.setShowTickMarks(true);
        angleSlider.setShowTickLabels(true);
        angleSlider.setMajorTickUnit(15);
        angleSlider.setMinorTickCount(4);
        angleSlider.setBlockIncrement(5);

        Label lengthLabel = new Label("Length (cm)");
        lengthSlider = new Slider(50, 300, LENGTH);
        lengthSlider.setShowTickMarks(true);
        lengthSlider.setShowTickLabels(true);
        lengthSlider.setMajorTickUnit(50);
        lengthSlider.setMinorTickCount(5);
        lengthSlider.setBlockIncrement(10);

        Label gravityLabel = new Label("Gravity (m/s²)");
        gravitySlider = new Slider(0, 40, GRAVITY);
        gravitySlider.setShowTickMarks(true);
        gravitySlider.setShowTickLabels(true);
        gravitySlider.setMajorTickUnit(5);
        gravitySlider.setMinorTickCount(4);
        gravitySlider.setBlockIncrement(1);

        angleSlider.valueProperty().addListener((obs, oldVal, newVal) -> MAX_ANGLE = Math.toRadians(newVal.doubleValue()));
        lengthSlider.valueProperty().addListener((obs, oldVal, newVal) -> LENGTH = newVal.doubleValue());
        gravitySlider.valueProperty().addListener((obs, oldVal, newVal) -> GRAVITY = newVal.doubleValue() * MULTIPLIER);

        VBox sliderBox = new VBox(10, angleLabel, angleSlider, lengthLabel, lengthSlider, gravityLabel, gravitySlider);
        sliderBox.setLayoutX(10); // Sliders on top-left
        sliderBox.setLayoutY(10);

        this.getChildren().add(sliderBox);
    }

    private void startAnimation() {
        timer = new AnimationTimer() {
            private long lastTime = 0;

            @Override
            public void handle(long now) {
                if (lastTime == 0) {
                    lastTime = now;
                    return;
                }

                double deltaTime = (now - lastTime) / 1_000_000_000.0;
                lastTime = now;
                time += deltaTime;

                updatePendulumPosition();
            }
        };
        timer.start();
    }

    private void updatePendulumPosition() {
        double angle = MAX_ANGLE * Math.cos(Math.sqrt(GRAVITY / LENGTH) * time);
        double x = PIVOT_X + LENGTH * Math.sin(angle);
        double y = PIVOT_Y + LENGTH * Math.cos(angle);

        ball.setCenterX(x);
        ball.setCenterY(y);
        rod.setStartX(PIVOT_X);
        rod.setStartY(PIVOT_Y);
        rod.setEndX(x);
        rod.setEndY(y);
    }

    public double getCurrentAngle() {
        return MAX_ANGLE * Math.cos(Math.sqrt(GRAVITY / LENGTH) * time);
    }
}
