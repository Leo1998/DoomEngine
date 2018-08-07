package com.doomengine.math;

public abstract class BoundingVolume {

	protected Vector3f center;

	public BoundingVolume() {
		this.center = new Vector3f();
	}

	public BoundingVolume(Vector3f center) {
		this.center = center;
	}

	public abstract boolean intersects(BoundingVolume other);

	public Vector3f getCenter() {
		return center;
	}

	public void setCenter(Vector3f center) {
		this.center = center;
	}

}
