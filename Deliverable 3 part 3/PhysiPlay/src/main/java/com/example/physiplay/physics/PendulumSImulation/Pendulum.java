package com.example.physiplay.physics.PendulumSImulation;

import javafx.animation.AnimationTimer;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;


public class Pendulum extends Pane implements StartStopControllable{
    private static final double WIDTH = 600;
    private static final double HEIGHT = 500;

    private static final double PIVOT_X = 300;  // Shifted to bottom-left
    private static final double PIVOT_Y = HEIGHT - 230;

    private static final double BALL_RADIUS = 20;
    private Arc angleArc;

    public static double MAX_ANGLE = Math.toRadians(90); // radians
    public static double LENGTH = 150; // cm
    public static double GRAVITY = 9.81; // m/s² now, fixed!

    private double time = 0;
    private Circle ball;
    private Line rod;
    private Line verticalLine;
    private Label angleDisplay;

    private AnimationTimer timer;
    private boolean running = true;
    private long lastTime = 0;

    private Slider angleSlider;
    private Slider lengthSlider;
    private Slider gravitySlider;

    public Pendulum() {
        this.setPrefSize(WIDTH, HEIGHT);
        initializePendulum();
        initializeSliders();
        startAnimation();
    }

    private void initializePendulum() {
        ball = new Circle(BALL_RADIUS, Color.RED);
        rod = new Line();

        // Pivot dot
        Circle pivotDot = new Circle(PIVOT_X, PIVOT_Y, 5, Color.BLACK);

        // Dashed vertical line
        verticalLine = new Line(PIVOT_X, PIVOT_Y, PIVOT_X, PIVOT_Y + LENGTH);
        verticalLine.getStrokeDashArray().addAll(5.0, 5.0);
        verticalLine.setStroke(Color.GRAY);

        // Angle arc
        angleArc = new Arc(PIVOT_X, PIVOT_Y, 40, 40, 270, 0);
        angleArc.setStroke(Color.DARKGREEN);
        angleArc.setFill(null);
        angleArc.setStrokeWidth(2);
        angleArc.setType(ArcType.OPEN);

        // Angle label
        angleDisplay = new Label(String.format("θ = %.0f°", Math.toDegrees(MAX_ANGLE)));
        angleDisplay.setLayoutX(PIVOT_X - 20);
        angleDisplay.setLayoutY(PIVOT_Y - 30);

        updatePendulumPosition();
        this.getChildren().addAll(verticalLine, rod, ball, pivotDot, angleArc, angleDisplay);
    }



    private void initializeSliders() {
        Label angleLabel = new Label("Max Angle (°)");
        angleSlider = new Slider(5, 90, Math.toDegrees(MAX_ANGLE));
        angleSlider.setShowTickMarks(true);
        angleSlider.setShowTickLabels(true);
        angleSlider.setMajorTickUnit(15);

        Label lengthLabel = new Label("Length (cm)");
        lengthSlider = new Slider(50, 250, LENGTH);
        lengthSlider.setShowTickMarks(true);
        lengthSlider.setShowTickLabels(true);
        lengthSlider.setMajorTickUnit(50);

        Label gravityLabel = new Label("Gravity (m/s²)");
        gravitySlider = new Slider(1, 20, GRAVITY);
        gravitySlider.setShowTickMarks(true);
        gravitySlider.setShowTickLabels(true);
        gravitySlider.setMajorTickUnit(5);

        angleSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            MAX_ANGLE = Math.toRadians(newVal.doubleValue());
            angleDisplay.setText(String.format("θ = %.0f°", newVal.doubleValue()));
        });

        lengthSlider.valueProperty().addListener((obs, oldVal, newVal) -> LENGTH = newVal.doubleValue());
        gravitySlider.valueProperty().addListener((obs, oldVal, newVal) -> GRAVITY = newVal.doubleValue());

        VBox sliderBox = new VBox(10, angleLabel, angleSlider, lengthLabel, lengthSlider, gravityLabel, gravitySlider);
        sliderBox.setLayoutX(10);
        sliderBox.setLayoutY(10);

        this.getChildren().add(sliderBox);
    }

    private void startAnimation() {
        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (lastTime == 0) {
                    lastTime = now;
                    return;
                }

                double deltaTime = ((now - lastTime) / 1_000_000_000.0) * 1.5; // 3x speed boost

                lastTime = now;
                time += deltaTime;

                updatePendulumPosition();
            }
        };

        timer.start();
    }

    private void updatePendulumPosition() {
        // convert LENGTH to meters for physics formula (LENGTH is in cm)
        double lengthMeters = LENGTH / 100.0;
        double omega = Math.sqrt(GRAVITY / lengthMeters);
        double angle = MAX_ANGLE * Math.cos(omega * time);
        angleArc.setLength(Math.toDegrees(angle));

        double x = PIVOT_X + LENGTH * Math.sin(angle);
        double y = PIVOT_Y + LENGTH * Math.cos(angle);

        ball.setCenterX(x);
        ball.setCenterY(y);
        rod.setStartX(PIVOT_X);
        rod.setStartY(PIVOT_Y);
        rod.setEndX(x);
        rod.setEndY(y);
        angleDisplay.setText(String.format("θ = %.1f°", Math.toDegrees(angle)));

    }

    public double getCurrentAngle() {
        double lengthMeters = LENGTH / 100.0;
        double omega = Math.sqrt(GRAVITY / lengthMeters);
        return MAX_ANGLE * Math.cos(omega * time);
    }

    public double getTime() {
        return time;
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

    public boolean isRunning() {
        return running;
    }
}
