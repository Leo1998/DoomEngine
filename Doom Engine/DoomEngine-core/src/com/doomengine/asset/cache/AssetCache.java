package com.doomengine.asset.cache;

import com.doomengine.asset.AssetKey;

public interface AssetCache {

	public <T> void addToCache(AssetKey<T> key, T obj);

	public <T> T getFromCache(AssetKey<T> key);

	public boolean deleteFromCache(AssetKey<?> key);

	public void clearCache();

}
