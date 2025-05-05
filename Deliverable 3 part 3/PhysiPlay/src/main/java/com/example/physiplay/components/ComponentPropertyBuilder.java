package com.example.physiplay.components;

import com.example.physiplay.NumberOnlyTextField;
import com.example.physiplay.widgets.Vector2Field;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import java.util.*;

/**
 * A utility class for building and managing UI form fields (properties)
 * used for configuring simulation components.
 */
public class ComponentPropertyBuilder {

    /** A map storing the property fields by their name. */
    public Map<String, Node> propertyMap = new LinkedHashMap<>();

    /** Container for holding all added properties visually. */
    private VBox vbox = new VBox();

    /** Default constructor. */
    public ComponentPropertyBuilder() {}

    /**
     * Adds a checkbox property to the form.
     *
     * @param propertyName the unique identifier of the property
     * @param text the label text shown beside the checkbox
     * @param checkbox the CheckBox instance to add
     * @return this builder instance for chaining
     */
    public ComponentPropertyBuilder addCheckboxProperty(String propertyName, String text, CheckBox checkbox) {
        checkbox.setText(text);
        propertyMap.put(propertyName, checkbox);
        return this;
    }

    /**
     * Adds a color picker property to the form.
     *
     * @param propertyName the unique identifier of the property
     * @param text the prompt text to display in the color picker
     * @param picker the ColorPicker instance to add
     * @return this builder instance for chaining
     */
    public ComponentPropertyBuilder addColorPickerProperty(String propertyName, String text, ColorPicker picker) {
        picker.setPromptText(text);
        picker.setPrefWidth(Region.USE_COMPUTED_SIZE);
        picker.setPrefHeight(Region.USE_COMPUTED_SIZE);
        picker.setMaxWidth(Region.USE_PREF_SIZE);
        picker.setMaxHeight(Region.USE_PREF_SIZE);
        propertyMap.put(propertyName, picker);
        return this;
    }

    /**
     * Adds a label property to the form.
     *
     * @param propertyName the unique identifier of the property
     * @param text the text to display in the label
     * @param label the Label instance to add
     * @return this builder instance for chaining
     */
    public ComponentPropertyBuilder addLabelProperty(String propertyName, String text, Label label) {
        label.setStyle("-fx-font-size: 14px");
        label.setText(text);
        propertyMap.put(propertyName, label);
        return this;
    }

    /**
     * Adds a plain text field to the form.
     *
     * @param propertyName the unique identifier of the property
     * @param title the placeholder text shown inside the text field
     * @param textField the TextField instance to add
     * @return this builder instance for chaining
     */
    public ComponentPropertyBuilder addTextFieldProperty(String propertyName, String title, TextField textField) {
        textField.setPromptText(title);
        propertyMap.put(propertyName, textField);
        return this;
    }

    /**
     * Adds a numeric-only text field to the form.
     *
     * @param propertyName the unique identifier of the property
     * @param title the placeholder or prompt text
     * @param textField the TextField instance to configure as numeric
     * @return this builder instance for chaining
     */
    public ComponentPropertyBuilder addNumberInputFieldProperty(String propertyName, String title, TextField textField) {
        NumberOnlyTextField textField1 = new NumberOnlyTextField();
        textField1.numberOnly(textField);
        textField.setPromptText(title + " (Enter a number)");
        propertyMap.put(propertyName, textField);
        return this;
    }

    /**
     * Adds a 2D vector input field to the form.
     *
     * @param propertyName the unique identifier of the property
     * @param title the label shown on the vector field
     * @param vector2Field the Vector2Field instance to add
     * @return this builder instance for chaining
     */
    public ComponentPropertyBuilder addVector2Property(String propertyName, String title, Vector2Field vector2Field) {
        vector2Field.setName(title);
        propertyMap.put(propertyName, vector2Field);
        return this;
    }

    /**
     * Retrieves a {@link Vector2Field} from the property map.
     *
     * @param propertyName the name of the property to retrieve
     * @return the Vector2Field or a fallback instance if not found or invalid
     */
    public Vector2Field getVector2Field(String propertyName) {
        try {
            return (Vector2Field) propertyMap.getOrDefault(propertyName, new Vector2Field("Not found"));
        } catch (ClassCastException ignored) {
            return new Vector2Field("Error");
        }
    }

    /**
     * Retrieves a {@link Label} from the property map.
     *
     * @param propertyName the name of the property to retrieve
     * @return the Label or a fallback instance if not found or invalid
     */
    public Label getLabelField(String propertyName) {
        try {
            return (Label) propertyMap.getOrDefault(propertyName, new Label("Not found"));
        } catch (ClassCastException ignored) {
            return new Label("Error");
        }
    }

    /**
     * Retrieves a {@link TextField} from the property map.
     *
     * @param propertyName the name of the property to retrieve
     * @return the TextField or a fallback instance if not found or invalid
     */
    public TextField getTextField(String propertyName) {
        try {
            return (TextField) propertyMap.getOrDefault(propertyName, new TextField("Not found"));
        } catch (ClassCastException ignored) {
            return new TextField("Error");
        }
    }

    /**
     * Retrieves a {@link ColorPicker} from the property map.
     *
     * @param propertyName the name of the property to retrieve
     * @return the ColorPicker or a fallback instance if not found or invalid
     */
    public ColorPicker getColorPicker(String propertyName) {
        try {
            return (ColorPicker) propertyMap.getOrDefault(propertyName, new ColorPicker());
        } catch (ClassCastException ignored) {
            return new ColorPicker();
        }
    }

    /**
     * Retrieves a {@link CheckBox} from the property map.
     *
     * @param propertyName the name of the property to retrieve
     * @return the CheckBox or a fallback instance if not found or invalid
     */
    public CheckBox getCheckBox(String propertyName) {
        try {
            return (CheckBox) propertyMap.getOrDefault(propertyName, new CheckBox());
        } catch (ClassCastException ignored) {
            return new CheckBox();
        }
    }

    /**
     * Returns a VBox containing all the property nodes added so far.
     *
     * @return a VBox of all property UI elements
     */
    public VBox getAllProperties() {
        vbox.getChildren().clear();
        vbox.getChildren().addAll(propertyMap.values());
        return vbox;
    }
}
