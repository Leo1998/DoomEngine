package com.doomengine.renderer;

public class RenderQueue {

	private GeometryList opaqueList;

	public RenderQueue() {
		this.opaqueList = new GeometryList();
	}

	private void renderGeometryList(GeometryList list, Technique technique, boolean clear) {
		for (int i = 0; i < list.size(); i++) {
			Geometry obj = list.get(i);

			technique.renderGeometry(obj);
		}
		if (clear) {
			list.clear();
		}
	}

	public Geometry put(Geometry obj) {
		opaqueList.add(obj);

		return obj;
	}

	public void renderQueue(Technique technique, boolean clear) {
		renderGeometryList(opaqueList, technique, clear);
	}

	public void clear() {
		opaqueList.clear();
	}

}
