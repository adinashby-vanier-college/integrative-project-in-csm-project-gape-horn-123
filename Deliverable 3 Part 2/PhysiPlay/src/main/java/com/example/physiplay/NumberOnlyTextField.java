package com.example.physiplay;

import javafx.beans.value.ChangeListener;
import javafx.scene.control.TextField;

public class NumberOnlyTextField {

    public NumberOnlyTextField(){
    }

    /**
     *
     * @param textField targeted textField that needs to be number only
     * ChangeListener for when typing
     * If the new value contains something else than numbers then it replaces it with nothing
     * Add listener to the targeted textField, when text is changed
     *
     */
    public void numberOnly(TextField textField){
        ChangeListener<String> numbersOnly = (observableValue, oldValue, newValue) -> {
            if (!newValue.matches("\\d*(\\.\\d*)?")) textField.setText(oldValue);
        };
        textField.textProperty().addListener(numbersOnly);
    }

}
