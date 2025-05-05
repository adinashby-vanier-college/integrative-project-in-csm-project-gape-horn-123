package com.example.physiplay.physics.SpringSimulation;

import com.example.physiplay.physics.PendulumSImulation.StartStopControllable;

import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.Pane;

public class VelocityGraphSpring extends Pane implements StartStopControllable{
    private static final int MAX_POINTS = 1000;
    private static final double WINDOW_SIZE = 10;

    private LineChart<Number, Number> lineChart;
    private XYChart.Series<Number, Number> series;
    private double time = 0;
    private AnimationTimer timer;
    private Spring spring;
    private boolean running = true;
    private long lastTime = 0;

    public VelocityGraphSpring(Spring spring, String langCode) {
        this.spring = spring;
        initializeChart();
        startAnimation();
    }

    private void initializeChart() {
        NumberAxis xAxis = new NumberAxis(0, WINDOW_SIZE, 1);
        xAxis.setLabel("Time (s)");
        xAxis.setAutoRanging(false);

        NumberAxis yAxis = new NumberAxis(-5, 5, 1); // in m/s
        yAxis.setLabel("Velocity (m/s)");

        lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setTitle("Spring Velocity vs Time");
        lineChart.setPrefSize(600, 200);

        // ✅ Enable symbols for tooltip targets (we’ll hide them)
        lineChart.setCreateSymbols(true);
        lineChart.setLegendVisible(false);

        series = new XYChart.Series<>();
        lineChart.getData().add(series);
        this.getChildren().add(lineChart);
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

                double A = Spring.MAX_DISPLACEMENT;
                double omega = Spring.OMEGA;
                double velocity = (-A * omega * Math.sin(omega * time)) / Spring.PIXELS_PER_METER;

                // ✅ Create point for tooltip support
                XYChart.Data<Number, Number> point = new XYChart.Data<>(time, velocity);
                series.getData().add(point);

                if (series.getData().size() > MAX_POINTS) {
                    series.getData().remove(0);
                }

                // ✅ Tooltip & bigger hover area
                String tooltipText = String.format("t = %.2f s\nv = %.2f m/s", time, velocity);
                Tooltip tooltip = new Tooltip(tooltipText);
                tooltip.setStyle("-fx-font-size: 12px;");

                Platform.runLater(() -> {
                    if (point.getNode() != null) {
                        point.getNode().setStyle("""
                            -fx-background-color: transparent;
                            -fx-padding: 10px;
                            -fx-shape: "M0,0 h1 v1 h-1 z";
                        """);
                        Tooltip.install(point.getNode(), tooltip);
                    }
                });

                // ✅ Dynamic Y-axis scaling
                double safeMargin = 0.5;
                double maxVel = Math.abs(A * omega) / Spring.PIXELS_PER_METER + safeMargin;
                maxVel = Math.max(maxVel, 1);
                NumberAxis yAxis = (NumberAxis) lineChart.getYAxis();
                yAxis.setLowerBound(-maxVel);
                yAxis.setUpperBound(maxVel);

                // ✅ X-axis scroll
                NumberAxis xAxis = (NumberAxis) lineChart.getXAxis();
                if (time > WINDOW_SIZE) {
                    xAxis.setLowerBound(time - WINDOW_SIZE);
                    xAxis.setUpperBound(time);
                } else {
                    xAxis.setLowerBound(0);
                    xAxis.setUpperBound(WINDOW_SIZE);
                }

                xAxis.setTickUnit(1);
            }
        };

        timer.start();
    }

    public void pause() {
        timer.stop();
    }

    public void play() {
        lastTime = 0;
        timer.start();
        running = true;
    }
}
