package com.doomengine.lwjgl;

import java.awt.Toolkit;

public class LwjglUtil {

	private LwjglUtil() {
	}

	public static int getFullscreenWidth() {
		return (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
	}

	public static int getFullscreenHeight() {
		return (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();
	}

}
