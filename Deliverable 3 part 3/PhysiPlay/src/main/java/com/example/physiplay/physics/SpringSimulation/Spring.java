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

    // ui and physics constants
    private static final double WIDTH = 600;       // width of the spring simulation window
    private static final double HEIGHT = 500;      // height of the spring simulation window
    private static final double ANCHOR_X = WIDTH / 2;   // Horizontal center of the screen 
    private static final double ANCHOR_Y = 60;          // vertical offset from the top fro the spring's anchor
    private static final double SPRING_LENGTH = 200;    //default un-stretched length of the spring in pixels before displacement is added
    private static final double MASS_RADIUS = 20;       // Radius of the circular mass at the bottom of the spring

    public static final double PIXELS_PER_METER = 100.0;  // conversion factor: 1 meter = 100 pixels

    // physics properties (modifiable via sliders)
    public static double MAX_DISPLACEMENT = 1.0 * PIXELS_PER_METER;
    private static double springConstant = 20;
    private static double massValue = 2;
    public static double OMEGA = Math.sqrt(springConstant / massValue);  // Angular frequency:  ω = √(k/m)

    // Simulation state variables
    private double time = 0;        // time tracking fo rsimulation
    private Circle mass;            
    private Polyline spring;        // visual spring drawn as zigzag 
    private AnimationTimer timer;
    private boolean running = true;   //keeps tracking of if the simulation is running
    private Slider amplitudeSlider, springConstantSlider, massSlider;
    private long lastTime = 0;        // time of the previous frame, used to calculate deltatime
    private String langCode = "en";   //language code 

    // UI controls
    private Label amplitudeLabel;
    private Label springLabel;
    private Label massLabel;

    
    public Spring() {
        this.setPrefSize(WIDTH, HEIGHT);
        initializeSpring();  // setup mass and spring visuals
        initializeSliders();  // setup control sliders
        startAnimation();     // start real time animation
    }

    
    private void initializeSpring() {
        mass = new Circle(MASS_RADIUS, Color.BLUE);   // mass bob
        spring = new Polyline();       // zigzag spring
        spring.setStroke(Color.BLACK);
        spring.setStrokeWidth(3);

        double startY = ANCHOR_Y + SPRING_LENGTH + MAX_DISPLACEMENT; //initial vertical position of mass

        mass.setCenterX(ANCHOR_X);
        mass.setCenterY(startY);
        updateSpringShape(startY);  // draw the zigzag shape 

        this.getChildren().addAll(spring, mass);
    }

    
     //Returns the current displacement of the mass in the spring system.
    public double getCurrentDisplacement() {
        return MAX_DISPLACEMENT * Math.cos(OMEGA * time);
    }

    
    //return the time in seconds
    public double getTime() {
        return time;
    }

    
    private void initializeSliders() {

        // amplitude slider that controls how far the spring stretched from equilibrium
        amplitudeLabel = new Label("Amplitude (m)");
        amplitudeSlider = new Slider(0.1, 2.0, MAX_DISPLACEMENT / PIXELS_PER_METER);  //Range 0.1m to 2.0m
        amplitudeSlider.setShowTickMarks(true);    //Show tick marks on the track
        amplitudeSlider.setShowTickLabels(true);   // Show numeric labels
        amplitudeSlider.setMajorTickUnit(0.5);     // Big ticks every 0.5 m

        // spring constant slider that adjusts stiffness of the spring(k)
        springLabel = new Label("Spring Constant (N/m)");
        springConstantSlider = new Slider(5, 100, springConstant);  // Range: 5 to 100 N/m
        springConstantSlider.setShowTickMarks(true);
        springConstantSlider.setShowTickLabels(true);
        springConstantSlider.setMajorTickUnit(20);      // Big ticks every 20 N/m
        
        // Mass slider that sets the mass attached to the spring (m)
        massLabel = new Label("Mass (kg)");
        massSlider = new Slider(0.5, 10, massValue);  // Range: 0.5 kg to 10 kg
        massSlider.setShowTickMarks(true);
        massSlider.setShowTickLabels(true);
        massSlider.setMajorTickUnit(2);          // Big ticks every 2kg

        // Event listeners: update physics on change
        amplitudeSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            MAX_DISPLACEMENT = newVal.doubleValue() * PIXELS_PER_METER;
        });
        springConstantSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            updateOmega(newVal.doubleValue(), massValue);
        });
        massSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            updateOmega(springConstant, newVal.doubleValue());
        });

        // layout the slider box
        VBox sliderBox = new VBox(10, amplitudeLabel, amplitudeSlider, springLabel, springConstantSlider, massLabel, massSlider);
        sliderBox.setLayoutX(10);
        sliderBox.setLayoutY(10);

        this.getChildren().add(sliderBox);
    }

    // Recalculates ω when spring or mass values change
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
        spring.getPoints().addAll(ANCHOR_X, ANCHOR_Y);  // starts of spring

        int coilCount = 15;
        double coilWidth = 20;
        double dynamicSpacing = (endY - ANCHOR_Y) / coilCount;

        //create zigzag coil pattern
        for (int i = 1; i <= coilCount; i++) {
            double xOffset = (i % 2 == 0) ? coilWidth : -coilWidth;
            double y = ANCHOR_Y + i * dynamicSpacing;
            spring.getPoints().addAll(ANCHOR_X + xOffset, y);
        }
        spring.getPoints().addAll(ANCHOR_X, endY); //end of the sprimg
    }

    @Override
    public void pause() {
        timer.stop();
        running = false;
    }

    @Override
    public void play() {
        lastTime = 0;  //reset timing
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

                //position of mass: y = equilibrium + Acos(ωt)
                double y = ANCHOR_Y + SPRING_LENGTH + MAX_DISPLACEMENT * Math.cos(OMEGA * time);

                mass.setCenterY(y);    // move the mass
                updateSpringShape(y);   // update spring shape accordingly
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
