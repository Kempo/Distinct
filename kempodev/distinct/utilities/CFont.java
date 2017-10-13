package kempodev.distinct.utilities;

import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.GL_NICEST;
import static org.lwjgl.opengl.GL11.GL_POLYGON_SMOOTH_HINT;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glColor4f;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glHint;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glScaled;
import static org.lwjgl.opengl.GL11.glTexCoord2f;
import static org.lwjgl.opengl.GL11.glVertex2d;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.renderer.texture.TextureUtil;

/**
 * 
 * @author TheObliterator
 * 
 *         A class to create and draw true type fonts onto the Minecraft game
 *         engine.
 */

public class CFont {
	public static final int IMAGE_WIDTH = 512;
	public static final int IMAGE_HEIGHT = 512;
	
	private int texID;
	private final IntObject[] chars = new IntObject[256];
	private final FontMetrics metrics;
	private final Font font;
	private int fontHeight = -1;

	/**
	 * @param font
	 * 			Font we're using.
	 * @param antiAlias 
	 * 			Whether or not to anti alias the font.
	 */
	public CFont(Font font, boolean antiAlias) {
		this.font = font;
		final BufferedImage img = new BufferedImage(IMAGE_WIDTH, IMAGE_HEIGHT,
				BufferedImage.TYPE_INT_ARGB);
		final Graphics g = img.getGraphics();
		final Graphics2D g1 = (Graphics2D) g;
		if (antiAlias) {
			g1.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		}
		g1.setFont(font);

		g1.setColor(new Color(255, 255, 255, 0));
		g1.fillRect(0, 0, IMAGE_WIDTH, IMAGE_HEIGHT);
		g1.setColor(Color.white);
		metrics = g1.getFontMetrics();
		
		int rowHeight = 0;
		int positionX = 0;
		int positionY = 0;
		// Stolen from the Slick library, it loads the characters into the image used for font rendering.
		for (int i = 0; i < 256; i++) {
			char ch = (char) i;
			BufferedImage fontImage = getFontImage(ch, antiAlias);

			IntObject newIntObject = new IntObject();

			newIntObject.width = fontImage.getWidth();
			newIntObject.height = fontImage.getHeight();

			if (positionX + newIntObject.width >= IMAGE_WIDTH) {
				positionX = 0;
				positionY += rowHeight;
				rowHeight = 0;
			}

			newIntObject.storedX = positionX;
			newIntObject.storedY = positionY;

			if (newIntObject.height > fontHeight) {
				fontHeight = newIntObject.height;
			}

			if (newIntObject.height > rowHeight) {
				rowHeight = newIntObject.height;
			}
			chars[i] = newIntObject;
			g1.drawImage(fontImage, positionX, positionY, null);

			positionX += newIntObject.width;
		}

		// Render the finished bitmap into the Minecraft
		// graphics engine.
		try {
			texID = TextureUtil.uploadTextureImageAllocate(TextureUtil.glGenTextures(), img, true, true);
		} catch (final NullPointerException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Stolen from the Slick library, loads an image with the character specified.
	 * */
	private BufferedImage getFontImage(char ch, boolean antiAlias) {
		BufferedImage tempfontImage = new BufferedImage(1, 1,
				BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = (Graphics2D) tempfontImage.getGraphics();
		if (antiAlias) {
			g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		}
		g.setFont(font);
		FontMetrics fontMetrics = g.getFontMetrics();
		int charwidth = fontMetrics.charWidth(ch) + 8;

		if (charwidth <= 0) {
			charwidth = 7;
		}
		int charheight = fontMetrics.getHeight() + 3;
		if (charheight <= 0) {
			charheight = font.getSize();
		}

		BufferedImage fontImage;
		fontImage = new BufferedImage(charwidth, charheight, BufferedImage.TYPE_INT_ARGB);
		Graphics2D gt = (Graphics2D) fontImage.getGraphics();
		if (antiAlias) {
			gt.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		}
		gt.setFont(font);

		gt.setColor(Color.WHITE);
		int charx = 3;
		int chary = 1;
		gt.drawString(String.valueOf(ch), (charx),
				(chary) + fontMetrics.getAscent());

		return fontImage;

	}
	
	/**
	 * Private drawing method used within other drawing methods.
	 */
	public void drawChar(char c, float x, float y)
			throws ArrayIndexOutOfBoundsException {
		final Rectangle2D bounds = metrics.getStringBounds(
				Character.toString(c), null);
		try {
			if(bounds != null && chars.length > c)
				drawQuad(x, y, (float) chars[c].width, (float) chars[c].height, chars[c].storedX, chars[c].storedY, (float) chars[c].width, (float) chars[c].height);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Again, stolen from the Slick library. Renders at the given image coordinates.
	 * */
	private void drawQuad(float x, float y, float width, float height, float srcX, float srcY, float srcWidth, float srcHeight) {
		float renderSRCX = srcX / IMAGE_WIDTH,
		renderSRCY = srcY / IMAGE_HEIGHT,
		renderSRCWidth = (srcWidth) / IMAGE_WIDTH,
		renderSRCHeight = (srcHeight) / IMAGE_HEIGHT;
		glBegin(GL_TRIANGLES);
		glTexCoord2f(renderSRCX + renderSRCWidth, renderSRCY);
		glVertex2d(x + width, y);
		glTexCoord2f(renderSRCX, renderSRCY);
		glVertex2d(x, y);
		glTexCoord2f(renderSRCX, renderSRCY + renderSRCHeight);
		glVertex2d(x, y + height);
		glTexCoord2f(renderSRCX, renderSRCY + renderSRCHeight);
		glVertex2d(x , y + height);
		glTexCoord2f(renderSRCX + renderSRCWidth, renderSRCY + renderSRCHeight);
		glVertex2d(x + width, y + height);
		glTexCoord2f(renderSRCX + renderSRCWidth, renderSRCY);
		glVertex2d(x + width, y);
		glEnd();
		/*glBegin(GL_QUADS);
		glTexCoord2f(renderSRCX, renderSRCY);
		glVertex2d(x, y);
		glTexCoord2f(renderSRCX + renderSRCWidth, renderSRCY);
		glVertex2d(x + width, y);
		glTexCoord2f(renderSRCX + renderSRCWidth, renderSRCY + renderSRCHeight);
		glVertex2d(x + width, y + height);
		glTexCoord2f(renderSRCX, renderSRCY + renderSRCHeight);
		glVertex2d(x, y + height);
		glEnd();*/
	}

	/**
	 * Draws a given string onto a gui/subclass.
	 * 
	 * @param text
	 *            The string to be drawn
	 * @param x
	 *            The x position to start drawing
	 * @param y
	 *            The y position to start drawing
	 * @param color
	 *            The color of the non-shadowed text (Hex)
	 */
	public final void drawString(String text, double x, double y,
			Color color, boolean shadow) {
		x *= 2;
		y = (y * 2) - 6;
		glPushMatrix();
		glDisable(GL_DEPTH_TEST);
		glEnable(GL_BLEND);
		glHint(GL_POLYGON_SMOOTH_HINT, GL_NICEST);
		glScaled(0.5D, 0.5D, 0.5D);
		glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		glEnable(GL_TEXTURE_2D);
		glBindTexture(GL_TEXTURE_2D, texID);
		glColor(shadow ? new Color(0.05F, 0.05F, 0.05F, (float) color.getAlpha() / 255F):color);
		final double startX = x;
		final int size = text.length();
		for (int indexInString = 0; indexInString < size; indexInString++) {
			final char character = text.charAt(indexInString);
			drawChar(character, (float) x, (float) y);
			x += metrics.getStringBounds(Character.toString(character), null).getWidth();
		}
		glDisable(GL_BLEND);
		glEnable(GL_DEPTH_TEST);
		glPopMatrix();
	}
	
	/**
	 * OpenGL coloring.
	 * */
	public void glColor(Color color) {
		float red = (float) color.getRed() / 255F, green = (float) color.getGreen() / 255F, blue = (float) color.getBlue() / 255F, alpha = (float) color.getAlpha() / 255F;
		glColor4f(red, green, blue, alpha);
	}

	/**
	 * A method that returns a Rectangle that contains the width and height
	 * demensions of the given string.
	 * 
	 * @param text
	 *            The string to be measured
	 * @return Rectangle containing width and height that the text will consume
	 *         when drawn.
	 */
	private final Rectangle getBounds(String text) {
		int w = 0;
		int h = 0;
		int tw = 0;
		final int size = text.length();

		for (int i = 0; i < size; i++) {
			final char c = text.charAt(i);
			tw += metrics.stringWidth(Character.toString(c));
		}

		if (tw > w) {
			w = tw;
		}

		h += metrics.getAscent();
		return new Rectangle(0, 0, w, h);
	}

	/**
	 * Returns the created FontMetrics which is used to retrive various
	 * information about the True Type Font
	 * 
	 * @return FontMetrics of the created font.
	 */
	public FontMetrics getMetrics() {
		return metrics;
	}

	/**
	 * Gets the drawing height of a given string of string.
	 * 
	 * @param text
	 *            The string to be measured
	 * @return The height of the given string.
	 */
	public int getStringHeight(String text) {
		return (int) getBounds(text).getHeight() / 2;
	}
	
	/**
	 * @return Total height that the current font can take up.
	 * */
	public int getHeight() {
		return fontHeight / 2;
	}

	/**
	 * Gets the drawing width of a given string of string.
	 * 
	 * @param text
	 *            The string to be measured
	 * @return The width of the given string.
	 */
	public int getStringWidth(String text) {
		if(text == null)
			return 0;
		return (int) metrics.getStringBounds(text, null).getWidth() / 2;
	}
	
	
	public List<String> getFonts() {
		List<String> fontList = new ArrayList<String>();
		final GraphicsEnvironment enviro = GraphicsEnvironment
				.getLocalGraphicsEnvironment();
		final String[] fontTypes = enviro.getAvailableFontFamilyNames();

		for (final String fontType : fontTypes) {
			fontList.add(fontType);
		}
		return fontList;
	}
	
	
	private class IntObject {
		public int width;
		public int height;
		public int storedX;
		public int storedY;
	}
}
