package com.doomengine.scene;

import com.doomengine.components.Camera3D;
import com.doomengine.renderer.RenderQueue;

/**
 * The Class Scene.
 */
public class Scene {

	/** The root. */
	private GameObject root;

	private Camera3D mainCamera = null;

	public Scene() {
		root = new GameObject();

		root.setScene(this);
	}

	public void input() {
		getRootObject().inputAll();
	}

	public void update(float deltaTime) {
		getRootObject().updateAll(deltaTime);
	}

	public void render(RenderQueue queue) {
		getRootObject().renderAll(queue);
	}

	public void addObject(GameObject object) {
		getRootObject().addChild(object);
	}

	public GameObject getRootObject() {
		return root;
	}

	public Camera3D getMainCamera() {
		return mainCamera;
	}

	public void setMainCamera(Camera3D mainCamera) {
		this.mainCamera = mainCamera;
	}
}
