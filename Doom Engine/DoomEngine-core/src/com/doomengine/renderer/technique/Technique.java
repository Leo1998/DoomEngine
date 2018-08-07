package com.doomengine.renderer.technique;

import java.util.HashMap;

import com.doomengine.lighting.LightList;
import com.doomengine.math.Camera;
import com.doomengine.renderer.Geometry;
import com.doomengine.renderer.GeometryList;
import com.doomengine.renderer.RenderQueue;
import com.doomengine.renderer.Renderer;
import com.doomengine.shader.Shader;
import com.doomengine.shader.Uniform;
import com.doomengine.shader.VarType;

public abstract class Technique {

	protected Shader shader;
	protected Renderer renderer;

	public Technique(Renderer renderer, Shader shader) {
		this.shader = shader;
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

	protected void clearUniformsSetByCurrentMaterial(Shader shader) {
		HashMap<String, Uniform> uniforms = shader.getUniformMap();
		for (Uniform u : uniforms.values()) {
			u.clearSetByCurrentMaterial();
		}
	}

	protected void resetUniformsNotSetByCurrentMaterial(Shader shader) {
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
		LightList lights = queue.getLightList();

		cam.bind(shader);

		GeometryList list = queue.getOpaqueList();
		for (int i = 0; i < list.size(); i++) {
			Geometry geometry = list.get(i);

			renderGeometry(geometry, lights);
		}

		queue.clear();
	}

	protected abstract void renderGeometry(Geometry geometry, LightList lights);

	public Renderer getRenderer() {
		return renderer;
	}

}
