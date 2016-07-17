package com.doomengine.asset;

public class AssetNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public AssetNotFoundException(String message) {
		super(message);
	}

	public AssetNotFoundException(String message, Exception ex) {
		super(message, ex);
	}
}
