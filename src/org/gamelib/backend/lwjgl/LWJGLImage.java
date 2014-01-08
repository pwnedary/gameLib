/**
 * 
 */
package org.gamelib.backend.lwjgl;

import static org.lwjgl.opengl.GL11.*;

import org.gamelib.backend.Graphics;
import org.gamelib.backend.Image;

/**
 * Textures are automatically made p^2 for compatibility.
 * @author pwnedary
 */
public class LWJGLImage implements Image {
	/** The GL target type */
	public final int target;
	/** The GL texture */
	public final int texture;
	/** The width of the image */
	private int width;
	/** The height of the image */
	private int height;
	/** The width of the GL texture */
	private int texWidth;
	/** The height of the GL texture */
	private int texHeight;
	/* The region's texture coordinates */
	public float u = 0, v = 0, u2 = 1, v2 = 1;

	public LWJGLImage(int target, int texture) {
		this.target = target = GL_TEXTURE_2D;
		this.texture = texture;
	}

	/**
	 * Binds the specified GL context to a texture.
	 * @param gl the GL context to bind to
	 */
	public void bind() {
		glBindTexture(target, texture);
	}

	public void unbind() {
		glBindTexture(target, 0);
	}

	@Override
	public int getWidth() {
		return width;
	}

	@Override
	public void setWidth(int width) {
		this.width = width;
	}

	@Override
	public int getHeight() {
		return height;
	}

	@Override
	public void setHeight(int height) {
		this.height = height;
	}

	/**
	 * @return the texWidth
	 */
	public int getTexWidth() {
		return texWidth;
	}

	/**
	 * @param texWidth the texWidth to set
	 */
	public void setTexWidth(int texWidth) {
		this.texWidth = texWidth;
	}

	/**
	 * @return the texHeight
	 */
	public int getTexHeight() {
		return texHeight;
	}

	/**
	 * @param texHeight the texHeight to set
	 */
	public void setTexHeight(int texHeight) {
		this.texHeight = texHeight;
	}

	@Override
	public void draw(Graphics g, float delta) {
		g.drawImage(this, 0, 0, 0 + getWidth(), 0 + getHeight(), 0, 0, 0 + getWidth(), 0 + getHeight());
	}

	@Override
	public void dispose() {
		glDeleteTextures(texture);
	}

	@Override
	public void setFilter(Filter min, Filter mag) {
		bind();
		glTexParameteri(target, GL_TEXTURE_MIN_FILTER, min.getGLEnum());
		glTexParameteri(target, GL_TEXTURE_MAG_FILTER, mag.getGLEnum());
		unbind();
	}

	@Override
	public void setWrap(Wrap u, Wrap v) {
		bind();
		glTexParameteri(target, GL_TEXTURE_WRAP_S, u.getGLEnum());
		glTexParameteri(target, GL_TEXTURE_WRAP_T, v.getGLEnum());
		unbind();
	}

	@Override
	public Image region(int x, int y, int width, int height) {
		LWJGLImage image = new LWJGLImage(target, texture);
		image.setWidth(width);
		image.setHeight(height);
		image.setTexWidth(getTexWidth());
		image.setTexHeight(getTexHeight());
		image.u = (float) x / image.getTexWidth();
		image.v = (float) y / image.getTexHeight();
		image.u2 = (float) (x + width) / image.getTexWidth();
		image.v2 = (float) (y + height) / image.getTexHeight();
		return image;
	}
}
