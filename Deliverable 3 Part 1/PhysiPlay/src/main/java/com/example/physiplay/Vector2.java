package com.example.physiplay;

public class Vector2 {
    public float x;
    public float y;

    public static Vector2 ZERO = new Vector2(0, 0);
    public Vector2(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public void add(Vector2 v2) {
        this.x += v2.x;
        this.y += v2.y;
    }
}
