package kempodev.distinct.modules.misc.radar;

import kempodev.distinct.main.Distinct;
import kempodev.distinct.utilities.RenderUtil;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.AxisAlignedBB;

import org.lwjgl.opengl.GL11;

public class Utilities 
{
	private RenderUtil render = new RenderUtil();
    public void drawHollowCircle(float cx, float cy, float r, int num_segments, float lw, int c) 
    { 
    	Distinct.getInstance().getMinecraft().entityRenderer.disableLightmap(0.0D);
        float f = (float) (c >> 24 & 0xff) / 255F;
        float f1 = (float) (c >> 16 & 0xff) / 255F;
        float f2 = (float) (c >> 8 & 0xff) / 255F;
        float f3 = (float) (c & 0xff) / 255F;
    	float theta = (float) (2 * 3.1415926 / (num_segments)); 
    	float p = (float) Math.cos(theta);//calculate the sine and cosine
    	float s = (float) Math.sin(theta);
    	float t;
        GL11.glColor4f(f1, f2, f3, f);
    	float x = r;
    	float y = 0;//start at angle = 0  
    	render.glOpen();
        GL11.glLineWidth(lw);
    	GL11.glBegin(GL11.GL_LINE_LOOP); 
    	for(int ii = 0; ii < num_segments; ii++) 
    	{ 
    		GL11.glVertex2f(x + cx, y + cy);//final vertex vertex 
            
    		//rotate the stuff
    		t = x;
    		x = p * x - s * y;
    		y = s * t + p * y;
    	} 
    	GL11.glEnd(); 
    	Distinct.getInstance().getMinecraft().entityRenderer.enableLightmap(1.0D);
    	render.glClose();
    }
       
    public static void drawFullCircle(int cx, int cy, double r, int c) 
    {
        float f = (float) (c >> 24 & 0xff) / 255F;
        float f1 = (float) (c >> 16 & 0xff) / 255F;
        float f2 = (float) (c >> 8 & 0xff) / 255F;
        float f3 = (float) (c & 0xff) / 255F;
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GL11.glBlendFunc(770, 771);
        GL11.glColor4f(f1, f2, f3, f);
        GL11.glBegin(GL11.GL_TRIANGLE_FAN);
        for(int i = 0; i <= 360; i++) 
        {
            double x = Math.sin((i * 3.141526D / 180)) * r;
            double y = Math.cos((i * 3.141526D / 180)) * r;
            GL11.glVertex2d(cx + x, cy + y);
        }
        GL11.glEnd();
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_LINE_SMOOTH);

    }

    
    public void drawIsoscolesTriangleOutline(double cx, double cy, int sizefactor, float theta, int lw, int c)
    {
    	GL11.glTranslated(cx, cy, 0);
    	GL11.glRotatef(180 + theta, 0F,0F,1.0F);
        float f = (float) (c >> 24 & 0xff) / 255F;
        float f1 = (float) (c >> 16 & 0xff) / 255F;
        float f2 = (float) (c >> 8 & 0xff) / 255F;
        float f3 = (float) (c & 0xff) / 255F;
        GL11.glColor4f(f1, f2, f3, f);
        Distinct.getInstance().getMinecraft().entityRenderer.disableLightmap(0.0D);
        render.glOpen();
        GL11.glLineWidth(lw);
        GL11.glBegin(GL11.GL_LINE_LOOP);
        
        GL11.glVertex2d(0,(1*sizefactor));
        GL11.glVertex2d((1*sizefactor),-(1*sizefactor));
        GL11.glVertex2d(-(1*sizefactor),-(1*sizefactor));
        
        GL11.glEnd();
        Distinct.getInstance().getMinecraft().entityRenderer.enableLightmap(1.0D);
       render.glClose();
    	GL11.glRotatef(-180 - theta, 0F,0F,1.0F);
    	GL11.glTranslated(-cx, -cy, 0);
    }
    public void drawIsoscolesTriangle(double cx, double cy, int sizefactor, float theta, int c)
    {
    	render.glOpen();
    	Distinct.getInstance().getMinecraft().entityRenderer.disableLightmap(0.0D);
    	GL11.glTranslated(cx, cy, 0);
    	GL11.glRotatef(180 + theta, 0F,0F,1.0F);
        float f = (float) (c >> 24 & 0xff) / 255F;
        float f1 = (float) (c >> 16 & 0xff) / 255F;
        float f2 = (float) (c >> 8 & 0xff) / 255F;
        float f3 = (float) (c & 0xff) / 255F;
        GL11.glColor4f(f1, f2, f3, f);
        
        GL11.glBegin(GL11.GL_TRIANGLES);
        
        GL11.glVertex2d(0,(1*sizefactor));
        GL11.glVertex2d((1*sizefactor),-(1*sizefactor));
        GL11.glVertex2d(-(1*sizefactor),-(1*sizefactor));
        
        GL11.glEnd();
        
    	GL11.glRotatef(-180 - theta, 0F,0F,1.0F);
    	GL11.glTranslated(-cx, -cy, 0);
    	Distinct.getInstance().getMinecraft().entityRenderer.enableLightmap(1.0D);
    	render.glClose();
    }
}
