package com.doomengine.material;

import java.util.HashMap;
import java.util.Map;

import com.doomengine.asset.CloneableAsset;
import com.doomengine.math.ColorRGBA;
import com.doomengine.math.Matrix4f;
import com.doomengine.math.Vector2f;
import com.doomengine.math.Vector3f;
import com.doomengine.math.Vector4f;
import com.doomengine.renderer.Technique;
import com.doomengine.shader.VarType;
import com.doomengine.texture.Texture;

public class Material implements CloneableAsset {

	private String name = null;
	private Map<String, MatParam> paramValues = new HashMap<String, MatParam>();

	private int nextTexUnit = 0;

	public Material(String name) {
		this(name, (MatParam[]) null);
	}

	public Material(String name, MatParam... params) {
		this.name = name;

		if (params != null) {
			for (MatParam param : params) {
				if (param != null && param.getValue() != null) {
					setParam(param.getName(), param.getVarType(), param.getValue());
				}
			}
		}
	}

	@Override public Material clone() {
		try {
			Material mat = (Material) super.clone();

			mat.paramValues = new HashMap<String, MatParam>();
			for (MatParam param : paramValues.values()) {
				mat.paramValues.put(param.getName(), param.clone());
			}

			return mat;
		} catch (CloneNotSupportedException ex) {
			throw new AssertionError(ex);
		}
	}

	public void apply(Technique technique) {
		for (MatParam param : paramValues.values()) {
			param.apply(technique);
		}
	}

	public String getName() {
		return name;
	}

	public MatParam getParam(String name) {
		return paramValues.get(name);
	}

	public MatParamTexture getTextureParam(String name) {
		MatParam param = paramValues.get(name);
		if (param instanceof MatParamTexture) {
			return (MatParamTexture) param;
		}
		return null;
	}

	public void setParam(String name, VarType type, Object value) {
		if (type.isTextureType()) {
			setTextureParam(name, type, (Texture) value);
		} else {
			MatParam val = getParam(name);
			if (val == null) {
				paramValues.put(name, new MatParam(type, name, value));
			} else {
				val.setValue(value);
			}
		}
	}

	public void setTextureParam(String name, VarType type, Texture value) {
		MatParamTexture val = getTextureParam(name);
		if (val == null) {
			paramValues.put(name, new MatParamTexture(type, name, value, nextTexUnit++, null));
		} else {
			val.setTextureValue(value);
		}
	}

	public void setTexture(String name, Texture value) {
		VarType paramType = null;
		switch (value.getType()) {
		case TwoDimensional:
			paramType = VarType.Texture2D;
			break;
		case Cubemap:
			paramType = VarType.TextureCubeMap;
			break;
		default:
			throw new UnsupportedOperationException("Unknown texture type: " + value.getType());
		}

		setTextureParam(name, paramType, value);
	}

	public void setMatrix4(String name, Matrix4f value) {
		setParam(name, VarType.Matrix4, value);
	}

	public void setBoolean(String name, boolean value) {
		setParam(name, VarType.Boolean, value);
	}

	public void setFloat(String name, float value) {
		setParam(name, VarType.Float, value);
	}

	public void setFloat(String name, Float value) {
		setParam(name, VarType.Float, value);
	}

	public void setInt(String name, int value) {
		setParam(name, VarType.Int, value);
	}

	public void setColor(String name, ColorRGBA value) {
		setParam(name, VarType.Vector4, value);
	}

	public void setVector2(String name, Vector2f value) {
		setParam(name, VarType.Vector2, value);
	}

	public void setVector3(String name, Vector3f value) {
		setParam(name, VarType.Vector3, value);
	}

	public void setVector4(String name, Vector4f value) {
		setParam(name, VarType.Vector4, value);
	}

}
