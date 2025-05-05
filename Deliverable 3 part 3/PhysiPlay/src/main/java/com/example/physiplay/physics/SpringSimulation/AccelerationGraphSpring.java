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

public class AccelerationGraphSpring extends Pane implements StartStopControllable{
    private static final int MAX_POINTS = 1000;
    private static final double WINDOW_SIZE = 10;

    private LineChart<Number, Number> lineChart;
    private XYChart.Series<Number, Number> series;
    private double time = 0;
    private AnimationTimer timer;
    private Spring spring;
    private long lastTime = 0;

    public AccelerationGraphSpring(Spring spring, String langCode) {
        this.spring = spring;
        initializeChart(langCode);
        startAnimation();
    }

    private void initializeChart(String langCode) {
        Locale locale = new Locale(langCode);
        ResourceBundle bundle = ResourceBundle.getBundle("languages.messages", locale);

        NumberAxis xAxis = new NumberAxis(0, WINDOW_SIZE, 1);
        xAxis.setLabel(bundle.getString("axis.time"));
        xAxis.setAutoRanging(false);

        NumberAxis yAxis = new NumberAxis(-10, 10, 2);
        yAxis.setLabel(bundle.getString("axis.acceleration"));

        lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setTitle(bundle.getString("title.springAccelerationVsTime"));
        lineChart.setPrefSize(600, 200);

        // ✅ Enable symbols for tooltip hover
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
                double acceleration = (-A * omega * omega * Math.cos(omega * time)) / Spring.PIXELS_PER_METER;

                XYChart.Data<Number, Number> point = new XYChart.Data<>(time, acceleration);
                series.getData().add(point);

                if (series.getData().size() > MAX_POINTS) {
                    series.getData().remove(0);
                }

                // ✅ Tooltip setup
                String tooltipText = String.format("t = %.2f s\na = %.2f m/s²", time, acceleration);
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

                // ✅ Y-axis scaling
                double safeMargin = 1;
                double maxAccel = Math.abs(A * omega * omega) / Spring.PIXELS_PER_METER + safeMargin;
                maxAccel = Math.max(maxAccel, 2);

                NumberAxis yAxis = (NumberAxis) lineChart.getYAxis();
                yAxis.setLowerBound(-maxAccel);
                yAxis.setUpperBound(maxAccel);

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
    }
}
