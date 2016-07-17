package com.doomengine.util;

import java.util.ArrayDeque;
import java.util.ArrayList;

import com.doomengine.system.Logger;

public class NativeObjectManager {

	private static final int MAX_REMOVES_PER_FRAME = 10;

	private ArrayList<NativeObject> registeredObjects = new ArrayList<NativeObject>();

	private ArrayDeque<NativeObject> userDeletionQueue = new ArrayDeque<NativeObject>();

	public void registerObject(NativeObject obj, Object rendererObject) {
		if (obj.getId() <= 0) {
			throw new IllegalArgumentException("object id must be greater than zero");
		}

		registeredObjects.add(obj);

		obj.setNativeObjectManager(this);
		obj.setRendererObject(rendererObject);
	}

	private void deleteNativeObject(NativeObject obj) {
		assert obj != null;

		if (obj.getId() <= 0) {
			Logger.log("Object already deleted: " + obj);
		} else {
			if (!registeredObjects.contains(obj)) {
				throw new IllegalArgumentException("This NativeObject is not registered in this NativeObjectManager");
			}
			registeredObjects.remove(obj);

			int id = obj.getId();

			obj.deleteObject();
			assert obj.getId() == -1;

			Logger.log("Deleted: " + obj.getClass().getSimpleName() + " id: " + id);
		}

		obj.deleteNativeBuffers();
	}

	/**
	 * Deletes unused NativeObjects. Will delete at most
	 * {@link #MAX_REMOVES_PER_FRAME} objects.
	 * 
	 * @param rendererObject
	 */
	public void deleteUnused() {
		int removed = 0;
		while (removed < MAX_REMOVES_PER_FRAME && !userDeletionQueue.isEmpty()) {
			NativeObject obj = userDeletionQueue.pop();
			deleteNativeObject(obj);
			removed++;
		}

		if (removed >= 1) {
			Logger.log("NativeObjectManager: " + removed + " native objects were removed from NativeObjectManager");
		}
	}

	/**
	 * Must only be called when display is destroyed.
	 */
	public void deleteAllObjects() {
		deleteUnused();

		ArrayList<NativeObject> mapCopy = new ArrayList<NativeObject>(registeredObjects);
		for (int i = 0; i < mapCopy.size(); i++) {
			deleteNativeObject(mapCopy.get(i));
		}

		mapCopy.clear();
	}

	/**
	 * Marks the given <code>NativeObject</code> as unused, to be deleted on the
	 * next frame.
	 * 
	 * @param obj
	 *            The object to mark as unused.
	 */
	void enqueueUnusedObject(NativeObject obj) {
		userDeletionQueue.push(obj);
	}

	public void resetObjects() {
		for (NativeObject obj : registeredObjects) {
			obj.resetObject();
		}
	}

}
