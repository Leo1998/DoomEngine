package com.doomengine.renderer.technique;

import com.doomengine.lighting.LightList;
import com.doomengine.material.Material;
import com.doomengine.renderer.Geometry;
import com.doomengine.renderer.Renderer;
import com.doomengine.shader.Shader;
import com.doomengine.shader.VarType;

public class UnshadedTechnique extends Technique {

	public UnshadedTechnique(Renderer renderer) {
		super(renderer, new Shader("unshaded.vert", "unshaded.frag"));
	}

	protected void renderGeometry(Geometry geometry, LightList lights) {
		shader.getUniform("model").setValue(VarType.Matrix4, geometry.getTransformationMatrix());

		clearUniformsSetByCurrentMaterial(shader);

		Material mat = geometry.getMaterial();
		mat.apply(this);

		resetUniformsNotSetByCurrentMaterial(shader);

		renderer.renderMesh(geometry.getMesh());
	}

}
