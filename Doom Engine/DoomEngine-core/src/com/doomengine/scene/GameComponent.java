package com.doomengine.scene;

import com.doomengine.asset.CloneableAsset;
import com.doomengine.math.Transform;
import com.doomengine.renderer.RenderQueue;

public abstract class GameComponent implements CloneableAsset {

	protected GameObject parent;

	@Override
	public GameComponent clone() {
		try {
			GameComponent clone = (GameComponent) super.clone();

			// NOTE: No need to clone parent

			return clone;
		} catch (CloneNotSupportedException e) {
			throw new AssertionError();
		}
	}

	public void input() {
	}

	public void update(float deltaTime) {
	}

	public void render(RenderQueue queue) {
	}

	public GameObject getParent() {
		return parent;
	}

	public void setParent(GameObject parent) {
		this.parent = parent;
	}

	public Transform getTransform() {
		return parent.getTransform();
	}

	public Scene getScene() {
		return this.parent.getScene();
	}

	public void addToScene(Scene scene) {
	}

	public void removeFromScene(Scene scene) {
	}

}
