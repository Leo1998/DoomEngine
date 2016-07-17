package com.doomengine.asset;

import java.io.InputStream;

public abstract class AssetInfo {

	protected AssetManager manager;
	protected AssetKey<?> key;

	public AssetInfo(AssetManager manager, AssetKey<?> key) {
		this.manager = manager;
		this.key = key;
	}

	public AssetKey<?> getKey() {
		return key;
	}

	public AssetManager getManager() {
		return manager;
	}

	public abstract InputStream openStream();

}
