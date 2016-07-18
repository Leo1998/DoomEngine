package com.wilderness;

import com.doomengine.app.Application;
import com.doomengine.components.Camera3D;
import com.doomengine.components.EulerController;
import com.doomengine.components.GeometryComponent;
import com.doomengine.math.ColorRGBA;
import com.doomengine.math.ShapeFactory;
import com.doomengine.math.Vector3f;
import com.doomengine.scene.GameObject;
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
//		appSettings.setFullscreen(true);
		appSettings.setVSync(true);
		appSettings.setSamples(4);

		return appSettings;
	}

	public Game(ContextAllocator contextAllocator) {
		super(contextAllocator, createAppSettings());
	}

	private Scene scene;

	@Override public void appCreate() {
		this.scene = new Scene();

		GameObject player = new GameObject();
		player.addComponent(new EulerController(2.0f, 0.7f));
		player.addComponent(new Camera3D(this.appSettings.getWidth(), this.appSettings.getHeight(), 70, 0.01f, 1000f));
		scene.addObject(player);

		GameObject box = ShapeFactory.createBox(new Vector3f(0, 0, 3), 1, 1, 1);
		box.getComponent(GeometryComponent.class).getGeometry().getMaterial().setColor("Diffuse", ColorRGBA.GREEN);
		scene.addObject(box);

		GameObject sphere = ShapeFactory.createSphere(new Vector3f(2, 0, 3), 1.5f);
		scene.addObject(sphere);

		this.viewport.setScene(scene);
		this.viewport.setBackgroundColor(ColorRGBA.WHITE);
		this.viewport.setClearFlags(true, true, false);
	}

	@Override public void appUpdate(float deltaTime) {
		scene.update(deltaTime);

		if (Input.getKey(Keys.KEY_LCONTROL) && Input.getKeyDown(Keys.KEY_R)) {
			this.context.restart();
		}
	}

	@Override public void appResize(int width, int height) {

	}

	@Override public void appDestroy() {

	}

}
