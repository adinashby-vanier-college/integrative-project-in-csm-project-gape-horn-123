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

public class AccelerationGraphPendulum extends Pane implements StartStopControllable {
    private static final int MAX_POINTS = 1000;   //Max number of data points shown on graph
    private static final double WINDOW_SIZE = 10; //Duration of x-axis in seconds

    private LineChart<Number, Number> lineChart; //the chart itself
    private XYChart.Series<Number, Number> series; //data series
    private AnimationTimer timer;  //timer to update graph in real time
    private Pendulum pendulum;    //pendulum object
    private boolean running = true;   //whether the graph is running or not
    private String langCode;    //language for labels


    //Constructor sets up chart and start animation

    public AccelerationGraphPendulum(Pendulum pendulum, String langCode) {
        this.pendulum = pendulum;
        this.langCode = langCode;
        initializeChart(langCode);
        startAnimation();
    }

   // creates and configures the Linechart
    private void initializeChart(String langCode) {
        Locale locale = new Locale(langCode);
        ResourceBundle bundle = ResourceBundle.getBundle("languages.messages", locale);

        //X-axis (time)
        NumberAxis xAxis = new NumberAxis(0, WINDOW_SIZE, 1);
        xAxis.setLabel(bundle.getString("axis.time"));
        xAxis.setAutoRanging(false);

        //y-axis (angular acceleration)
        NumberAxis yAxis = new NumberAxis(-10, 10, 2); // Approx default
        yAxis.setLabel(bundle.getString("axis.angularAcceleration"));

        //setup the line chart
        lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setTitle(bundle.getString("title.pendulumAngularAccelerationVsTime"));
        lineChart.setPrefSize(600, 200);
        lineChart.setCreateSymbols(true);
        lineChart.setLegendVisible(false);

        //Add data series to the the chart
        series = new XYChart.Series<>();
        lineChart.getData().add(series);
        this.getChildren().add(lineChart);
    }

    //Pause the animation
    public void pause() {
        timer.stop();
    }

    //Resume animation
    public void play() {
        timer.start();
        running = true;
    }

    //using animation timer, the graph is updated
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

                //keep only recent max points
                if (series.getData().size() > MAX_POINTS) {
                    series.getData().remove(0);
                }

                //create tooltip showing time and acceleration
                String tooltipText = String.format("t = %.2f s\nα = %.2f rad/s²", t, acceleration);
                Tooltip tooltip = new Tooltip(tooltipText);
                tooltip.setStyle("-fx-font-size: 12px;");

                //apply tooltip to data point
                Platform.runLater(() -> {
                    if (point.getNode() != null) {
                        point.getNode().setStyle("-fx-background-color: transparent; -fx-padding: 3px;");
                        Tooltip.install(point.getNode(), tooltip);
                    }
                });

                // Auto adjust Y-axis range based on current max acceleration
                NumberAxis yAxis = (NumberAxis) lineChart.getYAxis();
                double maxAccel = Math.abs((Pendulum.GRAVITY / lengthMeters)) + 1;
                maxAccel = Math.max(maxAccel, 2);
                yAxis.setLowerBound(-maxAccel);
                yAxis.setUpperBound(maxAccel);
                
                // scrolls x axis as time progresses
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

        // start the animation
        timer.start();
    }
}
