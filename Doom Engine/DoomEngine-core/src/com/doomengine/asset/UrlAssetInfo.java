package com.doomengine.asset;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class UrlAssetInfo extends AssetInfo {

	private URL url;
	private InputStream in;

	public static UrlAssetInfo create(AssetManager assetManager, AssetKey<?> key, URL url) throws IOException {
		URLConnection conn = url.openConnection();
		conn.setUseCaches(false);
		InputStream in = conn.getInputStream();

		if (in == null) {
			return null;
		} else {
			return new UrlAssetInfo(assetManager, key, url, in);
		}
	}

	private UrlAssetInfo(AssetManager assetManager, AssetKey<?> key, URL url, InputStream in) throws IOException {
		super(assetManager, key);
		this.url = url;
		this.in = in;
	}

	public boolean hasInitialConnection() {
		return in != null;
	}

	@Override public InputStream openStream() {
		if (in != null) {
			InputStream in2 = in;
			in = null;
			return in2;
		} else {
			try {
				URLConnection conn = url.openConnection();
				conn.setUseCaches(false);
				return conn.getInputStream();
			} catch (IOException ex) {
				throw new AssetLoadException("Failed to read URL " + url, ex);
			}
		}
	}
}