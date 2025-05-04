package com.example.physiplay.components;

import com.example.physiplay.NumberOnlyTextField;
import com.example.physiplay.widgets.Vector2Field;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import java.util.*;

public class ComponentPropertyBuilder {
    public Map<String, Node> propertyMap = new LinkedHashMap<>();
    public ComponentPropertyBuilder() {
    }

    private VBox vbox = new VBox();
    public ComponentPropertyBuilder addCheckboxProperty(String propertyName, String text, CheckBox checkbox) {
        checkbox.setText(text);
        propertyMap.put(propertyName, checkbox);
        return this;
    }
    public ComponentPropertyBuilder addColorPickerProperty(String propertyName, String text, ColorPicker picker) {
        picker.setPromptText(text);
        picker.setPrefWidth(Region.USE_COMPUTED_SIZE);
        picker.setPrefHeight(Region.USE_COMPUTED_SIZE);
        picker.setMaxWidth(Region.USE_PREF_SIZE);
        picker.setMaxHeight(Region.USE_PREF_SIZE);
        propertyMap.put(propertyName, picker);
        return this;
    }

    public ComponentPropertyBuilder addLabelProperty(String propertyName, String text, Label label) {
        label.setStyle("-fx-font-size: 14px");
        label.setText(text);
        propertyMap.put(propertyName, label);
        return this;
    }

    public ComponentPropertyBuilder addTextFieldProperty(String propertyName, String title, TextField textField) {
        textField.setPromptText(title);
        propertyMap.put(propertyName, textField);
        return this;
    }

    public ComponentPropertyBuilder addNumberInputFieldProperty(String propertyName, String title, TextField textField) {
        NumberOnlyTextField textField1 = new NumberOnlyTextField();
        textField1.numberOnly(textField);
        textField.setPromptText(title + " (Enter a number)");
        propertyMap.put(propertyName, textField);
        return this;
    }
    public ComponentPropertyBuilder addVector2Property(String propertyName, String title, Vector2Field vector2Field) {
        vector2Field.setName(title);
        propertyMap.put(propertyName, vector2Field);
        return this;
    }

    public Vector2Field getVector2Field(String propertyName) {
        try {
            return (Vector2Field) propertyMap.getOrDefault(propertyName, new Vector2Field("Not found"));
        }
        catch (ClassCastException ignored) {
            return new Vector2Field("Error");
        }
    }

    public Label getLabelField(String propertyName) {
        try {
            return (Label) propertyMap.getOrDefault(propertyName, new Label("Not found"));
        }
        catch (ClassCastException ignored) {
            return new Label("Error");
        }
    }

    public TextField getTextField(String propertyName) {
        try {
            return (TextField) propertyMap.getOrDefault(propertyName, new TextField("Not found"));
        }
        catch (ClassCastException ignored) {
            return new TextField("Error");
        }
    }

    public ColorPicker getColorPicker(String propertyName) {
        try {
            return (ColorPicker) propertyMap.getOrDefault(propertyName, new ColorPicker());
        }
        catch (ClassCastException ignored) {
            return new ColorPicker();
        }
    }
    public CheckBox getCheckBox(String propertyName) {
        try {
            return (CheckBox) propertyMap.getOrDefault(propertyName, new CheckBox());
        }
        catch (ClassCastException ignored) {
            return new CheckBox();
        }
    }
    public VBox getAllProperties() {
        vbox.getChildren().clear();
        vbox.getChildren().addAll(propertyMap.values());
        return vbox;
    }
}
