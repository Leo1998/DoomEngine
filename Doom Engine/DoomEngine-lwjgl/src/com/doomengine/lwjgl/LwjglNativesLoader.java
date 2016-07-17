package com.doomengine.lwjgl;

import java.io.File;

import com.doomengine.system.DoomException;
import com.doomengine.util.NativeLibraryLoader;
import com.doomengine.util.OS;

public class LwjglNativesLoader {

	private static final boolean disableAudio = true;

	public static void load() {
		NativeLibraryLoader loader = new NativeLibraryLoader();
		File nativesDir = null;
		try {
			if (OS.isWindows) {
				nativesDir = loader.extractFile(OS.is64Bit ? "lwjgl64.dll" : "lwjgl.dll", null).getParentFile();
				if (!disableAudio)
					loader.extractFile(OS.is64Bit ? "OpenAL64.dll" : "OpenAL32.dll", nativesDir.getName());
			}
			// } else if (OS.isMac) {
			// nativesDir = loader.extractFile("liblwjgl.jnilib",
			// null).getParentFile();
			// if (!disableAudio)
			// loader.extractFile("openal.dylib", nativesDir.getName());
			// } else if (OS.isLinux) {
			// nativesDir = loader.extractFile(OS.is64Bit ? "liblwjgl64.so" :
			// "liblwjgl.so", null).getParentFile();
			// if (!disableAudio)
			// loader.extractFile(OS.is64Bit ? "libopenal64.so" :
			// "libopenal.so", nativesDir.getName());
			// }
		} catch (Exception e) {
			throw new DoomException("Unable to extract LWJGL natives.");
		}
		System.setProperty("org.lwjgl.librarypath", nativesDir.getAbsolutePath());
	}

}
