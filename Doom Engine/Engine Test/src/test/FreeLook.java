package test;

import com.doomengine.components.Camera3D;
import com.doomengine.math.Vector2f;
import com.doomengine.math.Vector3f;
import com.doomengine.scene.GameComponent;
import com.doomengine.system.Input;
import com.doomengine.system.Input.Keys;

public class FreeLook extends GameComponent {

	private boolean mouseLocked = false;

	private float sensitivity = 0.6f;

	private int unlockMouseKey = Keys.KEY_ESCAPE;

	public void input() {
		Camera3D cam = this.getScene().getMainCamera();
		Vector2f centerPosition = new Vector2f(cam.getCam().getWidth() / 2, cam.getCam().getHeight() / 2);

		if (Input.getKey(unlockMouseKey)) {
			Input.setCursor(true);
			mouseLocked = false;
		}
		if (Input.getMouseDown(0)) {
			Input.setMousePosition(centerPosition);
			Input.setCursor(false);
			mouseLocked = true;
		}

		if (mouseLocked) {
			Vector2f deltaPos = Input.getMousePosition().sub(centerPosition);

			boolean rotY = deltaPos.getX() != 0;
			boolean rotX = deltaPos.getY() != 0;

			if (rotY)
				getTransform().rotate(new Vector3f(0, 1, 0), (float) Math.toRadians(deltaPos.getX() * sensitivity));
			if (rotX)
				getTransform().rotate(getTransform().getRotation().getRight(), (float) Math.toRadians(-deltaPos.getY() * sensitivity));

			if (rotY || rotX)
				Input.setMousePosition(centerPosition);
		}
	}

}
