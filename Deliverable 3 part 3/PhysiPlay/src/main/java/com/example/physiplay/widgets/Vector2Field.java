package com.example.physiplay.widgets;

import javafx.beans.value.ChangeListener;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class Vector2Field extends VBox {
    public TextField x = new TextField();
    public TextField y = new TextField();
    private Label title = new Label("Vector2");
    public Vector2Field() {
        title.setStyle("-fx-font-size: 20px");
        numberOnly(x);
        numberOnly(y);
        Label xLabel = new Label("x:"), yLabel = new Label("y:");
        xLabel.setWrapText(false);
        yLabel.setWrapText(false);
        xLabel.setMinWidth(Region.USE_PREF_SIZE);
        xLabel.setPrefWidth(Region.USE_COMPUTED_SIZE);
        xLabel.setMaxWidth(Region.USE_PREF_SIZE);
        yLabel.setMinWidth(Region.USE_PREF_SIZE);
        yLabel.setPrefWidth(Region.USE_COMPUTED_SIZE);
        yLabel.setMaxWidth(Region.USE_PREF_SIZE);
        xLabel.setStyle("-fx-font-size: 20px");
        yLabel.setStyle("-fx-font-size: 20px");
        HBox hbox = new HBox(xLabel, x, yLabel, y);
        hbox.setSpacing(3);
        getChildren().addAll(title, hbox);
    }
    public Vector2Field(String name) {
        this();
        setName(name);
    }
    public void setName(String name) {
        title.setText(name);
    }

    public double getX() {
        return Double.parseDouble(x.getText().isBlank() ? "0" : x.getText());
    }

    public double getY() {
        return Double.parseDouble(y.getText().isBlank() ? "0" : y.getText());
    }
    private void numberOnly(TextField textField){
        ChangeListener<String> numbersOnly = (observableValue, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) textField.setText(newValue.replaceAll("[^\\d]", ""));
        };
        textField.textProperty().addListener(numbersOnly);
    }
}
