package com.doomengine.math;

public class ColorRGBA extends Vector4f {

	public static final ColorRGBA OPAQUE = new ColorRGBA(0.0f, 0.0f, 0.0f, 0.0f);

	public static final ColorRGBA WHITE = new ColorRGBA(1.0f, 1.0f, 1.0f);

	public static final ColorRGBA BLACK = new ColorRGBA(0.0f, 0.0f, 0.0f);

	public static final ColorRGBA BLACK_NO_ALPHA = new ColorRGBA(0.0f, 0.0f, 0.0f, 0.0f);

	public static final ColorRGBA RED = new ColorRGBA(1.0f, 0.0f, 0.0f);

	public static final ColorRGBA GREEN = new ColorRGBA(0.0f, 1.0f, 0.0f);

	public static final ColorRGBA BLUE = new ColorRGBA(0.0f, 0.0f, 1.0f);

	public ColorRGBA() {
		this(BLACK);
	}

	public ColorRGBA(ColorRGBA color) {
		this(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
	}

	public ColorRGBA(float r, float g, float b) {
		this(r, g, b, 1.0f);
	}

	public ColorRGBA(float r, float g, float b, float a) {
		super(r, g, b, a);
	}

	public ColorRGBA(int r, int g, int b, int a) {
		super(r / 255, g / 255, b / 255, a / 255);

	}

	public float getRed() {
		return getX();
	}

	public void setRed(float r) {
		setX(r);
	}

	public float getGreen() {
		return getY();
	}

	public void setGreen(float g) {
		setY(g);
	}

	public float getBlue() {
		return getZ();
	}

	public void setBlue(float b) {
		setZ(b);
	}

	public float getAlpha() {
		return getW();
	}

	public void setA(float a) {
		setW(w);
	}

}
