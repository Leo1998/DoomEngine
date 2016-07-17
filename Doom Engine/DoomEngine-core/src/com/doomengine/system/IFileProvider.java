package com.doomengine.system;

import com.doomengine.files.FileHandle;

public interface IFileProvider {

	public FileHandle internal(String path);

	public FileHandle external(String path);

	public FileHandle absolute(String path);

	public String getExternalStoragePath();

}
