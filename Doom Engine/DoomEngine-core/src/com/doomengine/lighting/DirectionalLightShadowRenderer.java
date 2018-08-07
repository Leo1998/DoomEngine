package com.doomengine.lighting;

public class DirectionalLightShadowRenderer extends AbstractShadowRenderer<DirectionalLight> {

	public DirectionalLightShadowRenderer(DirectionalLight target) {
		super(target);
		
		initFrameBuffer(1024);
	}
	
}
