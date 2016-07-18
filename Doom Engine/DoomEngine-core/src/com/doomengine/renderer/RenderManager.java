package com.doomengine.renderer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.doomengine.math.Camera;
import com.doomengine.math.ColorRGBA;
import com.doomengine.scene.Scene;
import com.doomengine.texture.FrameBuffer;

public class RenderManager {

	private Renderer renderer;
	private ArrayList<Viewport> viewPorts = new ArrayList<Viewport>();

	private Technique defaultTechnique;

	public RenderManager(Renderer renderer) {
		this.renderer = renderer;

		this.defaultTechnique = new Technique(renderer);

		renderer.setCullFace(CullFace.Front);
		renderer.enableDepthTest(true);
		renderer.setDepthFunc(TestFunc.LessOrEqual);
	}

	public Viewport createMainView(String viewName) {
		Viewport vp = new Viewport(viewName);
		viewPorts.add(vp);
		return vp;
	}

	public List<Viewport> getMainViews() {
		return Collections.unmodifiableList(viewPorts);
	}

	public Viewport getMainView(String viewName) {
		for (int i = 0; i < viewPorts.size(); i++) {
			if (viewPorts.get(i).getName().equals(viewName)) {
				return viewPorts.get(i);
			}
		}
		return null;
	}

	public boolean removeMainView(String viewName) {
		for (int i = 0; i < viewPorts.size(); i++) {
			if (viewPorts.get(i).getName().equals(viewName)) {
				viewPorts.remove(i);
				return true;
			}
		}
		return false;
	}

	public boolean removeMainView(Viewport view) {
		return viewPorts.remove(view);
	}

	public void renderViewPort(Viewport vp) {
		if (!vp.isEnabled()) {
			return;
		}

		FrameBuffer fb = vp.getOutputFrameBuffer();
		renderer.bindFramebuffer(fb);

		ColorRGBA clearColor = vp.getBackgroundColor();
		renderer.setClearColor(clearColor.getRed(), clearColor.getGreen(), clearColor.getBlue(), clearColor.getAlpha());
		renderer.clear(vp.isClearColor(), vp.isClearDepth(), vp.isClearStencil());

		Scene scene = vp.getScene();

		if (scene != null) {
			Camera cam = scene.getMainCamera().getCam();
			setupViewport(cam);

			Technique technique = defaultTechnique;
			if (vp.getForcedTechnique() != null)
				technique = vp.getForcedTechnique();

			// NOTE: The Scenes Geometry gets added to the ViewPort's
			// RenderQueue
			scene.render(vp.getQueue());

			technique.render(vp.getQueue(), cam);
		}
	}

	public void render() {
		for (int i = 0; i < viewPorts.size(); i++) {
			Viewport vp = viewPorts.get(i);
			renderViewPort(vp);
		}
	}

	public void resize(int width, int height) {
		for (int i = 0; i < viewPorts.size(); i++) {
			Viewport vp = viewPorts.get(i);
			vp.resizeCam(width, height);
		}
	}

	public void setupViewport(Camera cam) {
		int viewX = (int) (cam.getViewPortLeft() * cam.getWidth());
		int viewY = (int) (cam.getViewPortBottom() * cam.getHeight());
		int viewX2 = (int) (cam.getViewPortRight() * cam.getWidth());
		int viewY2 = (int) (cam.getViewPortTop() * cam.getHeight());
		renderer.setViewPort(viewX, viewY, viewX2, viewY2);
	}

	public Renderer getRenderer() {
		return renderer;
	}

	public Technique getDefaultTechnique() {
		return defaultTechnique;
	}

}
