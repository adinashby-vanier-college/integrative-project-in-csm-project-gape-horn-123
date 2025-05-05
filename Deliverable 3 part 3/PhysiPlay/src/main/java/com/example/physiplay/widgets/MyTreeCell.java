package com.example.physiplay.widgets;

import com.example.physiplay.Component;
import com.example.physiplay.SimulationObject;
import com.example.physiplay.controllers.TabController;
import com.example.physiplay.singletons.SimulationManager;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;

import java.io.IOException;
import java.util.Map;
import java.util.regex.Pattern;

public class MyTreeCell extends TreeCell<String> {
    private final Map<String, SimulationObject> dataMap;
    private ContextMenu contextMenu = new ContextMenu();
    private MenuItem deleteMenuItem = new MenuItem("Delete");
    private TabPane tabPane;

    private void addContextMenu() {
        contextMenu.getItems().add(deleteMenuItem);
    }
    public MyTreeCell(Map<String, SimulationObject> dataMap, TabPane tabPane) {
        this.tabPane = tabPane;
        this.dataMap = dataMap;
        addContextMenu();
        this.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                if (!isEmpty() && getItem() != null) {
                    SimulationObject object = dataMap.get(getItem());
                    if (object != null) {
                        createTab(object);
                    }
                }
                contextMenu.hide();
            }
            else if (event.getButton() == MouseButton.SECONDARY) {
                contextMenu.show(this.getParent(), event.getScreenX(), event.getScreenY());
                deleteInstance();
            }
        });
    }

    private void deleteInstance() {
        deleteMenuItem.setOnAction(event -> {
            contextMenu.hide();
            if (!isEmpty() && getItem() != null) {
                SimulationObject object = dataMap.get(getItem());
                if (object != null) {
                    for (Component c : object.getComponents()) {
                        c.Remove();
                    }
                    TreeItem<String> item = this.getTreeItem();
                    TreeItem<String> parent = item.getParent();
                    if (parent != null) {
                        parent.getChildren().remove(item);
                        if (getTabById(object.uuid) != null)
                            tabPane.getTabs().remove(getTabById(object.uuid));
                        for (Component c: object.getComponents()) {
                            c.Remove();
                        }
                        SimulationManager.getInstance().dataMap.remove( object.name + " (" + object.uuid + ")");
                        SimulationManager.getInstance().simulationObjectList.remove(object);
                    }
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

    private Tab getTabById(String id) {
        for (Tab tab : tabPane.getTabs()) {
            if (tab.getId().equals(id)) return tab;
        }
        return null;
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
