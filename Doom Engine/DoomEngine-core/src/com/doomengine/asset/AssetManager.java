package com.doomengine.asset;

import com.doomengine.asset.cache.AssetCache;

public interface AssetManager {

	public void registerLoader(Class<? extends AssetLoader> loaderClass, String... extensions);

	public void unregisterLoader(Class<? extends AssetLoader> loaderClass);

	public void registerLocator(Class<? extends AssetLocator> locatorClass);

	public void unregisterLocator(Class<? extends AssetLocator> locatorClass);

	public AssetInfo locateAsset(AssetKey<?> key);

	public <T> T loadAsset(AssetKey<T> key);

	public AssetCache getCache();

	public void clearCache();

	public boolean hasCache();

}
