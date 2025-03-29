package com.example.physiplay.widgets;

import javafx.beans.value.ChangeListener;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class Vector2Field extends VBox {
    public TextField x = new TextField();
    public TextField y = new TextField();
    private Label title = new Label("Vector2");
    public Vector2Field() {
        numberOnly(x);
        numberOnly(y);
        HBox hbox = new HBox(new Label("x: "), x, new Label("y: "), y);
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
        return Double.parseDouble(x.getText());
    }

    public double getY() {
        return Double.parseDouble(y.getText());
    }
    private void numberOnly(TextField textField){
        ChangeListener<String> numbersOnly = (observableValue, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) textField.setText(newValue.replaceAll("[^\\d]", ""));
        };
        textField.textProperty().addListener(numbersOnly);
    }
}
