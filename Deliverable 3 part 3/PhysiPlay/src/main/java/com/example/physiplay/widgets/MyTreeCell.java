package com.example.physiplay.widgets;

import com.example.physiplay.SimulationObject;
import com.example.physiplay.controllers.TabController;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TreeCell;

import java.io.IOException;
import java.util.Map;
import java.util.regex.Pattern;

public class MyTreeCell extends TreeCell<String> {
    private final Map<String, SimulationObject> dataMap;
    private TabPane tabPane;
    public MyTreeCell(Map<String, SimulationObject> dataMap, TabPane tabPane) {
        this.tabPane = tabPane;
        this.dataMap = dataMap;
        this.setOnMouseClicked(event -> {
            if (!isEmpty() && getItem() != null) {
                SimulationObject object = dataMap.get(getItem());
                if (object != null) {
                    createTab(object);
                }
            }
        });
    }

    private boolean canAddTab(String id) {
        for (Tab tab : tabPane.getTabs()) {
            if (tab.getId().equals(id)) return false;
        }
        return true;
    }
    private void createTab(SimulationObject target) {
        Tab tab = new Tab(target.name);
        tab.setId(target.uuid);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/gameObjectTab.fxml"));
        loader.setController(new TabController(target));
        try {
            tab.setContent(loader.load());
        }
        catch (IOException e) {
            tab.setContent(new Label("Unable to load tab"));
        }
        if (canAddTab(target.uuid)) {
            tabPane.getTabs().add(tab);
        }
        else tabPane.getSelectionModel().select(tab);

    }
    @Override
    protected void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);
        if (empty || item == null) {
            setText(null);
        }
        else {
            setText(item.split(Pattern.quote(" ("))[0]);
        }
    }
}
