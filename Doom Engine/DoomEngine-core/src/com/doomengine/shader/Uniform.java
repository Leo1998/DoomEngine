package com.doomengine.shader;

import java.nio.Buffer;
import java.nio.FloatBuffer;

import com.doomengine.math.Matrix3f;
import com.doomengine.math.Matrix4f;
import com.doomengine.math.Vector2f;
import com.doomengine.math.Vector3f;
import com.doomengine.math.Vector4f;
import com.doomengine.util.BufferUtils;

public class Uniform extends ShaderVariable {

	private static final Integer ZERO_INT = Integer.valueOf(0);
	private static final Float ZERO_FLT = Float.valueOf(0);
	private static final FloatBuffer ZERO_BUF = BufferUtils.createFloatBuffer(4 * 4);

	/**
	 * Currently set value of the uniform.
	 */
	protected Object value = null;

	/**
	 * For arrays or matrices, efficient format that can be sent to GL faster.
	 */
	protected FloatBuffer multiData = null;

	/**
	 * Type of uniform
	 */
	protected VarType varType;

	/**
	 * Used to track which uniforms to clear to avoid values leaking from other
	 * materials that use the same shader.
	 */
	protected boolean setByCurrentMaterial = false;

	public boolean isSetByCurrentMaterial() {
		return setByCurrentMaterial;
	}

	public void clearSetByCurrentMaterial() {
		setByCurrentMaterial = false;
	}

	public VarType getVarType() {
		return varType;
	}

	public Object getValue() {
		return value;
	}

	public void clearValue() {
		setUpdateNeeded();

		if (multiData != null) {
			multiData.clear();

			while (multiData.remaining() > 0) {
				ZERO_BUF.clear();
				ZERO_BUF.limit(Math.min(multiData.remaining(), 16));
				multiData.put(ZERO_BUF);
			}

			multiData.clear();

			return;
		}

		if (varType == null) {
			return;
		}

		switch (varType) {
		case Int:
			this.value = ZERO_INT;
			break;
		case Boolean:
			this.value = Boolean.FALSE;
			break;
		case Float:
			this.value = ZERO_FLT;
			break;
		case Vector2:
			this.value = Vector2f.ZERO;
			break;
		case Vector3:
			this.value = Vector3f.ZERO;
			break;
		case Vector4:
			this.value = Vector4f.ZERO;
			break;
		default:
			// won't happen because those are either textures
			// or multidata types
		}
	}

	public void setValue(VarType type, Object value) {
		if (location == -1) {
			return;
		}

		if (varType != null && varType != type) {
			throw new IllegalArgumentException("Expected a " + varType.name() + " value!");
		}

		if (value == null) {
			throw new NullPointerException("Value cannot be null!");
		}

		setByCurrentMaterial = true;

		switch (type) {
		case Matrix3:
			Matrix3f m3 = (Matrix3f) value;
			if (multiData == null) {
				multiData = BufferUtils.createFloatBuffer(9);
			}
			m3.store(multiData);
			multiData.clear();
			break;
		case Matrix4:
			Matrix4f m4 = (Matrix4f) value;
			if (multiData == null) {
				multiData = BufferUtils.createFloatBuffer(16);
			}
			m4.store(multiData);
			multiData.clear();
			break;
		case Int:
		case Float:
		case Boolean:
			if (this.value != null && this.value.equals(value)) {
				return;
			}
			this.value = value;
			break;
		default:
			this.value = value;// unneeded
			break;
		}

		if (multiData != null) {
			this.value = multiData;
		}

		varType = type;
		setUpdateNeeded();
	}

	public void deleteNativeBuffers() {
		if (value instanceof Buffer) {
			BufferUtils.disposeDirectBuffer((Buffer) value);
			value = null;
		}
	}
}
