package com.doomengine.texture;

public enum WrapMode {
	/**
	 * Only the fractional portion of the coordinate is considered.
	 */
	REPEAT,

	/**
	 * Only the fractional portion of the coordinate is considered, but if the
	 * integer portion is odd, we'll use 1 - the fractional portion. (Introduced
	 * around OpenGL1.4) Falls back on Repeat if not supported.
	 */
	MIRRORED_REPEAT,

	/**
	 * coordinate will be clamped to the range [1/(2N), 1 - 1/(2N)] where N is
	 * the size of the texture in the direction of clamping. Falls back on Clamp
	 * if not supported.
	 */
	EDGE_CLAMP;
}
