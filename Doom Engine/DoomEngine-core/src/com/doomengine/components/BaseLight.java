package com.doomengine.components;

import com.doomengine.math.ColorRGBA;
import com.doomengine.scene.GameComponent;
import com.doomengine.shader.Shader;
import com.doomengine.shader.VarType;

public abstract class BaseLight extends GameComponent {

	private ColorRGBA color;

	private float intensity;

	public BaseLight(ColorRGBA color, float intensity) {
		this.color = color;
		this.intensity = intensity;
	}

	public void apply(Shader shader, String parent) {
		shader.getUniform(parent + "color").setValue(VarType.Vector4, getColor());
		shader.getUniform(parent + "intensity").setValue(VarType.Float, getIntensity());
	}

	public ColorRGBA getColor() {
		return color;
	}

	public void setColor(ColorRGBA color) {
		this.color = color;
	}

	public float getIntensity() {
		return intensity;
	}

	public void setIntensity(float intensity) {
		this.intensity = intensity;
	}

}
