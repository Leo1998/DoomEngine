package com.doomengine.lighting;

import com.doomengine.math.ColorRGBA;
import com.doomengine.math.Vector3f;

public class PointLight extends BaseLight {

	private float radius;

	public PointLight(ColorRGBA color, float radius) {
		super(color);

		this.radius = radius;
	}

	public float getRadius() {
		return radius;
	}

	public void setRadius(float radius) {
		this.radius = radius;
	}

	public Vector3f getLightPosition() {
		return this.getTransform().getTransformedPosition();
	}

}
