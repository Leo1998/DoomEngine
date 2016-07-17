package com.doomengine.renderer.opengl;

import java.nio.ByteBuffer;

public interface GL2 extends GL {

	public static final int GL_ALPHA8 = 0x803C;
	public static final int GL_ALPHA_TEST = 0xBC0;
	public static final int GL_BGR = 0x80E0;
	public static final int GL_BGRA = 0x80E1;
	public static final int GL_COMPARE_R_TO_TEXTURE = 0x884E;
	public static final int GL_DEPTH_COMPONENT24 = 0x81A6;
	public static final int GL_DEPTH_COMPONENT32 = 0x81A7;
	public static final int GL_DEPTH_TEXTURE_MODE = 0x884B;
	public static final int GL_DOUBLEBUFFER = 0xC32;
	public static final int GL_DRAW_BUFFER = 0xC01;
	public static final int GL_FILL = 0x1B02;
	public static final int GL_GENERATE_MIPMAP = 0x8191;
	public static final int GL_INTENSITY = 0x8049;
	public static final int GL_LINE = 0x1B01;
	public static final int GL_LUMINANCE8 = 0x8040;
	public static final int GL_LUMINANCE8_ALPHA8 = 0x8045;
	public static final int GL_MAX_ELEMENTS_INDICES = 0x80E9;
	public static final int GL_MAX_ELEMENTS_VERTICES = 0x80E8;
	public static final int GL_MAX_FRAGMENT_UNIFORM_COMPONENTS = 0x8B49;
	public static final int GL_MAX_VERTEX_UNIFORM_COMPONENTS = 0x8B4A;
	public static final int GL_READ_BUFFER = 0xC02;
	public static final int GL_RGB8 = 0x8051;
	public static final int GL_STACK_OVERFLOW = 0x503;
	public static final int GL_STACK_UNDERFLOW = 0x504;
	public static final int GL_TEXTURE_3D = 0x806F;
	public static final int GL_POINT_SPRITE = 0x8861;
	public static final int GL_TEXTURE_COMPARE_FUNC = 0x884D;
	public static final int GL_TEXTURE_COMPARE_MODE = 0x884C;
	public static final int GL_TEXTURE_WRAP_R = 0x8072;
	public static final int GL_VERTEX_PROGRAM_POINT_SIZE = 0x8642;
	public static final int GL_UNSIGNED_INT_8_8_8_8 = 0x8035;

	public void glAlphaFunc(int func, float ref);

	public void glPointSize(float size);

	public void glPolygonMode(int face, int mode);

	public void glDrawBuffer(int mode);

	public void glReadBuffer(int mode);

	public void glCompressedTexImage3D(int target, int level, int internalformat, int width, int height, int depth, int border, ByteBuffer data);

	public void glCompressedTexSubImage3D(int target, int level, int xoffset, int yoffset, int zoffset, int width, int height, int depth, int format, ByteBuffer data);

	public void glTexImage3D(int target, int level, int internalFormat, int width, int height, int depth, int border, int format, int type, ByteBuffer data);

	public void glTexSubImage3D(int target, int level, int xoffset, int yoffset, int zoffset, int width, int height, int depth, int format, int type, ByteBuffer data);

}
