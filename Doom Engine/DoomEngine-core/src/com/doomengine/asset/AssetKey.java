package com.doomengine.asset;

import java.util.LinkedList;

import com.doomengine.system.Logger;

public class AssetKey<T> {

	protected String name;
	protected transient String folder;
	protected transient String extension;

	public AssetKey(String name) {
		this.name = reducePath(name);
		this.extension = getExtension(this.name);
	}

	private static String getExtension(String name) {
		int idx = name.lastIndexOf('.');

		if (idx <= 0 || idx == name.length() - 1) {
			return "";
		} else {
			return name.substring(idx + 1).toLowerCase();
		}
	}

	private static String reducePath(String path) {
		if (path == null || path.indexOf("./") == -1) {
			return path;
		}
		String[] parts = path.split("/");
		LinkedList<String> list = new LinkedList<String>();
		for (int i = 0; i < parts.length; i++) {
			String string = parts[i];
			if (string.length() == 0 || string.equals(".")) {
				// do nothing
			} else if (string.equals("..")) {
				if (list.size() > 0 && !list.getLast().equals("..")) {
					list.removeLast();
				} else {
					list.add("..");
					Logger.log("Asset path is outside assetmanager root");
				}
			} else {
				list.add(string);
			}
		}
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < list.size(); i++) {
			String string = list.get(i);
			if (i != 0) {
				builder.append("/");
			}
			builder.append(string);
		}
		return builder.toString();
	}

	private static String getFolder(String name) {
		int idx = name.lastIndexOf('/');
		if (idx <= 0 || idx == name.length() - 1) {
			return "";
		} else {
			return name.substring(0, idx + 1);
		}
	}

	public String getFolder() {
		if (folder == null)
			folder = getFolder(name);

		return folder;
	}

	public String getName() {
		return name;
	}

	public String getExtension() {
		return extension;
	}

	@Override public boolean equals(Object other) {
		if (!(other instanceof AssetKey)) {
			return false;
		}
		return name.equals(((AssetKey<?>) other).name);
	}

	@Override public int hashCode() {
		return name.hashCode();
	}

	@Override public String toString() {
		return name;
	}

}
