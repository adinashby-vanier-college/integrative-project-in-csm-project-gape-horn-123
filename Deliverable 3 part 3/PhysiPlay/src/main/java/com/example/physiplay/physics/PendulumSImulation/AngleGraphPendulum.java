package com.example.physiplay.physics.PendulumSImulation;

import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.Pane;

public class AngleGraphPendulum extends Pane {
    private static final int MAX_POINTS = 1000;
    private static final double WINDOW_SIZE = 10;

    private LineChart<Number, Number> lineChart;
    private XYChart.Series<Number, Number> series;
    private AnimationTimer timer;
    private Pendulum pendulum;

    public AngleGraphPendulum(Pendulum pendulumPane) {
        this.pendulum = pendulumPane;
        initializeChart();
        startAnimation();
    }

    private void initializeChart() {
        NumberAxis xAxis = new NumberAxis(0, WINDOW_SIZE, 1);
        xAxis.setLabel("Time (s)");
        xAxis.setAutoRanging(false);

        NumberAxis yAxis = new NumberAxis(-90, 90, 15); // Degrees
        yAxis.setLabel("Angle (°)");

        lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setTitle("Pendulum Angle vs Time");
        lineChart.setPrefSize(600, 200); // wider chart
        lineChart.setCreateSymbols(true); // For hover tooltips
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
    }

    private void startAnimation() {
        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                double t = pendulum.getTime();
                double angleDeg = Math.toDegrees(pendulum.getCurrentAngle());

                XYChart.Data<Number, Number> point = new XYChart.Data<>(t, angleDeg);
                series.getData().add(point);

                if (series.getData().size() > MAX_POINTS) {
                    series.getData().remove(0); // just prune oldest when too many
                }

                Tooltip tooltip = new Tooltip(String.format("t = %.2f s\nθ = %.1f°", t, angleDeg));
                tooltip.setStyle("-fx-font-size: 12px;");
                Platform.runLater(() -> {
                    if (point.getNode() != null) {
                        point.getNode().setStyle("-fx-background-color: transparent; -fx-padding: 3px;");
                        Tooltip.install(point.getNode(), tooltip);
                    }
                });

                // Y-axis stays fixed
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

        timer.start();
    }
}
