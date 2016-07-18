package com.doomengine.math;

public class Vector4f {

	public static final Vector4f ZERO = new Vector4f(0, 0, 0, 0);

	protected float x;
	protected float y;
	protected float z;
	protected float w;

	public Vector4f() {
		this(ZERO);
	}

	public Vector4f(Vector4f vector) {
		this(vector.getX(), vector.getY(), vector.getZ(), vector.getW());
	}

	public Vector4f(float x, float y, float z, float w) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
	}

	public float length() {
		return (float) Math.sqrt(x * x + y * y + z * z + w * w);
	}

	public Vector4f normalized() {
		float length = length();

		return new Vector4f(x / length, y / length, z / length, w / length);
	}
	
	public Vector4f lerp(Vector4f dest, float lerpFactor) {
		return dest.sub(this).mul(lerpFactor).add(this);
	}

	public Vector4f mul(float r) {
		return new Vector4f(x * r, y * r, z * r, w * r);
	}

	public Vector4f mul(Vector4f r) {
		float w_ = w * r.getW() - x * r.getX() - y * r.getY() - z * r.getZ();
		float x_ = x * r.getW() + w * r.getX() + y * r.getZ() - z * r.getY();
		float y_ = y * r.getW() + w * r.getY() + z * r.getX() - x * r.getZ();
		float z_ = z * r.getW() + w * r.getZ() + x * r.getY() - y * r.getX();

		return new Vector4f(x_, y_, z_, w_);
	}

	public Vector4f mul(Vector3f r) {
		float w_ = -x * r.getX() - y * r.getY() - z * r.getZ();
		float x_ = w * r.getX() + y * r.getZ() - z * r.getY();
		float y_ = w * r.getY() + z * r.getX() - x * r.getZ();
		float z_ = w * r.getZ() + x * r.getY() - y * r.getX();

		return new Vector4f(x_, y_, z_, w_);
	}

	public Vector4f sub(Vector4f r) {
		return new Vector4f(x - r.getX(), y - r.getY(), z - r.getZ(), w - r.getW());
	}

	public Vector4f add(Vector4f r) {
		return new Vector4f(x + r.getX(), y + r.getY(), z + r.getZ(), w + r.getW());
	}

	public float max() {
		return Math.max(x, Math.max(y, Math.max(z, w)));
	}

	public float dot(Vector4f r) {
		return x * r.getX() + y * r.getY() + z * r.getZ() + w * r.getW();
	}

	public Vector4f set(float x, float y, float z, float w) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
		return this;
	}

	public Vector4f set(Vector4f r) {
		set(r.getX(), r.getY(), r.getZ(), r.getW());
		return this;
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	public float getZ() {
		return z;
	}

	public void setZ(float z) {
		this.z = z;
	}

	public float getW() {
		return w;
	}

	public void setW(float w) {
		this.w = w;
	}

	@Override public String toString() {
		return "(" + x + " " + y + " " + z + " " + w + ")";
	}

	public boolean equals(Vector4f r) {
		return x == r.getX() && y == r.getY() && z == r.getZ() && w == r.getW();
	}

}
