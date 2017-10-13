package kempodev.distinct.utilities;

import kempodev.distinct.main.Distinct;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.AxisAlignedBB;

import org.lwjgl.opengl.GL11;

public class RenderUtil {
	 private ColorUtil e = new ColorUtil();
	public void glOpen() {
		/**
		Distinct.getInstance().getMinecraft().entityRenderer.disableLightmap(0.0D);
        GL11.glDisable(GL11.GL_FOG);
        GL11.glPushMatrix();
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glHint(GL11.GL_LINE_SMOOTH_HINT, GL11.GL_NICEST);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glHint(GL11.GL_LINE_SMOOTH_HINT, GL11.GL_NICEST);
		 **/
		Distinct.getInstance().getMinecraft().entityRenderer.disableLightmap(0.0D);
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glBlendFunc(770, 771);
		GL11.glDisable(2896);
		GL11.glDisable(2929);
		GL11.glDisable(3553);
		GL11.glEnable(3042);
		GL11.glDisable(2929);
		GL11.glEnable(2848);
		GL11.glLineWidth(1.5f);
	}
	public void glClose() {
		/**
		GL11.glPopMatrix();/
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_LINE_SMOOTH);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_FOG);
        Distinct.getInstance().getMinecraft().entityRenderer.enableLightmap(1.0D);
		 **/
		Distinct.getInstance().getMinecraft().entityRenderer.disableLightmap(1.0D);
		//GL11.glEnable(GL11.GL_FOG);
		GL11.glDepthMask(true);
		GL11.glEnable(2929);
		GL11.glDisable(3042);
		GL11.glEnable(3553);
		GL11.glEnable(2929);
	}
	public static void drawOutlinedBoundingBox(AxisAlignedBB axbb) {
		VertexRenderer var2 = VertexRenderer.instance;
		var2.startDrawing(3);

		var2.addVertex(axbb.minX, axbb.minY, axbb.minZ);
		var2.addVertex(axbb.maxX, axbb.minY, axbb.minZ);
		var2.addVertex(axbb.maxX, axbb.minY, axbb.maxZ);
		var2.addVertex(axbb.minX, axbb.minY, axbb.maxZ);
		var2.addVertex(axbb.minX, axbb.minY, axbb.minZ);
		var2.draw();
		var2.startDrawing(3);

		var2.addVertex(axbb.minX, axbb.maxY, axbb.minZ);
		var2.addVertex(axbb.maxX, axbb.maxY, axbb.minZ);
		var2.addVertex(axbb.maxX, axbb.maxY, axbb.maxZ);
		var2.addVertex(axbb.minX, axbb.maxY, axbb.maxZ);
		var2.addVertex(axbb.minX, axbb.maxY, axbb.minZ);
		var2.draw();
		var2.startDrawing(1);

		var2.addVertex(axbb.minX, axbb.minY, axbb.minZ);
		var2.addVertex(axbb.minX, axbb.maxY, axbb.minZ);
		var2.addVertex(axbb.maxX, axbb.minY, axbb.minZ);
		var2.addVertex(axbb.maxX, axbb.maxY, axbb.minZ);
		var2.addVertex(axbb.maxX, axbb.minY, axbb.maxZ);
		var2.addVertex(axbb.maxX, axbb.maxY, axbb.maxZ);
		var2.addVertex(axbb.minX, axbb.minY, axbb.maxZ);
		var2.addVertex(axbb.minX, axbb.maxY, axbb.maxZ);
		var2.draw();
		
		var2.startDrawing(1);
		var2.addVertex(axbb.minX, axbb.minY, axbb.maxZ);
		var2.addVertex(axbb.minX, axbb.maxY, axbb.minZ);
		var2.draw();
		
		var2.startDrawing(1);
		var2.addVertex(axbb.maxX, axbb.minY, axbb.minZ);
		var2.addVertex(axbb.minX, axbb.maxY, axbb.minZ);
		var2.draw();
		
		var2.startDrawing(1);
		var2.addVertex(axbb.maxX, axbb.minY, axbb.maxZ);
		var2.addVertex(axbb.minX, axbb.maxY, axbb.maxZ);
		var2.draw();
		
		var2.startDrawing(1);
		var2.addVertex(axbb.maxX, axbb.minY, axbb.maxZ);
		var2.addVertex(axbb.maxX, axbb.maxY, axbb.minZ);
		var2.draw();
		
		var2.startDrawing(1);
		var2.addVertex(axbb.minX, axbb.minY, axbb.minZ);
		var2.addVertex(axbb.minX, axbb.minY, axbb.maxZ);
		var2.draw();
		
		var2.startDrawing(1);
		var2.addVertex(axbb.minX, axbb.maxY, axbb.minZ);
		var2.addVertex(axbb.maxX, axbb.maxY, axbb.maxZ);
		var2.draw();
		
		var2.startDrawing(1);
		var2.addVertex(axbb.maxX, axbb.minY, axbb.maxZ);
		var2.addVertex(axbb.minX, axbb.minY, axbb.minZ);
		var2.draw();
		
	}
	public void drawRect(double startX, double startY, double endX, double endY, int color) {
        double var5;

        if (startX < endX)
        {
            var5 = startX;
            startX = endX;
            endX = var5;
        }

        if (startY < endY)
        {
            var5 = startY;
            startY = endY;
            endY = var5;
        }
        GL11.glPushAttrib(GL11.GL_BLEND);
        GL11.glPushMatrix();
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        e.setHex(color);
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glVertex2d(startX, endY);
        GL11.glVertex2d(endX, endY);
        GL11.glVertex2d(endX, startY);
        GL11.glVertex2d(startX, startY);
        GL11.glEnd();
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glPopMatrix();
        GL11.glPopAttrib();
    }

    public void drawBorderedRect(double startX, double startY, double endX, double endY, int color, int lineColor, float lineWidth) {
        drawRect(startX, startY, endX, endY, color);
       glOpen();
      e.setHex(lineColor);
        GL11.glLineWidth(lineWidth);
        GL11.glBegin(GL11.GL_LINE_LOOP);
        GL11.glVertex2d(startX, startY);
        GL11.glVertex2d(endX, startY);
        GL11.glVertex2d(endX, endY);
        GL11.glVertex2d(startX, endY);
        glClose();
    }
}
