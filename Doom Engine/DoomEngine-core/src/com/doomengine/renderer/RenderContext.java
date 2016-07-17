package com.doomengine.renderer;

import com.doomengine.math.ColorRGBA;
import com.doomengine.shader.Shader;
import com.doomengine.texture.Texture;

public class RenderContext {

	public CullFace cullface;
	public boolean depthTestEnabled;
	public boolean depthWriteEnabled;
	public TestFunc depthFunc;
	public ColorRGBA clearColor;
	public BlendMode blendMode;

	public Shader boundShader;
	public VertexAttributes currentVertexAttribs;

	public Texture[] boundTextures;
	public int boundTextureUnit;

	public int boundFBO;
	public int boundRB;

	public RenderContext() {
		this.reset();
	}

	/**
	 * Reset the RenderContext to default state
	 */
	public void reset() {
		this.cullface = CullFace.Off;
		this.depthTestEnabled = false;
		this.depthWriteEnabled = true;
		this.depthFunc = TestFunc.LessOrEqual;
		this.clearColor = new ColorRGBA(0, 0, 0, 0);
		this.blendMode = BlendMode.Off;

		this.boundShader = null;
		this.currentVertexAttribs = null;

		this.boundTextures = new Texture[16];
		this.boundTextureUnit = 0;

		this.boundFBO = 0;
		this.boundRB = 0;
	}

}
