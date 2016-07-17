package com.doomengine.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

public class BufferUtils {

	private BufferUtils() {
	}

	public static void disposeDirectBuffer(Buffer buffer) {
		if (buffer.isDirect()) {
			try {
				if (!buffer.getClass().getName().equals("java.nio.DirectByteBuffer")) {
					Field attField = buffer.getClass().getDeclaredField("att");
					attField.setAccessible(true);
					buffer = (Buffer) attField.get(buffer);
				}

				Method cleanerMethod = buffer.getClass().getMethod("cleaner");
				cleanerMethod.setAccessible(true);
				Object cleaner = cleanerMethod.invoke(buffer);
				Method cleanMethod = cleaner.getClass().getMethod("clean");
				cleanMethod.setAccessible(true);
				cleanMethod.invoke(cleaner);
			} catch (Exception e) {
				throw new RuntimeException("Could not destroy direct buffer " + buffer, e);
			}
		}
	}

	public static Buffer copy(Buffer buf) {
		if (buf instanceof FloatBuffer) {
			return copy((FloatBuffer) buf);
		} else if (buf instanceof ShortBuffer) {
			return copy((ShortBuffer) buf);
		} else if (buf instanceof ByteBuffer) {
			return copy((ByteBuffer) buf);
		} else if (buf instanceof IntBuffer) {
			return copy((IntBuffer) buf);
		} else if (buf instanceof DoubleBuffer) {
			return copy((DoubleBuffer) buf);
		} else {
			throw new UnsupportedOperationException();
		}
	}

	public static ByteBuffer createByteBuffer(byte... array) {
		ByteBuffer result = createByteBuffer(array.length);
		result.put(array);
		result.flip();
		return result;
	}

	public static ByteBuffer createByteBuffer(int size) {
		return ByteBuffer.allocateDirect(size).order(ByteOrder.nativeOrder());
	}

	public static byte[] getByteArray(ByteBuffer buff) {
		byte[] inds = new byte[buff.limit()];
		for (int x = 0; x < inds.length; x++) {
			inds[x] = buff.get();
		}
		return inds;
	}

	public static ByteBuffer copy(ByteBuffer buff) {
		buff.rewind();
		ByteBuffer result = createByteBuffer(buff.limit());

		result.put(buff);

		buff.rewind();
		result.rewind();

		return result;
	}

	public static ShortBuffer createShortBuffer(short... array) {
		ShortBuffer result = createShortBuffer(array.length);
		result.put(array);
		result.flip();
		return result;
	}

	public static ShortBuffer createShortBuffer(int size) {
		return createByteBuffer(size << 1).asShortBuffer();
	}

	public static short[] getShortArray(ShortBuffer buff) {
		short[] inds = new short[buff.limit()];
		for (int x = 0; x < inds.length; x++) {
			inds[x] = buff.get();
		}
		return inds;
	}

	public static ShortBuffer copy(ShortBuffer buff) {
		buff.rewind();
		ShortBuffer result = createShortBuffer(buff.limit());

		result.put(buff);

		buff.rewind();
		result.rewind();

		return result;
	}

	public static FloatBuffer createFloatBuffer(float... array) {
		FloatBuffer result = createFloatBuffer(array.length);
		result.put(array);
		result.flip();
		return result;
	}

	public static FloatBuffer createFloatBuffer(int size) {
		return createByteBuffer(size << 2).asFloatBuffer();
	}

	public static float[] getFloatArray(FloatBuffer buff) {
		float[] inds = new float[buff.limit()];
		for (int x = 0; x < inds.length; x++) {
			inds[x] = buff.get();
		}
		return inds;
	}

	public static FloatBuffer copy(FloatBuffer buff) {
		buff.rewind();
		FloatBuffer result = createFloatBuffer(buff.limit());

		result.put(buff);

		buff.rewind();
		result.rewind();

		return result;
	}

	public static IntBuffer createIntBuffer(int... array) {
		IntBuffer result = createIntBuffer(array.length);
		result.put(array);
		result.flip();
		return result;
	}

	public static IntBuffer createIntBuffer(int size) {
		return createByteBuffer(size << 2).asIntBuffer();
	}

	public static int[] getIntArray(IntBuffer buff) {
		int[] inds = new int[buff.limit()];
		for (int x = 0; x < inds.length; x++) {
			inds[x] = buff.get();
		}
		return inds;
	}

	public static IntBuffer copy(IntBuffer buff) {
		buff.rewind();
		IntBuffer result = createIntBuffer(buff.limit());

		result.put(buff);

		buff.rewind();
		result.rewind();

		return result;
	}

	public static DoubleBuffer createDoubleBuffer(double... array) {
		DoubleBuffer result = createDoubleBuffer(array.length);
		result.put(array);
		result.flip();
		return result;
	}

	public static DoubleBuffer createDoubleBuffer(int size) {
		return createByteBuffer(size << 3).asDoubleBuffer();
	}

	public static double[] getDoubleArray(DoubleBuffer buff) {
		double[] inds = new double[buff.limit()];
		for (int x = 0; x < inds.length; x++) {
			inds[x] = buff.get();
		}
		return inds;
	}

	public static DoubleBuffer copy(DoubleBuffer buff) {
		buff.rewind();
		DoubleBuffer result = createDoubleBuffer(buff.limit());

		result.put(buff);

		buff.rewind();
		result.rewind();

		return result;
	}

}
