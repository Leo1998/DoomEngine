package com.doomengine.renderer;

import java.nio.ShortBuffer;

public class IndexBufferObject extends VertexBuffer {

	public IndexBufferObject(int numIndices, Usage usage) {
		super(DataType.UnsignedShort, usage);

		this.type = Type.IndexData;

		setupBuffer(numIndices);
	}

	public int getNumIndices() {
		return this.getSize();
	}

	public void setIndices(short[] indices) {
		setUpdateNeeded();

		data.position(0);
		data.limit(indices.length);

		((ShortBuffer) data).put(indices);

		data.rewind();
	}

	@Override
	public void deleteObject() {
		((Renderer) this.rendererObject).deleteVertexBuffer(this);

		id = -1;
	}

	@Override
	public void resetObject() {
		id = -1;

		setUpdateNeeded();
	}

}
