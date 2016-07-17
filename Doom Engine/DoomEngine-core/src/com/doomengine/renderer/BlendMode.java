package com.doomengine.renderer;

public enum BlendMode {

	/**
	 * No blending mode is used.
	 */
	Off,
	/**
	 * Additive blending. For use with glows and particle emitters.
	 * <p>
	 * Result = Source Color + Destination Color -> (GL_ONE, GL_ONE)
	 */
	Additive,
	/**
	 * Premultiplied alpha blending, for use with premult alpha textures.
	 * <p>
	 * Result = Source Color + (Dest Color * (1 - Source Alpha) ) -> (GL_ONE,
	 * GL_ONE_MINUS_SRC_ALPHA)
	 */
	PremultAlpha,
	/**
	 * Additive blending that is multiplied with source alpha. For use with
	 * glows and particle emitters.
	 * <p>
	 * Result = (Source Alpha * Source Color) + Dest Color -> (GL_SRC_ALPHA,
	 * GL_ONE)
	 */
	AlphaAdditive,
	/**
	 * Color blending, blends in color from dest color using source color.
	 * <p>
	 * Result = Source Color + (1 - Source Color) * Dest Color -> (GL_ONE,
	 * GL_ONE_MINUS_SRC_COLOR)
	 */
	Color,
	/**
	 * Alpha blending, interpolates to source color from dest color using source
	 * alpha.
	 * <p>
	 * Result = Source Alpha * Source Color + (1 - Source Alpha) * Dest Color ->
	 * (GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA)
	 */
	Alpha,
	/**
	 * Multiplies the source and dest colors.
	 * <p>
	 * Result = Source Color * Dest Color -> (GL_DST_COLOR, GL_ZERO)
	 */
	Modulate,
	/**
	 * Multiplies the source and dest colors then doubles the result.
	 * <p>
	 * Result = 2 * Source Color * Dest Color -> (GL_DST_COLOR, GL_SRC_COLOR)
	 */
	ModulateX2,
	/**
	 * Opposite effect of Modulate/Multiply. Invert both colors, multiply and
	 * then invert the result.
	 * <p>
	 * Result = 1 - (1 - Source Color) * (1 - Dest Color) -> (GL_ONE,
	 * GL_ONE_MINUS_SRC_COLOR)
	 */
	Screen,
	/**
	 * Mixes the destination and source colors similar to a color-based XOR
	 * operation. This is directly equivalent to Photoshop's "Exclusion" blend.
	 * <p>
	 * Result = (Source Color * (1 - Dest Color)) + (Dest Color * (1 - Source
	 * Color)) -> (GL_ONE_MINUS_DST_COLOR, GL_ONE_MINUS_SRC_COLOR)
	 */
	Exclusion

}
