package com.doomengine.shader;

import com.doomengine.renderer.Renderer;
import com.doomengine.util.NativeObject;

public class ShaderSource extends NativeObject {

	private ShaderType sourceType;
	private String source;

	/**
	 * The shaders defines, that will be added at the beginning of the source.
	 * Might be NULL!
	 */
	private String defines;

	public ShaderSource(ShaderType type, String source, String defines) {
		this.sourceType = type;
		if (type == null) {
			throw new IllegalArgumentException("The shader type must be specified");
		}

		this.setSource(source);
		this.setDefines(defines);
	}

	@Override
	public void deleteObject() {
		((Renderer) this.rendererObject).deleteShaderSource(this);

		id = -1;
	}

	@Override
	public void resetObject() {
		id = -1;

		setUpdateNeeded();
	}

	public ShaderType getType() {
		return sourceType;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		if (source == null) {
			throw new IllegalArgumentException("Shader source cannot be null");
		}
		this.source = source;
	}

	public String getDefines() {
		return defines;
	}

	public void setDefines(String defines) {
		this.defines = defines;
	}

	@Override
	public String toString() {
		String nameTxt = "";
		if (defines != null)
			nameTxt += "defines, ";

		return getClass().getSimpleName() + "[" + nameTxt + "type=" + sourceType.name() + "]";
	}

}
