package com.doomengine.texture;

import com.doomengine.renderer.Renderer;
import com.doomengine.texture.image.Format;

public class TextureCubeMap extends Texture {

	private WrapMode wrapS = WrapMode.EDGE_CLAMP;
	private WrapMode wrapT = WrapMode.EDGE_CLAMP;
	private WrapMode wrapR = WrapMode.EDGE_CLAMP;

	// TODO: load cubemap assets

	public TextureCubeMap(int width, int height, Format format) {
		super(width, height, format);
		this.type = Type.Cubemap;
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

	public WrapMode getWrapR() {
		return wrapR;
	}

	public void setWrapR(WrapMode wrapR) {
		this.wrapR = wrapR;
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
