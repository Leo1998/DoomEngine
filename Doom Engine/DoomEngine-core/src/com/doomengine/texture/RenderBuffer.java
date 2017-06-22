package com.doomengine.texture;

import com.doomengine.texture.image.Format;

public class RenderBuffer {

	Texture tex;
	Format format;
	int id = -1;
	int slot = FrameBuffer.SLOT_UNDEF;

	/**
	 * @return The image format of the render buffer.
	 */
	public Format getFormat() {
		return format;
	}

	/**
	 * @return The texture to render to for this <code>RenderBuffer</code> or
	 *         null if content should be rendered into a buffer.
	 */
	public Texture getTexture() {
		return tex;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getSlot() {
		return slot;
	}

	public void resetObject() {
		id = -1;
	}

	@Override
	public String toString() {
		if (tex != null) {
			return "TextureTarget[format=" + format + "]";
		} else {
			return "BufferTarget[format=" + format + "]";
		}
	}

}
