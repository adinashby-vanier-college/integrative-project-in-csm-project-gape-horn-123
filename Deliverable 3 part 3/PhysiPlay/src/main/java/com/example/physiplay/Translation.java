package com.example.physiplay;

import com.example.physiplay.singletons.SettingsSingleton;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;

import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;

public class Translation {

    public Translation(){

    }

    public void translate(ArrayList<Label> labelArraylist, ArrayList<Menu> menuArrayList){
        Locale locale = new Locale(SettingsSingleton.getInstance().language);
        ResourceBundle bundle = ResourceBundle.getBundle("languages.messages", locale);
        for (int i = 0; i < labelArraylist.size(); i++) {
            labelArraylist.get(i).setText("");
        }
    }

}
