package com.doomengine.math;

import com.doomengine.asset.CloneableAsset;

public class Transform implements CloneableAsset {

	private Transform parent;

	private Matrix4f parentMatrix;
	private Matrix4f transformationMatrix;

	private Vector3f position;
	private Quaternion rotation;
	private Vector3f scale;

	private Vector3f oldPosition;
	private Quaternion oldRotation;
	private Vector3f oldScale;

	public Transform() {
		this(new Vector3f(0, 0, 0), new Quaternion(0, 0, 0, 1), new Vector3f(1, 1, 1));
	}

	public Transform(Vector3f position) {
		this(position, new Quaternion(0, 0, 0, 1), new Vector3f(1, 1, 1));
	}

	public Transform(Vector3f position, Quaternion rotation) {
		this(position, rotation, new Vector3f(1, 1, 1));
	}

	public Transform(Vector3f position, Quaternion rotation, Vector3f scale) {
		this.position = position;
		this.rotation = rotation;
		this.scale = scale;

		this.parentMatrix = new Matrix4f().initIdentity();

		update();
	}

	@Override
	public Transform clone() {
		try {
			Transform clone = (Transform) super.clone();

			clone.parent = this.parent;

			clone.position = new Vector3f(this.position);
			clone.rotation = new Quaternion(this.rotation);
			clone.scale = new Vector3f(this.scale);

			// NOTE: This just forces an update
			clone.oldPosition = new Vector3f(0, 0, 0).set(clone.position).add(1.0f);
			clone.oldRotation = new Quaternion(clone.rotation.mul(0.5f));
			clone.oldScale = new Vector3f(0, 0, 0).set(clone.scale).add(1.0f);

			clone.transformationMatrix = null;

			return clone;
		} catch (CloneNotSupportedException e) {
			throw new AssertionError();
		}
	}

	@Override
	public String toString() {
		return "Transform [position=" + position + ", rotation=" + rotation + ", scale=" + scale + "]";
	}

	public boolean equals(Transform other) {
		if (other.position.equals(position)) {
			return true;
		}
		if (other.rotation.equals(rotation)) {
			return true;
		}
		if (other.scale.equals(scale)) {
			return true;
		}

		return false;
	}

	public void update() {
		if (oldPosition != null) {
			oldPosition.set(position);
			oldRotation.set(rotation);
			oldScale.set(scale);
		} else {
			oldPosition = new Vector3f(0, 0, 0).set(position).add(1.0f);
			oldRotation = new Quaternion(rotation.mul(0.5f));
			oldScale = new Vector3f(0, 0, 0).set(scale).add(1.0f);
		}
	}

	public boolean hasChanged() {
		if (parent != null && parent.hasChanged())
			return true;
		if (!position.equals(oldPosition))
			return true;
		if (!rotation.equals(oldRotation))
			return true;
		if (!scale.equals(oldScale))
			return true;

		return false;
	}

	public Matrix4f getTransformation() {
		if (this.hasChanged()) {
			transformationMatrix = getTransformationMatrix();

			update();
		}

		return transformationMatrix;
	}

	private Matrix4f getTransformationMatrix() {
		Matrix4f translation = getPositionMatrix();
		Matrix4f rotation = getRotationMatrix();
		Matrix4f scale = getScaleMatrix();

		Matrix4f mat = getParentMatrix().mul(translation.mul(rotation.mul(scale)));

		return mat;
	}

	public Matrix4f getPositionMatrix() {
		float x = getPosition().getX();
		float y = getPosition().getY();
		float z = getPosition().getZ();

		return new Matrix4f().initTranslation(x, y, z);
	}

	public Matrix4f getRotationMatrix() {
		return rotation.toRotationMatrix();
	}

	public Matrix4f getScaleMatrix() {
		float sx = getScale().getX();
		float sy = getScale().getY();
		float sz = getScale().getZ();

		return new Matrix4f().initScale(sx, sy, sz);
	}

	public Matrix4f getTransformedPositionMatrix() {
		Vector3f transformedPos = getTransformedPosition();

		float x = transformedPos.getX();
		float y = transformedPos.getY();
		float z = transformedPos.getZ();

		return new Matrix4f().initTranslation(x, y, z);
	}

	public Vector3f getTransformedPosition() {
		return getParentMatrix().transform(getPosition());
	}

	public Quaternion getTransformedRotation() {
		Quaternion parentRotation = new Quaternion(0, 0, 0, 1);

		if (parent != null)
			parentRotation = parent.getTransformedRotation();

		return new Quaternion(parentRotation.mul(rotation));
	}

	private Matrix4f getParentMatrix() {
		if (this.parent != null && parent.hasChanged()) {
			parentMatrix = parent.getTransformation();
		}
		return parentMatrix;
	}

	public Transform getParent() {
		return parent;
	}

	public void setParent(Transform parent) {
		this.parent = parent;
	}

	public void moveInDirection(Vector3f dir, float amount) {
		setPosition(getPosition().add(dir.mul(amount)));
	}

	public void rotate(Vector3f axis, float angle) {
		Vector4f vector = new Quaternion(axis, angle).mul(getRotation()).normalized();
		setRotation(new Quaternion(vector));
	}

	public void lookAt(Vector3f point, Vector3f up) {
		setRotation(getLookAtRotation(point, up));
	}

	private Quaternion getLookAtRotation(Vector3f point, Vector3f up) {
		return new Quaternion(new Matrix4f().initRotation(point.sub(getPosition()).normalized(), up));
	}

	public final void setTo(Transform other) {// TODO allow to set in world
												// coordinates
		this.setPosition(other.getPosition());
		this.setRotation(other.getRotation());
		this.setScale(other.getScale());
	}

	public final Vector3f getPosition() {
		return position;
	}

	public void setPosition(Vector3f position) {
		this.position.set(position);
	}

	public final Quaternion getRotation() {
		return rotation;
	}

	public void setRotation(Quaternion rotation) {
		this.rotation.set(rotation);
	}

	public final Vector3f getScale() {
		return scale;
	}

	public void setScale(Vector3f scale) {
		this.scale.set(scale);
	}

}
