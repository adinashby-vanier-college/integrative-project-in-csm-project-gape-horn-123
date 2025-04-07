package main.java.com.example.physiplay.physicsconcepts;

import javafx.animation.AnimationTimer;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.Pane;

public class SimpleHarmonicSineGraph extends Pane {
    private static final int MAX_POINTS = 1000; // Number of points before removing old ones
    private LineChart<Number, Number> lineChart;
    private XYChart.Series<Number, Number> series;
    private double time = 0;
    private AnimationTimer timer;
    private PendulumSimulation pendulumSimulation;
    private SpringSimulation springSimulation;
    private boolean isPendulum;

    public SimpleHarmonicSineGraph(PendulumSimulation pendulumSimulation) {
        this.pendulumSimulation = pendulumSimulation;
        this.isPendulum = true;
        initializeChart();
        startAnimation();
    }

    public SimpleHarmonicSineGraph(SpringSimulation springSimulation) {
        this.springSimulation = springSimulation;
        this.isPendulum = false;
        initializeChart();
        startAnimation();
    }

    private void initializeChart() {
        NumberAxis xAxis = new NumberAxis();
        NumberAxis yAxis;
        if (isPendulum) {
            yAxis = new NumberAxis(-90, 90, 10);
            xAxis.setLabel("Time (s)");
            yAxis.setLabel("Angle (°)");
        } else {
            yAxis = new NumberAxis(-200,200, 10);
            xAxis.setLabel("Time (s)");
            yAxis.setLabel("Displacement (cm)");
        }

        lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setPrefSize(600, 200);
        lineChart.setCreateSymbols(false);
        lineChart.setLegendVisible(false);

        series = new XYChart.Series<>();
        lineChart.getData().add(series);
        this.getChildren().add(lineChart);
    }

    private void startAnimation() {
        timer = new AnimationTimer() {
            private long lastTime = 0;

            @Override
            public void handle(long now) {
                if (lastTime == 0) {
                    lastTime = now;
                    return;
                }

                double deltaTime = (now - lastTime) / 1_000_000_000.0;
                lastTime = now;
                time += deltaTime;

                double value;
                if (isPendulum) {
                    value = Math.toDegrees(pendulumSimulation.getCurrentAngle()); // Pendulum angle in degrees
                } else {
                    value = SpringSimulation.MAX_DISPLACEMENT * Math.cos(SpringSimulation.OMEGA * time); // Spring displacement
                }

                series.getData().add(new XYChart.Data<>(time, value));

                if (series.getData().size() > MAX_POINTS) {
                    series.getData().remove(0);
                }
            }
        };
        timer.start();
    }
}
