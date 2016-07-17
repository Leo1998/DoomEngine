package com.doomengine.math;

public class MathHelper {

	public static boolean isPowerOfTwo(int number) {
		return (number > 0) && (number & (number - 1)) == 0;
	}

}
