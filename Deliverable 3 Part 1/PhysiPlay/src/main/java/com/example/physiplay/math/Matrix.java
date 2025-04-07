package com.example.physiplay.math;

public class Matrix {
	
	private double[][] matrix;

	public Matrix(
			double m00, double m01, double m02,
			double m10, double m11, double m12,
			double m20, double m21, double m22) {
		matrix = new double[3][3];
		matrix[0][0] = m00;
		matrix[0][1] = m01;
		matrix[0][2] = m02;
		matrix[1][0] = m10;
		matrix[1][1] = m11;
		matrix[1][2] = m12;
		matrix[2][0] = m20;
		matrix[2][1] = m21;
		matrix[2][2] = m22;
	}
	
	public Matrix(double[][] matrix) {
		if (matrix.length != 3)
			throw new IllegalArgumentException("the amount of rows must be 3");
		for (int i = 0; i < 3; i++) {
			if (matrix[i].length != 3)
				throw new IllegalArgumentException("the amount of columns must be 3");
		}
		this.matrix = matrix;
	}
	
	public static Matrix translationMatrix(double tx, double ty) {
		return new Matrix(
				1, 0, tx, 
				0, 1, ty, 
				0, 0, 1);
	}
	
	public static Matrix scalingMatrix(double sx, double sy) {
		return new Matrix(
				sx, 0, 0, 
				0, sy, 0, 
				0, 0, 1);
	}
	
	public static Matrix rotationMatrix(double radians) {
		return new Matrix(
				Math.cos(radians), -Math.sin(radians), 0, 
				Math.sin(radians), Math.cos(radians), 0, 
				0, 0, 1);
	}

}
