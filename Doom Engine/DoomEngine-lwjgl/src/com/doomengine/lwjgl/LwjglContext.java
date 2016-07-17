package com.doomengine.lwjgl;

import java.net.URL;

import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.GLContext;

import com.doomengine.app.Application;
import com.doomengine.files.Files;
import com.doomengine.renderer.Renderer;
import com.doomengine.renderer.opengl.GL;
import com.doomengine.renderer.opengl.GLDebugDesktop;
import com.doomengine.renderer.opengl.GLExt;
import com.doomengine.renderer.opengl.GLFbo;
import com.doomengine.renderer.opengl.GLRenderer;
import com.doomengine.system.AppSettings;
import com.doomengine.system.DoomContext;
import com.doomengine.system.DoomException;
import com.doomengine.system.Input;
import com.doomengine.system.Logger;
import com.doomengine.util.OS;

public abstract class LwjglContext implements DoomContext {

	protected boolean created = false;
	protected Application app;
	protected AppSettings settings;
	protected Renderer renderer;
	protected int framerate;

	public LwjglContext(Application app) {
		this.app = app;
		this.settings = app.getAppSettings();
	}

	@Override public Application getApplication() {
		return app;
	}

	@Override public AppSettings getSettings() {
		return settings;
	}

	@Override public URL getAssetConfigURL() {
		return Thread.currentThread().getContextClassLoader().getResource("Desktop.cfg");
	}

	@Override public Renderer getRenderer() {
		return renderer;
	}

	@Override public boolean isCreated() {
		return created;
	}

	@Override public boolean isRenderable() {
		return renderer != null;
	}

	protected ContextAttribs createContextAttribs() {
		boolean openGL3 = false;
		if (settings.getBoolean("GraphicsDebug") || openGL3) {
			ContextAttribs attr;
			if (openGL3) {
				attr = new ContextAttribs(3, 2);
				attr = attr.withProfileCore(true).withForwardCompatible(true).withProfileCompatibility(false);
			} else {
				attr = new ContextAttribs();
			}
			if (settings.getBoolean("GraphicsDebug")) {
				attr = attr.withDebug(true);
			}
			return attr;
		} else {
			return null;
		}
	}

	public void initContextFirstTime() {
		Logger.setAdapter(new LwjglLoggerAdapter());

		if (!GLContext.getCapabilities().OpenGL20) {
			throw new DoomException("OpenGL 2.0 or higher is required for DoomEngine");
		}

		GL gl = new LwjglGL();
		GLExt glext = new LwjglGLExt();
		GLFbo glfbo;

		if (GLContext.getCapabilities().OpenGL30) {
			glfbo = new LwjglGLFboGL3();
		} else {
			glfbo = new LwjglGLFboEXT();
		}

		if (settings.getBoolean("GraphicsDebug")) {
			gl = new GLDebugDesktop(gl, glext, glfbo);
			glext = (GLExt) gl;
			glfbo = (GLFbo) gl;
		}
		this.renderer = new GLRenderer(gl, glext, glfbo);

		this.renderer.setMainFrameBufferSrgb(settings.isGammaCorrection());
		this.renderer.setLinearizeSrgbImages(settings.isGammaCorrection());

		Input.setAdapter(new LwjglInputAdapter());
		Files.setProvider(new LwjglFileProvider());
	}

	protected void loadNatives() {
		if (!OS.isWindows)
			throw new DoomException("Sorry only Windows is supported yet!");

		LwjglNativesLoader.load();
	}
}
