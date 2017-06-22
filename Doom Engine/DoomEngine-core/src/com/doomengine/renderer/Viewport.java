package com.doomengine.renderer;

import com.doomengine.math.ColorRGBA;
import com.doomengine.renderer.technique.Technique;
import com.doomengine.scene.Scene;
import com.doomengine.texture.FrameBuffer;

public class Viewport {

	// TODO: Replace with List of GameObjects to render
	private Scene scene;
	private final String name;
	private boolean enabled = true;
	private Technique forcedTechnique = null;
	private boolean clearColor = false;
	private boolean clearDepth = false;
	private boolean clearStencil = false;
	private final RenderQueue queue = new RenderQueue();

	private ColorRGBA backgroundColor = ColorRGBA.BLACK;
	private FrameBuffer out = null;

	public Viewport(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public Scene getScene() {
		return scene;
	}

	public void setScene(Scene scene) {
		this.scene = scene;
	}

	public ColorRGBA getBackgroundColor() {
		return backgroundColor;
	}

	public void setBackgroundColor(ColorRGBA backgroundColor) {
		this.backgroundColor = backgroundColor;
	}

	public boolean isClearColor() {
		return clearColor;
	}

	public void setClearColor(boolean clearColor) {
		this.clearColor = clearColor;
	}

	public boolean isClearDepth() {
		return clearDepth;
	}

	public void setClearDepth(boolean clearDepth) {
		this.clearDepth = clearDepth;
	}

	public boolean isClearStencil() {
		return clearStencil;
	}

	public void setClearStencil(boolean clearStencil) {
		this.clearStencil = clearStencil;
	}

	public void setClearFlags(boolean color, boolean depth, boolean stencil) {
		this.clearColor = color;
		this.clearDepth = depth;
		this.clearStencil = stencil;
	}

	public FrameBuffer getOutputFrameBuffer() {
		return out;
	}

	public void setOutputFrameBuffer(FrameBuffer out) {
		this.out = out;
	}

	public void resizeCam(int width, int height) {
		if (scene != null && scene.getMainCamera() != null) {
			scene.getMainCamera().getCam().resize(width, height);
		}
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public Technique getForcedTechnique() {
		return forcedTechnique;
	}

	public void setForcedTechnique(Technique forcedTechnique) {
		this.forcedTechnique = forcedTechnique;
	}

	public RenderQueue getQueue() {
		return queue;
	}

}
