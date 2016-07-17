package com.doomengine.shader;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import com.doomengine.system.DoomException;

public class ShaderPreprocessor {

	public static String loadShader(String filename) {
		StringBuilder shaderSource = new StringBuilder();
		BufferedReader shaderReader = null;
		final String INCLUDE_DIRECTIVE = "#include";

		try {
			shaderReader = new BufferedReader(new InputStreamReader(ShaderPreprocessor.class.getResourceAsStream("/engine/shaders/" + filename)));
			String line;

			while ((line = shaderReader.readLine()) != null) {
				if (line.startsWith(INCLUDE_DIRECTIVE)) {
					String newFilename = line.substring(INCLUDE_DIRECTIVE.length() + 2, line.length() - 1);
					shaderSource.append(loadShader(newFilename));
				} else {
					shaderSource.append(line).append("\n");
				}
			}

			shaderReader.close();
		} catch (Exception e) {
			throw new DoomException("Shader could not be loaded!", e);
		}

		return shaderSource.toString();
	}

}
