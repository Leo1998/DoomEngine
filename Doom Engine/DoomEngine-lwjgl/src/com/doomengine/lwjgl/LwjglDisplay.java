package com.doomengine.lwjgl;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.ARBMultisample;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLContext;
import org.lwjgl.opengl.PixelFormat;

import com.doomengine.app.Application;
import com.doomengine.system.AppSettings;
import com.doomengine.system.Logger;
import com.doomengine.util.NanoTimer;
import com.doomengine.util.Timer;

public class LwjglDisplay extends LwjglContext implements Runnable {

	private static final String THREAD_NAME = "DoomEngine";

	private boolean wasActive = false;
	private boolean needRestart = false;
	private PixelFormat pixelFormat;

	public LwjglDisplay(Application app) {
		super(app);
	}

	protected DisplayMode getFullscreenDisplayMode(int width, int height, int bpp, int freq) {
		try {
			DisplayMode[] modes = Display.getAvailableDisplayModes();
			for (DisplayMode mode : modes) {
				if (mode.getWidth() == width && mode.getHeight() == height && (mode.getBitsPerPixel() == bpp || (bpp == 24 && mode.getBitsPerPixel() == 32)) && (mode.getFrequency() == freq || (freq == 60 && mode.getFrequency() == 59))) {
					return mode;
				}
			}
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
		return null;
	}

	protected void createContext(AppSettings settings) throws LWJGLException {
		DisplayMode displayMode;
		if (settings.getWidth() <= 0 || settings.getHeight() <= 0) {
			displayMode = Display.getDesktopDisplayMode();
			settings.setResolution(displayMode.getWidth(), displayMode.getHeight());
		} else if (settings.isFullscreen()) {
			int fullscreenWidth = LwjglUtil.getFullscreenWidth();
			int fullscreenHeight = LwjglUtil.getFullscreenHeight();
			displayMode = getFullscreenDisplayMode(fullscreenWidth, fullscreenHeight, settings.getBitsPerPixel(), settings.getFrequency());
			settings.setResolution(fullscreenWidth, fullscreenHeight);
			if (displayMode == null) {
				throw new RuntimeException("Unable to find fullscreen display mode matching settings");
			}
		} else {
			displayMode = new DisplayMode(settings.getWidth(), settings.getHeight());
		}

		int samples = settings.getSamples();
		PixelFormat pf = new PixelFormat(settings.getBitsPerPixel(), 0, settings.getDepthBits(), settings.getStencilBits(), samples, 0, 0, 0, false);

		framerate = settings.getFrameRate();

		boolean pixelFormatChanged = false;
		if (created && (pixelFormat.getBitsPerPixel() != pf.getBitsPerPixel() || pixelFormat.getDepthBits() != pf.getDepthBits() || pixelFormat.getStencilBits() != pf.getStencilBits() || pixelFormat.getSamples() != pf.getSamples())) {
			renderer.resetContext();
			Display.destroy();
			pixelFormatChanged = true;
		}
		pixelFormat = pf;

		Display.setTitle(settings.getTitle());
		Display.setResizable(settings.isResizable());

		if (displayMode != null) {
			if (settings.isFullscreen()) {
				Display.setDisplayModeAndFullscreen(displayMode);
			} else {
				Display.setFullscreen(false);
				Display.setDisplayMode(displayMode);
			}
		}

		Display.setVSyncEnabled(settings.isVSync());

		if (created && !pixelFormatChanged) {
			Display.releaseContext();
			Display.makeCurrent();
			Display.update();
		}

		if (!created || pixelFormatChanged) {
			ContextAttribs attr = createContextAttribs();
			if (attr != null) {
				Display.create(pixelFormat, attr);
			} else {
				Display.create(pixelFormat);
			}

			if (pixelFormatChanged && pixelFormat.getSamples() > 1 && GLContext.getCapabilities().GL_ARB_multisample) {
				GL11.glEnable(ARBMultisample.GL_MULTISAMPLE_ARB);
			}
		}
	}

	@Override public void create() {
		new Thread(this, THREAD_NAME).start();
	}

	@Override public void restart() {
		needRestart = true;
	}

	@Override public void destroy() {
		try {
			app.destroy();
			renderer.cleanup();
			Display.releaseContext();
			Display.destroy();
		} catch (Exception e) {
			Logger.log("Failed to destroy context!");
		}
	}

	@Override public void run() {
		loadNatives();

		try {
			createContext(settings);
		} catch (LWJGLException e) {
			if (Display.isCreated())
				Display.destroy();
			throw new RuntimeException("Failed to create display!", e);
		}

		initContextFirstTime();

		Logger.log("Running Engine: ");
		Logger.log("LWJGL version: " + org.lwjgl.Sys.getVersion());
		Logger.log("Processor arch: " + System.getProperty("os.arch"));
		Logger.log("\n");

		app.create();

		created = true;

		boolean needClose = false;
		boolean firstRender = true;

		Timer timer = new NanoTimer();
		Timer fpsTimer = new NanoTimer();
		int frames = 0;
		while (true) {
			float deltaTime = timer.getTimeInSeconds();
			timer.reset();

			if (fpsTimer.getTimeInSeconds() >= 1.0f) {
				int fps = frames;
				frames = 0;
				fpsTimer.reset();

				Logger.log("fps: " + fps);
			}

			if (Display.isCloseRequested()) {
				needClose = true;
			}
			if (wasActive != Display.isActive()) {
				if (!wasActive) {
					app.gainFocus();
					wasActive = true;
				} else {
					app.loseFocus();
					wasActive = false;
				}
			}

			app.update(deltaTime);

			app.render();
			Display.update(false);
			Display.sync(framerate);

			Display.processMessages();

			if (needRestart) {
				needRestart = false;
				Logger.log("Restarting Context!");
				try {
					createContext(settings);
				} catch (LWJGLException ex) {
					Logger.log("Failed to set display settings!");
				}
			} else if (Display.wasResized() || firstRender) {
				int newWidth = Display.getWidth();
				int newHeight = Display.getHeight();
				settings.setResolution(newWidth, newHeight);
				app.resize(newWidth, newHeight);
			}

			firstRender = false;
			frames++;

			if (needClose)
				break;
		}

		destroy();
	}

}
