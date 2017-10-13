package kempodev.distinct.radar;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;

import java.util.*;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.nio.ByteBuffer;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import java.awt.image.BufferedImage;

/*
MapWriterRender contains most of the code for drawing the overlay.
This includes:
 - loading textures from images
 - saving textures to images
 - allocating and setting up GL textures
 - drawing coloured and textured quads (using minecraft Tesselator class)
 
*/

public class MapWriterRender {
	private static IntBuffer singleIntBuf;
	private static IntBuffer textureBuf;
	private static double zDepth = -100.0D;
	private static final int MAX_TEXTURE_SIZE = MapWriter.REGION_SIZE;
	private static int[] pixelBuf;
	
	static {
		singleIntBuf = newDirectIntBuffer(1);
		textureBuf = newDirectIntBuffer(MAX_TEXTURE_SIZE * MAX_TEXTURE_SIZE);
		pixelBuf = new int[MAX_TEXTURE_SIZE * MAX_TEXTURE_SIZE];
	}
	
	// GL will only load textures from direct int buffers
	public static IntBuffer newDirectIntBuffer(int size) {
		return ByteBuffer.allocateDirect(size * 4).order(ByteOrder.nativeOrder()).asIntBuffer();
	}
	
	public static void setColor(int color) {
		float A = ((float) ((color >> 24) & 0xff) / 255.0f);
		float R = ((float) ((color >> 16) & 0xff) / 255.0f);
		float G = ((float) ((color >> 8)  & 0xff) / 255.0f);
		float B = ((float) ((color >> 0)  & 0xff) / 255.0f);
		GL11.glColor4f(R, G, B, A);
	}
	
	public static void resetColor() {
		GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
	}
	
	public static void setZDepth(double z) {
		zDepth = z;
	}
	
	/*
	Data transfer methods.
	
	Used for transferring pixels into and out of textures.
	*/
	
	public static void copyPixelsToBuffer(int[] pixels) {
		textureBuf.clear();
		textureBuf.put(pixels);
		textureBuf.clear();
	}
	
	public static void copyBufferToPixels(int[] pixels) {
		textureBuf.clear();
		textureBuf.get(pixels);
		textureBuf.clear();
	}
	
	public static void fillBuffer(int color) {
		textureBuf.clear();
		for (int i = 0; i < textureBuf.capacity(); i++) {
			textureBuf.put(color);
		}
		textureBuf.clear();
	}
	
	/*
	GL code for rendering to the overlay.
	
	Somewhat reinventing the wheel here as could do mostly the same thing
	using methods from Gui and RenderEngine.
	*/
	
	public static int allocateTexture() {
		singleIntBuf.clear();
		GL11.glGenTextures(singleIntBuf);
		return singleIntBuf.get();
	}
	
	public static int allocateAndFillTexture(int w, int h, int colour) {
		int texture = allocateTexture();
		fillBuffer(colour);
		setupTexture(texture, w, h);
		return texture;
	}
	
	// create texture from pixels in textureBuf
	public static void setupTexture(int texture, int w, int h) {
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_CLAMP);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_CLAMP);
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, w, h, 0, GL12.GL_BGRA, GL12.GL_UNSIGNED_INT_8_8_8_8_REV, textureBuf);
	}
	
	// update already created texture from pixels in textureBuf
	public static void updateTexture(int texture, int x, int y, int w, int h) {
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_CLAMP);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_CLAMP);
		GL11.glTexSubImage2D(GL11.GL_TEXTURE_2D, 0, x, y, w, h, GL12.GL_BGRA, GL12.GL_UNSIGNED_INT_8_8_8_8_REV, textureBuf);
	}
	
	// copy pixels from texture to textureBuf
	public static void getTexture(int texture) {
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture);
		GL11.glGetTexImage(GL11.GL_TEXTURE_2D, 0, GL12.GL_BGRA, GL12.GL_UNSIGNED_INT_8_8_8_8_REV, textureBuf);
	}
	
	// free up the resources used by the GL texture
	public static void deleteTexture(int texture) {
		singleIntBuf.clear();
		singleIntBuf.put(texture);
		singleIntBuf.flip();
		GL11.glDeleteTextures(singleIntBuf);
	}
	
    /*public void setupOverlayRendering()
    {
        ScaledResolution var1 = new ScaledResolution(mc.gameSettings, mc.displayWidth, mc.displayHeight);
        GL11.glClear(256);
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        GL11.glOrtho(0.0D, var1.getScaledWidth_double(), var1.getScaledHeight_double(), 0.0D, 1000.0D, 3000.0D);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glLoadIdentity();
        GL11.glTranslatef(0.0F, 0.0F, -2000.0F);
    }*/
	
	/*
	Drawing Methods
	
	Note that EntityRenderer.setupOverlayRendering must be called before drawing for the scene
	to appear correctly on the overlay.
	If these functions are called from the hookUpdateCameraAndRender method of MapWriter this
	will have already been done.
	*/
	
	// draw rectangle with texture stretched to fill the shape
	public static void drawTexturedRect(double x, double y, double w, double h, int texture) {
		drawTexturedRect(x, y, w, h, 0.0D, 0.0D, 1.0D, 1.0D, texture);
	}
	
	// draw rectangle with texture UV coordinates specified (so only part of the texture fills the rectangle).
	public static void drawTexturedRect(double x, double y, double w, double h, double u1, double v1, double u2, double v2, int texture) {
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture);
        Tessellator tes = Tessellator.instance;
        tes.startDrawingQuads();
		tes.addVertexWithUV(x,     y,     zDepth, u1, v1);
        tes.addVertexWithUV(x,     y + h, zDepth, u1, v2);
		tes.addVertexWithUV(x + w, y + h, zDepth, u2, v2);
		tes.addVertexWithUV(x + w, y,     zDepth, u2, v1);
        tes.draw();
		GL11.glDisable(GL11.GL_BLEND);
	}
	
	// note that the coords must be in the correct sequence, following the perimeter of the quad,
	// and in anticlockwise order.
	// otherwise one or both of the triangles that make the quad will not be rendered.
	//  1 +------+ 4      2 +------+ 1     1 +------+ 2
	//    |      |          |      |         |      |
	//    |      |          |      |         |      |
	//  2 +------+ 3      3 +------+ 4     3 +------+ 4
	//     works             works            fails
	//
	public static void drawColoredQuad(double[] coords) {
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		Tessellator tes = Tessellator.instance;
        tes.startDrawingQuads();
        tes.addVertex(coords[0], coords[1], zDepth);
        tes.addVertex(coords[2], coords[3], zDepth);
        tes.addVertex(coords[4], coords[5], zDepth);
        tes.addVertex(coords[6], coords[7], zDepth);
        tes.draw();
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_BLEND);
	}
	
	public static void drawArrow(double x, double y, double angle, double length, int color) {
		double arrowBackAngle = 0.75D * Math.PI;	// angle the back corners will be drawn at relative to the pointing angle
		double[] coords = new double[8];
		
		coords[0] = x + (length * Math.cos(angle));
		coords[1] = y + (length * Math.sin(angle));
		coords[2] = x + (length * 0.5D * Math.cos(angle - arrowBackAngle));
		coords[3] = y + (length * 0.5D * Math.sin(angle - arrowBackAngle));
		coords[4] = x;
		coords[5] = y;
		coords[6] = x + (length * 0.5D * Math.cos(angle + arrowBackAngle));
		coords[7] = y + (length * 0.5D * Math.sin(angle + arrowBackAngle));
		
		MapWriterRender.setColor(color);
		MapWriterRender.drawColoredQuad(coords);
		MapWriterRender.resetColor();
	}
	
	public static void drawRect(double x, double y, double w, double h, int color) {
		double[] coords = new double[8];
		
		coords[0] = x + w;
		coords[1] = y;
		coords[2] = x;
		coords[3] = y;
		coords[4] = x;
		coords[5] = y + h;
		coords[6] = x + w;
		coords[7] = y + h;
		
		MapWriterRender.setColor(color);
		MapWriterRender.drawColoredQuad(coords);
		MapWriterRender.resetColor();
	}
	
	
	/*
	Load pixels from the given BufferedImage into the given texture.
	*/
	public static boolean updateTextureFromImage(int texture, BufferedImage img) {
		boolean error = false;
		int w = img.getWidth();
		int h = img.getHeight();
		if ((w <= MAX_TEXTURE_SIZE) && (h <= MAX_TEXTURE_SIZE)) {
			img.getRGB(0, 0, w, h, pixelBuf, 0, w);
			copyPixelsToBuffer(pixelBuf);
			updateTexture(texture, 0, 0, w, h);
		} else {
			error = true;
		}
		return error;
	}
	
	/*
	Create new BufferedImage from the pixels in the given texture.
	*/
	public static BufferedImage createImageFromTexture(int texture, int w, int h) {
		BufferedImage img = null;
		if ((w <= MAX_TEXTURE_SIZE) && (h <= MAX_TEXTURE_SIZE)) {
			getTexture(texture);
			copyBufferToPixels(pixelBuf);
			img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
			img.setRGB(0, 0, w, h, pixelBuf, 0, w);
		} else {
			img = null;
		}
		return img;
	}
}