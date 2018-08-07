package com.doomengine.lighting;

import com.doomengine.math.ColorRGBA;
import com.doomengine.renderer.RenderQueue;
import com.doomengine.scene.GameComponent;

public abstract class BaseLight extends GameComponent {

	private ColorRGBA color;

	public BaseLight(ColorRGBA color) {
		this.color = color;
	}

	public void render(RenderQueue queue) {
		queue.put(this);
	}

	public ColorRGBA getColor() {
		return color;
	}

	public void setColor(ColorRGBA color) {
		this.color = color;
	}

}
