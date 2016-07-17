package com.doomengine.renderer.opengl;

import java.nio.IntBuffer;

public interface GL3 extends GL2 {

	public static final int GL_DEPTH_STENCIL_ATTACHMENT = 0x821A;
	public static final int GL_GEOMETRY_SHADER = 0x8DD9;
	public static final int GL_NUM_EXTENSIONS = 0x821D;
	public static final int GL_R8 = 0x8229;
	public static final int GL_R16F = 0x822D;
	public static final int GL_R32F = 0x822E;
	public static final int GL_RG16F = 0x822F;
	public static final int GL_RG32F = 0x8230;
	public static final int GL_RG = 0x8227;
	public static final int GL_RG8 = 0x822B;
	public static final int GL_TEXTURE_SWIZZLE_A = 0x8E45;
	public static final int GL_TEXTURE_SWIZZLE_B = 0x8E44;
	public static final int GL_TEXTURE_SWIZZLE_G = 0x8E43;
	public static final int GL_TEXTURE_SWIZZLE_R = 0x8E42;

	public void glBindFragDataLocation(int param1, int param2, String param3); // GL3+

	public void glBindVertexArray(int param1); // GL3+

	public void glDeleteVertexArrays(IntBuffer arrays); // GL3+

	public void glGenVertexArrays(IntBuffer param1); // GL3+

	public String glGetString(int param1, int param2); // GL3+

}
