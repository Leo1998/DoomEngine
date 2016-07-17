package test;

import com.doomengine.math.Vector3f;
import com.doomengine.scene.GameComponent;

public class AutoTurn extends GameComponent {

	private Vector3f axis = new Vector3f(0, 1, 0);

	private float angle = 0.005f;

	public void tick() {
		getTransform().rotate(axis, angle);
	}

}
