package com.doomengine.lwjgl;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import com.doomengine.math.Vector2f;
import com.doomengine.system.IInputAdapter;

public class LwjglInputAdapter implements IInputAdapter {

	@Override public boolean getKey(int keyCode) {
		return Keyboard.isKeyDown(keyCode);
	}

	@Override public boolean getMouse(int mouseButton) {
		return Mouse.isButtonDown(mouseButton);
	}

	@Override public Vector2f getMousePosition() {
		return new Vector2f(Mouse.getX(), Mouse.getY());
	}

	@Override public void setMousePosition(Vector2f pos) {
		Mouse.setCursorPosition((int) pos.getX(), (int) pos.getY());
	}

	@Override public void setCursor(boolean enabled) {
		Mouse.setGrabbed(!enabled);
	}

}
