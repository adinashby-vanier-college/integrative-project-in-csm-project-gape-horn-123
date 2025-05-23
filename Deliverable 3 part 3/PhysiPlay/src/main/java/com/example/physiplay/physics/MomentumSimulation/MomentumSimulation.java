package com.example.physiplay.physics.MomentumSimulation;

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

import java.util.Locale;
import java.util.ResourceBundle;

public class MomentumSimulation {

    //Constants for ball size and box
    private static final int BOX_X = 0;
    private static final int BOX_Y = 0;
    private static final int BOX_WIDTH = 400;
    private static final int BOX_HEIGHT = 300;
    private static final int BALL_RADIUS = 15;

    //initial velocities and masses
    private double dx1 = 3, dy1 = 3;
    private double dx2 = -3, dy2 = -3;
    private double mass1 = 1.0, mass2 = 1.0;

    private Timeline animation;
    private Pane simulationPane;
    private Circle ball1, ball2;

    //Ui stuff
    public Label speedLabel1,speedLabel2, massLabel1, massLabel2;
    public Button startButton, stopButton;
    private String langCode = "en";

    // Constructor that sets up the simulation area with a box and two balls
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

    //returns the simulation pane
    public Pane getSimulationPane() {
        return simulationPane;
    }

    //returns the control pane
    public VBox getControlPane() {

        Locale locale = new Locale(langCode);
        ResourceBundle bundle = ResourceBundle.getBundle("languages.messages", locale);

        // Sliders for speed
        speedLabel1 = new Label(bundle.getString("label.speed1"));
        Slider speedSlider1 = createSlider(1, 10, dx1);
        speedLabel2 = new Label(bundle.getString("label.speed2"));
        Slider speedSlider2 = createSlider(1, 10, dx2);

        // Sliders for mass
        massLabel1 = new Label(bundle.getString("label.mass1"));
        Slider massSlider1 = createSlider(0.5, 5, mass1);
        massLabel2 = new Label(bundle.getString("label.mass2"));
        Slider massSlider2 = createSlider(0.5, 5, mass2);

        // start and stop button
        startButton = new Button(bundle.getString("button.startSimulation"));
        stopButton = new Button(bundle.getString("button.stopSimulation"));

        //Actions
        startButton.setOnAction(e -> startSimulation(speedSlider1, speedSlider2, massSlider1, massSlider2));
        stopButton.setOnAction(e -> stopSimulation());

        //arrange sliders and buttons
        HBox sliders = new HBox(20, new VBox(speedLabel1, speedSlider1, massLabel1, massSlider1),
                new VBox(speedLabel2, speedSlider2, massLabel2, massSlider2));
        sliders.setAlignment(Pos.CENTER);

        VBox controls = new VBox(10, sliders, startButton, stopButton);
        controls.setAlignment(Pos.CENTER);

        return controls;
    }

    //create slider with labels and marks
    private Slider createSlider(double min, double max, double value) {
        Slider slider = new Slider(min, max, value);
        slider.setShowTickLabels(true);
        slider.setShowTickMarks(true);
        slider.setMajorTickUnit((max - min) / 2);
        slider.setBlockIncrement(0.5);
        return slider;
    }

    //starts simulation with current slider values
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

    //Stops the animation
    private void stopSimulation() {
        if (animation != null) animation.stop();
    }

    //Moves the balls and handles collision
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

        //Bounce off left or right walls
        if (ball.getCenterX() - BALL_RADIUS < BOX_X) {
            ball.setCenterX(BOX_X + BALL_RADIUS);
            dx *= -1;
        }
        if (ball.getCenterX() + BALL_RADIUS > BOX_X + BOX_WIDTH) {
            ball.setCenterX(BOX_X + BOX_WIDTH - BALL_RADIUS);
            dx *= -1;
        }
        //Bounce off top or bottom walls
        if (ball.getCenterY() - BALL_RADIUS < BOX_Y) {
            ball.setCenterY(BOX_Y + BALL_RADIUS);
            dy *= -1;
        }
        if (ball.getCenterY() + BALL_RADIUS > BOX_Y + BOX_HEIGHT) {
            ball.setCenterY(BOX_Y + BOX_HEIGHT - BALL_RADIUS);
            dy *= -1;
        }

        //Update velocity
        if (isBall1) {
            dx1 = dx;
            dy1 = dy;
        } else {
            dx2 = dx;
            dy2 = dy;
        }
    }

    //Handles collisions between two balls using 2d elastic collision equations
    private void handleBallCollision() {
        double dx = ball2.getCenterX() - ball1.getCenterX();
        double dy = ball2.getCenterY() - ball1.getCenterY();
        double distance = Math.sqrt(dx * dx + dy * dy);

        //check collision
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

    // changing to french/english
    public void setLanguage(String langCode){
        this.langCode = langCode;
    }

}
