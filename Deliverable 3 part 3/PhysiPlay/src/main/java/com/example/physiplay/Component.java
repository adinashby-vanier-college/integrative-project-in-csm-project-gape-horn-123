package com.example.physiplay;

import com.google.gson.annotations.Expose;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TitledPane;

public abstract class Component
{
    public boolean allowMultipleInstances = false;
    public SimulationObject parent;
    public String componentId;
    public Tab componentTab = new Tab("Component");
    public abstract void Start();
    public abstract void Use();

    public abstract void displayComponent();
    public abstract void Remove();
}
