package com.doomengine.renderer;

import com.doomengine.shader.Shader;
import com.doomengine.shader.ShaderSource;
import com.doomengine.texture.FrameBuffer;
import com.doomengine.texture.Texture;

public abstract class Renderer {

	protected final RenderContext context;

	/**
	 * This MUST be invoked by the superclass!
	 */
	public Renderer() {
		this.context = new RenderContext();
	}

	public abstract void newFrame();

	public abstract void resetContext();

	public abstract void cleanup();

	public abstract void setCullFace(CullFace cullface);

	public abstract void enableDepthTest(boolean flag);

	public abstract void setDepthFunc(TestFunc depthfunc);

	public abstract void setDepthWriteEnabled(boolean flag);

	public abstract void setClearColor(float r, float g, float b, float a);

	public abstract void clear(boolean color, boolean depth, boolean stencil);

	public abstract void setViewPort(int x, int y, int w, int h);

	public abstract void setBlendMode(BlendMode mode);

	// ---------------------------------------
	// Textures
	// ---------------------------------------

	public abstract void deleteTexture(Texture texture);

	public abstract void bindTexture(Texture texture, int unit);

	// ---------------------------------------
	// Framebuffers
	// ---------------------------------------

	public abstract void copyFrameBuffer(FrameBuffer src, FrameBuffer dst);

	public abstract void copyFrameBuffer(FrameBuffer src, FrameBuffer dst, boolean copyDepth);

	public abstract void deleteFramebuffer(FrameBuffer framebuffer);

	public abstract void bindFramebuffer(FrameBuffer framebuffer);

	// ---------------------------------------
	// Vertex and Index Data
	// ---------------------------------------

	// public abstract int genVertexArray();
	//
	// public abstract void deleteVertexArray(int handle);
	//
	// public abstract void bindVertexArray(int handle);

	public abstract void renderMesh(Mesh mesh);

	public abstract void deleteVertexBuffer(VertexBuffer buffer);

	public abstract void bindVertexBuffer(VertexBuffer buffer);

	// ---------------------------------------
	// Shaders
	// ---------------------------------------

	public abstract void bindShader(Shader shader);

	public abstract void deleteShader(Shader shader);

	public abstract void deleteShaderSource(ShaderSource source);

	// ---------------------------------------
	// Vertex Attributes
	// ---------------------------------------

	public abstract void setVertexAttributes(VertexAttributes attributes);

	public abstract void clearVertexAttributes();

	// ---------------------------------------
	// GammaCorrection
	// ---------------------------------------

	public abstract void setMainFrameBufferSrgb(boolean enableSrgb);

	public abstract void setLinearizeSrgbImages(boolean linearize);

}
