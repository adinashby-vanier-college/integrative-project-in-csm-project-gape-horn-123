package main.java.com.example.physiplay.physics.SpringSimulation;

import javafx.animation.AnimationTimer;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polyline;

public class Spring extends Pane {
    private static final double WIDTH = 600;
    private static final double HEIGHT = 500;
    private static final double ANCHOR_X = WIDTH / 2;
    private static final double ANCHOR_Y = 60;
    private static final double SPRING_LENGTH = 200;
    private static final double MASS_RADIUS = 20;

    public static double MAX_DISPLACEMENT = 100; // Amplitude
    public static final double PIXELS_PER_METER = 100.0;
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

    public Spring() {
        this.setPrefSize(WIDTH, HEIGHT);
        initializeSpring();
        initializeSliders();
        startAnimation();
    }

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

    public double getCurrentDisplacement() {
        return MAX_DISPLACEMENT * Math.cos(OMEGA * time);
    }

    public double getTime() {
        return time;
    }

    private void initializeSliders() {
        Label amplitudeLabel = new Label("Amplitude (px)");
        amplitudeSlider = new Slider(10, 200, MAX_DISPLACEMENT);
        amplitudeSlider.setShowTickMarks(true);
        amplitudeSlider.setShowTickLabels(true);
        amplitudeSlider.setMajorTickUnit(50);

        Label springLabel = new Label("Spring Constant (N/m)");
        springConstantSlider = new Slider(5, 100, springConstant);
        springConstantSlider.setShowTickMarks(true);
        springConstantSlider.setShowTickLabels(true);
        springConstantSlider.setMajorTickUnit(20);

        Label massLabel = new Label("Mass (kg)");
        massSlider = new Slider(0.5, 10, massValue);
        massSlider.setShowTickMarks(true);
        massSlider.setShowTickLabels(true);
        massSlider.setMajorTickUnit(2);

        amplitudeSlider.valueProperty().addListener((obs, oldVal, newVal) -> MAX_DISPLACEMENT = newVal.doubleValue());
        springConstantSlider.valueProperty().addListener((obs, oldVal, newVal) -> updateOmega(newVal.doubleValue(), massValue));
        massSlider.valueProperty().addListener((obs, oldVal, newVal) -> updateOmega(springConstant, newVal.doubleValue()));

        VBox sliderBox = new VBox(10, amplitudeLabel, amplitudeSlider, springLabel, springConstantSlider, massLabel, massSlider);
        sliderBox.setLayoutX(10);
        sliderBox.setLayoutY(10);

        this.getChildren().add(sliderBox);
    }

    private void updateOmega(double k, double m) {
        springConstant = k;
        massValue = m;
        OMEGA = Math.sqrt(springConstant / massValue);
    }

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

    public void pause() {
        timer.stop();
        running = false;
    }

    public void play() {
        lastTime = 0;
        timer.start();
        running = true;
    }

    public boolean isRunning() {
        return running;
    }

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
}