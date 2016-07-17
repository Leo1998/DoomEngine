package com.doomengine.asset;

public interface AssetLocator {

	/**
	 * Request to locate an asset.
	 * 
	 * @param manager
	 * @param key
	 * @return The {@link AssetInfo} that was located, or null if not found.
	 */
	public AssetInfo locate(AssetManager manager, AssetKey<?> key);
}