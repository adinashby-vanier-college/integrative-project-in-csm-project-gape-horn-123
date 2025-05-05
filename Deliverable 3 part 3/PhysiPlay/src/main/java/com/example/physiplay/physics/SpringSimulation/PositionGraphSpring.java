/**
 * A JavaFX Pane that displays a live-updating line chart representing the displacement
 * of a spring over time. This class uses {@link AnimationTimer} to simulate and
 * visualize spring motion in real time.
 *
 * <p>The graph updates dynamically as the simulation progresses, showing a windowed
 * view of the last few seconds of motion. The X-axis represents time in seconds,
 * and the Y-axis represents displacement in meters.</p>
 */
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

public class PositionGraphSpring extends Pane implements StartStopControllable {
    private static final int MAX_POINTS = 1000;
    private static final double WINDOW_SIZE = 10;

    private LineChart<Number, Number> lineChart;
    private XYChart.Series<Number, Number> series;
    private AnimationTimer timer;
    private Spring spring;
    private boolean running = true;

    /**
     * Constructs a new PositionGraphSpring instance for visualizing spring displacement.
     *
     * @param spring   the Spring object that provides simulation data
     * @param langCode the language code used to localize chart labels and titles
     */
    public PositionGraphSpring(Spring spring, String langCode) {
        this.spring = spring;
        initializeChart(langCode);
        startAnimation();
    }

    /**
     * Initializes the chart with localized labels and appropriate axis configurations.
     *
     * @param langCode the language code for localization
     */
    private void initializeChart(String langCode) {
        Locale locale = new Locale(langCode);
        ResourceBundle bundle = ResourceBundle.getBundle("languages.messages", locale);

        NumberAxis xAxis = new NumberAxis(0, WINDOW_SIZE, 1);
        xAxis.setLabel(bundle.getString("axis.time"));
        xAxis.setAutoRanging(false);

        NumberAxis yAxis = new NumberAxis(-1, 1, 0.1);
        yAxis.setLabel(bundle.getString("axis.displacement"));

        lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setTitle(bundle.getString("title.springDisplacementVsTime"));
        lineChart.setPrefSize(600, 200);
        lineChart.setCreateSymbols(true);

        series = new XYChart.Series<>();
        lineChart.getData().add(series);
        this.getChildren().add(lineChart);
    }

    /**
     * Pauses the animation timer, effectively stopping the graph updates.
     */
    public void pause() {
        timer.stop();
    }

    /**
     * Starts or resumes the animation timer to continue updating the graph.
     */
    public void play() {
        timer.start();
        running = true;
    }

    /**
     * Starts the animation timer and handles periodic updates to the chart based on
     * the spring's current displacement and time.
     */
    private void startAnimation() {
        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                double t = spring.getTime();
                double A = Spring.MAX_DISPLACEMENT;
                double omega = Spring.OMEGA;
                double value = (A * Math.cos(omega * t)) / Spring.PIXELS_PER_METER;

                XYChart.Data<Number, Number> point = new XYChart.Data<>(t, value);
                series.getData().add(point);

                if (series.getData().size() > MAX_POINTS) {
                    series.getData().remove(0);
                }

                String tooltipText = String.format("t = %.2f s\ny = %.2f m", t, value);
                Tooltip tooltip = new Tooltip(tooltipText);
                tooltip.setStyle("-fx-font-size: 12px;");

                Platform.runLater(() -> {
                    if (point.getNode() != null) {
                        point.getNode().setStyle("-fx-background-color: transparent; -fx-padding: 3px;");
                        Tooltip.install(point.getNode(), tooltip);
                    }
                });

                double maxDisp = A / Spring.PIXELS_PER_METER;
                NumberAxis yAxis = (NumberAxis) lineChart.getYAxis();
                yAxis.setLowerBound(-maxDisp);
                yAxis.setUpperBound(maxDisp);

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
