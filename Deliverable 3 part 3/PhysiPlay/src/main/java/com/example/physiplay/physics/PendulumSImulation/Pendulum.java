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

import java.util.Locale;
import java.util.ResourceBundle;


public class Pendulum extends Pane implements StartStopControllable{

    //Constants
    private static final double WIDTH = 600;
    private static final double HEIGHT = 500;

    // Pivot point for the pendulum
    private static final double PIVOT_X = 300;  // Shifted to bottom-left
    private static final double PIVOT_Y = HEIGHT - 230;

    //size of the pendulum bob
    private static final double BALL_RADIUS = 20;
    //Arc used to see the current angle
    private Arc angleArc;  

    // simualtion parameters
    public static double MAX_ANGLE = Math.toRadians(90); // max swing angle in radians
    public static double LENGTH = 150; // length in cm
    public static double GRAVITY = 9.81; // gravity m/s²

    private double time = 0;  //time tracker for oscillation

    //Main pendulum components
    private Circle ball;
    private Line rod;
    private Line verticalLine;
    private Label angleDisplay;

    private AnimationTimer timer;
    private boolean running = true;
    private long lastTime = 0;

   // Sliders and labels for user controls
    private Slider angleSlider;
    private Slider lengthSlider;
    private Slider gravitySlider;
    private Label lengthLabel;
    private Label angleLabel;
    private Label gravityLabel;

    private String langCode = "en"; //language support

    // Constructor sets size, builds ui and starts animation	
    public Pendulum() {
        this.setPrefSize(WIDTH, HEIGHT);
        initializePendulum();
        initializeSliders();
        startAnimation();
    }

    // Sets up pendulum visuals (ball, string, pivot , arc)
    private void initializePendulum() {
        ball = new Circle(BALL_RADIUS, Color.RED);
        rod = new Line();

        // Pivot dot
        Circle pivotDot = new Circle(PIVOT_X, PIVOT_Y, 5, Color.BLACK);

        // Dashed vertical reference line
        verticalLine = new Line(PIVOT_X, PIVOT_Y, PIVOT_X, PIVOT_Y + LENGTH);
        verticalLine.getStrokeDashArray().addAll(5.0, 5.0);
        verticalLine.setStroke(Color.GRAY);

        // Angle arc showing angular displacement from vertical
        angleArc = new Arc(PIVOT_X, PIVOT_Y, 40, 40, 270, 0);
        angleArc.setStroke(Color.DARKGREEN);
        angleArc.setFill(null);
        angleArc.setStrokeWidth(2);
        angleArc.setType(ArcType.OPEN);

        // Angle label in degree
        angleDisplay = new Label(String.format("θ = %.0f°", Math.toDegrees(MAX_ANGLE)));
        angleDisplay.setLayoutX(PIVOT_X - 20);
        angleDisplay.setLayoutY(PIVOT_Y - 30);

	//set initial rod and ball position
        updatePendulumPosition();
        this.getChildren().addAll(verticalLine, rod, ball, pivotDot, angleArc, angleDisplay);
    }



    private void initializeSliders() {
        Locale locale = new Locale(langCode);
        ResourceBundle bundle = ResourceBundle.getBundle("languages.messages", locale);

	// Max angle slider
        angleLabel = new Label(bundle.getString("label.maxAngle"));
        angleSlider = new Slider(5, 90, Math.toDegrees(MAX_ANGLE));
        angleSlider.setShowTickMarks(true);
        angleSlider.setShowTickLabels(true);
        angleSlider.setMajorTickUnit(15);

	// Length slider in cm    
        lengthLabel = new Label(bundle.getString("label.length"));
        lengthSlider = new Slider(50, 250, LENGTH);
        lengthSlider.setShowTickMarks(true);
        lengthSlider.setShowTickLabels(true);
        lengthSlider.setMajorTickUnit(50);

	// gravity slider in m/s²
        gravityLabel = new Label(bundle.getString("label.gravity"));
        gravitySlider = new Slider(1, 20, GRAVITY);
        gravitySlider.setShowTickMarks(true);
        gravitySlider.setShowTickLabels(true);
        gravitySlider.setMajorTickUnit(5);

	// slider event listeners    
        angleSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            MAX_ANGLE = Math.toRadians(newVal.doubleValue());
            angleDisplay.setText(String.format("θ = %.0f°", newVal.doubleValue()));
        });

	// Arrange sliders vertically    
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
		    // First frame initializes time
                if (lastTime == 0) {
                    lastTime = now;
                    return;
                }

		// Time step with a speed multiplier because its slow asf    
                double deltaTime = ((now - lastTime) / 1_000_000_000.0) * 1.5; // 1.5 time boost

                lastTime = now;
                time += deltaTime;

                updatePendulumPosition();  // redraw pendulum based on time
            }
        };

        timer.start();
    }

    // calculates new position and angle of the pendulum at current time
    private void updatePendulumPosition() {
        
        double lengthMeters = LENGTH / 100.0;  // Convert length to meters
        double omega = Math.sqrt(GRAVITY / lengthMeters); // this formula:  ω = √(g/L)
        double angle = MAX_ANGLE * Math.cos(omega * time); // this formula: θ(t) = θ₀ cos(ωt)

	//update arc length for display
	angleArc.setLength(Math.toDegrees(angle)); 
	    
        // compute x and y position of the bob
        double x = PIVOT_X + LENGTH * Math.sin(angle);
        double y = PIVOT_Y + LENGTH * Math.cos(angle);

	// Move ball and rod to match new position    
        ball.setCenterX(x);
        ball.setCenterY(y);
        rod.setStartX(PIVOT_X);
        rod.setStartY(PIVOT_Y);
        rod.setEndX(x);
        rod.setEndY(y);

	// update angle label with current value    
        angleDisplay.setText(String.format("θ = %.1f°", Math.toDegrees(angle)));

    }

    // returns the current angle of the pendulum in radians
    public double getCurrentAngle() {
        double lengthMeters = LENGTH / 100.0;
        double omega = Math.sqrt(GRAVITY / lengthMeters);
        return MAX_ANGLE * Math.cos(omega * time);
    }

    // returns the elapsed time of the simulation 
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

    // returns if animation running
    public boolean isRunning() {
        return running;
    }

    // switches language of ui labels 
    public void switchLanguage(String langCode) {
        this.langCode = langCode;
        Locale locale = new Locale(langCode);
        ResourceBundle bundle = ResourceBundle.getBundle("languages.messages", locale);
        lengthLabel.setText(bundle.getString("label.length"));
        angleLabel.setText(bundle.getString("label.maxAngle"));
        gravityLabel.setText(bundle.getString("label.gravity"));
    }
}
