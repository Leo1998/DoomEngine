package com.doomengine.components;

import com.doomengine.math.ColorRGBA;
import com.doomengine.shader.Shader;
import com.doomengine.shader.VarType;

public class DirectionalLight extends BaseLight {

	public DirectionalLight(ColorRGBA color, float intensity) {
		super(color, intensity);
	}

	@Override public void apply(Shader shader, String parent) {
		super.apply(shader, parent + "base.");

		shader.getUniform(parent + "direction").setValue(VarType.Vector3, this.getTransform().getTransformedRotation().getForward());
	}

}
