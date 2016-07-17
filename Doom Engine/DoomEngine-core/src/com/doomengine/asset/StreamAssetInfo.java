package com.doomengine.asset;

import java.io.InputStream;

public class StreamAssetInfo extends AssetInfo {

	private boolean alreadyOpened;
	private final InputStream inputStream;

	public StreamAssetInfo(AssetManager assetManager, AssetKey<?> assetKey, InputStream inputStream) {
		super(assetManager, assetKey);
		this.inputStream = inputStream;
	}

	@Override public InputStream openStream() {
		if (alreadyOpened) {
			throw new IllegalStateException("Stream already opened");
		}
		alreadyOpened = true;
		return inputStream;
	}

}
