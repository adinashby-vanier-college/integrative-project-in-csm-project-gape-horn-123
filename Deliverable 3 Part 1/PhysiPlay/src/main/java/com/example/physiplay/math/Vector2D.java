package com.example.physiplay.math;

/**
 * a 2d vector class for physics and math shit
 * 
 * <p>
 * this immutable class provides common vector operations required for 2d physics simulation. shit has arithmetic, projections, rotations, reflection. all operations return a new Vector2D instance to maintain immutability.
 * </p>
 * <h2>example usage:</h2>
 * <pre>
 * Vector2D velocity = new Vector2D(3, 4);
 * Vector2D acceleration = new Vector2D(0, -9.8);
 * Vector2D newVelocity = velocity.add(acceleration.multiply(deltaTime));
 * </pre>
 * <h2>why use this shit? we can just have a mutable vector2d</h2>
 * <p>
 * 1. thread safety - can't fuck shit up, immutable objects are inherently thread-safe. kinda important for multithreaded stuff (javafx!!). also it allows us to make the physics engine multithreaded as well
 * </p>
 * <p>
 * 2. no side effects - super predictable (never changes) and you can't change shit accidentally when you're passing one vector in a method or something
 * </p>
 * <p>
 * 3. fluent chaining - you can do cool shit like this:
 * </p>
 * <pre>
 * Vector2D result = position
 *   .add(velocity.multiply(dt))
 *   .reflect(surfaceNormal)
 *   .rotate(angle);
 * </pre>
 */
public class Vector2D {
	
	private final double x, y;
	
	/**get the x component of this vector
	 * @return the x component*/
	public double getX() {
		return x;
	}
	
	/**get the y component of this vector
	 * @return the y component*/
	public double getY() {
		return y;
	}

	/**zero vector (0,0)*/
	public static final Vector2D ZERO = new Vector2D(0, 0);
	
	/**unit vector (1,1)*/
	public static final Vector2D ONE = new Vector2D(1, 1);
	
	/**x-axis basis vector (1,0)*/
	public static final Vector2D X_AXIS = new Vector2D(1, 0);
	
	/**y-axis basis vector (0,1)*/
	public static final Vector2D Y_AXIS = new Vector2D(0, 1);
	
	/**
	 * creates a new 2d vector
	 * @param x : x-component
	 * @param y : y-component
	 * */
	public Vector2D(double x, double y) {
		this.x = x;
		this.y = y;
	}	
	
	// basic arithmetic operations
	
	/**
	 * vector addition
	 * @param other: vector to add
	 * @return new vector whose components are the sum of the corresponding components of this vector and the other vector
	 * */
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
		return other.multiply(this.dot(other) / other.dot(other));
	}
	
	public double distance(Vector2D start, Vector2D end) {
		return end.subtract(start).magnitude();
	}
	
	public Vector2D perpendicular() {
		return new Vector2D(-this.y, this.x);
	}
	
	public double angleToOtherVector(Vector2D other) {
		return Math.acos(this.dot(other) / (this.magnitude() * other.magnitude()));
	}
}
