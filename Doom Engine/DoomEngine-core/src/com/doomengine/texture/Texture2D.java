package com.doomengine.texture;

import com.doomengine.renderer.Renderer;
import com.doomengine.texture.image.Format;
import com.doomengine.texture.image.Image;

public class Texture2D extends Texture {

	private WrapMode wrapS = WrapMode.EDGE_CLAMP;
	private WrapMode wrapT = WrapMode.EDGE_CLAMP;

	public Texture2D(Image image) {
		super(image);
		this.type = Type.TwoDimensional;
	}

	public Texture2D(int width, int height, Format format) {
		super(width, height, format);
		this.type = Type.TwoDimensional;
	}

	public WrapMode getWrapS() {
		return wrapS;
	}

	public void setWrapS(WrapMode wrapS) {
		this.wrapS = wrapS;
		setUpdateNeeded();
	}

	public WrapMode getWrapT() {
		return wrapT;
	}

	public void setWrapT(WrapMode wrapT) {
		this.wrapT = wrapT;
		setUpdateNeeded();
	}

	@Override
	protected void deleteNativeBuffers() {
		this.image.dispose();
	}

	@Override
	public void deleteObject() {
		((Renderer) this.rendererObject).deleteTexture(this);

		id = -1;
	}

	@Override
	public void resetObject() {
		id = -1;

		setUpdateNeeded();
	}

}
