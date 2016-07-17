package com.doomengine.system;

import com.doomengine.math.Vector2f;

public interface IInputAdapter {

	public boolean getKey(int keyCode);

	public boolean getMouse(int mouseButton);

	public Vector2f getMousePosition();

	public void setMousePosition(Vector2f pos);

	public void setCursor(boolean enabled);

}
