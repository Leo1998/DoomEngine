package com.doomengine.renderer;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.LongBuffer;
import java.nio.ShortBuffer;

import com.doomengine.util.BufferUtils;
import com.doomengine.util.NativeObject;

public abstract class VertexBuffer extends NativeObject {

	public static enum Type {
		/**
		 * Specifies the vertex buffer, for example VertexBufferObject.
		 */
		VertexData,
		/**
		 * Specifies the index buffer, must contain integer data (ubyte, ushort,
		 * or uint).
		 */
		IndexData;
	}

	public static enum Usage {

		/**
		 * Mesh data is sent once and very rarely updated.
		 */
		Static,

		/**
		 * Mesh data is updated occasionally (once per frame or less).
		 */
		Dynamic,

		/**
		 * Mesh data is updated every frame.
		 */
		Stream;
	}

	protected Buffer data = null;
	protected Type type;
	private Usage usage;
	private DataType dataType;

	public VertexBuffer(DataType dataType, Usage usage) {
		super();
		this.dataType = dataType;
		this.usage = usage;
	}

	protected void setupBuffer(int size) {
		switch (dataType) {
		case Byte:
		case UnsignedByte:
			data = BufferUtils.createByteBuffer(size);
			break;
		case Short:
		case UnsignedShort:
			data = BufferUtils.createShortBuffer(size);
			break;
		case Int:
		case UnsignedInt:
			data = BufferUtils.createIntBuffer(size);
			break;
		case Float:
			data = BufferUtils.createFloatBuffer(size);
			break;
		default:
			throw new UnsupportedOperationException("Unrecognized buffer type: " + dataType);
		}

		data.position(0);
		data.limit(data.capacity());
	}

	public boolean invariant() {
		if (data instanceof DoubleBuffer) {
			throw new AssertionError();
		} else if (data instanceof CharBuffer) {
			throw new AssertionError();
		} else if (data instanceof LongBuffer) {
			throw new AssertionError();
		} else if (data instanceof FloatBuffer && dataType != DataType.Float) {
			throw new AssertionError();
		} else if (data instanceof IntBuffer && dataType != DataType.Int && dataType != DataType.UnsignedInt) {
			throw new AssertionError();
		} else if (data instanceof ShortBuffer && dataType != DataType.Short && dataType != DataType.UnsignedShort) {
			throw new AssertionError();
		} else if (data instanceof ByteBuffer && dataType != DataType.Byte && dataType != DataType.UnsignedByte) {
			throw new AssertionError();
		}
		return true;
	}

	@Override
	protected void deleteNativeBuffers() {
		BufferUtils.disposeDirectBuffer(data);
	}

	public Buffer getData() {
		return data;
	}

	public Type getType() {
		return type;
	}

	public Usage getUsage() {
		return usage;
	}

	public void setUsage(Usage usage) {
		this.usage = usage;
		setUpdateNeeded();
	}

	public DataType getDataType() {
		return dataType;
	}

	public int getSize() {
		return data.limit();
	}

	public int getMaxSize() {
		return data.capacity();
	}

}
