package com.doomengine.components;

import com.doomengine.app.Application;
import com.doomengine.math.Vector2f;
import com.doomengine.math.Vector3f;
import com.doomengine.scene.GameComponent;
import com.doomengine.system.Input;
import com.doomengine.system.Input.Keys;

public class EulerController extends GameComponent {

	private static final Vector3f Y_AXIS = new Vector3f(0, 1, 0);

	private float speed;
	private int forwardKey;
	private int backKey;
	private int leftKey;
	private int rightKey;
	private float sensitivity;
	private int unlockMouseKey;

	private boolean m_mouseLocked = false;

	public EulerController(float speed, float sensitivity) {
		this(speed, Keys.KEY_W, Keys.KEY_S, Keys.KEY_A, Keys.KEY_D, sensitivity, Keys.KEY_ESCAPE);
	}

	public EulerController(float speed, int forwardKey, int backKey, int leftKey, int rightKey, float sensitivity, int unlockMouseKey) {
		this.speed = speed;
		this.forwardKey = forwardKey;
		this.backKey = backKey;
		this.leftKey = leftKey;
		this.rightKey = rightKey;
		this.sensitivity = sensitivity;
		this.unlockMouseKey = unlockMouseKey;
	}

	@Override public void update(float deltaTime) {
		float movAmt = speed * deltaTime;

		if (Input.getKey(forwardKey))
			getTransform().moveInDirection(getTransform().getRotation().getForward(), movAmt);
		if (Input.getKey(backKey))
			getTransform().moveInDirection(getTransform().getRotation().getForward(), -movAmt);
		if (Input.getKey(leftKey))
			getTransform().moveInDirection(getTransform().getRotation().getLeft(), movAmt);
		if (Input.getKey(rightKey))
			getTransform().moveInDirection(getTransform().getRotation().getRight(), movAmt);

		//

		Vector2f screenSize = Application.instance.getScreenSize();

		Vector2f centerPosition = new Vector2f((int) screenSize.getX() / 2, (int) screenSize.getY() / 2);

		if (Input.getKey(unlockMouseKey)) {
			Input.setCursor(true);
			m_mouseLocked = false;
		}
		if (Input.getMouseDown(0)) {
			Input.setMousePosition(centerPosition);
			Input.setCursor(false);
			m_mouseLocked = true;
		}

		if (m_mouseLocked) {
			Vector2f deltaPos = Input.getMousePosition().sub(centerPosition);

			boolean rotY = deltaPos.getX() != 0;
			boolean rotX = deltaPos.getY() != 0;

			if (rotY)
				getTransform().rotate(Y_AXIS, (float) Math.toRadians(deltaPos.getX() * sensitivity));
			if (rotX)
				getTransform().rotate(getTransform().getRotation().getRight(), (float) Math.toRadians(-deltaPos.getY() * sensitivity));

			if (rotY || rotX)
				Input.setMousePosition(centerPosition);
		}
	}

}
