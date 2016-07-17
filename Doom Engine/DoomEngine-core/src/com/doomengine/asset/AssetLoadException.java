package com.doomengine.asset;

public class AssetLoadException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public AssetLoadException(String message) {
		super(message);
	}

	public AssetLoadException(String message, Exception ex) {
		super(message, ex);
	}
}
