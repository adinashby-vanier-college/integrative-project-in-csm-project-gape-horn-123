package com.example.physiplay.components;

import com.example.physiplay.Component;
import com.example.physiplay.Vector2;

public class Rigidbody extends Component {
    public Vector2 velocity = Vector2.ZERO;
    public boolean useGravity = false;
    public Vector2 gravity = new Vector2(0, -9.81f);
    @Override
    public void Start() {
        System.out.println("Rigidbody component activated!");
    }

    @Override
    public void Use() {
        parent.position.add(velocity);
    }

    @Override
    public void Remove() {

    }
}
