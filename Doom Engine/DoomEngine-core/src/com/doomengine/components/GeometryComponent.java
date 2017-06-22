package com.doomengine.components;

import com.doomengine.math.Transform;
import com.doomengine.renderer.Geometry;
import com.doomengine.renderer.RenderQueue;
import com.doomengine.scene.GameComponent;

public class GeometryComponent extends GameComponent {

	private Geometry geometry;

	public GeometryComponent(Geometry geometry) {
		this.geometry = geometry;
	}

	@Override
	public GameComponent clone() {
		GameComponent clone = super.clone();

		((GeometryComponent) clone).geometry = this.geometry.clone();

		return clone;
	}

	@Override
	public void render(RenderQueue queue) {
		Transform transform = this.getTransform();
		geometry.setTransformationMatrix(transform.getTransformation());

		queue.put(geometry);
	}

	public Geometry getGeometry() {
		return geometry;
	}

	public void setGeometry(Geometry geometry) {
		this.geometry = geometry;
	}

}
