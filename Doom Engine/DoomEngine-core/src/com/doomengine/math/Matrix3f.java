package com.doomengine.math;

import java.nio.FloatBuffer;

public class Matrix3f {

	private float[][] m;

	public Matrix3f() {
		m = new float[3][3];
		initIdentity();
	}

	public Matrix3f initIdentity() {
		m[0][0] = 1;
		m[0][1] = 0;
		m[0][2] = 0;

		m[1][0] = 0;
		m[1][1] = 1;
		m[1][2] = 0;

		m[2][0] = 0;
		m[2][1] = 0;
		m[2][2] = 1;

		return this;
	}

	public Matrix3f initTranslation(float x, float y) {
		m[2][0] = x;
		m[2][1] = y;

		return this;
	}

	public Matrix3f initRotation2D(float angle) {
		float r = (float) Math.toRadians(angle);
		float cos = (float) Math.cos(r);
		float sin = (float) Math.sin(r);

		m[0][0] = cos;
		m[1][0] = sin;
		m[0][1] = -sin;
		m[1][1] = cos;

		return this;
	}

	public Matrix3f initScale(float x, float y) {
		m[0][0] = x;
		m[1][1] = y;

		return this;
	}

	public Matrix3f mul(Matrix3f r) {
		Matrix3f res = new Matrix3f();

		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				res.set(i, j, m[i][0] * r.get(0, j) + m[i][1] * r.get(1, j) + m[i][2] * r.get(2, j));
			}
		}

		return res;
	}

	public float[][] getM() {
		float[][] res = new float[3][3];

		for (int i = 0; i < 3; i++)
			for (int j = 0; j < 3; j++)
				res[i][j] = m[i][j];

		return res;
	}

	public float get(int x, int y) {
		return m[x][y];
	}

	public void setM(float[][] m) {
		this.m = m;
	}

	public void set(int x, int y, float value) {
		m[x][y] = value;
	}

	public Matrix3f store(FloatBuffer buf) {
		for (int i = 0; i < 3; i++)
			for (int j = 0; j < 3; j++)
				buf.put(m[i][j]);

		return this;
	}

	@Override public String toString() {
		return "(" + m[0][0] + "  " + m[1][0] + "  " + m[2][0] + "\n" + m[0][1] + "  " + m[1][1] + "  " + m[2][1] + "\n" + m[0][2] + "  " + m[1][2] + "  " + m[2][2] + ")";
	}
}
