package com.doomengine.math;

import com.doomengine.asset.AssetManager;
import com.doomengine.scene.GameObject;
import com.doomengine.scene.ModelKey;

public class ShapeFactory {

	private static GameObject baseBox;

	public static void init(AssetManager assetManager) {
		baseBox = assetManager.loadAsset(new ModelKey("/engine/box.obj"));
	}

	public static GameObject createBox(Vector3f center, float width, float height, float depth) {
		GameObject o = baseBox.clone();

		o.getTransform().setPosition(center);
		o.getTransform().setScale(new Vector3f(width, height, depth));

		return o;
	}

}
