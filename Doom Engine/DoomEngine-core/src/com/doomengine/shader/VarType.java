package com.doomengine.shader;

public enum VarType {

	Float("float"),

	Vector2("vec2"),

	Vector3("vec3"),

	Vector4("vec4"),

	Boolean("bool"),

	Matrix3(true, false, "mat3"),

	Matrix4(true, false, "mat4"),

	TextureBuffer(false, true, "sampler1D|sampler1DShadow"),

	Texture2D(false, true, "sampler2D|sampler2DShadow"),

	Texture3D(false, true, "sampler3D"),

	TextureArray(false, true, "sampler2DArray"),

	TextureCubeMap(false, true, "samplerCube"),

	Int("int");

	private boolean usesMultiData = false;
	private boolean textureType = false;
	private String glslType;

	VarType(String glslType) {
		this.glslType = glslType;
	}

	VarType(boolean multiData, boolean textureType, String glslType) {
		usesMultiData = multiData;
		this.textureType = textureType;
		this.glslType = glslType;
	}

	public boolean isTextureType() {
		return textureType;
	}

	public boolean usesMultiData() {
		return usesMultiData;
	}

	public String getGlslType() {
		return glslType;
	}
}
