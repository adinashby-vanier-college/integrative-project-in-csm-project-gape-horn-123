package com.example.physiplay.math;

public class Vector2D {
	
	private final double x, y;
	
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
	
	public double dot(Vector2D other) {
		return this.x * other.x + this.y * other.y;
	}
	
	public double cross(Vector2D other) {
		return this.x * other.y - this.y * other.x;
	}
	
	public double magnitude() {
		return Math.sqrt(this.x * this.x + this.y * this.y);
	}
	
	public Vector2D normal() {
		return this.multiply(1/this.magnitude());
	}
	
	public Vector2D reflect(Vector2D normal) {
		return this.subtract(normal.multiply(2 * this.dot(normal)));
	}
	
	public Vector2D rotate(double radians) {
		return new Vector2D(this.x * Math.cos(radians) - this.y * Math.sin(radians), this.x * Math.sin(radians) + this.y * Math.cos(radians));
	}
	
	public Vector2D project(Vector2D other) {
		return other.multiply(this.dot(other)/other.dot(other));
	}
	
}
