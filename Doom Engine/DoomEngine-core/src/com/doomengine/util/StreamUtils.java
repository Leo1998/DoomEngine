package com.doomengine.util;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class StreamUtils {

	public static void closeQuietly(Closeable c) {
		if (c != null)
			try {
				c.close();
			} catch (Exception e) {
				// ignore
			}
	}

	public static void copyStream(InputStream input, OutputStream output) throws IOException {
		copyStream(input, output, 4096);
	}

	public static void copyStream(InputStream input, OutputStream output, int bufferSize) throws IOException {
		byte[] buffer = new byte[bufferSize];
		int bytesRead;
		while ((bytesRead = input.read(buffer)) != -1) {
			output.write(buffer, 0, bytesRead);
		}
	}

	public static byte[] copyStreamToByteArray(InputStream input) throws IOException {
		return copyStreamToByteArray(input, input.available());
	}

	public static byte[] copyStreamToByteArray(InputStream input, int estimatedSize) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream(Math.max(0, estimatedSize));
		copyStream(input, baos);
		return baos.toByteArray();
	}

}
