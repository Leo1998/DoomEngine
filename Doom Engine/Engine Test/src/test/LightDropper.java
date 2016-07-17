package test;

import com.doomengine.components.PointLight;
import com.doomengine.math.ColorRGBA;
import com.doomengine.math.Quaternion;
import com.doomengine.math.Vector3f;
import com.doomengine.scene.GameComponent;
import com.doomengine.scene.GameObject;
import com.doomengine.system.Input;
import com.doomengine.system.Input.Keys;

public class LightDropper extends GameComponent {

	public void input() {
		if (Input.getKeyDown(Keys.KEY_SPACE)) {
			GameObject lightObject = new GameObject();
			lightObject.getTransform().setPosition(new Vector3f(this.getTransform().getTransformedPosition()));
			lightObject.getTransform().setRotation(new Quaternion(this.getTransform().getTransformedRotation()));
			lightObject.addComponent(new PointLight(new ColorRGBA((float) (Math.random()), (float) (Math.random()), (float) (Math.random())), 7.0f));
			this.getScene().addObject(lightObject);
		}
	}

}
