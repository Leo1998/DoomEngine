package com.doomengine.renderer;

public class VertexAttributes {

	public static final VertexAttribute POSITION_ATTRIB = new VertexAttribute("a_position", DataType.Float, 3, false);
	public static final VertexAttribute NORMAL_ATTRIB = new VertexAttribute("a_normal", DataType.Float, 3, false);
	public static final VertexAttribute TEXCOORD_ATTRIB = new VertexAttribute("a_texCoord", DataType.Float, 2, false);

	/**
	 * The size of a single vertex
	 */
	public final int vertexSize;

	private VertexAttribute[] attributes;

	public VertexAttributes(VertexAttribute... attributes) {
		VertexAttribute[] list = new VertexAttribute[attributes.length];
		for (int i = 0; i < attributes.length; i++) {
			if (attributes[i] != null) {
				list[i] = attributes[i];
			}
		}

		this.attributes = list;

		this.vertexSize = calculateOffsets() / 4;
	}

	private int calculateOffsets() {
		int count = 0;
		for (int i = 0; i < attributes.length; i++) {
			VertexAttribute attribute = attributes[i];
			attribute.offset = count;

			count += 4 * attribute.getSize();// TODO: handle different data
												// types!
		}

		return count;
	}

	public int getCount() {
		return attributes.length;
	}

	public VertexAttribute getVertexAttribute(int index) {
		return attributes[index];
	}

	public VertexAttribute getVertexAttribute(String name) {
		for (int i = 0; i < attributes.length; i++) {
			VertexAttribute attribute = attributes[i];

			if (attribute.getName().equals(name)) {
				return attribute;
			}
		}

		return null;
	}

	public int getVertexAttributeIndex(VertexAttribute attribute) {
		for (int i = 0; i < attributes.length; i++) {
			VertexAttribute attribute0 = attributes[i];

			if (attribute0 == attribute) {
				return i;
			}
		}

		return -1;
	}

}
