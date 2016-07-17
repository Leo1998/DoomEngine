package com.doomengine.util;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.Method;
import java.util.UUID;

public class FileUtils {

	public static boolean canWrite(File file) {
		File parent = file.getParentFile();
		File testFile;
		if (file.exists()) {
			if (!file.canWrite() || !canExecute(file))
				return false;
			testFile = new File(parent, UUID.randomUUID().toString());
		} else {
			parent.mkdirs();
			if (!parent.isDirectory())
				return false;
			testFile = file;
		}
		try {
			new FileOutputStream(testFile).close();
			if (!canExecute(testFile))
				return false;
			return true;
		} catch (Throwable ex) {
			return false;
		} finally {
			testFile.delete();
		}
	}

	private static boolean canExecute(File file) {
		try {
			Method canExecute = File.class.getMethod("canExecute");
			if ((Boolean) canExecute.invoke(file))
				return true;

			Method setExecutable = File.class.getMethod("setExecutable", boolean.class, boolean.class);
			setExecutable.invoke(file, true, false);

			return (Boolean) canExecute.invoke(file);
		} catch (Exception ignored) {
		}
		return false;
	}

}
