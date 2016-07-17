package com.doomengine.shader;

public class ShaderVariable {

	/**
	 * if -2, location not known if -1, not defined in shader if >= 0, uniform
	 * defined and available.
	 */
	protected int location = -2;

	/**
	 * Name of the uniform as was declared in the shader.
	 */
	private String name = null;

	/**
	 * If the shader value was changed.
	 */
	private boolean updateNeeded = true;

	public int getLocation() {
		return location;
	}

	public void setLocation(int location) {
		this.location = location;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Forces an update of the Variables location
	 */
	public void resetLocation() {
		location = -2;
	}

	public boolean isUpdateNeeded() {
		return updateNeeded;
	}

	public void setUpdateNeeded() {
		updateNeeded = true;
	}

	public void clearUpdateNeeded() {
		updateNeeded = false;
	}
}
