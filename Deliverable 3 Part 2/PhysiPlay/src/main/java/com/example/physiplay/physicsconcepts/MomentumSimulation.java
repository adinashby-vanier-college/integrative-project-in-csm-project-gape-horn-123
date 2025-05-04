package com.example.physiplay.physicsconcepts;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class MomentumSimulation {
    private static final int BOX_X = 0;
    private static final int BOX_Y = 0;
    private static final int BOX_WIDTH = 400;
    private static final int BOX_HEIGHT = 400;
    private static final int BALL_RADIUS = 15;

    private double dx1 = 3, dy1 = 3;
    private double dx2 = -3, dy2 = -3;
    private double mass1 = 1.0, mass2 = 1.0;

    private Timeline animation;
    private Pane simulationPane;
    private Circle ball1, ball2;

    public MomentumSimulation() {
        simulationPane = new Pane();

        Rectangle box = new Rectangle(BOX_X, BOX_Y, BOX_WIDTH, BOX_HEIGHT);
        box.setFill(Color.TRANSPARENT);
        box.setStroke(Color.BLACK);
        box.setStrokeWidth(2);

        ball1 = new Circle(200, 150, BALL_RADIUS, Color.RED);
        ball2 = new Circle(250, 200, BALL_RADIUS, Color.BLUE);

        simulationPane.getChildren().addAll(box, ball1, ball2);
    }

    public Pane getSimulationPane() {
        return simulationPane;
    }

    public VBox getControlPane() {
        // Sliders for speed
        Label speedLabel1 = new Label("Ball 1 Speed (red):");
        Slider speedSlider1 = createSlider(1, 10, dx1);
        Label speedLabel2 = new Label("Ball 2 Speed (blue):");
        Slider speedSlider2 = createSlider(1, 10, dx2);

        // Sliders for mass
        Label massLabel1 = new Label("Ball 1 Mass (red):");
        Slider massSlider1 = createSlider(0.5, 5, mass1);
        Label massLabel2 = new Label("Ball 2 Mass (blue):");
        Slider massSlider2 = createSlider(0.5, 5, mass2);

        // Buttons
        Button startButton = new Button("Start Simulation");
        Button stopButton = new Button("Stop Simulation");

        startButton.setOnAction(e -> startSimulation(speedSlider1, speedSlider2, massSlider1, massSlider2));
        stopButton.setOnAction(e -> stopSimulation());

        HBox sliders = new HBox(20, new VBox(speedLabel1, speedSlider1, massLabel1, massSlider1),
                new VBox(speedLabel2, speedSlider2, massLabel2, massSlider2));
        sliders.setAlignment(Pos.CENTER);

        VBox controls = new VBox(10, sliders, startButton, stopButton);
        controls.setAlignment(Pos.CENTER);

        return controls;
    }

    private Slider createSlider(double min, double max, double value) {
        Slider slider = new Slider(min, max, value);
        slider.setShowTickLabels(true);
        slider.setShowTickMarks(true);
        slider.setMajorTickUnit((max - min) / 2);
        slider.setBlockIncrement(0.5);
        return slider;
    }

    private void startSimulation(Slider speedSlider1, Slider speedSlider2, Slider massSlider1, Slider massSlider2) {
        dx1 = speedSlider1.getValue();
        dy1 = speedSlider1.getValue();
        dx2 = speedSlider2.getValue();
        dy2 = speedSlider2.getValue();
        mass1 = massSlider1.getValue();
        mass2 = massSlider2.getValue();

        if (animation != null) animation.stop();
        animation = new Timeline(new KeyFrame(Duration.millis(20), e -> moveBalls()));
        animation.setCycleCount(Timeline.INDEFINITE);
        animation.play();
    }

    private void stopSimulation() {
        if (animation != null) animation.stop();
    }

    private void moveBalls() {
        ball1.setCenterX(ball1.getCenterX() + dx1);
        ball1.setCenterY(ball1.getCenterY() + dy1);
        ball2.setCenterX(ball2.getCenterX() + dx2);
        ball2.setCenterY(ball2.getCenterY() + dy2);

        // Ball-wall collision handling
        handleWallCollision(ball1, true);
        handleWallCollision(ball2, false);

        // Ball-to-ball collision
        handleBallCollision();
    }

    private void handleWallCollision(Circle ball, boolean isBall1) {
        double dx = isBall1 ? dx1 : dx2;
        double dy = isBall1 ? dy1 : dy2;

        if (ball.getCenterX() - BALL_RADIUS < BOX_X) {
            ball.setCenterX(BOX_X + BALL_RADIUS);
            dx *= -1;
        }
        if (ball.getCenterX() + BALL_RADIUS > BOX_X + BOX_WIDTH) {
            ball.setCenterX(BOX_X + BOX_WIDTH - BALL_RADIUS);
            dx *= -1;
        }
        if (ball.getCenterY() - BALL_RADIUS < BOX_Y) {
            ball.setCenterY(BOX_Y + BALL_RADIUS);
            dy *= -1;
        }
        if (ball.getCenterY() + BALL_RADIUS > BOX_Y + BOX_HEIGHT) {
            ball.setCenterY(BOX_Y + BOX_HEIGHT - BALL_RADIUS);
            dy *= -1;
        }

        if (isBall1) {
            dx1 = dx;
            dy1 = dy;
        } else {
            dx2 = dx;
            dy2 = dy;
        }
    }

    private void handleBallCollision() {
        double dx = ball2.getCenterX() - ball1.getCenterX();
        double dy = ball2.getCenterY() - ball1.getCenterY();
        double distance = Math.sqrt(dx * dx + dy * dy);

        if (distance <= 2 * BALL_RADIUS) {
            double v1xNew = (dx1 * (mass1 - mass2) + 2 * mass2 * dx2) / (mass1 + mass2);
            double v2xNew = (dx2 * (mass2 - mass1) + 2 * mass1 * dx1) / (mass1 + mass2);
            double v1yNew = (dy1 * (mass1 - mass2) + 2 * mass2 * dy2) / (mass1 + mass2);
            double v2yNew = (dy2 * (mass2 - mass1) + 2 * mass1 * dy1) / (mass1 + mass2);

            dx1 = v1xNew;
            dy1 = v1yNew;
            dx2 = v2xNew;
            dy2 = v2yNew;
        }
    }
}
