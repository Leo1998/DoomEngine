package com.doomengine.math;

public class BoundingBox extends BoundingVolume {

	private float xExtend;
	private float yExtend;
	private float zExtend;

	public BoundingBox() {
		super();
	}

	public BoundingBox(Vector3f c, float x, float y, float z) {
		super(c);

		this.xExtend = x;
		this.yExtend = y;
		this.zExtend = z;
	}

	public BoundingBox(Vector3f min, Vector3f max) {
		super();

		setMinMax(min, max);
	}

	@Override
	public boolean intersects(BoundingVolume other) {
		if (other instanceof BoundingBox) {
			return intersectsBoundingBox((BoundingBox) other);
		} else {
			throw new UnsupportedOperationException();
		}
	}

	public boolean intersectsBoundingBox(BoundingBox bb) {
		if (center.getX() + xExtend < bb.center.getX() - bb.xExtend || center.getX() - xExtend > bb.center.getX() + bb.xExtend) {
			return false;
		} else if (center.getY() + yExtend < bb.center.getY() - bb.yExtend || center.getY() - yExtend > bb.center.getY() + bb.yExtend) {
			return false;
		} else if (center.getZ() + zExtend < bb.center.getZ() - bb.zExtend || center.getZ() - zExtend > bb.center.getZ() + bb.zExtend) {
			return false;
		} else {
			return true;
		}
	}

	public void setMinMax(Vector3f min, Vector3f max) {
		this.center.set(max).add(min).div(2);
		xExtend = Math.abs(max.getX() - center.getX());
		yExtend = Math.abs(max.getY() - center.getY());
		zExtend = Math.abs(max.getZ() - center.getZ());
	}

	public float getXExtend() {
		return xExtend;
	}

	public void setXExtend(float xExtend) {
		this.xExtend = xExtend;
	}

	public float getYExtend() {
		return yExtend;
	}

	public void setYExtend(float yExtend) {
		this.yExtend = yExtend;
	}

	public float getZExtend() {
		return zExtend;
	}

	public void setZExtend(float zExtend) {
		this.zExtend = zExtend;
	}

}
