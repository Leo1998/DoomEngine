package com.doomengine.math;

public class PerspectiveCamera extends Camera {

	private float fov;
	private float zNear;
	private float zFar;

	public PerspectiveCamera(int width, int height, float fov, float zNear, float zFar) {
		super(new Matrix4f().initPerspective(fov, (float) width / (float) height, zNear, zFar), width, height);

		this.fov = fov;
		this.zNear = zNear;
		this.zFar = zFar;
	}

	@Override public void resize(int width, int height) {
		super.resize(width, height);

		float aspect = (float) width / (float) height;

		this.projection = new Matrix4f().initPerspective(fov, aspect, zNear, zFar);
	}
}
