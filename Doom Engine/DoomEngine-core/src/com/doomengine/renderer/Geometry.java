package com.doomengine.renderer;

import com.doomengine.asset.CloneableAsset;
import com.doomengine.material.Material;
import com.doomengine.math.Matrix4f;

public class Geometry implements CloneableAsset {

	private Mesh mesh;
	private Material material;
	private Matrix4f transformationMatrix = new Matrix4f().initIdentity();

	public Geometry(Mesh mesh, Material material) {
		this.mesh = mesh;
		this.material = material;
	}

	@Override
	public Geometry clone() {
		try {
			Geometry clone = (Geometry) super.clone();

			clone.mesh = this.mesh;
			clone.material = this.material.clone();
			clone.transformationMatrix = new Matrix4f().initIdentity();

			return clone;
		} catch (CloneNotSupportedException e) {
			throw new AssertionError();
		}
	}

	public Mesh getMesh() {
		return mesh;
	}

	public void setMesh(Mesh mesh) {
		this.mesh = mesh;
	}

	public Material getMaterial() {
		return material;
	}

	public void setMaterial(Material material) {
		this.material = material;
	}

	public Matrix4f getTransformationMatrix() {
		return transformationMatrix;
	}

	public void setTransformationMatrix(Matrix4f transformationMatrix) {
		this.transformationMatrix = transformationMatrix;
	}

}
