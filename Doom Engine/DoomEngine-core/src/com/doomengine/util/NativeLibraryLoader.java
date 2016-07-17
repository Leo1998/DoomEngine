package com.doomengine.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import com.doomengine.system.DoomException;

public class NativeLibraryLoader {

	public String nativesJar;

	private String uuid = UUID.randomUUID().toString();

	public NativeLibraryLoader() {
	}

	public NativeLibraryLoader(String nativesJar) {
		this();
		this.nativesJar = nativesJar;
	}

	private InputStream readFile(String path) {
		if (nativesJar == null) {
			InputStream input = NativeLibraryLoader.class.getResourceAsStream("/" + path);
			if (input == null)
				throw new DoomException("Unable to read file for extraction: " + path);
			return input;
		}

		try {
			@SuppressWarnings("resource") // TODO fix leak
			ZipFile file = new ZipFile(nativesJar);
			ZipEntry entry = file.getEntry(path);
			if (entry == null)
				throw new DoomException("Couldn't find '" + path + "' in JAR: " + nativesJar);
			return file.getInputStream(entry);
		} catch (IOException ex) {
			throw new DoomException("Error reading '" + path + "' in JAR: " + nativesJar);
		}
	}

	public File extractFile(String sourcePath, String dirName) throws IOException {
		File extractedFile = getExtractedFile(dirName, new File(sourcePath).getName());
		return extractFile(sourcePath, extractedFile);
	}

	private File extractFile(String sourcePath, File extractedFile) throws IOException {
		try {
			InputStream input = readFile(sourcePath);
			extractedFile.getParentFile().mkdirs();
			FileOutputStream output = new FileOutputStream(extractedFile);
			StreamUtils.copyStream(input, output);
			input.close();
			output.close();
		} catch (IOException ex) {
			throw new DoomException("Error extracting file: " + sourcePath + "To: " + extractedFile.getAbsolutePath());
		}

		return extractedFile;
	}

	private File getExtractedFile(String dirName, String fileName) {
		File idealFile = new File(System.getProperty("java.io.tmpdir") + "/doomengine/" + uuid + "/" + dirName, fileName);
		if (FileUtils.canWrite(idealFile))
			return idealFile;

		try {
			File file = File.createTempFile(dirName, null);
			if (file.delete()) {
				file = new File(file, fileName);
				if (FileUtils.canWrite(file))
					return file;
			}
		} catch (IOException ignored) {
		}

		File file = new File(System.getProperty("user.home") + "/.doomengine/" + uuid + "/" + dirName, fileName);
		if (FileUtils.canWrite(file))
			return file;

		file = new File(".temp/" + uuid + "/" + dirName, fileName);
		if (FileUtils.canWrite(file))
			return file;

		return idealFile;
	}

}
