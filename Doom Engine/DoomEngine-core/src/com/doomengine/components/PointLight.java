package com.doomengine.components;

import com.doomengine.math.ColorRGBA;
import com.doomengine.shader.Shader;
import com.doomengine.shader.VarType;

public class PointLight extends BaseLight {

	public PointLight(ColorRGBA color, float intensity) {
		super(color, intensity);
	}

	@Override public void apply(Shader shader, String parent) {
		super.apply(shader, parent + "base.");

		shader.getUniform(parent + "position").setValue(VarType.Vector3, this.getTransform().getTransformedPosition());
	}

}
