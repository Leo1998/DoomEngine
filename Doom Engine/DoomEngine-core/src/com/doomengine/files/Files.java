package com.doomengine.files;

import com.doomengine.system.IFileProvider;

public class Files {

	private static IFileProvider provider;

	public static void setProvider(IFileProvider p) {
		provider = p;
	}

	public static IFileProvider getProvider() {
		return provider;
	}

	public static FileHandle internal(String path) {
		return provider.internal(path);
	}

	public static FileHandle external(String path) {
		return provider.external(path);
	}

	public static FileHandle absolute(String path) {
		return provider.absolute(path);
	}

}
