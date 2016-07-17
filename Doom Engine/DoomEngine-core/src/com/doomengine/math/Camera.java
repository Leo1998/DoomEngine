package com.doomengine.math;

import com.doomengine.shader.Shader;
import com.doomengine.shader.VarType;

public abstract class Camera {

	protected Matrix4f view;

	protected Matrix4f projection;

	private Transform camTransform = new Transform();

	private int width;
	private int height;

	protected float viewPortLeft;
	protected float viewPortRight;
	protected float viewPortTop;
	protected float viewPortBottom;

	public Camera(Matrix4f projection, int width, int height) {
		this.width = width;
		this.height = height;
		this.projection = projection;

		viewPortLeft = 0.0f;
		viewPortRight = 1.0f;
		viewPortTop = 1.0f;
		viewPortBottom = 0.0f;
	}

	public void bind(Shader shader) {
		shader.getUniform("viewProjection").setValue(VarType.Matrix4, this.getViewProjection());

		shader.getUniform("eyePos").setValue(VarType.Vector3, this.getCamTransform().getPosition());
	}

	private void updateView() {
		Vector3f cameraPos = camTransform.getTransformedPosition().mul(-1);
		Matrix4f translationMatrix = new Matrix4f().initTranslation(cameraPos.getX(), cameraPos.getY(), cameraPos.getZ());
		Matrix4f rotationMatrix = camTransform.getTransformedRotation().conjugate().toRotationMatrix();

		Matrix4f mat = rotationMatrix.mul(translationMatrix);

		this.view = mat;
	}

	public void resize(int width, int height) {
		this.width = width;
		this.height = height;
	}

	public Matrix4f getViewMatrix() {
		return view;
	}

	public Matrix4f getProjectionMatrix() {
		return projection;
	}

	public Matrix4f getViewProjection() {
		Matrix4f mat = projection.mul(view);

		return mat;
	}

	public static Vector2f worldToScreenCoordinates(Vector3f worldPos, Camera camera) {
		// TODO: FIX!!!!!
		Vector3f point = new Vector3f(worldPos);
		point = point.normalized();

		Matrix4f v = camera.getViewMatrix();
		Matrix4f p = camera.getProjectionMatrix();

		Matrix4f mat = v.mul(p);

		point = mat.transform(point);

		float x = (point.getX() + 1) / 2;
		float y = (1 - point.getY()) / 2;

		Vector2f position = new Vector2f(x, y);

		return position;
	}

	public final Transform getCamTransform() {
		return camTransform;
	}

	public void setCamTransform(Transform camTransform) {
		this.camTransform = camTransform;
		this.updateView();
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public float getViewPortLeft() {
		return viewPortLeft;
	}

	public void setViewPortLeft(float viewPortLeft) {
		this.viewPortLeft = viewPortLeft;
	}

	public float getViewPortRight() {
		return viewPortRight;
	}

	public void setViewPortRight(float viewPortRight) {
		this.viewPortRight = viewPortRight;
	}

	public float getViewPortTop() {
		return viewPortTop;
	}

	public void setViewPortTop(float viewPortTop) {
		this.viewPortTop = viewPortTop;
	}

	public float getViewPortBottom() {
		return viewPortBottom;
	}

	public void setViewPortBottom(float viewPortBottom) {
		this.viewPortBottom = viewPortBottom;
	}
}
