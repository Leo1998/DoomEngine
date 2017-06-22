package com.doomengine.renderer;

import com.doomengine.components.BaseLight;
import com.doomengine.components.LightList;

public class RenderQueue {

	private GeometryList opaqueList;
	private LightList lightList;

	public RenderQueue() {
		this.opaqueList = new GeometryList();
		this.lightList = new LightList();
	}

	public Geometry put(Geometry obj) {
		opaqueList.add(obj);

		return obj;
	}

	public GeometryList getOpaqueList() {
		return opaqueList;
	}

	public BaseLight put(BaseLight obj) {
		lightList.add(obj);

		return obj;
	}

	public LightList getLightList() {
		return lightList;
	}

	public void clear() {
		opaqueList.clear();
		lightList.clear();
	}

}
