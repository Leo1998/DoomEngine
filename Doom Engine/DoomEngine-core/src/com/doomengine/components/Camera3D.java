package com.doomengine.components;

import com.doomengine.math.Camera;
import com.doomengine.math.PerspectiveCamera;
import com.doomengine.scene.GameComponent;
import com.doomengine.scene.Scene;

public class Camera3D extends GameComponent {

	private Camera cam;

	public Camera3D(int width, int height, float fov, float zNear, float zFar) {
		this.cam = new PerspectiveCamera(width, height, fov, zNear, zFar);
	}

	@Override
	public void update(float deltaTime) {
		this.cam.setCamTransform(this.getTransform());
	}

	@Override
	public void addToScene(Scene scene) {
		scene.setMainCamera(this);
	}

	@Override
	public void removeFromScene(Scene scene) {
		scene.setMainCamera(null);
	}

	public Camera getCam() {
		return cam;
	}

}
