package com.doomengine.renderer;

import java.nio.FloatBuffer;

public class VertexBufferObject extends VertexBuffer {

	private VertexAttributes attributes;

	public VertexBufferObject(int numVertices, Usage usage, VertexAttributes attributes) {
		super(DataType.Float, usage);
		this.attributes = attributes;

		this.type = Type.VertexData;

		setupBuffer(numVertices);
	}

	public int getNumVertices() {
		return this.getSize() / attributes.vertexSize;
	}

	public void setVertices(float[] vertices) {
		setUpdateNeeded();

		data.position(0);
		data.limit(vertices.length);

		((FloatBuffer) data).put(vertices);

		data.rewind();
	}

	@Override public void deleteObject() {
		((Renderer) this.rendererObject).deleteVertexBuffer(this);

		id = -1;
	}

	@Override public void resetObject() {
		id = -1;

		setUpdateNeeded();
	}

	public VertexAttributes getAttributes() {
		return attributes;
	}

}
