package com.wilderness.launcher;

import com.doomengine.app.Application;
import com.doomengine.lwjgl.LwjglDisplay;
import com.doomengine.system.ContextAllocator;
import com.doomengine.system.DoomContext;
import com.wilderness.Game;

public class DesktopLauncher {

	public static void main(String[] args) {
		ContextAllocator allocator = new ContextAllocator() {
			@Override public DoomContext allocateContext(Application app) {
				return new LwjglDisplay(app);
			}
		};

		Application app = new Game(allocator);
		app.start();
	}

}
