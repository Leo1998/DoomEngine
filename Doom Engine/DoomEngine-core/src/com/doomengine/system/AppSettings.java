package com.doomengine.system;

import java.util.HashMap;

public final class AppSettings extends HashMap<String, Object> {
	private static final long serialVersionUID = 1L;

	private static final AppSettings defaults = new AppSettings(false);

	static {
		defaults.put("Width", 800);
		defaults.put("Height", 600);
		defaults.put("BitsPerPixel", 24);
		defaults.put("Frequency", 60);
		defaults.put("DepthBits", 24);
		defaults.put("StencilBits", 0);
		defaults.put("Samples", 0);
		defaults.put("Fullscreen", false);
		defaults.put("Title", "DoomEngine");
		defaults.put("VSync", false);
		defaults.put("FrameRate", -1);
		defaults.put("MinHeight", 0);
		defaults.put("MinWidth", 0);
		defaults.put("GammaCorrection", false);
		defaults.put("Resizable", false);
	}

	public AppSettings(boolean loadDefaults) {
		if (loadDefaults) {
			putAll(defaults);
		}
	}

	public void copyFrom(AppSettings other) {
		this.putAll(other);
	}

	public int getInteger(String key) {
		Integer i = (Integer) get(key);
		if (i == null) {
			return 0;
		}

		return i.intValue();
	}

	public boolean getBoolean(String key) {
		Boolean b = (Boolean) get(key);
		if (b == null) {
			return false;
		}

		return b.booleanValue();
	}

	public String getString(String key) {
		String s = (String) get(key);
		if (s == null) {
			return null;
		}

		return s;
	}

	public float getFloat(String key) {
		Float f = (Float) get(key);
		if (f == null) {
			return 0f;
		}

		return f.floatValue();
	}

	public void putInteger(String key, int value) {
		put(key, Integer.valueOf(value));
	}

	public void putBoolean(String key, boolean value) {
		put(key, Boolean.valueOf(value));
	}

	public void putString(String key, String value) {
		put(key, value);
	}

	public void putFloat(String key, float value) {
		put(key, Float.valueOf(value));
	}

	public int getWidth() {
		return getInteger("Width");
	}

	public void setWidth(int value) {
		putInteger("Width", value);
	}

	public int getHeight() {
		return getInteger("Height");
	}

	public void setHeight(int value) {
		putInteger("Height", value);
	}

	public int getMinWidth() {
		return getInteger("MinWidth");
	}

	public void setMinWidth(int value) {
		putInteger("MinWidth", value);
	}

	public int getMinHeight() {
		return getInteger("MinHeight");
	}

	public void setMinHeight(int value) {
		putInteger("MinHeight", value);
	}

	public void setResolution(int width, int height) {
		setWidth(width);
		setHeight(height);
	}

	public void setMinResolution(int width, int height) {
		setMinWidth(width);
		setMinHeight(height);
	}

	public int getBitsPerPixel() {
		return getInteger("BitsPerPixel");
	}

	public void setBitsPerPixel(int value) {
		putInteger("BitsPerPixel", value);
	}

	public int getFrequency() {
		return getInteger("Frequency");
	}

	public void setFrequency(int value) {
		putInteger("Frequency", value);
	}

	public int getDepthBits() {
		return getInteger("DepthBits");
	}

	public void setDepthBits(int value) {
		putInteger("DepthBits", value);
	}

	public int getFrameRate() {
		return getInteger("FrameRate");
	}

	public void setFrameRate(int frameRate) {
		putInteger("FrameRate", frameRate);
	}

	public int getStencilBits() {
		return getInteger("StencilBits");
	}

	public void setStencilBits(int value) {
		putInteger("StencilBits", value);
	}

	public int getSamples() {
		return getInteger("Samples");
	}

	public void setSamples(int value) {
		putInteger("Samples", value);
	}

	public String getTitle() {
		return getString("Title");
	}

	public void setTitle(String title) {
		putString("Title", title);
	}

	public boolean isVSync() {
		return getBoolean("VSync");
	}

	public void setVSync(boolean value) {
		putBoolean("VSync", value);
	}

	public boolean isFullscreen() {
		return getBoolean("Fullscreen");
	}

	public void setFullscreen(boolean value) {
		putBoolean("Fullscreen", value);
	}

	public boolean isResizable() {
		return getBoolean("Resizable");
	}

	public void setResizable(boolean value) {
		putBoolean("Resizable", value);
	}

	public boolean isGammaCorrection() {
		return getBoolean("GammaCorrection");
	}

	public void setGammaCorrection(boolean value) {
		putBoolean("GammaCorrection", value);
	}

}
