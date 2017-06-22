package com.doomengine.material;

import com.doomengine.math.ColorSpace;
import com.doomengine.renderer.Renderer;
import com.doomengine.renderer.technique.Technique;
import com.doomengine.shader.VarType;
import com.doomengine.texture.Texture;

public class MatParamTexture extends MatParam {

	private Texture texture;
	private int unit;
	private ColorSpace colorSpace;

	public MatParamTexture(VarType type, String name, Texture texture, int unit, ColorSpace colorSpace) {
		super(type, name, texture);
		this.texture = texture;
		this.unit = unit;
		this.colorSpace = colorSpace;
	}

	@Override
	public void apply(Technique technique) {
		Renderer renderer = technique.getRenderer();
		renderer.bindTexture(getTextureValue(), getUnit());
		technique.updateUniformParam(getPrefixedName(), getVarType(), Integer.valueOf(getUnit()));
	}

	public Texture getTextureValue() {
		return texture;
	}

	public void setTextureValue(Texture value) {
		this.value = value;
		this.texture = value;
	}

	@Override
	public void setValue(Object value) {
		if (!(value instanceof Texture)) {
			throw new IllegalArgumentException("value must be a texture");
		}
		this.value = value;
		this.texture = (Texture) value;
	}

	public ColorSpace getColorSpace() {
		return colorSpace;
	}

	public void setColorSpace(ColorSpace colorSpace) {
		this.colorSpace = colorSpace;
	}

	public int getUnit() {
		return unit;
	}

	public void setUnit(int unit) {
		this.unit = unit;
	}

}
