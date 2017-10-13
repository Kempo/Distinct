package kempodev.distinct.modules.misc.radar;
import kempodev.distinct.main.Distinct;
import kempodev.distinct.utilities.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.Tessellator;

import org.lwjgl.opengl.GL11;
/**
 * 
 * @author JMTNerdy
 * I adapted JMTNerdy's old terrain rendering radar which was first released 2 years ago(ish)
 * His terrain rendering however is not very smart and is somewhat heavily modified to actually work in this case lol.
 * 
 */
public class Radar extends Gui 
{
	private int posX;
	private int posY;
	private Minecraft mc;
	private Gui g;
	private EntityHandler eh;
	private TerrainHandler th;
	private Utilities jg;
	private ScaledResolution sr;
	private boolean isDrawing = false;
	private RenderUtil render = new RenderUtil();
	public Radar(int posX, int posY, Minecraft mc, int entityRadius)
	{
		this.mc = mc;
		g = this;
		this.posX = posX;
		this.posY = posY;
		eh = new EntityHandler(mc, g, entityRadius - 1);
		th = new TerrainHandler(mc, g, -entityRadius, entityRadius);
		jg = new Utilities();
		sr = new ScaledResolution(mc.gameSettings, mc.displayWidth, mc.displayHeight);
		drawRadar(entityRadius);
	}
	
	public void setLocation(int x, int y)
	{
		this.posX = x;
		this.posY = y;
	}
	
	public void drawRadar(int r)
	{
		
		if(!Tessellator.instance.isDrawing) {
		GL11.glPushMatrix();
    	GL11.glTranslatef(posX, posY, 0);
    	GL11.glRotatef(-mc.thePlayer.rotationYaw, 0, 0, 1);
        try
        {
        	if(Boolean.parseBoolean(Distinct.getInstance().getModuleByName("Radar").getValueByField("renderTerrain").toString()))
        	{
        		th.drawSurface(0, 0);
        	}
			eh.renderSurroundingEntities(0, 0);
		}
		catch(Exception e)
		{
		}		
        GL11.glRotatef(mc.thePlayer.rotationYaw, 0, 0, 1);
        jg.drawHollowCircle(0, 0, (float)r, 360, (float)1.5*sr.scaleFactor, 0xffffffff);
        jg.drawIsoscolesTriangle(0, 0, 2, 0, 0xaaffff00);
        jg.drawIsoscolesTriangleOutline(0, 0, 2, 0, 2, 0xffffff00);
    	GL11.glTranslatef(-posX, -posY, 0);
        GL11.glPopMatrix();
		}
	}
}
