package kempodev.distinct.modules.misc.radar;

import kempodev.distinct.main.Distinct;
import kempodev.distinct.utilities.ColorUtil;
import kempodev.distinct.utilities.RenderUtil;
import kempodev.distinct.utilities.VertexRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.IIcon;
import net.minecraft.util.StringUtils;

import org.lwjgl.opengl.GL11;



public class EntityHandler 
{
	private int radius;
	private Minecraft mc;
	private Gui g;
	private RenderUtil render = new RenderUtil();
	private ColorUtil colorUtil = new ColorUtil();
	public EntityHandler(Minecraft mc, Gui g, int radius)
	{
		this.radius = radius;
		this.mc = mc;
		this.g = g;
	}
	
	public void renderSurroundingEntities(int centerX, int centerY)
	{
		for(Object o : getClient().getWorld().loadedEntityList) {
			if(o instanceof EntityLivingBase) {
			EntityLivingBase e = (EntityLivingBase)o;
			double dPosX = getClient().getPlayer().posX - e.posX;
			double dPosZ = getClient().getPlayer().posZ - e.posZ;
			
			if(dPosX*dPosX + dPosZ*dPosZ > radius*radius)
        	{
        		continue;
        	}
				if(e != null) {
		            drawEntity(dPosX,dPosZ,e);
				}
			}
		}
	}

	private void drawEntity(double x, double y,EntityLivingBase e) {
		GL11.glPushMatrix();
		if(!(e instanceof EntityBat || e instanceof EntitySquid)) {
	    drawCircle((int)x,(int)y,0.9D,getColor(e),1,true);
		}
	    if(e instanceof EntityPlayer) {
	    	//GL11.glScalef(0.5F, 0.5F, 0.5F);
	    	GL11.glTranslated(x, y, 0.0F);
	        GL11.glRotatef(getClient().getPlayer().rotationYaw, 0.0F, 0.0F, 1.0F);
	        GL11.glTranslated(-x, -y, 0.0F);
	        GL11.glScalef(0.5F, 0.5F, 0.5F);
	        if(e != getClient().getPlayer())
	        //getClient().getFontRenderer().drawStringWithShadow(e.getCommandSenderName(), (int)x + 5, (int)y, getNametagColor(e));
	        g.drawCenteredString(getClient().getFontRenderer(), e.getCommandSenderName(), (int)(x * 2), (int)(y * 2) - 12, getNametagColor(e));
	        GL11.glScalef(2.0F, 2.0F, 2.0F);
	    }
	    GL11.glPopMatrix();
	}
	private int getColor(EntityLivingBase e) {
		if(e instanceof EntityPlayer && e != getClient().getPlayer()) {
			return 0x54BA5C;
		}
		if(e instanceof EntityAnimal) {
			return 0xF56DFC;
		}
		if(e instanceof EntityMob || e instanceof EntitySlime) {
			return 0xED6161;
		}
		if(e.getCommandSenderName().equals(getClient().getPlayer().getCommandSenderName())) {
			return 0xDEDE40;
		}
		//System.out.println(e.getCommandSenderName());
		return -1;
	}
	private int getNametagColor(EntityLivingBase e) {
		EntityPlayer player = (EntityPlayer)e;
		if(player != null) {
			if(getClient().getFriendManager().getFriends().containsKey(StringUtils.stripControlCodes(player.getCommandSenderName()))) {
				return 0x62CBE3;
			}
		}
		return 0xFFFFFF;
	}
	private void drawCircle(double x, double y, double r, int c,double opac, boolean filled)
	{
	    colorUtil.setHex(c);
	    render.glOpen();
	    GL11.glColor4d(colorUtil.getRed(), colorUtil.getGreen(), colorUtil.getBlue(), opac);
		GL11.glBegin(filled ? 6 : GL11.GL_LINE_LOOP);
	    for (int i = 0; i <= 360; i++)
	    {
	      double x2 = Math.sin(i * 3.141526D / 180.0D) * r;
	      double y2 = Math.cos(i * 3.141526D / 180.0D) * r;
	      GL11.glVertex2d(x + x2, y + y2);
	    }
	    GL11.glEnd();
	    render.glClose();
	}
	public void drawHollowCircle(float cx, float cy, float r, float lw,int c) 
    { 
		colorUtil.setHex(c);
		GL11.glColor3d(colorUtil.getRed(), colorUtil.getGreen(), colorUtil.getBlue());
    	Distinct.getInstance().getMinecraft().entityRenderer.disableLightmap(0.0D);
    	float theta = (float) (2 * 3.1415926 / (360)); 
    	float p = (float) Math.cos(theta);//calculate the sine and cosine
    	float s = (float) Math.sin(theta);
    	float t;
    	float x = r;
    	float y = 0;//start at angle = 0  
    	render.glOpen();
        GL11.glLineWidth(lw);
    	GL11.glBegin(GL11.GL_LINE_LOOP); 
    	for(int ii = 0; ii < 360; ii++) 
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
	public Distinct getClient() {
		return Distinct.getInstance();
	}
}
