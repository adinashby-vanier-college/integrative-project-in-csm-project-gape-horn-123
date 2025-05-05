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

public class AngleGraphPendulum extends Pane implements StartStopControllable{
    private static final int MAX_POINTS = 1000;  // Max num of data points
    private static final double WINDOW_SIZE = 10;   //time window shown on x axis

    private LineChart<Number, Number> lineChart;   // Line chart for plotting angle vs time
    private XYChart.Series<Number, Number> series;  // data series shown on the chart
    private AnimationTimer timer;     // timer to update chart every frame
    private Pendulum pendulum;        // reference to the pendulum object
    private String langCode;          // lamgauge code for localization

    // Constructor that sets up chart and starts animation
    public AngleGraphPendulum(Pendulum pendulumPane, String langCode) {
        this.pendulum = pendulumPane;
        this.langCode = langCode;
        initializeChart(this.langCode);
        startAnimation();
    }

    // initializes chart and configures its axes and labels
    private void initializeChart(String langCode) {
        Locale locale = new Locale(langCode);
        ResourceBundle bundle = ResourceBundle.getBundle("languages.messages", locale);
        NumberAxis xAxis = new NumberAxis(0, WINDOW_SIZE, 1);
        
        // x axis time in seconds
        xAxis.setLabel(bundle.getString("axis.time"));
        xAxis.setAutoRanging(false);

        // y axis angle in degrees from -90° to +90°
        NumberAxis yAxis = new NumberAxis(-90, 90, 15); 
        yAxis.setLabel(bundle.getString("axis.angle"));

        // configure tge linechart
        lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setTitle(bundle.getString("title.pendulumAngleVsTime"));
        lineChart.setPrefSize(600, 200); // wider chart
        lineChart.setCreateSymbols(true); // For hover tooltips
        lineChart.setLegendVisible(false); // No legend need for one line

        // attach the data series
        series = new XYChart.Series<>();
        lineChart.getData().add(series);
        this.getChildren().add(lineChart);
    }

    public void pause() {
        timer.stop(); //pause timer
    }

    public void play() {
        timer.start(); //start timer
    }

    // Starts updating the chart in real time
    private void startAnimation() {
        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                // get current time from the pendulum and angle in degrees
                double t = pendulum.getTime();
                double angleDeg = Math.toDegrees(pendulum.getCurrentAngle());

                //Add the new point to the graph
                XYChart.Data<Number, Number> point = new XYChart.Data<>(t, angleDeg);
                series.getData().add(point);

                //remove old points to keep chart clean and performant
                if (series.getData().size() > MAX_POINTS) {
                    series.getData().remove(0); 
                }

                // Create a tooltip showing (t, θ) for this point
                Tooltip tooltip = new Tooltip(String.format("t = %.2f s\nθ = %.1f°", t, angleDeg));
                tooltip.setStyle("-fx-font-size: 12px;");
                Platform.runLater(() -> {
                    if (point.getNode() != null) {
                        point.getNode().setStyle("-fx-background-color: transparent; -fx-padding: 3px;");
                        Tooltip.install(point.getNode(), tooltip);
                    }
                });

                // Y-axis bounded to the max swing angle 
                double maxDeg = Math.toDegrees(Pendulum.MAX_ANGLE);
                NumberAxis yAxis = (NumberAxis) lineChart.getYAxis();
                yAxis.setLowerBound(-maxDeg);
                yAxis.setUpperBound(maxDeg);

                // X-axis smoothly scrolls as time progresses
                NumberAxis xAxis = (NumberAxis) lineChart.getXAxis();
                if (t > WINDOW_SIZE) {
                    xAxis.setLowerBound(t - WINDOW_SIZE);
                    xAxis.setUpperBound(t);
                } else {
                    xAxis.setLowerBound(0);
                    xAxis.setUpperBound(WINDOW_SIZE);
                }
            }
        };

        timer.start(); // start the timer
    }
}
