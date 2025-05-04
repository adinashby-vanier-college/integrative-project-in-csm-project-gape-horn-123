package com.example.physiplay;

import com.example.physiplay.singletons.SettingsSingleton;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;

import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;

public class Translation {

    String langCode = "en";

    public Translation(String langCode){
        this.langCode = langCode;
    }

    public void translate(ArrayList<Label> labelArraylist, ArrayList<Menu> menuArrayList, ArrayList<MenuItem> menuItemArrayList){
        Locale locale = new Locale(langCode);
        ResourceBundle bundle = ResourceBundle.getBundle("languages.messages", locale);
        for (int i = 0; i < labelArraylist.size(); i++) {
            labelArraylist.get(i).setText(bundle.getString("label." + labelArraylist.get(i).getId().substring(5).toLowerCase()));
        }
        for (int i = 0; i < menuArrayList.size(); i++) {
            menuArrayList.get(i).setText(bundle.getString("menu." + menuArrayList.get(i).getId().substring(4).toLowerCase()));
        }
        for (int i = 0; i < menuItemArrayList.size(); i++) {
            menuItemArrayList.get(i).setText(bundle.getString("menuItem.") + menuItemArrayList.get(i).getId().substring(8).toLowerCase());
        }

    }

}
