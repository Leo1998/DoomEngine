package com.doomengine.util;

public class NanoTimer extends Timer {

	private static final long TIMER_RESOLUTION = 1000000000L;

	private long startTime;

	public NanoTimer() {
		startTime = System.nanoTime();
	}

	@Override
	public long getTime() {
		return System.nanoTime() - startTime;
	}

	@Override
	public long getResolution() {
		return TIMER_RESOLUTION;
	}

	@Override
	public void reset() {
		startTime = System.nanoTime();
	}

}
