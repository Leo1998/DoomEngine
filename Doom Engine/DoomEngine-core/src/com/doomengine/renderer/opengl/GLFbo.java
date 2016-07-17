package com.doomengine.renderer.opengl;

import java.nio.IntBuffer;

public interface GLFbo {

	public static final int GL_COLOR_ATTACHMENT0_EXT = 0x8CE0;
	public static final int GL_COLOR_ATTACHMENT1_EXT = 0x8CE1;
	public static final int GL_COLOR_ATTACHMENT2_EXT = 0x8CE2;
	public static final int GL_COLOR_ATTACHMENT3_EXT = 0x8CE3;
	public static final int GL_COLOR_ATTACHMENT4_EXT = 0x8CE4;
	public static final int GL_COLOR_ATTACHMENT5_EXT = 0x8CE5;
	public static final int GL_COLOR_ATTACHMENT6_EXT = 0x8CE6;
	public static final int GL_COLOR_ATTACHMENT7_EXT = 0x8CE7;
	public static final int GL_COLOR_ATTACHMENT8_EXT = 0x8CE8;
	public static final int GL_COLOR_ATTACHMENT9_EXT = 0x8CE9;
	public static final int GL_COLOR_ATTACHMENT10_EXT = 0x8CEA;
	public static final int GL_COLOR_ATTACHMENT11_EXT = 0x8CEB;
	public static final int GL_COLOR_ATTACHMENT12_EXT = 0x8CEC;
	public static final int GL_COLOR_ATTACHMENT13_EXT = 0x8CED;
	public static final int GL_COLOR_ATTACHMENT14_EXT = 0x8CEE;
	public static final int GL_COLOR_ATTACHMENT15_EXT = 0x8CEF;
	public static final int GL_DEPTH_ATTACHMENT_EXT = 0x8D00;
	public static final int GL_DRAW_FRAMEBUFFER_BINDING_EXT = 0x8CA6;
	public static final int GL_DRAW_FRAMEBUFFER_EXT = 0x8CA9;
	public static final int GL_FRAMEBUFFER_ATTACHMENT_OBJECT_NAME_EXT = 0x8CD1;
	public static final int GL_FRAMEBUFFER_ATTACHMENT_OBJECT_TYPE_EXT = 0x8CD0;
	public static final int GL_FRAMEBUFFER_COMPLETE_EXT = 0x8CD5;
	public static final int GL_FRAMEBUFFER_EXT = 0x8D40;
	public static final int GL_FRAMEBUFFER_INCOMPLETE_ATTACHMENT_EXT = 0x8CD6;
	public static final int GL_FRAMEBUFFER_INCOMPLETE_DIMENSIONS_EXT = 0x8CD9;
	public static final int GL_FRAMEBUFFER_INCOMPLETE_DRAW_BUFFER_EXT = 0x8CDB;
	public static final int GL_FRAMEBUFFER_INCOMPLETE_FORMATS_EXT = 0x8CDA;
	public static final int GL_FRAMEBUFFER_INCOMPLETE_MISSING_ATTACHMENT_EXT = 0x8CD7;
	public static final int GL_FRAMEBUFFER_INCOMPLETE_MULTISAMPLE_EXT = 0x8D56;
	public static final int GL_FRAMEBUFFER_INCOMPLETE_READ_BUFFER_EXT = 0x8CDC;
	public static final int GL_FRAMEBUFFER_UNSUPPORTED_EXT = 0x8CDD;
	public static final int GL_INVALID_FRAMEBUFFER_OPERATION_EXT = 0x506;
	public static final int GL_MAX_COLOR_ATTACHMENTS_EXT = 0x8CDF;
	public static final int GL_MAX_RENDERBUFFER_SIZE_EXT = 0x84E8;
	public static final int GL_READ_FRAMEBUFFER_BINDING_EXT = 0x8CAA;
	public static final int GL_READ_FRAMEBUFFER_EXT = 0x8CA8;
	public static final int GL_RENDERBUFFER_EXT = 0x8D41;

	public void glBindFramebufferEXT(int param1, int param2);

	public void glBindRenderbufferEXT(int param1, int param2);

	public void glBlitFramebufferEXT(int srcX0, int srcY0, int srcX1, int srcY1, int dstX0, int dstY0, int dstX1, int dstY1, int mask, int filter);

	public int glCheckFramebufferStatusEXT(int param1);

	public void glDeleteFramebuffersEXT(IntBuffer param1);

	public void glDeleteRenderbuffersEXT(IntBuffer param1);

	public void glFramebufferRenderbufferEXT(int param1, int param2, int param3, int param4);

	public void glFramebufferTexture2DEXT(int param1, int param2, int param3, int param4, int param5);

	public void glGenFramebuffersEXT(IntBuffer param1);

	public void glGenRenderbuffersEXT(IntBuffer param1);

	public void glGenerateMipmapEXT(int param1);

	public void glRenderbufferStorageEXT(int param1, int param2, int param3, int param4);

	public void glRenderbufferStorageMultisampleEXT(int target, int samples, int internalformat, int width, int height);

}
