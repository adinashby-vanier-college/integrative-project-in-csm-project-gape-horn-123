package com.example.physiplay.components;

import com.example.physiplay.Component;
import com.example.physiplay.Vector2;
import com.example.physiplay.singletons.SimulationManager;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;


public class Renderer extends Component {
    public Color color = Color.rgb(255, 0, 0);
    public Vector2 size = new Vector2(50, 50);
    private GraphicsContext gc = SimulationManager.getInstance().gc;
    @Override
    public void Start() {
        System.out.println("Renderer activated!");
    }

    @Override
    public void Use() {
        gc.setFill(color);

        gc.fillRect(parent.getWorldPosition().x, parent.getWorldPosition().y, size.x, size.y);
    }

    @Override
    public void Remove() {

    }
}
