package com.doomengine.util;

import com.doomengine.asset.CloneableAsset;

public abstract class NativeObject implements CloneableAsset {

	protected int id = -1;
	protected Object rendererObject;
	private NativeObjectManager manager;
	protected boolean updateNeeded = true;

	public NativeObject() {
	}

	protected NativeObject(int id) {
		this.id = id;
	}

	@Override public NativeObject clone() {
		try {
			NativeObject obj = (NativeObject) super.clone();
			obj.manager = null;
			obj.id = -1;
			obj.updateNeeded = true;
			return obj;
		} catch (CloneNotSupportedException ex) {
			throw new AssertionError();
		}
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		if (this.id != -1) {
			throw new IllegalStateException("ID has already been set for this object.");
		}
		this.id = id;
	}

	public Object getRendererObject() {
		return rendererObject;
	}

	public void setRendererObject(Object rendererObject) {
		if (this.rendererObject != null) {
			throw new IllegalStateException("rendererObject has already been set for this object.");
		}
		this.rendererObject = rendererObject;
	}

	public NativeObjectManager getNativeObjectManager() {
		return manager;
	}

	public void setNativeObjectManager(NativeObjectManager manager) {
		if (this.manager != null) {
			throw new IllegalStateException("This Object allready has a NativeObjectManager!");
		}
		this.manager = manager;
	}

	public boolean isUpdateNeeded() {
		return updateNeeded;
	}

	public void clearUpdateNeeded() {
		updateNeeded = false;
	}

	public void setUpdateNeeded() {
		updateNeeded = true;
	}

	@Override public String toString() {
		return this.getClass().getSimpleName() + " id: " + id;
	}

	/**
	 * Only implementations that manage native buffers need to override this
	 * method.
	 */
	protected void deleteNativeBuffers() {
	}

	/**
	 * This method is called when this NativeObject should be deleted. After
	 * calling this method the objects id must be -1 and the NativeObject should
	 * not be used anymore.
	 * 
	 */
	public abstract void deleteObject();

	/**
	 * This method is called when the Context gets restarted. After calling this
	 * method the objects id must be -1.
	 */
	public abstract void resetObject();

}
