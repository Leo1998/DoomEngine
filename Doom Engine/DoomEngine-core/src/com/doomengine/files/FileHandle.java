package com.doomengine.files;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

import com.doomengine.system.DoomException;

public class FileHandle {

	/** The file. */
	protected File file;

	/** The FileType. */
	protected FileType type;

	public FileHandle(String fileName) {
		this.file = new File(fileName);
		this.type = FileType.ABSOLUTE;
	}

	public FileHandle(File file) {
		this.file = file;
		this.type = FileType.ABSOLUTE;
	}

	public FileHandle(String fileName, FileType type) {
		this.type = type;
		file = new File(fileName);
	}

	public FileHandle(File file, FileType type) {
		this.file = file;
		this.type = type;
	}

	public String getPath() {
		return file.getPath().replace('\\', '/');
	}

	public String getAbsolutePath() {
		return getFile().getAbsolutePath().replace('\\', '/');
	}

	public String getName() {
		return file.getName();
	}

	public File getFile() {
		if (type == FileType.EXTERNAL)
			return new File(Files.getProvider().getExternalStoragePath(), file.getPath());
		return file;
	}

	public String getExtension() {
		String name = file.getName();
		int dotIndex = name.lastIndexOf('.');
		if (dotIndex == -1)
			return "";
		return name.substring(dotIndex + 1);
	}

	public FileHandle getParentFile() {
		if (type == FileType.INTERNAL)
			throw new DoomException("Cannot return the parent of an internal file: + file");
		return new FileHandle(file.getParentFile());
	}

	public FileHandle child(String name) {
		if (file.getPath().length() == 0)
			return new FileHandle(new File(name), type);
		return new FileHandle(new File(file, name), type);
	}

	public boolean exists() {
		return getFile().exists();
	}

	public void delete() {
		getFile().delete();
	}

	public void mkdirs() {
		if (type == FileType.INTERNAL)
			throw new DoomException("Cannot mkdirs with an internal file: " + file);
		getFile().mkdirs();
	}

	public void createNewFile() {
		if (type == FileType.INTERNAL)
			throw new DoomException("Cannot create new Internal files!");

		try {
			getFile().createNewFile();
		} catch (IOException e) {
			throw new DoomException("Error creating new file: " + file + " (" + type + ")", e);
		}
	}

	public InputStream read() {
		if (type == FileType.INTERNAL) {
			InputStream input = FileHandle.class.getResourceAsStream("/" + getPath());
			if (input == null)
				throw new DoomException("File not found: " + file + " (" + type + ")");
			return input;
		}

		try {
			return new FileInputStream(getFile());
		} catch (Exception ex) {
			if (getFile().isDirectory())
				throw new DoomException("Cannot open a stream to a directory: " + file + " (" + type + ")");
			throw new DoomException("Error reading file: " + file + " (" + type + ")");
		}
	}

	public OutputStream write() {
		if (type == FileType.INTERNAL) {
			throw new DoomException("Cannot write to Internal files!");
		}

		try {
			return new FileOutputStream(getFile());
		} catch (Exception e) {
			if (getFile().isDirectory())
				throw new DoomException("Cannot open a stream to a directory: " + file + " (" + type + ")");
			throw new DoomException("Error writing file: " + file + " (" + type + ")", e);
		}
	}

	public String readString() {
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(read()));
			String result = "";

			String line;
			while ((line = reader.readLine()) != null) {
				result += line += "\n";
			}

			reader.close();

			return result;
		} catch (Exception e) {
			throw new DoomException("Error reading file: " + file + " (" + type + ")", e);
		}
	}

	public void writeString(String string) {
		try {
			Writer writer = new OutputStreamWriter(write());
			writer.write(string);
			writer.close();
		} catch (Exception e) {
			throw new DoomException("Error writing file: " + file + " (" + type + ")", e);
		}
	}

	public FileType getType() {
		return type;
	}

	@Override
	public String toString() {
		return "path: " + getPath() + "  " + "type: " + getType();
	}

}
