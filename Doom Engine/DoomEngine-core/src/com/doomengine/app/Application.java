package com.doomengine.app;

import com.doomengine.asset.AssetManager;
import com.doomengine.asset.SimpleAssetManager;
import com.doomengine.renderer.RenderManager;
import com.doomengine.renderer.Renderer;
import com.doomengine.renderer.Viewport;
import com.doomengine.scene.Scene;
import com.doomengine.scene.SceneList;
import com.doomengine.system.AppSettings;
import com.doomengine.system.ContextAllocator;
import com.doomengine.system.DoomContext;
import com.doomengine.system.Input;

public abstract class Application {

	protected DoomContext context;
	protected AppSettings appSettings;
	protected AssetManager assetManager;
	protected RenderManager renderManager;

	private SceneList scenes = new SceneList();

	public Application(ContextAllocator contextAllocator) {
		this(contextAllocator, new AppSettings(true));
	}

	public Application(ContextAllocator contextAllocator, AppSettings appSettings) {
		this.appSettings = appSettings;

		this.context = contextAllocator.allocateContext(this);
	}

	public void start() {
		getContext().create();
	}

	public void create() {
		this.renderManager = new RenderManager(getRenderer());
		this.assetManager = new SimpleAssetManager(context.getAssetConfigURL());
	}

	public void update() {
		Input.update();

		this.scenes.tickAll();
	}

	public void render() {
		getRenderer().newFrame();
		renderManager.render();
	}

	public void resize(int width, int height) {
		renderManager.resize(width, height);
	}

	public void gainFocus() {

	}

	public void loseFocus() {

	}

	public void destroy() {

	}

	public DoomContext getContext() {
		return context;
	}

	public AppSettings getAppSettings() {
		return appSettings;
	}

	public Renderer getRenderer() {
		if (context.isRenderable()) {
			return context.getRenderer();
		}

		return null;
	}

	public RenderManager getRendererManager() {
		return renderManager;
	}

	public AssetManager getAssetManager() {
		return assetManager;
	}

	public Scene addScene(Scene scene) {
		this.scenes.addScene(scene);

		Viewport vp = this.renderManager.createMainView("main_view");
		vp.setClearFlags(true, true, true);

		vp.setScene(scene);

		return scene;
	}

}
