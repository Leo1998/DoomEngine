package com.wilderness;

import com.doomengine.app.Application;
import com.doomengine.math.ColorRGBA;
import com.doomengine.system.AppSettings;
import com.doomengine.system.ContextAllocator;

public class Game extends Application {

	private static AppSettings createAppSettings() {
		AppSettings appSettings = new AppSettings(true);

		appSettings.setResizable(true);
		appSettings.setTitle("Wilderness");
		appSettings.setResolution(800, 600);

		return appSettings;
	}

	public Game(ContextAllocator contextAllocator) {
		super(contextAllocator, createAppSettings());
	}

	@Override public void appCreate() {
		this.viewport.setBackgroundColor(ColorRGBA.WHITE);
	}

	@Override public void appUpdate(float deltaTime) {

	}

	@Override public void appResize(int width, int height) {

	}

	@Override public void appDestroy() {

	}

}
