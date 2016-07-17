package com.doomengine.asset;

public interface CloneableAsset extends Cloneable {

	/**
	 * Creates a Clone of the Asset
	 * 
	 * {@link Object#clone()}
	 * 
	 * @return
	 */
	public Object clone();

}
