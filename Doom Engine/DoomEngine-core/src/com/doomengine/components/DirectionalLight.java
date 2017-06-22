package com.doomengine.components;

import com.doomengine.math.ColorRGBA;
import com.doomengine.math.Vector3f;

public class DirectionalLight extends BaseLight {

	public DirectionalLight(ColorRGBA color) {
		super(color);
	}

	public Vector3f getLightDirection() {
		return this.getTransform().getTransformedRotation().getForward();
	}
}
