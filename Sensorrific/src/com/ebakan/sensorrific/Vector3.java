package com.ebakan.sensorrific;

public class Vector3 {
	public double x, y, z;
	public Vector3() {
		this(0.0, 0.0, 0.0);
	}
	public Vector3(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	public double magnitude() {
		return Math.sqrt(x*x + y*y + z*z);
	}
	public void normalize() {
		double mag = magnitude();
		x /= mag;
		y /= mag;
		z /= mag;
	}
	public Vector3 cross(Vector3 vect) {
		Vector3 out = new Vector3();
		out.x = y*vect.z + z*vect.y;
		out.y = z*vect.x + x*vect.z;
		out.z = x*vect.y + y*vect.x;
		return out;
	}
	public double dot(Vector3 vect) {
		return x*vect.x + y*vect.y + z*vect.z;
	}
}
