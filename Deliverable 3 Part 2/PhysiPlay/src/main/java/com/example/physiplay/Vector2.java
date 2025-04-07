package com.example.physiplay;

import java.util.Objects;

public class Vector2 {
    public double x;
    public double y;

    public static Vector2 ZERO = new Vector2(0, 0);
    public Vector2(double x, double y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vector2 vector2 = (Vector2) o;
        return Double.compare(x, vector2.x) == 0 && Double.compare(y, vector2.y) == 0;
    }

    @Override
    public String toString() {
        return "Vector2{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    public void add(Vector2 v2) {
        this.x += v2.x;
        this.y += v2.y;
    }
}
