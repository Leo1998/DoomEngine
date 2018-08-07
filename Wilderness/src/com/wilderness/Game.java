package com.wilderness;

import com.doomengine.app.Application;
import com.doomengine.components.Camera3D;
import com.doomengine.components.EulerController;
import com.doomengine.lighting.DirectionalLight;
import com.doomengine.math.ColorRGBA;
import com.doomengine.math.Vector3f;
import com.doomengine.scene.GameObject;
import com.doomengine.scene.ModelKey;
import com.doomengine.scene.Scene;
import com.doomengine.system.AppSettings;
import com.doomengine.system.ContextAllocator;
import com.doomengine.system.Input;
import com.doomengine.system.Input.Keys;

public class Game extends Application {

	private static AppSettings createAppSettings() {
		AppSettings appSettings = new AppSettings(true);

		appSettings.setResizable(true);
		appSettings.setTitle("Wilderness");
		appSettings.setResolution(800, 600);
		// appSettings.setFullscreen(true);
		appSettings.setVSync(false);
		appSettings.setSamples(4);

		return appSettings;
	}

	public Game(ContextAllocator contextAllocator) {
		super(contextAllocator, createAppSettings());
	}

	private Scene scene;

	@Override
	public void appCreate() {
		this.scene = new Scene();

		GameObject player = new GameObject();
		player.addComponent(new EulerController(2.0f, 0.7f));
		player.addComponent(new Camera3D(this.appSettings.getWidth(), this.appSettings.getHeight(), 70, 0.01f, 1000f));
		player.addComponent(new LightPlacer());
		scene.addObject(player);

		GameObject terrain = assetManager.loadAsset(new ModelKey("/models/terrain/model.obj"));
		scene.addObject(terrain);

		GameObject windmill = assetManager.loadAsset(new ModelKey("/models/windmill/model.obj"));
		scene.addObject(windmill);

		GameObject sun = new GameObject();
		sun.addComponent(new DirectionalLight(ColorRGBA.WHITE));
		sun.getTransform().getPosition().set(0, 500, 100);
		sun.getTransform().lookAt(new Vector3f(0, 0, 0), new Vector3f(0, 0, 1));
		scene.addObject(sun);

		this.viewport.setScene(scene);

		this.viewport.setBackgroundColor(new ColorRGBA(0.55f, 0.88f, 0.98f, 1.0f));
	}

	@Override
	public void appUpdate(float deltaTime) {
		scene.update(deltaTime);

		if (Input.getKey(Keys.KEY_LCONTROL) && Input.getKeyDown(Keys.KEY_R)) {
			this.context.restart();
		}
	}

	@Override
	public void appResize(int width, int height) {

	}

	@Override
	public void appDestroy() {

	}

}
