package com.example.physiplay.physics.SpringSimulation;

import com.example.physiplay.physics.PendulumSImulation.StartStopControllable;

import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.Pane;

public class PositionGraphSpring extends Pane implements StartStopControllable{
    private static final int MAX_POINTS = 1000;
    private static final double WINDOW_SIZE = 10;

    private LineChart<Number, Number> lineChart;
    private XYChart.Series<Number, Number> series;
    private AnimationTimer timer;
    private Spring spring;
    private boolean running = true;

    public PositionGraphSpring(Spring spring, String langCode) {
        this.spring = spring;
        initializeChart();
        startAnimation();
    }

    private void initializeChart() {
        NumberAxis xAxis = new NumberAxis(0, WINDOW_SIZE, 1);
        xAxis.setLabel("Time (s)");
        xAxis.setAutoRanging(false);

        NumberAxis yAxis = new NumberAxis(-1, 1, 0.1);
        yAxis.setLabel("Displacement (m)");

        lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setTitle("Spring Displacement vs Time");
        lineChart.setPrefSize(600, 200);

        // Enable symbols so tooltips work, but we’ll hide them later
        lineChart.setCreateSymbols(true);

        series = new XYChart.Series<>();
        lineChart.getData().add(series);
        this.getChildren().add(lineChart);
    }

    public void pause() {
        timer.stop();
    }

    public void play() {
        timer.start();
        running = true;
    }

    private void startAnimation() {
        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                double t = spring.getTime();
                double A = Spring.MAX_DISPLACEMENT;
                double omega = Spring.OMEGA;
                double value = (A * Math.cos(omega * t)) / Spring.PIXELS_PER_METER;

                // Create and add data point
                XYChart.Data<Number, Number> point = new XYChart.Data<>(t, value);
                series.getData().add(point);

                if (series.getData().size() > MAX_POINTS) {
                    series.getData().remove(0);
                }

                // Install tooltip + hide the visible dot
                String tooltipText = String.format("t = %.2f s\ny = %.2f m", t, value);
                Tooltip tooltip = new Tooltip(tooltipText);
                tooltip.setStyle("-fx-font-size: 12px;");

                Platform.runLater(() -> {
                    if (point.getNode() != null) {
                        // Hide the dot but keep it hoverable
                        point.getNode().setStyle("-fx-background-color: transparent; -fx-padding: 3px;");
                        Tooltip.install(point.getNode(), tooltip);
                    }
                });

                // Lock Y-axis range based on amplitude (in meters)
                double maxDisp = A / Spring.PIXELS_PER_METER;
                NumberAxis yAxis = (NumberAxis) lineChart.getYAxis();
                yAxis.setLowerBound(-maxDisp);
                yAxis.setUpperBound(maxDisp);

                // Scroll X-axis forward with time
                NumberAxis xAxis = (NumberAxis) lineChart.getXAxis();
                if (t > WINDOW_SIZE) {
                    xAxis.setLowerBound(t - WINDOW_SIZE);
                    xAxis.setUpperBound(t);
                } else {
                    xAxis.setLowerBound(0);
                    xAxis.setUpperBound(WINDOW_SIZE);
                }

                xAxis.setTickUnit(1);
            }
        };

        timer.start();
    }
}
