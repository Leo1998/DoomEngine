package com.doomengine.desktop.launcher;

import test.Test;

import com.doomengine.lwjgl.LwjglContext;
import com.doomengine.lwjgl.LwjglDisplay;
import com.doomengine.system.AppSettings;
import com.doomengine.system.DoomSystem;

public class DesktopLauncher {

	public static void main(String[] args) {
		AppSettings settings = new AppSettings(true);
		settings.setResizable(true);
		// settings.setVSync(true);
		// settings.setFrameRate(60);
//		settings.setGammaCorrection(true);
		// settings.setFullscreen(true);

		settings.putBoolean("GraphicsDebug", true);

		LwjglContext context = new LwjglDisplay(settings);

		DoomSystem system = new DoomSystem(new Test(), context);
		context.setSystem(system);

		context.create();
	}

}
