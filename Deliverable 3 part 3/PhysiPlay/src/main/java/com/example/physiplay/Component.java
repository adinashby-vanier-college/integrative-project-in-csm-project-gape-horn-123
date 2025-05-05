package com.example.physiplay;

import javafx.scene.control.Tab;

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
