package com.doomengine.renderer;

import com.doomengine.renderer.VertexBuffer.Usage;

public class Mesh {

	private VertexBufferObject vertexBuffer;
	private IndexBufferObject indexBuffer;

	private Usage usage;
	private DrawMode drawMode = DrawMode.Triangles;

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

}
