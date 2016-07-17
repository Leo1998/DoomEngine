package com.doomengine.texture.image;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import com.doomengine.math.ColorSpace;
import com.doomengine.util.BufferUtils;

public class Image {

	private Format format;
	private int width;
	private int height;
	private int depth;
	private int multiSamples = 1;
	private ArrayList<ByteBuffer> data;
	private ColorSpace colorSpace = null;

	public Image(Format format, int width, int height) {
		this(format, width, height, BufferUtils.createByteBuffer(width * height * format.getBitsPerPixel() * 4 * 4));
	}

	public Image(Format format, int width, int height, ByteBuffer buffer) {
		this(format, width, height, buffer, ColorSpace.Linear);
	}

	public Image(Format format, int width, int height, ByteBuffer buffer, ColorSpace colorSpace) {
		this(format, width, height, 1, new ArrayList<ByteBuffer>(1), colorSpace);

		this.data.add(buffer);
	}

	public Image(Format format, int width, int height, int depth, ArrayList<ByteBuffer> data, ColorSpace colorSpace) {
		super();

		this.format = format;
		this.width = width;
		this.height = height;
		this.depth = depth;
		this.data = data;
		this.colorSpace = colorSpace;
	}

	@Override public Image clone() {
		try {
			Image clone = (Image) super.clone();
			clone.data = data != null ? new ArrayList<ByteBuffer>(data) : null;
			return clone;
		} catch (CloneNotSupportedException ex) {
			throw new AssertionError();
		}
	}

	public Format getFormat() {
		return format;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public int getDepth() {
		return depth;
	}

	public int getMultiSamples() {
		return multiSamples;
	}

	public List<ByteBuffer> getData() {
		return data;
	}

	public ByteBuffer getData(int index) {
		if (data.size() > index)
			return data.get(index);
		else
			return null;
	}

	public void setData(ArrayList<ByteBuffer> data) {
		this.data = data;
	}

	public void setData(ByteBuffer data) {
		this.data = new ArrayList<ByteBuffer>(1);
		this.data.add(data);
	}

	public void addData(ByteBuffer data) {
		this.data.add(data);
	}

	public ColorSpace getColorSpace() {
		return colorSpace;
	}

	public void dispose() {
		for (int i = 0; i < data.size(); i++) {
			BufferUtils.disposeDirectBuffer(data.get(i));
		}
	}

}
