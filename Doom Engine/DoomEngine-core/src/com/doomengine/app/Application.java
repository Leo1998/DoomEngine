package com.doomengine.app;

import com.doomengine.asset.AssetManager;
import com.doomengine.asset.SimpleAssetManager;
import com.doomengine.math.ShapeFactory;
import com.doomengine.math.Vector2f;
import com.doomengine.renderer.RenderManager;
import com.doomengine.renderer.Renderer;
import com.doomengine.renderer.Viewport;
import com.doomengine.system.AppSettings;
import com.doomengine.system.ContextAllocator;
import com.doomengine.system.DoomContext;
import com.doomengine.system.Input;
import com.doomengine.system.Logger;

public abstract class Application {

	public static Application instance;

	protected DoomContext context;
	protected AppSettings appSettings;
	protected AssetManager assetManager;
	protected RenderManager renderManager;
	protected Viewport viewport;

	public Application(ContextAllocator contextAllocator) {
		this(contextAllocator, new AppSettings(true));
	}

	public Application(ContextAllocator contextAllocator, AppSettings appSettings) {
		if (instance != null) {
			Logger.log("Only one instance should run at a time!!!");
		}
		instance = this;

		this.appSettings = appSettings;

		this.context = contextAllocator.allocateContext(this);
	}

	public void start() {
		getContext().create();
	}

	public void create() {
		this.renderManager = new RenderManager(getRenderer());
		this.assetManager = new SimpleAssetManager(context.getAssetConfigURL());

		ShapeFactory.init(assetManager);

		this.viewport = renderManager.createMainView("default");
		this.viewport.setClearFlags(true, true, false);

		appCreate();
	}

	public void update(float deltaTime) {
		appUpdate(deltaTime);

		Input.update();
	}

	public void render() {
		getRenderer().newFrame();
		renderManager.render();
	}

	public void resize(int width, int height) {
		renderManager.resize(width, height);

		appResize(width, height);
	}

	public void gainFocus() {

	}

	public void loseFocus() {

	}

	public void destroy() {
		appDestroy();
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

	public Vector2f getScreenSize() {
		return new Vector2f(appSettings.getWidth(), appSettings.getHeight());
	}

	public abstract void appCreate();

	public abstract void appUpdate(float deltaTime);

	public abstract void appResize(int width, int height);

	public abstract void appDestroy();

}
