package com.doomengine.texture;

public enum MinFilter {

	/**
	 * Nearest neighbor interpolation is the fastest and crudest filtering
	 * method - it simply uses the color of the texel closest to the pixel
	 * center for the pixel color. While fast, this results in aliasing and
	 * shimmering during minification. (GL equivalent: GL_NEAREST)
	 */
	NEAREST_NO_MIPMAPS(false),

	/**
	 * In this method the four nearest texels to the pixel center are sampled
	 * (at texture level 0), and their colors are combined by weighted averages.
	 * Though smoother, without mipmaps it suffers the same aliasing and
	 * shimmering problems as nearest NearestNeighborNoMipMaps. (GL equivalent:
	 * GL_LINEAR)
	 */
	BILINEAR_NO_MIPMAPS(false),

	/**
	 * Same as NearestNeighborNoMipMaps except that instead of using samples
	 * from texture level 0, the closest mipmap level is chosen based on
	 * distance. This reduces the aliasing and shimmering significantly, but
	 * does not help with blockiness. (GL equivalent: GL_NEAREST_MIPMAP_NEAREST)
	 */
	NEAREST_NEAREST_MIPMAPS(true),

	/**
	 * Same as BilinearNoMipMaps except that instead of using samples from
	 * texture level 0, the closest mipmap level is chosen based on distance. By
	 * using mipmapping we avoid the aliasing and shimmering problems of
	 * BilinearNoMipMaps. (GL equivalent: GL_LINEAR_MIPMAP_NEAREST)
	 */
	BILINEAR_NEAREST_MIPMAPS(true),

	/**
	 * Similar to NearestNeighborNoMipMaps except that instead of using samples
	 * from texture level 0, a sample is chosen from each of the closest (by
	 * distance) two mipmap levels. A weighted average of these two samples is
	 * returned. (GL equivalent: GL_NEAREST_MIPMAP_LINEAR)
	 */
	NEAREST_LINEAR_MIPMAPS(true),

	/**
	 * Trilinear filtering is a remedy to a common artifact seen in mipmapped
	 * bilinearly filtered images: an abrupt and very noticeable change in
	 * quality at boundaries where the renderer switches from one mipmap level
	 * to the next. Trilinear filtering solves this by doing a texture lookup
	 * and bilinear filtering on the two closest mipmap levels (one higher and
	 * one lower quality), and then linearly interpolating the results. This
	 * results in a smooth degradation of texture quality as distance from the
	 * viewer increases, rather than a series of sudden drops. Of course, closer
	 * than Level 0 there is only one mipmap level available, and the algorithm
	 * reverts to bilinear filtering (GL equivalent: GL_LINEAR_MIPMAP_LINEAR)
	 */
	TRILINEAR(true);

	private final boolean usesMipMapLevels;

	private MinFilter(boolean usesMipMapLevels) {
		this.usesMipMapLevels = usesMipMapLevels;
	}

	public boolean usesMipMapLevels() {
		return usesMipMapLevels;
	}
}
