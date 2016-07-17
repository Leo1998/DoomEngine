package com.doomengine.system;

import java.net.URL;

import com.doomengine.app.Application;
import com.doomengine.renderer.Renderer;

public interface DoomContext {

	/**
	 * @return The application.
	 */
	public Application getApplication();

	/**
	 * @return The current display settings.
	 */
	public AppSettings getSettings();

	/**
	 * @return An URL pointing to valid AssetConfig file.
	 */
	public URL getAssetConfigURL();

	/**
	 * @return The renderer for this context, or null if not created yet.
	 */
	public Renderer getRenderer();

	/**
	 * @return True if the context has been created but not yet destroyed.
	 */
	public boolean isCreated();

	/**
	 * @return True if the context contains a valid render surface.
	 */
	public boolean isRenderable();

	/**
	 * Creates the context and makes it active.
	 */
	public void create();

	/**
	 * Destroys and then re-creates the context. This should be called after the
	 * display settings have been changed.
	 */
	public void restart();

	/**
	 * Destroys the context completely.
	 */
	public void destroy();

}
