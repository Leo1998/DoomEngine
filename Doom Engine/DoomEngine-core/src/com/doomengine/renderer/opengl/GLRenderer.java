/**
 * Copyright (c) 2014, Leonard Fricke
 * All rights reserved.
 * 
 * You should not steal or modify anything of this code unless
 * you have any special conditions.
 *
 */
package com.doomengine.renderer.opengl;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.doomengine.math.ColorRGBA;
import com.doomengine.math.Quaternion;
import com.doomengine.math.Vector2f;
import com.doomengine.math.Vector3f;
import com.doomengine.math.Vector4f;
import com.doomengine.renderer.BlendMode;
import com.doomengine.renderer.CullFace;
import com.doomengine.renderer.DataType;
import com.doomengine.renderer.DrawMode;
import com.doomengine.renderer.IndexBufferObject;
import com.doomengine.renderer.Mesh;
import com.doomengine.renderer.Renderer;
import com.doomengine.renderer.TestFunc;
import com.doomengine.renderer.VertexAttribute;
import com.doomengine.renderer.VertexAttributes;
import com.doomengine.renderer.VertexBuffer;
import com.doomengine.renderer.VertexBuffer.Usage;
import com.doomengine.renderer.VertexBufferObject;
import com.doomengine.shader.Attribute;
import com.doomengine.shader.Shader;
import com.doomengine.shader.ShaderSource;
import com.doomengine.shader.ShaderType;
import com.doomengine.shader.Uniform;
import com.doomengine.system.DoomException;
import com.doomengine.system.Logger;
import com.doomengine.texture.FrameBuffer;
import com.doomengine.texture.MagFilter;
import com.doomengine.texture.MinFilter;
import com.doomengine.texture.RenderBuffer;
import com.doomengine.texture.Texture;
import com.doomengine.texture.Texture2D;
import com.doomengine.texture.TextureCubeMap;
import com.doomengine.texture.WrapAxis;
import com.doomengine.texture.WrapMode;
import com.doomengine.texture.image.Image;
import com.doomengine.util.BufferUtils;
import com.doomengine.util.NativeObjectManager;

public class GLRenderer extends Renderer {

	private static final Pattern GLVERSION_PATTERN = Pattern.compile(".*?(\\d+)\\.(\\d+).*");
	private static final boolean VALIDATE_SHADER = true;
	private final ByteBuffer nameBuf = BufferUtils.createByteBuffer(250);
	private final IntBuffer intBuf1 = BufferUtils.createIntBuffer(1);
	private final IntBuffer intBuf16 = BufferUtils.createIntBuffer(16);
	private final FloatBuffer floatBuf16 = BufferUtils.createFloatBuffer(16);
	private final NativeObjectManager objManager = new NativeObjectManager();

	private final EnumSet<Caps> caps = EnumSet.noneOf(Caps.class);
	private HashSet<String> extensions;

	private boolean linearizeSrgbImages;

	private int vpX, vpY, vpW, vpH;

	private GL gl;
	private GL2 gl2;
	private GL3 gl3;
	private GL4 gl4;
	private GLExt glext;
	private GLFbo glfbo;
	private TextureUtil texUtil;

	public GLRenderer(GL gl, GLExt glext, GLFbo glfbo) {
		super();

		this.gl = gl;
		this.glext = glext;
		this.glfbo = glfbo;

		this.gl2 = gl instanceof GL2 ? (GL2) gl : null;
		this.gl3 = gl instanceof GL3 ? (GL3) gl : null;
		this.gl4 = gl instanceof GL4 ? (GL4) gl : null;

		loadCapabilities();

		gl.glPixelStorei(GL.GL_UNPACK_ALIGNMENT, 1);

		this.texUtil = new TextureUtil(gl, gl2, glext);
		texUtil.initialize(caps);
	}

	private HashSet<String> loadExtensions() {
		HashSet<String> extensionSet = new HashSet<String>(64);
		if (caps.contains(Caps.OpenGL30)) {
			// If OpenGL3+ is available, use the non-deprecated way
			// of getting supported extensions.
			gl3.glGetInteger(GL3.GL_NUM_EXTENSIONS, intBuf16);
			int extensionCount = intBuf16.get(0);
			for (int i = 0; i < extensionCount; i++) {
				String extension = gl3.glGetString(GL.GL_EXTENSIONS, i);
				extensionSet.add(extension);
			}
		} else {
			extensionSet.addAll(Arrays.asList(gl.glGetString(GL.GL_EXTENSIONS).split(" ")));
		}
		return extensionSet;
	}

	public static int extractVersion(String version) {
		Matcher m = GLVERSION_PATTERN.matcher(version);
		if (m.matches()) {
			int major = Integer.parseInt(m.group(1));
			int minor = Integer.parseInt(m.group(2));
			if (minor >= 10 && minor % 10 == 0) {
				// some versions can look like "1.30" instead of "1.3".
				// make sure to correct for this
				minor /= 10;
			}
			return major * 100 + minor * 10;
		} else {
			return -1;
		}
	}

	private boolean hasExtension(String extensionName) {
		return extensions.contains(extensionName);
	}

	private void loadCapabilitiesES() {
		caps.add(Caps.GLSL100);
		caps.add(Caps.OpenGLES20);
		// Important: Do not add OpenGL20 - that's the desktop capability!
	}

	private void loadCapabilitiesGL2() {
		int oglVer = extractVersion(gl.glGetString(GL.GL_VERSION));

		if (oglVer >= 200) {
			caps.add(Caps.OpenGL20);
			if (oglVer >= 210) {
				caps.add(Caps.OpenGL21);
				if (oglVer >= 300) {
					caps.add(Caps.OpenGL30);
					if (oglVer >= 310) {
						caps.add(Caps.OpenGL31);
						if (oglVer >= 320) {
							caps.add(Caps.OpenGL32);
						}
						if (oglVer >= 330) {
							caps.add(Caps.OpenGL33);
							caps.add(Caps.GeometryShader);
						}
						if (oglVer >= 400) {
							caps.add(Caps.OpenGL40);
							caps.add(Caps.TesselationShader);
						}
					}
				}
			}
		}

		int glslVer = extractVersion(gl.glGetString(GL.GL_SHADING_LANGUAGE_VERSION));

		switch (glslVer) {
		default:
			if (glslVer < 400) {
				break;
			}
		case 400:
			caps.add(Caps.GLSL400);
		case 330:
			caps.add(Caps.GLSL330);
		case 150:
			caps.add(Caps.GLSL150);
		case 140:
			caps.add(Caps.GLSL140);
		case 130:
			caps.add(Caps.GLSL130);
		case 120:
			caps.add(Caps.GLSL120);
		case 110:
			caps.add(Caps.GLSL110);
		case 100:
			caps.add(Caps.GLSL100);
			break;
		}

		// Workaround, always assume we support GLSL100 & GLSL110
		// Supporting OpenGL 2.0 means supporting GLSL 1.10.
		caps.add(Caps.GLSL110);
		caps.add(Caps.GLSL100);
	}

	private void loadCapabilitiesCommon() {
		extensions = loadExtensions();

		// limits.put(Limits.VertexTextureUnits,
		// getInteger(GL.GL_MAX_VERTEX_TEXTURE_IMAGE_UNITS));
		// if (limits.get(Limits.VertexTextureUnits) > 0) {
		// caps.add(Caps.VertexTextureFetch);
		// }
		//
		// limits.put(Limits.FragmentTextureUnits,
		// getInteger(GL.GL_MAX_TEXTURE_IMAGE_UNITS));
		//
		// // gl.glGetInteger(GL.GL_MAX_VERTEX_UNIFORM_COMPONENTS, intBuf16);
		// // vertexUniforms = intBuf16.get(0);
		// // logger.log(Level.FINER, "Vertex Uniforms: {0}", vertexUniforms);
		// //
		// // gl.glGetInteger(GL.GL_MAX_FRAGMENT_UNIFORM_COMPONENTS, intBuf16);
		// // fragUniforms = intBuf16.get(0);
		// // logger.log(Level.FINER, "Fragment Uniforms: {0}", fragUniforms);
		// if (caps.contains(Caps.OpenGLES20)) {
		// limits.put(Limits.VertexUniformVectors,
		// getInteger(GL.GL_MAX_VERTEX_UNIFORM_VECTORS));
		// } else {
		// limits.put(Limits.VertexUniformVectors,
		// getInteger(GL.GL_MAX_VERTEX_UNIFORM_COMPONENTS) / 4);
		// }
		// limits.put(Limits.VertexAttributes,
		// getInteger(GL.GL_MAX_VERTEX_ATTRIBS));
		// limits.put(Limits.TextureSize, getInteger(GL.GL_MAX_TEXTURE_SIZE));
		// limits.put(Limits.CubemapSize,
		// getInteger(GL.GL_MAX_CUBE_MAP_TEXTURE_SIZE));

		if (hasExtension("GL_ARB_draw_instanced") && hasExtension("GL_ARB_instanced_arrays")) {
			caps.add(Caps.MeshInstancing);
		}

		if (hasExtension("GL_OES_element_index_uint") || gl2 != null) {
			caps.add(Caps.IntegerIndexBuffer);
		}

		if (hasExtension("GL_ARB_texture_buffer_object")) {
			caps.add(Caps.TextureBuffer);
		}

		// texture format extensions

		boolean hasFloatTexture;

		hasFloatTexture = hasExtension("GL_OES_texture_half_float") && hasExtension("GL_OES_texture_float");

		if (!hasFloatTexture) {
			hasFloatTexture = hasExtension("GL_ARB_texture_float") && hasExtension("GL_ARB_half_float_pixel");

			if (!hasFloatTexture) {
				hasFloatTexture = caps.contains(Caps.OpenGL30);
			}
		}

		if (hasFloatTexture) {
			caps.add(Caps.FloatTexture);
		}

		if (hasExtension("GL_OES_depth_texture") || gl2 != null) {
			caps.add(Caps.DepthTexture);

			// TODO: GL_OES_depth24
		}

		if (hasExtension("GL_OES_rgb8_rgba8") || hasExtension("GL_ARM_rgba8") || hasExtension("GL_EXT_texture_format_BGRA8888")) {
			caps.add(Caps.Rgba8);
		}

		if (caps.contains(Caps.OpenGL30) || hasExtension("GL_OES_packed_depth_stencil")) {
			caps.add(Caps.PackedDepthStencilBuffer);
		}

		if (hasExtension("GL_ARB_color_buffer_float") && hasExtension("GL_ARB_half_float_pixel")) {
			// XXX: Require both 16 and 32 bit float support for
			// FloatColorBuffer.
			caps.add(Caps.FloatColorBuffer);
		}

		if (hasExtension("GL_ARB_depth_buffer_float")) {
			caps.add(Caps.FloatDepthBuffer);
		}

		if ((hasExtension("GL_EXT_packed_float") && hasFloatTexture) || caps.contains(Caps.OpenGL30)) {
			// Either OpenGL3 is available or both packed_float &
			// half_float_pixel.
			caps.add(Caps.PackedFloatColorBuffer);
			caps.add(Caps.PackedFloatTexture);
		}

		if (hasExtension("GL_EXT_texture_shared_exponent") || caps.contains(Caps.OpenGL30)) {
			caps.add(Caps.SharedExponentTexture);
		}

		if (hasExtension("GL_EXT_texture_compression_s3tc")) {
			caps.add(Caps.TextureCompressionS3TC);
		}

		if (hasExtension("GL_ARB_ES3_compatibility")) {
			caps.add(Caps.TextureCompressionETC2);
			caps.add(Caps.TextureCompressionETC1);
		} else if (hasExtension("GL_OES_compressed_ETC1_RGB8_texture")) {
			caps.add(Caps.TextureCompressionETC1);
		}

		// end texture format extensions

		if (hasExtension("GL_ARB_vertex_array_object") || caps.contains(Caps.OpenGL30)) {
			caps.add(Caps.VertexBufferArray);
		}

		if (hasExtension("GL_ARB_texture_non_power_of_two") || hasExtension("GL_OES_texture_npot") || caps.contains(Caps.OpenGL30)) {
			caps.add(Caps.NonPowerOfTwoTextures);
		} else {
			Logger.log("Your graphics card does not " + "support non-power-of-2 textures. " + "Some features might not work.");
		}

		if (caps.contains(Caps.OpenGLES20)) {
			// OpenGL ES 2 has some limited support for NPOT textures
			caps.add(Caps.PartialNonPowerOfTwoTextures);
		}

		if (hasExtension("GL_EXT_texture_array") || caps.contains(Caps.OpenGL30)) {
			caps.add(Caps.TextureArray);
		}

		if (hasExtension("GL_EXT_texture_filter_anisotropic")) {
			caps.add(Caps.TextureFilterAnisotropic);
		}

		if (hasExtension("GL_EXT_framebuffer_object") || gl3 != null) {
			caps.add(Caps.FrameBuffer);

			// limits.put(Limits.RenderBufferSize,
			// getInteger(GLFbo.GL_MAX_RENDERBUFFER_SIZE_EXT));
			// limits.put(Limits.FrameBufferAttachments,
			// getInteger(GLFbo.GL_MAX_COLOR_ATTACHMENTS_EXT));

			if (hasExtension("GL_EXT_framebuffer_blit")) {
				caps.add(Caps.FrameBufferBlit);
			}

			if (hasExtension("GL_EXT_framebuffer_multisample")) {
				caps.add(Caps.FrameBufferMultisample);
				// limits.put(Limits.FrameBufferSamples,
				// getInteger(GLExt.GL_MAX_SAMPLES_EXT));
			}

			if (hasExtension("GL_ARB_texture_multisample")) {
				caps.add(Caps.TextureMultisample);
				// limits.put(Limits.ColorTextureSamples,
				// getInteger(GLExt.GL_MAX_COLOR_TEXTURE_SAMPLES));
				// limits.put(Limits.DepthTextureSamples,
				// getInteger(GLExt.GL_MAX_DEPTH_TEXTURE_SAMPLES));
				// if (!limits.containsKey(Limits.FrameBufferSamples)) {
				// // In case they want to query samples on main FB ...
				// limits.put(Limits.FrameBufferSamples,
				// limits.get(Limits.ColorTextureSamples));
				// }
			}

			if (hasExtension("GL_ARB_draw_buffers")) {
				// limits.put(Limits.FrameBufferMrtAttachments,
				// getInteger(GLExt.GL_MAX_DRAW_BUFFERS_ARB));
				// if (limits.get(Limits.FrameBufferMrtAttachments) > 1) {
				caps.add(Caps.FrameBufferMRT);
				// }
			} else {
				// limits.put(Limits.FrameBufferMrtAttachments, 1);
			}
		}

		if (hasExtension("GL_ARB_multisample")) {
			boolean available = getInteger(GLExt.GL_SAMPLE_BUFFERS_ARB) != 0;
			int samples = getInteger(GLExt.GL_SAMPLES_ARB);
			// Logger.log("Samples: " + samples);
			boolean enabled = gl.glIsEnabled(GLExt.GL_MULTISAMPLE_ARB);
			if (samples > 0 && available && !enabled) {
				// Doesn't seem to be neccessary .. OGL spec says its always
				// set by default?
				gl.glEnable(GLExt.GL_MULTISAMPLE_ARB);
			}
			caps.add(Caps.Multisample);
		}

		// Supports sRGB pipeline.
		if ((hasExtension("GL_ARB_framebuffer_sRGB") && hasExtension("GL_EXT_texture_sRGB")) || caps.contains(Caps.OpenGL30)) {
			caps.add(Caps.Srgb);
		}

		// Supports seamless cubemap
		if (hasExtension("GL_ARB_seamless_cube_map") || caps.contains(Caps.OpenGL32)) {
			caps.add(Caps.SeamlessCubemap);
		}

		if (caps.contains(Caps.OpenGL32) && !hasExtension("GL_ARB_compatibility")) {
			caps.add(Caps.CoreProfile);
		}

		if (hasExtension("GL_ARB_get_program_binary")) {
			int binaryFormats = getInteger(GLExt.GL_NUM_PROGRAM_BINARY_FORMATS);
			if (binaryFormats > 0) {
				caps.add(Caps.BinaryShader);
			}
		}

		Logger.log("OpenGL Renderer Information");
		Logger.log("Vendor: " + gl.glGetString(GL.GL_VENDOR));
		Logger.log("Renderer: " + gl.glGetString(GL.GL_RENDERER));
		Logger.log("OpenGL Version: " + gl.glGetString(GL.GL_VERSION));
		Logger.log("GLSL Version: " + gl.glGetString(GL.GL_SHADING_LANGUAGE_VERSION));
		Logger.log("Profile: " + (caps.contains(Caps.CoreProfile) ? "Core" : "Compatibility"));
		Logger.log("\n");

		StringBuilder sb = new StringBuilder();
		sb.append("Supported capabilities: \n");
		for (Caps cap : caps) {
			sb.append("\t").append(cap.toString()).append("\n");
		}
		Logger.log(sb.toString());
	}

	private void loadCapabilities() {
		if (gl2 != null) {
			loadCapabilitiesGL2();
		} else {
			loadCapabilitiesES();
		}
		loadCapabilitiesCommon();
	}

	private int getInteger(int en) {
		intBuf16.clear();
		gl.glGetInteger(en, intBuf16);
		return intBuf16.get(0);
	}

	private boolean getBoolean(int en) {
		gl.glGetBoolean(en, nameBuf);
		return nameBuf.get(0) != (byte) 0;
	}

	@Override public void newFrame() {
		objManager.deleteUnused();
	}

	@Override public void resetContext() {
		Logger.log("Resetting GLRenderer Context!");
		objManager.resetObjects();
		context.reset();
	}

	@Override public void cleanup() {
		Logger.log("Deleting GLRenderer Context!");
		objManager.deleteAllObjects();
		context.reset();
	}

	@Override public void setCullFace(CullFace cullface) {
		if (cullface != context.cullface) {
			if (cullface == CullFace.Off) {
				gl.glDisable(GL.GL_CULL_FACE);
			} else {
				gl.glEnable(GL.GL_CULL_FACE);
			}

			switch (cullface) {
			case Off:
				break;
			case Back:
				gl.glCullFace(GL.GL_BACK);
				break;
			case Front:
				gl.glCullFace(GL.GL_FRONT);
				break;
			case FrontAndBack:
				gl.glCullFace(GL.GL_FRONT_AND_BACK);
				break;
			default:
				throw new UnsupportedOperationException("Unrecognized face cull mode: " + cullface);
			}

			context.cullface = cullface;
		}
	}

	@Override public void enableDepthTest(boolean flag) {
		if (flag && !context.depthTestEnabled)
			gl.glEnable(GL.GL_DEPTH_TEST);
		else if (!flag && context.depthTestEnabled)
			gl.glDisable(GL.GL_DEPTH_TEST);

		context.depthTestEnabled = flag;

	}

	@Override public void setDepthFunc(TestFunc depthfunc) {
		if (context.depthFunc != depthfunc)
			gl.glDepthFunc(convertDepthFunc(depthfunc));

		context.depthFunc = depthfunc;

	}

	@Override public void setDepthWriteEnabled(boolean flag) {
		if (context.depthWriteEnabled != flag) {
			gl.glDepthMask(flag);
		}

		context.depthWriteEnabled = flag;

	}

	@Override public void setClearColor(float r, float g, float b, float a) {
		if (!context.clearColor.equals(new ColorRGBA(r, g, b, a)))
			gl.glClearColor(r, g, b, a);

		context.clearColor.set(r, g, b, a);

	}

	@Override public void clear(boolean color, boolean depth, boolean stencil) {
		int mask = 0;

		if (color)
			mask |= GL.GL_COLOR_BUFFER_BIT;
		if (depth)
			mask |= GL.GL_DEPTH_BUFFER_BIT;
		if (stencil)
			mask |= GL.GL_STENCIL_BUFFER_BIT;

		if (mask != 0) {
			gl.glClear(mask);
		}

	}

	@Override public void setViewPort(int x, int y, int w, int h) {
		if (x != vpX || vpY != y || vpW != w || vpH != h) {
			gl.glViewport(x, y, w, h);
			vpX = x;
			vpY = y;
			vpW = w;
			vpH = h;
		}
	}

	@Override public void setBlendMode(BlendMode blendMode) {
		if (blendMode != context.blendMode) {
			if (blendMode == BlendMode.Off) {
				gl.glDisable(GL.GL_BLEND);
			} else {
				if (context.blendMode == BlendMode.Off) {
					gl.glEnable(GL.GL_BLEND);
				}
				switch (blendMode) {
				case Off:
					break;
				case Additive:
					gl.glBlendFunc(GL.GL_ONE, GL.GL_ONE);
					break;
				case AlphaAdditive:
					gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE);
					break;
				case Alpha:
					gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
					break;
				case PremultAlpha:
					gl.glBlendFunc(GL.GL_ONE, GL.GL_ONE_MINUS_SRC_ALPHA);
					break;
				case Modulate:
					gl.glBlendFunc(GL.GL_DST_COLOR, GL.GL_ZERO);
					break;
				case ModulateX2:
					gl.glBlendFunc(GL.GL_DST_COLOR, GL.GL_SRC_COLOR);
					break;
				case Color:
				case Screen:
					gl.glBlendFunc(GL.GL_ONE, GL.GL_ONE_MINUS_SRC_COLOR);
					break;
				case Exclusion:
					gl.glBlendFunc(GL.GL_ONE_MINUS_DST_COLOR, GL.GL_ONE_MINUS_SRC_COLOR);
					break;
				default:
					throw new UnsupportedOperationException("Unrecognized blend mode: " + blendMode);
				}
			}

			context.blendMode = blendMode;
		}
	}

	@Override public void deleteTexture(Texture texture) {
		intBuf1.put(0, texture.getId());
		gl.glDeleteTextures(intBuf1);
	}

	@Override public void bindTexture(Texture texture, int unit) {
		if (texture != null && texture.isUpdateNeeded()) {
			updateTexture(texture);
		}

		Texture[] textures = context.boundTextures;

		int type = texture != null ? convertTextureType(texture.getType()) : GL.GL_TEXTURE_2D;// NOTE:
																								// workaround
		if (textures[unit] != texture) {
			if (context.boundTextureUnit != unit) {
				gl.glActiveTexture(GL.GL_TEXTURE0 + unit);
				context.boundTextureUnit = unit;
			}

			gl.glBindTexture(type, texture != null ? texture.getId() : 0);
			context.boundTextures[unit] = texture;
		}
	}

	public void updateTexture(Texture texture) {
		int id = texture.getId();
		if (id == -1) {
			gl.glGenTextures(intBuf1);
			id = intBuf1.get(0);
			texture.setId(id);

			objManager.registerObject(texture, this);
		}

		int target = convertTextureType(texture.getType());
		Image img = texture.getImage();

		if (texture.getType() == Texture.Type.TwoDimensional) {
			texUtil.uploadTexture(img, target, 0, linearizeSrgbImages);
		} else if (texture.getType() == Texture.Type.Cubemap) {
			List<ByteBuffer> data = img.getData();

			if (data.size() != 6) {
				Logger.log("Invalid texture: " + img + "\n" + "Cubemap textures must contain 6 data units.");
				return;
			}
			for (int i = 0; i < 6; i++) {
				texUtil.uploadTexture(img, GL.GL_TEXTURE_CUBE_MAP_POSITIVE_X + i, i, linearizeSrgbImages);
			}
		}

		setupTextureParams(texture);

		texture.clearUpdateNeeded();
	}

	public void setupTextureParams(Texture texture) {
		int type = convertTextureType(texture.getType());

		gl.glTexParameteri(type, GL.GL_TEXTURE_MAG_FILTER, convertMagFilter(texture.getMagFilter()));
		gl.glTexParameteri(type, GL.GL_TEXTURE_MIN_FILTER, convertMinFilter(texture.getMinFilter()));

		if (texture.getType() == Texture.Type.TwoDimensional) {
			Texture2D tex2D = (Texture2D) texture;
			{
				int axis = convertWrapAxis(WrapAxis.S);
				gl.glTexParameteri(type, axis, convertWrapMode(tex2D.getWrapS()));
			}
			{
				int axis = convertWrapAxis(WrapAxis.T);
				gl.glTexParameteri(type, axis, convertWrapMode(tex2D.getWrapT()));
			}
		} else if (texture.getType() == Texture.Type.Cubemap) {
			TextureCubeMap texCube = (TextureCubeMap) texture;
			{
				int axis = convertWrapAxis(WrapAxis.S);
				gl.glTexParameteri(type, axis, convertWrapMode(texCube.getWrapS()));
			}
			{
				int axis = convertWrapAxis(WrapAxis.T);
				gl.glTexParameteri(type, axis, convertWrapMode(texCube.getWrapT()));
			}
			{
				int axis = convertWrapAxis(WrapAxis.R);
				gl.glTexParameteri(type, axis, convertWrapMode(texCube.getWrapR()));
			}
		}

	}

	@Override public void copyFrameBuffer(FrameBuffer src, FrameBuffer dst) {
		copyFrameBuffer(src, dst, true);
	}

	@Override public void copyFrameBuffer(FrameBuffer src, FrameBuffer dst, boolean copyDepth) {
		if (caps.contains(Caps.FrameBufferBlit)) {
			int srcX0 = 0;
			int srcY0 = 0;
			int srcX1;
			int srcY1;

			int dstX0 = 0;
			int dstY0 = 0;
			int dstX1;
			int dstY1;

			int prevFBO = context.boundFBO;

			if (src != null && src.isUpdateNeeded()) {
				updateFramebuffer(src);
			}

			if (dst != null && dst.isUpdateNeeded()) {
				updateFramebuffer(dst);
			}

			if (src == null) {
				glfbo.glBindFramebufferEXT(GLFbo.GL_READ_FRAMEBUFFER_EXT, 0);
				srcX0 = vpX;
				srcY0 = vpY;
				srcX1 = vpX + vpW;
				srcY1 = vpY + vpH;
			} else {
				glfbo.glBindFramebufferEXT(GLFbo.GL_READ_FRAMEBUFFER_EXT, src.getId());
				srcX1 = src.getWidth();
				srcY1 = src.getHeight();
			}
			if (dst == null) {
				glfbo.glBindFramebufferEXT(GLFbo.GL_DRAW_FRAMEBUFFER_EXT, 0);
				dstX0 = vpX;
				dstY0 = vpY;
				dstX1 = vpX + vpW;
				dstY1 = vpY + vpH;
			} else {
				glfbo.glBindFramebufferEXT(GLFbo.GL_DRAW_FRAMEBUFFER_EXT, dst.getId());
				dstX1 = dst.getWidth();
				dstY1 = dst.getHeight();
			}
			int mask = GL.GL_COLOR_BUFFER_BIT;
			if (copyDepth) {
				mask |= GL.GL_DEPTH_BUFFER_BIT;
			}
			glfbo.glBlitFramebufferEXT(srcX0, srcY0, srcX1, srcY1, dstX0, dstY0, dstX1, dstY1, mask, GL.GL_NEAREST);

			glfbo.glBindFramebufferEXT(GLFbo.GL_FRAMEBUFFER_EXT, prevFBO);
		} else {
			throw new IllegalArgumentException("Framebuffer blitting not supported by the video hardware");
		}
	}

	@Override public void deleteFramebuffer(FrameBuffer framebuffer) {
		if (context.boundFBO == framebuffer.getId()) {
			glfbo.glBindFramebufferEXT(GLFbo.GL_FRAMEBUFFER_EXT, 0);
			context.boundFBO = 0;
		}

		if (framebuffer.getDepthBuffer() != null) {
			deleteRenderBuffer(framebuffer, framebuffer.getDepthBuffer());
		}
		if (framebuffer.getColorBuffer() != null) {
			deleteRenderBuffer(framebuffer, framebuffer.getColorBuffer());
		}

		intBuf1.put(0, framebuffer.getId());
		glfbo.glDeleteFramebuffersEXT(intBuf1);
	}

	@Override public void bindFramebuffer(FrameBuffer framebuffer) {
		int id = framebuffer != null ? framebuffer.getId() : 0;

		if (framebuffer != null && framebuffer.isUpdateNeeded()) {
			updateFramebuffer(framebuffer);
		}

		if (context.boundFBO != id) {
			glfbo.glBindFramebufferEXT(GLFbo.GL_FRAMEBUFFER_EXT, id);
			context.boundFBO = id;
		}
	}

	public void updateFramebuffer(FrameBuffer framebuffer) {
		int id = framebuffer.getId();
		if (id == -1) {
			glfbo.glGenFramebuffersEXT(intBuf1);
			id = intBuf1.get(0);
			framebuffer.setId(id);

			objManager.registerObject(framebuffer, this);
		}

		if (context.boundFBO != id) {
			glfbo.glBindFramebufferEXT(GLFbo.GL_FRAMEBUFFER_EXT, id);
			context.boundFBO = id;
		}

		RenderBuffer depthBuf = framebuffer.getDepthBuffer();
		if (depthBuf != null) {
			updateFrameBufferAttachment(framebuffer, depthBuf);
		}

		for (int i = 0; i < framebuffer.getNumColorBuffers(); i++) {
			RenderBuffer colorBuf = framebuffer.getColorBuffer(i);
			updateFrameBufferAttachment(framebuffer, colorBuf);
		}

		checkFrameBufferError();

		framebuffer.clearUpdateNeeded();
	}

	public void updateFrameBufferAttachment(FrameBuffer fb, RenderBuffer rb) {
		boolean needAttach;
		if (rb.getTexture() == null) {
			needAttach = rb.getId() == -1;
			updateRenderBuffer(fb, rb);
		} else {
			needAttach = false;
			updateRenderTexture(fb, rb);
		}
		if (needAttach) {
			glfbo.glFramebufferRenderbufferEXT(GLFbo.GL_FRAMEBUFFER_EXT, convertAttachmentSlot(rb.getSlot()), GLFbo.GL_RENDERBUFFER_EXT, rb.getId());
		}
	}

	private void checkFrameBufferError() {
		int status = glfbo.glCheckFramebufferStatusEXT(GLFbo.GL_FRAMEBUFFER_EXT);
		switch (status) {
		case GLFbo.GL_FRAMEBUFFER_COMPLETE_EXT:
			break;
		case GLFbo.GL_FRAMEBUFFER_UNSUPPORTED_EXT:
			// Choose different formats
			throw new IllegalStateException("Framebuffer object format is unsupported by the video hardware.");
		case GLFbo.GL_FRAMEBUFFER_INCOMPLETE_ATTACHMENT_EXT:
			throw new IllegalStateException("Framebuffer has erronous attachment.");
		case GLFbo.GL_FRAMEBUFFER_INCOMPLETE_MISSING_ATTACHMENT_EXT:
			throw new IllegalStateException("Framebuffer doesn't have any renderbuffers attached.");
		case GLFbo.GL_FRAMEBUFFER_INCOMPLETE_DIMENSIONS_EXT:
			throw new IllegalStateException("Framebuffer attachments must have same dimensions.");
		case GLFbo.GL_FRAMEBUFFER_INCOMPLETE_FORMATS_EXT:
			throw new IllegalStateException("Framebuffer attachments must have same formats.");
		case GLFbo.GL_FRAMEBUFFER_INCOMPLETE_DRAW_BUFFER_EXT:
			throw new IllegalStateException("Incomplete draw buffer.");
		case GLFbo.GL_FRAMEBUFFER_INCOMPLETE_READ_BUFFER_EXT:
			throw new IllegalStateException("Incomplete read buffer.");
		case GLFbo.GL_FRAMEBUFFER_INCOMPLETE_MULTISAMPLE_EXT:
			throw new IllegalStateException("Incomplete multisample buffer.");
		default:
			throw new IllegalStateException("Some video driver error or programming error occured. Framebuffer object status is invalid. ");
		}
	}

	public void updateRenderTexture(FrameBuffer fb, RenderBuffer rb) {
		Texture tex = rb.getTexture();
		if (tex.isUpdateNeeded()) {
			updateTexture(tex);

			setupTextureParams(tex);
		}

		glfbo.glFramebufferTexture2DEXT(GLFbo.GL_FRAMEBUFFER_EXT, convertAttachmentSlot(rb.getSlot()), convertTextureType(tex.getType()), tex.getId(), 0);
	}

	private void updateRenderBuffer(FrameBuffer fb, RenderBuffer rb) {
		int id = rb.getId();
		if (id == -1) {
			glfbo.glGenRenderbuffersEXT(intBuf1);
			id = intBuf1.get(0);
			rb.setId(id);
		}

		if (context.boundRB != id) {
			glfbo.glBindRenderbufferEXT(GLFbo.GL_RENDERBUFFER_EXT, id);
			context.boundRB = id;
		}

		// int rbSize = limits.get(Limits.RenderBufferSize);
		// if (fb.getWidth() > rbSize || fb.getHeight() > rbSize) {
		// throw new RendererException("Resolution " + fb.getWidth()
		// + ":" + fb.getHeight() + " is not supported.");
		// }

		GLImageFormat glFmt = texUtil.getImageFormatWithError(rb.getFormat(), fb.isSrgb());

		if (fb.getSamples() > 1 && caps.contains(Caps.FrameBufferMultisample)) {
			int samples = fb.getSamples();
			// int maxSamples = limits.get(Limits.FrameBufferSamples);
			// if (maxSamples < samples) {
			// samples = maxSamples;
			// }
			glfbo.glRenderbufferStorageMultisampleEXT(GLFbo.GL_RENDERBUFFER_EXT, samples, glFmt.internalFormat, fb.getWidth(), fb.getHeight());
		} else {
			glfbo.glRenderbufferStorageEXT(GLFbo.GL_RENDERBUFFER_EXT, glFmt.internalFormat, fb.getWidth(), fb.getHeight());
		}
	}

	private void deleteRenderBuffer(FrameBuffer fb, RenderBuffer rb) {
		intBuf1.put(0, rb.getId());
		glfbo.glDeleteRenderbuffersEXT(intBuf1);
	}

	// @Override public int genVertexArray() {
	// gl3.glGenVertexArrays(intBuf1);
	// int handle = intBuf1.get(0);
	//
	// return handle;
	// }
	//
	// @Override public void deleteVertexArray(int handle) {
	// intBuf1.put(0, handle);
	// gl3.glDeleteVertexArrays(intBuf1);
	//
	// }
	//
	// @Override public void bindVertexArray(int handle) {
	// gl3.glBindVertexArray(handle);
	//
	// }

	@Override public void renderMesh(Mesh mesh) {
		VertexBufferObject vertexBuffer = mesh.getVertexBuffer();
		IndexBufferObject indexBuffer = mesh.getIndexBuffer();

		bindVertexBuffer(vertexBuffer);
		setVertexAttributes(vertexBuffer.getAttributes());

		int mode = convertDrawMode(mesh.getDrawMode());
		int numVertices = mesh.getNumVertices();

		if (indexBuffer != null) {
			bindVertexBuffer(indexBuffer);
			int numIndices = mesh.getNumIndices();
			int indexDataType = convertDataType(indexBuffer.getDataType());

			gl.glDrawRangeElements(mode, 0, numVertices, numIndices, indexDataType, 0);
		} else {
			gl.glDrawArrays(mode, 0, numVertices);
		}

		clearVertexAttributes();
	}

	@Override public void deleteVertexBuffer(VertexBuffer buffer) {
		intBuf1.put(0, buffer.getId());
		gl.glDeleteBuffers(intBuf1);

	}

	@Override public void bindVertexBuffer(VertexBuffer buffer) {
		if (buffer != null && buffer.isUpdateNeeded()) {
			updateVertexBuffer(buffer);
		}

		int target;
		if (buffer != null && buffer.getType() == VertexBuffer.Type.IndexData) {
			target = GL.GL_ELEMENT_ARRAY_BUFFER;
		} else {
			target = GL.GL_ARRAY_BUFFER;
		}

		gl.glBindBuffer(target, buffer != null ? buffer.getId() : 0);
	}

	public void updateVertexBuffer(VertexBuffer buffer) {
		int id = buffer.getId();
		if (id == -1) {
			gl.glGenBuffers(intBuf1);
			id = intBuf1.get(0);
			buffer.setId(id);

			objManager.registerObject(buffer, this);
		}

		int target;
		if (buffer.getType() == VertexBuffer.Type.IndexData) {
			target = GL.GL_ELEMENT_ARRAY_BUFFER;
		} else {
			target = GL.GL_ARRAY_BUFFER;
		}

		gl.glBindBuffer(target, id);

		int usage = convertUsage(buffer.getUsage());
		buffer.invariant();
		buffer.getData().rewind();

		switch (buffer.getDataType()) {
		case Byte:
		case UnsignedByte:
			gl.glBufferData(target, (ByteBuffer) buffer.getData(), usage);
			break;
		case Short:
		case UnsignedShort:
			gl.glBufferData(target, (ShortBuffer) buffer.getData(), usage);
			break;
		case Int:
		case UnsignedInt:
			glext.glBufferData(target, (IntBuffer) buffer.getData(), usage);
			break;
		case Float:
			gl.glBufferData(target, (FloatBuffer) buffer.getData(), usage);
			break;
		default:
			throw new UnsupportedOperationException("Unsupported buffer format.");
		}

		gl.glBindBuffer(target, 0);

		buffer.clearUpdateNeeded();
	}

	@Override public void bindShader(Shader shader) {
		if (shader.isUpdateNeeded()) {
			updateShader(shader);
		}

		if (context.boundShader != shader) {
			gl.glUseProgram(shader != null ? shader.getId() : 0);
			context.boundShader = shader;
		}

		updateShaderUniforms(shader);
	}

	@Override public void deleteShader(Shader shader) {
		gl.glDeleteProgram(shader.getId());
	}

	public void updateShader(Shader shader) {
		int handle = shader.getId();
		if (handle == -1) {
			handle = gl.glCreateProgram();
			shader.setId(handle);
			objManager.registerObject(shader, this);
		}

		for (ShaderSource source : shader.getSources()) {
			if (source.isUpdateNeeded()) {
				updateShaderSource(source);
			}
		}

		for (ShaderSource source : shader.getSources()) {
			gl.glAttachShader(handle, source.getId());
		}

		gl.glLinkProgram(handle);

		gl.glGetProgram(handle, GL.GL_LINK_STATUS, intBuf1);
		boolean linkOK = intBuf1.get(0) == GL.GL_TRUE;
		String infoLog = null;

		if (VALIDATE_SHADER) {
			gl.glGetProgram(handle, GL.GL_INFO_LOG_LENGTH, intBuf1);
			int length = intBuf1.get(0);
			if (length > 0) {
				infoLog = gl.glGetProgramInfoLog(handle, length);
			}
		}

		if (linkOK) {
			if (infoLog != null) {
				Logger.log("Shader linked successfully. Linker warnings: " + infoLog);
			} else {
				Logger.log("Shader linked successfully.");
			}
		}

		for (ShaderSource source : shader.getSources()) {
			gl.glDetachShader(handle, source.getId());
		}

		shader.clearUpdateNeeded();
	}

	@Override public void deleteShaderSource(ShaderSource source) {
		gl.glDeleteShader(source.getId());
	}

	public void updateShaderSource(ShaderSource source) {
		int handle = source.getId();
		if (handle == -1) {
			handle = gl.glCreateShader(convertShaderType(source.getType()));
			source.setId(handle);
			objManager.registerObject(source, this);
		}

		StringBuilder stringBuf = new StringBuilder();

		if (source.getDefines() != null) {
			stringBuf.append(source.getDefines());
		}
		stringBuf.append(source.getSource());

		intBuf1.clear();
		intBuf1.put(0, stringBuf.length());
		gl.glShaderSource(handle, new String[] { stringBuf.toString() }, intBuf1);
		gl.glCompileShader(handle);

		gl.glGetShader(handle, GL.GL_COMPILE_STATUS, intBuf1);

		boolean compiledOK = intBuf1.get(0) == GL.GL_TRUE;
		String infoLog = null;

		if (VALIDATE_SHADER) {
			gl.glGetShader(handle, GL.GL_INFO_LOG_LENGTH, intBuf1);
			int length = intBuf1.get(0);
			if (length > 0) {
				infoLog = gl.glGetShaderInfoLog(handle, length);
			}
		}

		if (compiledOK) {
			if (infoLog != null) {
				Logger.log(source.getType().name() + " compiled successfully, compiler warnings: " + infoLog);
			} else {
				Logger.log(source.getType().name() + " compiled successfully.");
			}
		} else {
			Logger.log("Bad compile of:\n" + stringBuf.toString());
			if (infoLog != null) {
				throw new DoomException("compile error in: " + source + "\n" + infoLog);
			} else {
				throw new DoomException("compile error in: " + source + "\nerror: <not provided>");
			}
		}

		source.clearUpdateNeeded();
	}

	public void updateUniformLocation(Shader shader, Uniform uniform) {
		int loc = gl.glGetUniformLocation(shader.getId(), uniform.getName());
		if (loc < 0) {
			uniform.setLocation(-1);
		} else {
			uniform.setLocation(loc);
		}
	}

	public void updateUniform(Shader shader, Uniform uniform) {
		assert uniform.getName() != null;
		assert shader.getId() > 0;

		int loc = uniform.getLocation();
		if (loc == -1) {
			return;
		}

		if (loc == -2) {
			updateUniformLocation(shader, uniform);
			if (uniform.getLocation() == -1) {
				uniform.clearUpdateNeeded();
				return;
			}
			loc = uniform.getLocation();
		}

		if (uniform.getVarType() == null) {
			return;
		}

		uniform.clearUpdateNeeded();
		FloatBuffer fb;
		switch (uniform.getVarType()) {
		case Float:
			Float f = (Float) uniform.getValue();
			gl.glUniform1f(loc, f.floatValue());
			break;
		case Vector2:
			Vector2f v2 = (Vector2f) uniform.getValue();
			gl.glUniform2f(loc, v2.getX(), v2.getY());
			break;
		case Vector3:
			Vector3f v3 = (Vector3f) uniform.getValue();
			gl.glUniform3f(loc, v3.getX(), v3.getY(), v3.getZ());
			break;
		case Vector4:
			Object val = uniform.getValue();
			if (val instanceof ColorRGBA) {
				ColorRGBA c = (ColorRGBA) val;
				gl.glUniform4f(loc, c.getX(), c.getY(), c.getZ(), c.getW());
			} else if (val instanceof Vector4f) {
				Vector4f c = (Vector4f) val;
				gl.glUniform4f(loc, c.getX(), c.getY(), c.getZ(), c.getW());
			} else {
				Quaternion c = (Quaternion) uniform.getValue();
				gl.glUniform4f(loc, c.getX(), c.getY(), c.getZ(), c.getW());
			}
			break;
		case Boolean:
			Boolean b = (Boolean) uniform.getValue();
			gl.glUniform1i(loc, b.booleanValue() ? GL.GL_TRUE : GL.GL_FALSE);
			break;
		case Matrix3:
			fb = (FloatBuffer) uniform.getValue();
			assert fb.remaining() == 9;
			gl.glUniformMatrix3(loc, true, fb);
			break;
		case Matrix4:
			fb = (FloatBuffer) uniform.getValue();
			assert fb.remaining() == 16;
			gl.glUniformMatrix4(loc, true, fb);
			break;
		case Int:
			Integer i = (Integer) uniform.getValue();
			gl.glUniform1i(loc, i.intValue());
			break;
		default:
			throw new UnsupportedOperationException("Unsupported uniform type: " + uniform.getVarType());
		}
	}

	public void updateShaderUniforms(Shader shader) {
		HashMap<String, Uniform> uniforms = shader.getUniformMap();

		for (Uniform uniform : uniforms.values()) {
			if (uniform.isUpdateNeeded()) {
				updateUniform(shader, uniform);
			}
		}
	}

	public void updateAttributeLocation(Shader shader, Attribute attrib) {
		int loc = gl.glGetAttribLocation(shader.getId(), attrib.getName());
		if (loc < 0) {
			attrib.setLocation(-1);
		} else {
			attrib.setLocation(loc);
		}
	}

	public void updateAttribute(Shader shader, Attribute attrib) {
		assert attrib.getName() != null;
		assert shader.getId() > 0;

		int loc = attrib.getLocation();
		if (loc == -1) {
			return;
		}

		if (loc == -2) {
			updateAttributeLocation(shader, attrib);
			if (attrib.getLocation() == -1) {
				attrib.clearUpdateNeeded();
				return;
			}
			loc = attrib.getLocation();
		}

		attrib.clearUpdateNeeded();
	}

	@Override public void setVertexAttributes(VertexAttributes attributes) {
		Shader shader = context.boundShader;
		assert shader != null && shader.getId() > 0;

		for (int i = 0; i < attributes.getCount(); i++) {
			VertexAttribute attribute = attributes.getVertexAttribute(i);

			Attribute shaderAttrib = shader.getAttribute(attribute.getName());
			if (shaderAttrib.isUpdateNeeded()) {
				updateAttribute(shader, shaderAttrib);
			}
			int location = shaderAttrib.getLocation();

			if (location == -1) {
				if (shader.strict)
					throw new IllegalStateException("Location not found!");
				continue;
			}

			gl.glEnableVertexAttribArray(location);

			gl.glVertexAttribPointer(location, attribute.getSize(), convertDataType(attribute.getType()), attribute.isNormalized(), attributes.vertexSize * 4, attribute.getOffset());
		}

		context.currentVertexAttribs = attributes;
	}

	@Override public void clearVertexAttributes() {
		Shader shader = context.boundShader;
		assert shader != null && shader.getId() > 0;

		for (int i = 0; i < context.currentVertexAttribs.getCount(); i++) {
			VertexAttribute attribute = context.currentVertexAttribs.getVertexAttribute(i);

			Attribute shaderAttrib = shader.getAttribute(attribute.getName());
			if (shaderAttrib.isUpdateNeeded()) {
				updateAttribute(shader, shaderAttrib);
			}
			int location = shaderAttrib.getLocation();

			if (location == -1) {
				if (shader.strict)
					throw new IllegalStateException("Location not found!");
				continue;
			}

			gl.glDisableVertexAttribArray(location);

		}

		context.currentVertexAttribs = null;
	}

	@Override public void setMainFrameBufferSrgb(boolean enableSrgb) {
		if (!caps.contains(Caps.Srgb) && enableSrgb) {
			Logger.log("sRGB framebuffer is not supported by video hardware, but was requested.");

			return;
		}

		// TODO:
		// setFrameBuffer(null);

		if (enableSrgb) {
			if (!getBoolean(GLExt.GL_FRAMEBUFFER_SRGB_CAPABLE_EXT)) {
				Logger.log("Driver claims that default framebuffer is not sRGB capable. Enabling anyway.");
			}

			gl.glEnable(GLExt.GL_FRAMEBUFFER_SRGB_EXT);

			Logger.log("sRGB FrameBuffer enabled (Gamma Correction)");
		} else {
			gl.glDisable(GLExt.GL_FRAMEBUFFER_SRGB_EXT);
		}
	}

	@Override public void setLinearizeSrgbImages(boolean linearize) {
		if (caps.contains(Caps.Srgb)) {
			linearizeSrgbImages = linearize;
		}
	}

	public static int convertAttachmentSlot(int attachmentSlot) {
		if (attachmentSlot == FrameBuffer.SLOT_DEPTH) {
			return GLFbo.GL_DEPTH_ATTACHMENT_EXT;
		} else if (attachmentSlot == FrameBuffer.SLOT_DEPTH_STENCIL) {
			return GL3.GL_DEPTH_STENCIL_ATTACHMENT;
		} else if (attachmentSlot < 0 || attachmentSlot >= 16) {
			throw new UnsupportedOperationException("Invalid FBO attachment slot: " + attachmentSlot);
		}

		return GLFbo.GL_COLOR_ATTACHMENT0_EXT + attachmentSlot;
	}

	// -------------------------
	// Helpers
	// -------------------------

	public static int convertDepthFunc(TestFunc depthfunc) {
		if (depthfunc == TestFunc.Never)
			return GL.GL_NEVER;
		if (depthfunc == TestFunc.Less)
			return GL.GL_LESS;
		if (depthfunc == TestFunc.Equal)
			return GL.GL_EQUAL;
		if (depthfunc == TestFunc.LessOrEqual)
			return GL.GL_LEQUAL;
		if (depthfunc == TestFunc.Greater)
			return GL.GL_GREATER;
		if (depthfunc == TestFunc.NotEqual)
			return GL.GL_NOTEQUAL;
		if (depthfunc == TestFunc.GreaterOrEqual)
			return GL.GL_GEQUAL;
		if (depthfunc == TestFunc.Always)
			return GL.GL_ALWAYS;

		return -1;
	}

	public static int convertDrawMode(DrawMode mode) {
		if (mode == DrawMode.LineStrip)
			return GL.GL_LINE_STRIP;
		if (mode == DrawMode.LineLoop)
			return GL.GL_LINE_LOOP;
		if (mode == DrawMode.Lines)
			return GL.GL_LINES;
		if (mode == DrawMode.TriangleStrip)
			return GL.GL_TRIANGLE_STRIP;
		if (mode == DrawMode.TriangleFan)
			return GL.GL_TRIANGLE_FAN;
		if (mode == DrawMode.Triangles)
			return GL.GL_TRIANGLES;

		return -1;
	}

	public static int convertDataType(DataType type) {
		if (type == DataType.Byte)
			return GL.GL_BYTE;
		if (type == DataType.UnsignedByte)
			return GL.GL_UNSIGNED_BYTE;
		if (type == DataType.Short)
			return GL.GL_SHORT;
		if (type == DataType.UnsignedShort)
			return GL.GL_UNSIGNED_SHORT;
		if (type == DataType.Int)
			return GL.GL_INT;
		if (type == DataType.UnsignedInt)
			return GL.GL_UNSIGNED_INT;
		if (type == DataType.Float)
			return GL.GL_FLOAT;
		if (type == DataType.Double)
			return GL.GL_DOUBLE;

		return -1;
	}

	public static int convertUsage(Usage usage) {
		if (usage == VertexBuffer.Usage.Static)
			return GL.GL_STATIC_DRAW;
		if (usage == VertexBuffer.Usage.Dynamic)
			return GL.GL_DYNAMIC_DRAW;
		if (usage == VertexBuffer.Usage.Stream)
			return GL.GL_STREAM_DRAW;

		return -1;
	}

	public static int convertShaderType(ShaderType type) {
		if (type == ShaderType.VertexShader)
			return GL.GL_VERTEX_SHADER;
		if (type == ShaderType.FragmentShader)
			return GL.GL_FRAGMENT_SHADER;

		return -1;
	}

	public static int convertTextureType(Texture.Type type) {
		if (type == Texture.Type.TwoDimensional)
			return GL.GL_TEXTURE_2D;
		if (type == Texture.Type.Cubemap)
			return GL.GL_TEXTURE_CUBE_MAP;

		return -1;
	}

	public static int convertMagFilter(MagFilter filter) {
		if (filter == MagFilter.NEAREST)
			return GL.GL_NEAREST;
		if (filter == MagFilter.BILINEAR)
			return GL.GL_LINEAR;

		return -1;
	}

	public static int convertMinFilter(MinFilter filter) {
		if (filter == MinFilter.NEAREST_NO_MIPMAPS)
			return GL.GL_NEAREST;
		if (filter == MinFilter.BILINEAR_NO_MIPMAPS)
			return GL.GL_LINEAR;
		if (filter == MinFilter.NEAREST_NEAREST_MIPMAPS)
			return GL.GL_NEAREST_MIPMAP_NEAREST;
		if (filter == MinFilter.BILINEAR_NEAREST_MIPMAPS)
			return GL.GL_LINEAR_MIPMAP_NEAREST;
		if (filter == MinFilter.NEAREST_LINEAR_MIPMAPS)
			return GL.GL_NEAREST_MIPMAP_LINEAR;
		if (filter == MinFilter.TRILINEAR)
			return GL.GL_LINEAR_MIPMAP_LINEAR;

		return -1;
	}

	public static int convertWrapMode(WrapMode wrapMode) {
		if (wrapMode == WrapMode.REPEAT)
			return GL.GL_REPEAT;
		if (wrapMode == WrapMode.MIRRORED_REPEAT)
			return GL.GL_MIRRORED_REPEAT;
		if (wrapMode == WrapMode.EDGE_CLAMP)
			return GL.GL_CLAMP_TO_EDGE;

		return -1;
	}

	public static int convertWrapAxis(WrapAxis wrapAxis) {
		if (wrapAxis == WrapAxis.S)
			return GL.GL_TEXTURE_WRAP_S;
		if (wrapAxis == WrapAxis.T)
			return GL.GL_TEXTURE_WRAP_T;
		if (wrapAxis == WrapAxis.R)
			return GL2.GL_TEXTURE_WRAP_R;

		return -1;
	}
}