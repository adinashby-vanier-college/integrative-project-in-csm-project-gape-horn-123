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
    private static final int MAX_POINTS = 1000;
    private static final double WINDOW_SIZE = 10;

    private LineChart<Number, Number> lineChart;
    private XYChart.Series<Number, Number> series;
    private AnimationTimer timer;
    private Pendulum pendulum;
    private boolean running = true;
    private String langCode = "en";

    public VelocityGraphPendulum(Pendulum pendulum, String langCode) {
        this.pendulum = pendulum;
        this.langCode = langCode;
        initializeChart(this.langCode);
        startAnimation();
    }

    private void initializeChart(String langCode) {
        Locale locale = new Locale(langCode);
        ResourceBundle bundle = ResourceBundle.getBundle("languages.messages", locale);

        NumberAxis xAxis = new NumberAxis(0, WINDOW_SIZE, 1);
        xAxis.setLabel(bundle.getString("axis.time"));
        xAxis.setAutoRanging(false);

        NumberAxis yAxis = new NumberAxis(-5, 5, 1); // in rad/s
        yAxis.setLabel(bundle.getString("axis.angularVelocity"));

        lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setTitle(bundle.getString("title.angularVelocityVsTime"));
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
                double A = Pendulum.MAX_ANGLE;
                double omega = Math.sqrt(Pendulum.GRAVITY / (Pendulum.LENGTH / 100.0));
                double angularVelocity = -A * omega * Math.sin(omega * t);

                XYChart.Data<Number, Number> point = new XYChart.Data<>(t, angularVelocity);
                series.getData().add(point);

                if (series.getData().size() > MAX_POINTS) {
                    series.getData().remove(0);
                }

                String tooltipText = String.format("t = %.2f s\nω = %.2f rad/s", t, angularVelocity);
                Tooltip tooltip = new Tooltip(tooltipText);
                tooltip.setStyle("-fx-font-size: 12px;");
                Platform.runLater(() -> {
                    if (point.getNode() != null) {
                        point.getNode().setStyle("-fx-background-color: transparent; -fx-padding: 3px;");
                        Tooltip.install(point.getNode(), tooltip);
                    }
                });

                // Dynamic Y-axis
                NumberAxis yAxis = (NumberAxis) lineChart.getYAxis();
                double safeMargin = 0.5;
                double maxVel = Math.abs(A * omega) + safeMargin;
                yAxis.setLowerBound(-maxVel);
                yAxis.setUpperBound(maxVel);

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
