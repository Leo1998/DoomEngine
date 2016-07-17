package com.doomengine.renderer.opengl;

public interface GL4 extends GL3 {

	public static final int GL_TESS_CONTROL_SHADER = 0x8E88;
	public static final int GL_TESS_EVALUATION_SHADER = 0x8E87;
	public static final int GL_PATCHES = 0xE;

	public void glPatchParameter(int count);

}
