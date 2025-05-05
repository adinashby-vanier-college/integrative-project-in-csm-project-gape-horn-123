package com.example.physiplay.physics.PendulumSImulation;

import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.Pane;

import java.util.Locale;
import java.util.ResourceBundle;

public class VelocityGraphPendulum extends Pane implements StartStopControllable{
    private static final int MAX_POINTS = 1000;       // Max number of data points shown
    private static final double WINDOW_SIZE = 10;     // how many seconds to display on x-axis

    private LineChart<Number, Number> lineChart;      // the chart for plotting ω vs t
    private XYChart.Series<Number, Number> series;    // the actual data series on the chart
    private AnimationTimer timer;           // updates the chart in real time
    private Pendulum pendulum;              // reference to the simulated pendulum
    private boolean running = true;         // see if graph is running
    private String langCode = "en";         // language code for ui labels

    // constructor to set up chart and animation
    public VelocityGraphPendulum(Pendulum pendulum, String langCode) {
        this.pendulum = pendulum;
        this.langCode = langCode;
        initializeChart(this.langCode);  // build chart UI
        startAnimation();                // start live update
    }

    // Builds and configures the line chart
    private void initializeChart(String langCode) {
        Locale locale = new Locale(langCode);
        ResourceBundle bundle = ResourceBundle.getBundle("languages.messages", locale);

        // x-axis = time in seconds
        NumberAxis xAxis = new NumberAxis(0, WINDOW_SIZE, 1);
        xAxis.setLabel(bundle.getString("axis.time"));
        xAxis.setAutoRanging(false);  //manual scrolling

        // y-axis = angular velocity in rad/s
        NumberAxis yAxis = new NumberAxis(-5, 5, 1); 
        yAxis.setLabel(bundle.getString("axis.angularVelocity"));

        // Create the chart and apply settings
        lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setTitle(bundle.getString("title.angularVelocityVsTime"));
        lineChart.setPrefSize(600, 200);  
        lineChart.setCreateSymbols(true);   // Needed for tooltips
        lineChart.setLegendVisible(false);   // only 1 line, so there's no legend

        // create a data series and add it to the chart
        series = new XYChart.Series<>();
        lineChart.getData().add(series);
        this.getChildren().add(lineChart);
    }

    public void pause() {
        timer.stop(); //pause
    }

    public void play() {
        timer.start(); //paly
        running = true;  
    }

    // start aniamtion in real time for plotting data
    private void startAnimation() {
        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                double t = pendulum.getTime(); // Get the current time from the pendulum simulation
                double A = Pendulum.MAX_ANGLE; // Angular velocity formula:  -A * ω * sin(ωt) first derivative of postion
                double omega = Math.sqrt(Pendulum.GRAVITY / (Pendulum.LENGTH / 100.0));
                double angularVelocity = -A * omega * Math.sin(omega * t);

                // create a new point and add it to the graph
                XYChart.Data<Number, Number> point = new XYChart.Data<>(t, angularVelocity);
                series.getData().add(point);

                // keep only the most recent max points
                if (series.getData().size() > MAX_POINTS) {
                    series.getData().remove(0);
                }

                // add tooltip showing time and angular velocity at this point
                String tooltipText = String.format("t = %.2f s\nω = %.2f rad/s", t, angularVelocity);
                Tooltip tooltip = new Tooltip(tooltipText);
                tooltip.setStyle("-fx-font-size: 12px;");
                Platform.runLater(() -> {
                    if (point.getNode() != null) {
                        point.getNode().setStyle("-fx-background-color: transparent; -fx-padding: 3px;");
                        Tooltip.install(point.getNode(), tooltip);
                    }
                });

                // Dynamic Y-axis to fit the data
                NumberAxis yAxis = (NumberAxis) lineChart.getYAxis();
                double safeMargin = 0.5;
                double maxVel = Math.abs(A * omega) + safeMargin;
                yAxis.setLowerBound(-maxVel);
                yAxis.setUpperBound(maxVel);

                // smoothly scroll x axis with time
                NumberAxis xAxis = (NumberAxis) lineChart.getXAxis();
                if (t > WINDOW_SIZE) {
                    xAxis.setLowerBound(t - WINDOW_SIZE);
                    xAxis.setUpperBound(t);
                } else {
                    xAxis.setLowerBound(0);
                    xAxis.setUpperBound(WINDOW_SIZE);
                }
                xAxis.setTickUnit(1);  // tick every 1 second
            }
        };

        timer.start(); // begin
    }
}
