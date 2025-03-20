package com.example.physiplay;

public abstract class Component
{
    public boolean allowMultipleInstances = false;
    public SimulationObject parent;
    public String componentId;

    public abstract void Start();
    public abstract void Use();

    public abstract void Remove();
}
