package com.doomengine.lwjgl;

import com.doomengine.files.FileHandle;
import com.doomengine.files.FileType;
import com.doomengine.system.IFileProvider;

public class LwjglFileProvider implements IFileProvider {

	@Override public FileHandle internal(String path) {
		return new FileHandle(path, FileType.INTERNAL);
	}

	@Override public FileHandle external(String path) {
		return new FileHandle(path, FileType.EXTERNAL);
	}

	@Override public FileHandle absolute(String path) {
		return new FileHandle(path, FileType.ABSOLUTE);
	}

	@Override public String getExternalStoragePath() {
		return "";
	}

}
