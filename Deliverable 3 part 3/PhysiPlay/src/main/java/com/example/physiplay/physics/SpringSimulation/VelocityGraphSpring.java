package com.example.physiplay.physics.SpringSimulation;

import com.example.physiplay.physics.PendulumSImulation.StartStopControllable;

import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.Pane;

import java.util.Locale;
import java.util.ResourceBundle;

public class VelocityGraphSpring extends Pane implements StartStopControllable{
    private static final int MAX_POINTS = 1000; //Max number of points to dispaly on screen
    private static final double WINDOW_SIZE = 10; //Duration in seconds to show on the X-axis

    private LineChart<Number, Number> lineChart;  //Chart for plotting velocity vs time
    private XYChart.Series<Number, Number> series;  //series that holds the data points
    private double time = 0; //Elapsed time
    private AnimationTimer timer; 
    private Spring spring;  //reference to Spring simulation
    private boolean running = true;  //whether the graph is active or paused
    private long lastTime = 0;    //for tracking time between frames

    //Constructor that sets up the graph and starts animation
    public VelocityGraphSpring(Spring spring, String langCode) {
        this.spring = spring;
        initializeChart(langCode);
        startAnimation();
    }

    //Initializes and configures the chart
    private void initializeChart(String langCode) {
        Locale locale = new Locale(langCode);
        ResourceBundle bundle = ResourceBundle.getBundle("languages.messages", locale);

        //X-axsis time in seconds
        NumberAxis xAxis = new NumberAxis(0, WINDOW_SIZE, 1);
        xAxis.setLabel(bundle.getString("axis.time"));
        xAxis.setAutoRanging(false);

        //Y-axis velocity in m/s
        NumberAxis yAxis = new NumberAxis(-5, 5, 1); // in m/s
        yAxis.setLabel(bundle.getString("axis.velocity"));

        // Create the line chart
        lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setTitle(bundle.getString("title.springVelocityVsTime"));
        lineChart.setPrefSize(600, 200);

        //  Enable symbols for tooltip targets (we’ll hide them to not make graph look ugly asf)
        lineChart.setCreateSymbols(true);
        lineChart.setLegendVisible(false);

        // Create and attach the data series
        series = new XYChart.Series<>();
        lineChart.getData().add(series);
        this.getChildren().add(lineChart);
    }

    //Starts the animation loop that updates the graph
    private void startAnimation() {
        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                //Skip the first frame to initialize time
                if (lastTime == 0) {
                    lastTime = now;
                    return;
                }

                // Calculate time since last frame
                double deltaTime = (now - lastTime) / 1_000_000_000.0;
                lastTime = now;
                time += deltaTime;

                // Calculate spring velocity using v(t) = -Aωsin(ωt)
                double A = Spring.MAX_DISPLACEMENT;
                double omega = Spring.OMEGA;
                double velocity = (-A * omega * Math.sin(omega * time)) / Spring.PIXELS_PER_METER;

                //  Create point for tooltip support
                XYChart.Data<Number, Number> point = new XYChart.Data<>(time, velocity);
                series.getData().add(point);

                // keep only the most recent max points
                if (series.getData().size() > MAX_POINTS) {
                    series.getData().remove(0);
                }

                //  Tooltip showing time and velocity at each point
                String tooltipText = String.format("t = %.2f s\nv = %.2f m/s", time, velocity);
                Tooltip tooltip = new Tooltip(tooltipText);
                tooltip.setStyle("-fx-font-size: 12px;");

                //Add tooltip to the data point 
                Platform.runLater(() -> {
                    if (point.getNode() != null) {
                        point.getNode().setStyle("""
                            -fx-background-color: transparent;
                            -fx-padding: 10px;
                            -fx-shape: "M0,0 h1 v1 h-1 z";
                        """); //Make the hover area bigger
                        Tooltip.install(point.getNode(), tooltip);
                    }
                });

                //  Dynamic Y-axis scaling based on max possible velocities
                double safeMargin = 0.5;
                double maxVel = Math.abs(A * omega) / Spring.PIXELS_PER_METER + safeMargin;
                maxVel = Math.max(maxVel, 1);
                NumberAxis yAxis = (NumberAxis) lineChart.getYAxis();
                yAxis.setLowerBound(-maxVel);
                yAxis.setUpperBound(maxVel);

                //  X-axis scroll as time progresses
                NumberAxis xAxis = (NumberAxis) lineChart.getXAxis();
                if (time > WINDOW_SIZE) {
                    xAxis.setLowerBound(time - WINDOW_SIZE);
                    xAxis.setUpperBound(time);
                } else {
                    xAxis.setLowerBound(0);
                    xAxis.setUpperBound(WINDOW_SIZE);
                }

                xAxis.setTickUnit(1);  //tick every 1 second
            }
        };

        timer.start();
    }

    // pause the graph
    public void pause() {
        timer.stop();
    }

    // Resume the graph and reset time tracking
    public void play() {
        lastTime = 0;
        timer.start();
        running = true;
    }
}
