package com.example.physiplay.physics.PendulumSImulation;

import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.Pane;

public class AccelerationGraphPendulum extends Pane implements StartStopControllable {
    private static final int MAX_POINTS = 1000;
    private static final double WINDOW_SIZE = 10;
    private LineChart<Number, Number> lineChart;
    private XYChart.Series<Number, Number> series;
    private AnimationTimer timer;
    private Pendulum pendulum;
    private boolean running = true;

    public AccelerationGraphPendulum(Pendulum pendulum) {
        this.pendulum = pendulum;
        initializeChart();
        startAnimation();
    }

    private void initializeChart() {
        NumberAxis xAxis = new NumberAxis(0, WINDOW_SIZE, 1);
        xAxis.setLabel("Time (s)");
        xAxis.setAutoRanging(false);

        NumberAxis yAxis = new NumberAxis(-10, 10, 2); // Approx default
        yAxis.setLabel("Angular Acceleration (rad/s²)");

        lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setTitle("Pendulum Angular Acceleration vs Time");
        lineChart.setPrefSize(600, 200);
        lineChart.setCreateSymbols(true);
        lineChart.setLegendVisible(false);

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
                double t = pendulum.getTime();
                double angle = pendulum.getCurrentAngle(); // in radians
                double lengthMeters = Pendulum.LENGTH / 100.0;
                double acceleration = -(Pendulum.GRAVITY / lengthMeters) * Math.sin(angle); // angular acceleration

                XYChart.Data<Number, Number> point = new XYChart.Data<>(t, acceleration);
                series.getData().add(point);

                if (series.getData().size() > MAX_POINTS) {
                    series.getData().remove(0);
                }

                String tooltipText = String.format("t = %.2f s\nα = %.2f rad/s²", t, acceleration);
                Tooltip tooltip = new Tooltip(tooltipText);
                tooltip.setStyle("-fx-font-size: 12px;");

                Platform.runLater(() -> {
                    if (point.getNode() != null) {
                        point.getNode().setStyle("-fx-background-color: transparent; -fx-padding: 3px;");
                        Tooltip.install(point.getNode(), tooltip);
                    }
                });

                // Auto adjust Y-axis range
                NumberAxis yAxis = (NumberAxis) lineChart.getYAxis();
                double maxAccel = Math.abs((Pendulum.GRAVITY / lengthMeters)) + 1;
                maxAccel = Math.max(maxAccel, 2);
                yAxis.setLowerBound(-maxAccel);
                yAxis.setUpperBound(maxAccel);

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