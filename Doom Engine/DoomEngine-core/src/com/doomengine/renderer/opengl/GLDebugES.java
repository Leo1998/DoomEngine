package com.doomengine.renderer.opengl;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

public class GLDebugES extends GLDebug implements GL, GLExt, GLFbo {

	private GLFbo glfbo;
	private GLExt glext;

	public GLDebugES(GL gl, GLExt glext, GLFbo glfbo) {
		this.gl = gl;
		this.glext = glext;
		this.glfbo = glfbo;
	}

	@Override
	public void glActiveTexture(int texture) {
		gl.glActiveTexture(texture);
		checkError();
	}

	@Override
	public void glAttachShader(int program, int shader) {
		gl.glAttachShader(program, shader);
		checkError();
	}

	@Override
	public void glBindBuffer(int target, int buffer) {
		gl.glBindBuffer(target, buffer);
		checkError();
	}

	@Override
	public void glBindTexture(int target, int texture) {
		gl.glBindTexture(target, texture);
		checkError();
	}

	@Override
	public void glBlendFunc(int sfactor, int dfactor) {
		gl.glBlendFunc(sfactor, dfactor);
		checkError();
	}

	@Override
	public void glBufferData(int target, FloatBuffer data, int usage) {
		gl.glBufferData(target, data, usage);
		checkError();
	}

	@Override
	public void glBufferData(int target, ShortBuffer data, int usage) {
		gl.glBufferData(target, data, usage);
		checkError();
	}

	@Override
	public void glBufferData(int target, ByteBuffer data, int usage) {
		gl.glBufferData(target, data, usage);
		checkError();
	}

	@Override
	public void glBufferSubData(int target, long offset, FloatBuffer data) {
		gl.glBufferSubData(target, offset, data);
		checkError();
	}

	@Override
	public void glBufferSubData(int target, long offset, ShortBuffer data) {
		gl.glBufferSubData(target, offset, data);
		checkError();
	}

	@Override
	public void glBufferSubData(int target, long offset, ByteBuffer data) {
		gl.glBufferSubData(target, offset, data);
		checkError();
	}

	@Override
	public void glClear(int mask) {
		gl.glClear(mask);
		checkError();
	}

	@Override
	public void glClearColor(float red, float green, float blue, float alpha) {
		gl.glClearColor(red, green, blue, alpha);
		checkError();
	}

	@Override
	public void glColorMask(boolean red, boolean green, boolean blue, boolean alpha) {
		gl.glColorMask(red, green, blue, alpha);
		checkError();
	}

	@Override
	public void glCompileShader(int shader) {
		gl.glCompileShader(shader);
		checkError();
	}

	@Override
	public void glCompressedTexImage2D(int target, int level, int internalformat, int width, int height, int border, ByteBuffer data) {
		gl.glCompressedTexImage2D(target, level, internalformat, width, height, border, data);
		checkError();
	}

	@Override
	public void glCompressedTexSubImage2D(int target, int level, int xoffset, int yoffset, int width, int height, int format, ByteBuffer data) {
		gl.glCompressedTexSubImage2D(target, level, xoffset, yoffset, width, height, format, data);
		checkError();
	}

	@Override
	public int glCreateProgram() {
		int program = gl.glCreateProgram();
		checkError();
		return program;
	}

	@Override
	public int glCreateShader(int shaderType) {
		int shader = gl.glCreateShader(shaderType);
		checkError();
		return shader;
	}

	@Override
	public void glCullFace(int mode) {
		gl.glCullFace(mode);
		checkError();
	}

	@Override
	public void glDeleteBuffers(IntBuffer buffers) {
		gl.glDeleteBuffers(buffers);
		checkError();
	}

	@Override
	public void glDeleteProgram(int program) {
		gl.glDeleteProgram(program);
		checkError();
	}

	@Override
	public void glDeleteShader(int shader) {
		gl.glDeleteShader(shader);
		checkError();
	}

	@Override
	public void glDeleteTextures(IntBuffer textures) {
		gl.glDeleteTextures(textures);
		checkError();
	}

	@Override
	public void glDepthFunc(int func) {
		gl.glDepthFunc(func);
		checkError();
	}

	@Override
	public void glDepthMask(boolean flag) {
		gl.glDepthMask(flag);
		checkError();
	}

	@Override
	public void glDepthRange(double nearVal, double farVal) {
		gl.glDepthRange(nearVal, farVal);
		checkError();
	}

	@Override
	public void glDetachShader(int program, int shader) {
		gl.glDetachShader(program, shader);
		checkError();
	}

	@Override
	public void glDisable(int cap) {
		gl.glDisable(cap);
		checkError();
	}

	@Override
	public void glDisableVertexAttribArray(int index) {
		gl.glDisableVertexAttribArray(index);
		checkError();
	}

	@Override
	public void glDrawArrays(int mode, int first, int count) {
		gl.glDrawArrays(mode, first, count);
		checkError();
	}

	@Override
	public void glDrawRangeElements(int mode, int start, int end, int count, int type, long indices) {
		gl.glDrawRangeElements(mode, start, end, count, type, indices);
		checkError();
	}

	@Override
	public void glEnable(int cap) {
		gl.glEnable(cap);
		checkError();
	}

	@Override
	public void glEnableVertexAttribArray(int index) {
		gl.glEnableVertexAttribArray(index);
		checkError();
	}

	@Override
	public void glGenBuffers(IntBuffer buffers) {
		gl.glGenBuffers(buffers);
		checkError();
	}

	@Override
	public void glGenTextures(IntBuffer textures) {
		gl.glGenTextures(textures);
		checkError();
	}

	@Override
	public int glGetAttribLocation(int program, String name) {
		int location = gl.glGetAttribLocation(program, name);
		checkError();
		return location;
	}

	@Override
	public void glGetBoolean(int pname, ByteBuffer params) {
		gl.glGetBoolean(pname, params);
		checkError();
	}

	@Override
	public int glGetError() {
		// No need to check for error here? Haha
		return gl.glGetError();
	}

	@Override
	public void glGetInteger(int pname, IntBuffer params) {
		gl.glGetInteger(pname, params);
		checkError();
	}

	@Override
	public void glGetProgram(int program, int pname, IntBuffer params) {
		gl.glGetProgram(program, pname, params);
		checkError();
	}

	@Override
	public String glGetProgramInfoLog(int program, int maxSize) {
		String infoLog = gl.glGetProgramInfoLog(program, maxSize);
		checkError();
		return infoLog;
	}

	@Override
	public void glGetShader(int shader, int pname, IntBuffer params) {
		gl.glGetShader(shader, pname, params);
		checkError();
	}

	@Override
	public String glGetShaderInfoLog(int shader, int maxSize) {
		String infoLog = gl.glGetShaderInfoLog(shader, maxSize);
		checkError();
		return infoLog;
	}

	@Override
	public String glGetString(int name) {
		String string = gl.glGetString(name);
		checkError();
		return string;
	}

	@Override
	public int glGetUniformLocation(int program, String name) {
		int location = gl.glGetUniformLocation(program, name);
		checkError();
		return location;
	}

	@Override
	public boolean glIsEnabled(int cap) {
		boolean enabled = gl.glIsEnabled(cap);
		checkError();
		return enabled;
	}

	@Override
	public void glLineWidth(float width) {
		gl.glLineWidth(width);
		checkError();
	}

	@Override
	public void glLinkProgram(int program) {
		gl.glLinkProgram(program);
		checkError();
	}

	@Override
	public void glPixelStorei(int pname, int param) {
		gl.glPixelStorei(pname, param);
		checkError();
	}

	@Override
	public void glPolygonOffset(float factor, float units) {
		gl.glPolygonOffset(factor, units);
		checkError();
	}

	@Override
	public void glReadPixels(int x, int y, int width, int height, int format, int type, ByteBuffer data) {
		gl.glReadPixels(x, y, width, height, format, type, data);
		checkError();
	}

	@Override
	public void glReadPixels(int x, int y, int width, int height, int format, int type, long offset) {
		gl.glReadPixels(x, y, width, height, format, type, offset);
		checkError();
	}

	@Override
	public void glScissor(int x, int y, int width, int height) {
		gl.glScissor(x, y, width, height);
		checkError();
	}

	@Override
	public void glShaderSource(int shader, String[] string, IntBuffer length) {
		gl.glShaderSource(shader, string, length);
		checkError();
	}

	@Override
	public void glStencilFuncSeparate(int face, int func, int ref, int mask) {
		gl.glStencilFuncSeparate(face, func, ref, mask);
		checkError();
	}

	@Override
	public void glStencilOpSeparate(int face, int sfail, int dpfail, int dppass) {
		gl.glStencilOpSeparate(face, sfail, dpfail, dppass);
		checkError();
	}

	@Override
	public void glTexImage2D(int target, int level, int internalFormat, int width, int height, int border, int format, int type, ByteBuffer data) {
		gl.glTexImage2D(target, level, internalFormat, width, height, border, format, type, data);
		checkError();
	}

	@Override
	public void glTexParameterf(int target, int pname, float param) {
		gl.glTexParameterf(target, pname, param);
		checkError();
	}

	@Override
	public void glTexParameteri(int target, int pname, int param) {
		gl.glTexParameteri(target, pname, param);
		checkError();
	}

	@Override
	public void glTexSubImage2D(int target, int level, int xoffset, int yoffset, int width, int height, int format, int type, ByteBuffer data) {
		gl.glTexSubImage2D(target, level, xoffset, yoffset, width, height, format, type, data);
		checkError();
	}

	@Override
	public void glUniform1(int location, FloatBuffer value) {
		gl.glUniform1(location, value);
		checkError();
	}

	@Override
	public void glUniform1(int location, IntBuffer value) {
		gl.glUniform1(location, value);
		checkError();
	}

	@Override
	public void glUniform1f(int location, float v0) {
		gl.glUniform1f(location, v0);
		checkError();
	}

	@Override
	public void glUniform1i(int location, int v0) {
		gl.glUniform1i(location, v0);
		checkError();
	}

	@Override
	public void glUniform2(int location, IntBuffer value) {
		gl.glUniform2(location, value);
		checkError();
	}

	@Override
	public void glUniform2(int location, FloatBuffer value) {
		gl.glUniform2(location, value);
		checkError();
	}

	@Override
	public void glUniform2f(int location, float v0, float v1) {
		gl.glUniform2f(location, v0, v1);
		checkError();
	}

	@Override
	public void glUniform3(int location, IntBuffer value) {
		gl.glUniform3(location, value);
		checkError();
	}

	@Override
	public void glUniform3(int location, FloatBuffer value) {
		gl.glUniform3(location, value);
		checkError();
	}

	@Override
	public void glUniform3f(int location, float v0, float v1, float v2) {
		gl.glUniform3f(location, v0, v1, v2);
		checkError();
	}

	@Override
	public void glUniform4(int location, FloatBuffer value) {
		gl.glUniform4(location, value);
		checkError();
	}

	@Override
	public void glUniform4(int location, IntBuffer value) {
		gl.glUniform4(location, value);
		checkError();
	}

	@Override
	public void glUniform4f(int location, float v0, float v1, float v2, float v3) {
		gl.glUniform4f(location, v0, v1, v2, v3);
		checkError();
	}

	@Override
	public void glUniformMatrix3(int location, boolean transpose, FloatBuffer value) {
		gl.glUniformMatrix3(location, transpose, value);
		checkError();
	}

	@Override
	public void glUniformMatrix4(int location, boolean transpose, FloatBuffer value) {
		gl.glUniformMatrix4(location, transpose, value);
		checkError();
	}

	@Override
	public void glUseProgram(int program) {
		gl.glUseProgram(program);
		checkError();
	}

	@Override
	public void glVertexAttribPointer(int index, int size, int type, boolean normalized, int stride, long pointer) {
		gl.glVertexAttribPointer(index, size, type, normalized, stride, pointer);
		checkError();
	}

	@Override
	public void glViewport(int x, int y, int width, int height) {
		gl.glViewport(x, y, width, height);
		checkError();
	}

	@Override
	public void glBufferData(int target, long data_size, int usage) {
		gl.glBufferData(target, data_size, usage);
		checkError();
	}

	@Override
	public void glGetBufferSubData(int target, long offset, ByteBuffer data) {
		gl.glGetBufferSubData(target, offset, data);
		checkError();
	}

	// ----------------------------
	// Fbo methods
	// ----------------------------

	@Override
	public void glBindFramebufferEXT(int param1, int param2) {
		glfbo.glBindFramebufferEXT(param1, param2);
		checkError();
	}

	@Override
	public void glBindRenderbufferEXT(int param1, int param2) {
		glfbo.glBindRenderbufferEXT(param1, param2);
		checkError();
	}

	@Override
	public int glCheckFramebufferStatusEXT(int param1) {
		int result = glfbo.glCheckFramebufferStatusEXT(param1);
		checkError();
		return result;
	}

	@Override
	public void glDeleteFramebuffersEXT(IntBuffer param1) {
		glfbo.glDeleteFramebuffersEXT(param1);
		checkError();
	}

	@Override
	public void glDeleteRenderbuffersEXT(IntBuffer param1) {
		glfbo.glDeleteRenderbuffersEXT(param1);
		checkError();
	}

	@Override
	public void glFramebufferRenderbufferEXT(int param1, int param2, int param3, int param4) {
		glfbo.glFramebufferRenderbufferEXT(param1, param2, param3, param4);
		checkError();
	}

	@Override
	public void glFramebufferTexture2DEXT(int param1, int param2, int param3, int param4, int param5) {
		glfbo.glFramebufferTexture2DEXT(param1, param2, param3, param4, param5);
		checkError();
	}

	@Override
	public void glGenFramebuffersEXT(IntBuffer param1) {
		glfbo.glGenFramebuffersEXT(param1);
		checkError();
	}

	@Override
	public void glGenRenderbuffersEXT(IntBuffer param1) {
		glfbo.glGenRenderbuffersEXT(param1);
		checkError();
	}

	@Override
	public void glGenerateMipmapEXT(int param1) {
		glfbo.glGenerateMipmapEXT(param1);
		checkError();
	}

	@Override
	public void glRenderbufferStorageEXT(int param1, int param2, int param3, int param4) {
		glfbo.glRenderbufferStorageEXT(param1, param2, param3, param4);
		checkError();
	}

	@Override
	public void glBlitFramebufferEXT(int srcX0, int srcY0, int srcX1, int srcY1, int dstX0, int dstY0, int dstX1, int dstY1, int mask, int filter) {
		glfbo.glBlitFramebufferEXT(srcX0, srcY0, srcX1, srcY1, dstX0, dstY0, dstX1, dstY1, mask, filter);
		checkError();
	}

	@Override
	public void glRenderbufferStorageMultisampleEXT(int target, int samples, int internalformat, int width, int height) {
		glfbo.glRenderbufferStorageMultisampleEXT(target, samples, internalformat, width, height);
		checkError();
	}

	// ----------------------------
	// Ext methods
	// ----------------------------

	@Override
	public void glBufferData(int target, IntBuffer data, int usage) {
		glext.glBufferData(target, data, usage);
		checkError();
	}

	@Override
	public void glBufferSubData(int target, long offset, IntBuffer data) {
		glext.glBufferSubData(target, offset, data);
		checkError();
	}

	@Override
	public void glDrawArraysInstancedARB(int mode, int first, int count, int primcount) {
		glext.glDrawArraysInstancedARB(mode, first, count, primcount);
		checkError();
	}

	@Override
	public void glDrawBuffers(IntBuffer bufs) {
		glext.glDrawBuffers(bufs);
		checkError();
	}

	@Override
	public void glDrawElementsInstancedARB(int mode, int indices_count, int type, long indices_buffer_offset, int primcount) {
		glext.glDrawElementsInstancedARB(mode, indices_count, type, indices_buffer_offset, primcount);
		checkError();
	}

	@Override
	public void glGetMultisample(int pname, int index, FloatBuffer val) {
		glext.glGetMultisample(pname, index, val);
		checkError();
	}

	@Override
	public void glTexImage2DMultisample(int target, int samples, int internalformat, int width, int height, boolean fixedsamplelocations) {
		glext.glTexImage2DMultisample(target, samples, internalformat, width, height, fixedsamplelocations);
		checkError();
	}

	@Override
	public void glVertexAttribDivisorARB(int index, int divisor) {
		glext.glVertexAttribDivisorARB(index, divisor);
		checkError();
	}

	@Override
	public int glClientWaitSync(Object sync, int flags, long timeout) {
		int result = glext.glClientWaitSync(sync, flags, timeout);
		checkError();
		return result;
	}

	@Override
	public void glDeleteSync(Object sync) {
		glext.glDeleteSync(sync);
		checkError();
	}

	@Override
	public Object glFenceSync(int condition, int flags) {
		Object sync = glext.glFenceSync(condition, flags);
		checkError();
		return sync;
	}

}
