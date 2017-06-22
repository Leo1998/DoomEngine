package com.doomengine.material;

import com.doomengine.asset.CloneableAsset;
import com.doomengine.renderer.technique.Technique;
import com.doomengine.shader.VarType;

public class MatParam implements CloneableAsset {

	protected VarType type;
	protected String name;
	protected String prefixedName;
	protected Object value;

	public MatParam(VarType type, String name, Object value) {
		this.type = type;
		this.name = name;
		this.prefixedName = "m_" + name;
		this.value = value;
	}

	@Override
	public MatParam clone() {
		try {
			MatParam param = (MatParam) super.clone();
			return param;
		} catch (CloneNotSupportedException ex) {
			throw new AssertionError();
		}
	}

	public void apply(Technique technique) {
		technique.updateUniformParam(getPrefixedName(), getVarType(), getValue());
	}

	public VarType getVarType() {
		return type;
	}

	/**
	 * Returns the name of the material parameter.
	 * 
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns the name with "m_" prefixed to it.
	 *
	 * @return the name
	 */
	public String getPrefixedName() {
		return prefixedName;
	}

	public void setName(String name) {
		this.name = name;
		this.prefixedName = "m_" + name;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

}
