package com.wilderness;

import com.doomengine.lighting.PointLight;
import com.doomengine.math.ColorRGBA;
import com.doomengine.scene.GameComponent;
import com.doomengine.scene.GameObject;
import com.doomengine.system.Input;
import com.doomengine.system.Input.Keys;

public class LightPlacer extends GameComponent {
	
	private int key;
	
	public LightPlacer() {
		this(Keys.KEY_SPACE);
	}
	
	public LightPlacer(int key) {
		this.key = key;
	}
	
	@Override
	public void update(float deltaTime) {
		if (Input.getKeyDown(key)) {
			GameObject light = new GameObject();
			light.addComponent(new PointLight(ColorRGBA.WHITE, 3.0f));
			light.getTransform().setTo(this.getTransform());
			getScene().addObject(light);
		}
	}

}
