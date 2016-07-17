package com.doomengine.util;

public abstract class Timer {

	public abstract long getTime();

	public float getTimeInSeconds() {
		return getTime() / (float) getResolution();
	}

	public abstract long getResolution();

	public abstract void reset();
}
