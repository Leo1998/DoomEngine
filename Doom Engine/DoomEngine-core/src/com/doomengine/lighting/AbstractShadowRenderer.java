package com.doomengine.lighting;

import com.doomengine.texture.FrameBuffer;
import com.doomengine.texture.Texture2D;
import com.doomengine.texture.image.Format;

public abstract class AbstractShadowRenderer<T extends BaseLight> {

	private T target;
	protected FrameBuffer shadowFrameBuffer;
	protected Texture2D shadowMap;

	public AbstractShadowRenderer(T target) {
		this.target = target;
	}

	protected void initFrameBuffer(int shadowMapSize) {
		this.shadowFrameBuffer = new FrameBuffer(shadowMapSize, shadowMapSize);

		this.shadowFrameBuffer.setDepthBuffer(Format.Depth16);
	}

	public T getTarget() {
		return target;
	}

	public FrameBuffer getShadowFrameBuffer() {
		return shadowFrameBuffer;
	}

	public Texture2D getShadowMap() {
		return shadowMap;
	}

}
