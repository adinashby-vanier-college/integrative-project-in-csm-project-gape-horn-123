package main.java.com.example.physiplay;

import javafx.beans.value.ChangeListener;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.util.ArrayList;

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

    public void numberOnly(ArrayList<TextField> textField){

        for (int i = 0; i < textField.size(); i++) {
            TextField txtfld = textField.get(i);
            ChangeListener<String> numbersOnly = (observableValue, oldValue, newValue) -> {
                if (!newValue.matches("\\d*(\\.\\d*)?")) txtfld.setText(oldValue);
            };
            txtfld.textProperty().addListener(numbersOnly);
        }


    }

}
