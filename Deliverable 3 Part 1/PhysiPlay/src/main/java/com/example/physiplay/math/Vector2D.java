package com.example.physiplay.math;

public class Vector2D {
	
	private double x, y;
	
	public Vector2D(double x, double y) {
		this.x = x;
		this.y = y;
	}	
	
	public Vector2D add(Vector2D other) {
		return new Vector2D(this.x + other.x, this.y + other.y);
	}
	
	public Vector2D subtract(Vector2D other) {
		return new Vector2D(this.x - other.x, this.y - other.y);
	}
	
	public Vector2D multiply(double scalar) {
		return new Vector2D(this.x * scalar, this.y * scalar);
	}
	
}
