package com.doomengine.asset.cache;

import java.util.concurrent.ConcurrentHashMap;

import com.doomengine.asset.AssetKey;

public class SimpleAssetCache implements AssetCache {

	private final ConcurrentHashMap<AssetKey<?>, Object> keyToAssetMap = new ConcurrentHashMap<AssetKey<?>, Object>();

	@Override
	public <T> void addToCache(AssetKey<T> key, T obj) {
		keyToAssetMap.put(key, obj);
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> T getFromCache(AssetKey<T> key) {
		return (T) keyToAssetMap.get(key);
	}

	@Override
	public boolean deleteFromCache(AssetKey<?> key) {
		return keyToAssetMap.remove(key) != null;
	}

	@Override
	public void clearCache() {
		keyToAssetMap.clear();
	}

}
