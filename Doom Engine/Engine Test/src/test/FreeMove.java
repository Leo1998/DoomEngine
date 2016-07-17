package test;

import com.doomengine.math.Vector3f;
import com.doomengine.scene.GameComponent;
import com.doomengine.system.Input;
import com.doomengine.system.Input.Keys;

public class FreeMove extends GameComponent {

	private float speed = 0.31f;

	public void input() {
		float xn = 0;
		float yn = 0;
		float zn = 0;

		if (Input.getKey(Keys.KEY_W)) {
			zn += speed;
		}
		if (Input.getKey(Keys.KEY_S)) {
			zn -= speed;
		}
		if (Input.getKey(Keys.KEY_D)) {
			xn += speed;
		}
		if (Input.getKey(Keys.KEY_A)) {
			xn -= speed;
		}

		if (Input.getKey(Keys.KEY_X)) {
			yn += speed;
		}
		if (Input.getKey(Keys.KEY_Y)) {
			yn -= speed;
		}

		getTransform().moveInDirection(getTransform().getRotation().getForward(), zn);
		getTransform().moveInDirection(getTransform().getRotation().getRight(), xn);
		getTransform().setPosition(getTransform().getPosition().add(new Vector3f(0, yn, 0)));

	}

}
