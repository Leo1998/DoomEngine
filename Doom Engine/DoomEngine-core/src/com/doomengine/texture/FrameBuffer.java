package com.doomengine.texture;

import java.util.ArrayList;

import com.doomengine.renderer.Renderer;
import com.doomengine.texture.image.Format;
import com.doomengine.util.NativeObject;

public class FrameBuffer extends NativeObject {

	public static final int SLOT_UNDEF = -1;
	public static final int SLOT_DEPTH = -100;
	public static final int SLOT_DEPTH_STENCIL = -101;

	private final int width;
	private final int height;
	private final int samples;
	private ArrayList<RenderBuffer> colorBufs = new ArrayList<RenderBuffer>();
	private RenderBuffer depthBuf = null;
	private boolean srgb;

	public FrameBuffer(int width, int height) {
		this(width, height, 0);
	}

	public FrameBuffer(int width, int height, int samples) {
		if (width <= 0 || height <= 0)
			throw new IllegalArgumentException("FrameBuffer must have valid size.");

		this.width = width;
		this.height = height;
		this.samples = samples == 0 ? 1 : samples;
	}

	public void setDepthBuffer(Format format) {
		if (id != -1)
			throw new UnsupportedOperationException("FrameBuffer already initialized.");

		if (!format.isDepthFormat())
			throw new IllegalArgumentException("Depth buffer format must be depth.");

		depthBuf = new RenderBuffer();
		depthBuf.slot = format.isDepthStencilFormat() ? SLOT_DEPTH_STENCIL : SLOT_DEPTH;
		depthBuf.format = format;
	}

	public void setColorBuffer(Format format) {
		if (id != -1)
			throw new UnsupportedOperationException("FrameBuffer already initialized.");

		if (format.isDepthFormat())
			throw new IllegalArgumentException("Color buffer format must be color/luminance.");

		RenderBuffer colorBuf = new RenderBuffer();
		colorBuf.slot = 0;
		colorBuf.format = format;

		colorBufs.clear();
		colorBufs.add(colorBuf);
	}

	private void checkSetTexture(Texture tex, boolean depth) {
		if (depth && !tex.getFormat().isDepthFormat())
			throw new IllegalArgumentException("Texture image format must be depth.");
		else if (!depth && tex.getFormat().isDepthFormat())
			throw new IllegalArgumentException("Texture image format must be color/luminance.");

		// check that resolution matches texture resolution
		if (width != tex.getWidth() || height != tex.getHeight())
			throw new IllegalArgumentException("Texture image resolution " + "must match FB resolution");

		if (samples != tex.getImage().getMultiSamples())
			throw new IllegalStateException("Texture samples must match framebuffer samples");
	}

	public void addColorTexture(Texture2D tex) {
		if (id != -1)
			throw new UnsupportedOperationException("FrameBuffer already initialized.");

		checkSetTexture(tex, false);

		RenderBuffer colorBuf = new RenderBuffer();
		colorBuf.slot = colorBufs.size();
		colorBuf.tex = tex;
		colorBuf.format = tex.getFormat();

		colorBufs.add(colorBuf);
	}

	public int getNumColorBuffers() {
		return colorBufs.size();
	}

	public RenderBuffer getColorBuffer(int index) {
		return colorBufs.get(index);
	}

	public RenderBuffer getColorBuffer() {
		if (colorBufs.isEmpty())
			return null;

		return colorBufs.get(0);
	}

	public RenderBuffer getDepthBuffer() {
		return depthBuf;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public int getSamples() {
		return samples;
	}

	@Override
	public void deleteObject() {
		((Renderer) this.rendererObject).deleteFramebuffer(this);

		id = -1;
	}

	@Override
	public void resetObject() {
		id = -1;

		for (int i = 0; i < colorBufs.size(); i++) {
			colorBufs.get(i).resetObject();
		}

		if (depthBuf != null)
			depthBuf.resetObject();

		setUpdateNeeded();
	}

	public boolean isSrgb() {
		return srgb;
	}

	public void setSrgb(boolean srgb) {
		this.srgb = srgb;
	}

}
