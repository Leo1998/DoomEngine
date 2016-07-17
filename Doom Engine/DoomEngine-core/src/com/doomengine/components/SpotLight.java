package com.doomengine.components;

import com.doomengine.math.ColorRGBA;
import com.doomengine.shader.Shader;
import com.doomengine.shader.VarType;

public class SpotLight extends PointLight {

	/** The cutoff in degrees. */
	private float cutoff;

	public SpotLight(ColorRGBA color, float intensity, float cutoff) {
		super(color, intensity);
		this.cutoff = cutoff;
	}

	@Override public void apply(Shader shader, String parent) {
		super.apply(shader, parent + "pointLight.");

		shader.getUniform(parent + "direction").setValue(VarType.Vector3, this.getTransform().getTransformedRotation().getForward());
		shader.getUniform(parent + "cutoff").setValue(VarType.Float, cutoff);
	}

	public float getCutoff() {
		return cutoff;
	}

}
