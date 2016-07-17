package com.doomengine.shader;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import com.doomengine.renderer.Renderer;
import com.doomengine.util.NativeObject;

public class Shader extends NativeObject {

	/**
	 * True, if the shader is strict.
	 * 
	 * HINT: it's always better to keep this at false, it might cause problems
	 */
	public boolean strict = false;

	private List<ShaderSource> sources = new ArrayList<ShaderSource>();

	private HashMap<String, Uniform> uniforms = new HashMap<String, Uniform>();
	private HashMap<String, Attribute> attribs = new HashMap<String, Attribute>();

	public Shader(String vertexFile, String fragmentFile) {
		String vertexSource = ShaderPreprocessor.loadShader(vertexFile);
		String fragmentSource = ShaderPreprocessor.loadShader(fragmentFile);

		addShaderSource(ShaderType.VertexShader, vertexSource, null);
		addShaderSource(ShaderType.FragmentShader, fragmentSource, null);
	}

	@Override public void deleteObject() {
		((Renderer) this.rendererObject).deleteShader(this);

		id = -1;
	}

	@Override public void resetObject() {
		id = -1;

		for (ShaderSource source : sources) {
			source.resetObject();
		}

		setUpdateNeeded();
	}

	public Uniform getUniform(String name) {
		Uniform uniform = uniforms.get(name);
		if (uniform == null) {
			uniform = new Uniform();
			uniform.setName(name);
			uniforms.put(name, uniform);
		}
		return uniform;
	}

	public HashMap<String, Attribute> getAttributeMap() {
		return attribs;
	}

	public Attribute getAttribute(String name) {
		Attribute attrib = attribs.get(name);
		if (attrib == null) {
			attrib = new Attribute();
			attrib.setName(name);
			attribs.put(name, attrib);
		}
		return attrib;
	}

	public HashMap<String, Uniform> getUniformMap() {
		return uniforms;
	}

	public void resetLocations() {
		if (uniforms != null) {
			for (Uniform uniform : uniforms.values()) {
				uniform.resetLocation();
			}
		}
		if (attribs != null) {
			for (Attribute attrib : attribs.values()) {
				attrib.resetLocation();
			}
		}
	}

	public void addShaderSource(ShaderType type, String source, String defines) {
		ShaderSource shaderSource = new ShaderSource(type, source, defines);
		this.addShaderSource(shaderSource);
	}

	public void addShaderSource(ShaderSource source) {
		sources.add(source);
	}

	public List<ShaderSource> getSources() {
		return Collections.unmodifiableList(sources);
	}

}
