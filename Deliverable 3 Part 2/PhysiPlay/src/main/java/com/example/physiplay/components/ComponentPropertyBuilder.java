package com.example.physiplay.components;

import com.example.physiplay.NumberOnlyTextField;
import com.example.physiplay.widgets.Vector2Field;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.util.*;

public class ComponentPropertyBuilder {
    public Map<String, Node> propertyMap = new HashMap<>();
    public ComponentPropertyBuilder() {
        System.out.println("Component builder initiated!");
    }

    public ComponentPropertyBuilder addCheckboxProperty(String propertyName, String text, CheckBox checkbox) {
        checkbox.setText(text);
        propertyMap.put(propertyName, checkbox);
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

    public TextField getTextField(String propertyName) {
        try {
            return (TextField) propertyMap.getOrDefault(propertyName, new TextField("Not found"));
        }
        catch (ClassCastException ignored) {
            return new TextField("Error");
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
        VBox vbox = new VBox();
        vbox.getChildren().addAll(propertyMap.values());
        return vbox;
    }
}
