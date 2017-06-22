package com.doomengine.texture;

import com.doomengine.math.MathHelper;
import com.doomengine.texture.image.Format;
import com.doomengine.texture.image.Image;
import com.doomengine.util.NativeObject;

public abstract class Texture extends NativeObject {

	public enum Type {
		/**
		 * Two dimensional texture (default). A rectangle.
		 */
		TwoDimensional,

		/**
		 * A set of 6 TwoDimensional textures arranged as faces of a cube facing
		 * inwards.
		 */
		Cubemap;
	}

	protected Type type;

	protected Image image;
	private MinFilter minificationFilter = MinFilter.BILINEAR_NEAREST_MIPMAPS;
	private MagFilter magnificationFilter = MagFilter.BILINEAR;

	public Texture(int width, int height, Format format) {
		this(new Image(format, width, height));
	}

	public Texture(Image image) {
		super();

		this.image = image;
	}

	public boolean isPOT() {
		return MathHelper.isPowerOfTwo(getWidth()) && MathHelper.isPowerOfTwo(getHeight());
	}

	public MinFilter getMinFilter() {
		return minificationFilter;
	}

	public void setMinFilter(MinFilter minificationFilter) {
		this.minificationFilter = minificationFilter;
		setUpdateNeeded();
	}

	public MagFilter getMagFilter() {
		return magnificationFilter;
	}

	public void setMagFilter(MagFilter magnificationFilter) {
		this.magnificationFilter = magnificationFilter;
		setUpdateNeeded();
	}

	public int getWidth() {
		return image.getWidth();
	}

	public int getHeight() {
		return image.getHeight();
	}

	public Type getType() {
		return type;
	}

	public Format getFormat() {
		return image.getFormat();
	}

	public Image getImage() {
		return image;
	}

	public void setImage(Image image) {
		this.image = image;
		setUpdateNeeded();
	}

}
