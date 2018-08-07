package com.doomengine.renderer.technique;

import com.doomengine.lighting.BaseLight;
import com.doomengine.lighting.DirectionalLight;
import com.doomengine.lighting.LightList;
import com.doomengine.lighting.PointLight;
import com.doomengine.material.Material;
import com.doomengine.math.ColorRGBA;
import com.doomengine.math.Vector3f;
import com.doomengine.math.Vector4f;
import com.doomengine.renderer.BlendMode;
import com.doomengine.renderer.Geometry;
import com.doomengine.renderer.Renderer;
import com.doomengine.renderer.TestFunc;
import com.doomengine.shader.Shader;
import com.doomengine.shader.Uniform;
import com.doomengine.shader.VarType;

public class MultiPassLightingTechnique extends Technique {

	private static final ColorRGBA ambient = new ColorRGBA(0.1f, 0.1f, 0.1f, 1.0f);

	public MultiPassLightingTechnique(Renderer renderer) {
		super(renderer, new Shader("multipass.vert", "multipass.frag"));
	}

	protected void renderGeometry(Geometry geometry, LightList lights) {
		shader.getUniform("model").setValue(VarType.Matrix4, geometry.getTransformationMatrix());

		clearUniformsSetByCurrentMaterial(shader);

		Material mat = geometry.getMaterial();
		mat.apply(this);

		resetUniformsNotSetByCurrentMaterial(shader);

		Uniform lightColor = shader.getUniform("g_LightColor");
		Uniform lightPosition = shader.getUniform("g_LightPosition");
		Uniform lightDirection = shader.getUniform("g_LightDirection");
		Uniform ambientLightColor = shader.getUniform("g_AmbientLightColor");

		if (lights.isEmpty()) {
			ambientLightColor.setValue(VarType.Vector4, ambient);
			lightColor.setValue(VarType.Vector4, ColorRGBA.BLACK_NO_ALPHA);

			renderer.bindShader(shader);
			renderer.renderMesh(geometry.getMesh());
		} else {
			BlendMode oldBlendMode = renderer.context.blendMode;

			for (int i = 0; i < lights.size(); i++) {
				BaseLight light = lights.get(i);

				if (i == 0) {
					ambientLightColor.setValue(VarType.Vector4, ambient);
				} else {
					ambientLightColor.setValue(VarType.Vector4, ColorRGBA.BLACK_NO_ALPHA);

					renderer.setBlendMode(BlendMode.AlphaAdditive);
					renderer.setDepthWriteEnabled(false);
					renderer.setDepthFunc(TestFunc.Equal);
				}

				lightColor.setValue(VarType.Vector4, light.getColor());

				if (light instanceof DirectionalLight) {
					DirectionalLight dl = (DirectionalLight) light;
					Vector3f dir = dl.getLightDirection();

					lightPosition.setValue(VarType.Vector4, new Vector4f(dir.getX(), dir.getY(), dir.getZ(), -1));
					lightDirection.setValue(VarType.Vector4, Vector4f.ZERO);
				} else if (light instanceof PointLight) {
					PointLight pl = (PointLight) light;
					Vector3f pos = pl.getLightPosition();
					float radius = pl.getRadius();

					lightPosition.setValue(VarType.Vector4, new Vector4f(pos.getX(), pos.getY(), pos.getZ(), radius));
					lightDirection.setValue(VarType.Vector4, Vector4f.ZERO);
				}

				renderer.bindShader(shader);
				renderer.renderMesh(geometry.getMesh());
			}

			renderer.setBlendMode(oldBlendMode);
			renderer.setDepthWriteEnabled(true);
			renderer.setDepthFunc(TestFunc.LessOrEqual);
		}
	}

}
