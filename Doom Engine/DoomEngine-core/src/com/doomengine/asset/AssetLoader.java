package com.doomengine.asset;

import java.io.IOException;

public interface AssetLoader {

	/**
	 * Loads asset from the given input stream.
	 *
	 * @return An object.
	 * @throws java.io.IOException
	 */
	public Object load(AssetInfo assetInfo) throws IOException;
}