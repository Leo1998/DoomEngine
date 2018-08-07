package com.doomengine.renderer;

import com.doomengine.math.BoundingBox;
import com.doomengine.math.BoundingVolume;
import com.doomengine.math.Vector3f;
import com.doomengine.renderer.VertexBuffer.Usage;

public class Mesh {

	private VertexBufferObject vertexBuffer;
	private IndexBufferObject indexBuffer;

	private Usage usage;
	private DrawMode drawMode = DrawMode.Triangles;
	private BoundingVolume boundingVolume;

	public Mesh(float[] vertices, short[] indices, VertexAttributes attributes) {
		this(vertices, indices, Usage.Static, attributes);
	}

	public Mesh(float[] vertices, short[] indices, Usage usage, VertexAttributes attributes) {
		this.usage = usage;

		setVertices(vertices, indices, attributes);
	}

	private void setVertices(float[] vertices, short[] indices, VertexAttributes attributes) {
		vertexBuffer = new VertexBufferObject(vertices.length, usage, attributes);
		vertexBuffer.setVertices(vertices);

		indexBuffer = new IndexBufferObject(indices.length, usage);
		indexBuffer.setIndices(indices);

		calcBounds();
	}

	public int getNumVertices() {
		return vertexBuffer.getSize();
	}

	public int getNumIndices() {
		return indexBuffer != null ? indexBuffer.getSize() : 0;
	}

	public DrawMode getDrawMode() {
		return drawMode;
	}

	public void setDrawMode(DrawMode drawMode) {
		this.drawMode = drawMode;
	}

	public Usage getUsage() {
		return usage;
	}

	public VertexBufferObject getVertexBuffer() {
		return vertexBuffer;
	}

	public IndexBufferObject getIndexBuffer() {
		return indexBuffer;
	}

	public BoundingVolume getBoundingVolume() {
		return boundingVolume;
	}

	private void calcBounds() {
		float[] buffer = vertexBuffer.getBuffer("a_position");

		Vector3f min = Vector3f.POSITIVE_INFINITY;
		Vector3f max = Vector3f.NEGATIVE_INFINITY;

		Vector3f pos = new Vector3f();
		for (int i = 0; i < buffer.length; i += 3) {
			pos.set(buffer[i + 0], buffer[i + 1], buffer[i + 2]);

			if (pos.getX() < min.getX()) {
				min.setX(pos.getX());
			}
			if (pos.getX() > max.getX()) {
				max.setX(pos.getX());
			}

			if (pos.getY() < min.getY()) {
				min.setY(pos.getY());
			}
			if (pos.getY() > max.getY()) {
				max.setY(pos.getY());
			}

			if (pos.getZ() < min.getZ()) {
				min.setZ(pos.getZ());
			}
			if (pos.getZ() > max.getZ()) {
				max.setZ(pos.getZ());
			}
		}

		this.boundingVolume = new BoundingBox(min, max);
	}

}
