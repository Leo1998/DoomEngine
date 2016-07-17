package com.doomengine.renderer;

public class VertexAttribute {

	private String name;
	private DataType type;
	private int size;
	private boolean normalized;

	int offset;

	public VertexAttribute(String name, DataType type, int size, boolean normalized) {
		super();
		this.type = type;
		this.name = name;
		this.size = size;
		this.normalized = normalized;
	}

	public String getName() {
		return name;
	}

	public DataType getType() {
		return type;
	}

	public int getSize() {
		return size;
	}

	public boolean isNormalized() {
		return normalized;
	}

	public int getOffset() {
		return offset;
	}

	@Override public String toString() {
		return "VertexAttribute [name=" + name + ", type=" + type + ", size=" + size + ", normalized=" + normalized + ", offset=" + offset + "]";
	}

}
