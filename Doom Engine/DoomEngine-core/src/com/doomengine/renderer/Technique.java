package com.doomengine.renderer;

import java.util.HashMap;

import com.doomengine.material.Material;
import com.doomengine.math.Camera;
import com.doomengine.shader.Shader;
import com.doomengine.shader.Uniform;
import com.doomengine.shader.VarType;

public class Technique {

	private Shader shader;
	private Renderer renderer;

	public Technique(Renderer renderer) {
		this.shader = new Shader("base.vert", "base.frag");
		this.renderer = renderer;
	}

	public void updateUniformParam(String paramName, VarType type, Object value) {
		Uniform u = shader.getUniform(paramName);

		switch (type) {
		case TextureBuffer:
		case Texture2D:
		case Texture3D:
		case TextureArray:
		case TextureCubeMap:
		case Int:
			u.setValue(VarType.Int, value);
			break;
		default:
			u.setValue(type, value);
			break;
		}
	}

	private void clearUniformsSetByCurrent(Shader shader) {
		HashMap<String, Uniform> uniforms = shader.getUniformMap();
		for (Uniform u : uniforms.values()) {
			u.clearSetByCurrentMaterial();
		}
	}

	private void resetUniformsNotSetByCurrent(Shader shader) {
		HashMap<String, Uniform> uniforms = shader.getUniformMap();
		for (Uniform u : uniforms.values()) {
			if (!u.isSetByCurrentMaterial()) {
				if (u.getName().startsWith("m_")) {
					u.clearValue();
				}
			}
		}
	}

	public void render(RenderQueue queue, Camera cam) {
		cam.bind(shader);

		queue.renderQueue(this, true);
	}

	public void renderGeometry(Geometry geometry) {
		shader.getUniform("model").setValue(VarType.Matrix4, geometry.getTransformationMatrix());

		clearUniformsSetByCurrent(shader);

		Material mat = geometry.getMaterial();
		mat.apply(this);

		resetUniformsNotSetByCurrent(shader);

		renderer.bindShader(shader);
		renderer.renderMesh(geometry.getMesh());

	}

	public Renderer getRenderer() {
		return renderer;
	}

}
