package com.doomengine.math;

import com.doomengine.asset.AssetManager;
import com.doomengine.scene.GameObject;
import com.doomengine.scene.ModelKey;

public class ShapeFactory {

	private static GameObject baseBox;
	private static GameObject baseSphere;

	public static void init(AssetManager assetManager) {
		baseBox = assetManager.loadAsset(new ModelKey("/engine/box.obj"));
		baseSphere = assetManager.loadAsset(new ModelKey("/engine/sphere.obj"));
	}

	public static GameObject createBox(Vector3f center, float width, float height, float depth) {
		GameObject o = baseBox.clone();

		o.getTransform().setPosition(center);
		o.getTransform().setScale(new Vector3f(width, height, depth));

		return o;
	}

	public static GameObject createSphere(Vector3f center, float radius) {
		GameObject o = baseSphere.clone();

		o.getTransform().setPosition(center);
		o.getTransform().getScale().set(radius, radius, radius);

		return o;
	}

}
