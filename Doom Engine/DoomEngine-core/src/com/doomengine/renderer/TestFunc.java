package com.doomengine.renderer;

public enum TestFunc {

	/**
	 * The test always fails
	 */
	Never,
	/**
	 * The test succeeds if the input value is equal to the reference value.
	 */
	Equal,
	/**
	 * The test succeeds if the input value is less than the reference value.
	 */
	Less,
	/**
	 * The test succeeds if the input value is less than or equal to the
	 * reference value.
	 */
	LessOrEqual,
	/**
	 * The test succeeds if the input value is greater than the reference value.
	 */
	Greater,
	/**
	 * The test succeeds if the input value is greater than or equal to the
	 * reference value.
	 */
	GreaterOrEqual,
	/**
	 * The test succeeds if the input value does not equal the reference value.
	 */
	NotEqual,
	/**
	 * The test always passes
	 */
	Always,

}
