package kempodev.distinct.modules.visual;

import kempodev.distinct.base.BaseModule;
import kempodev.distinct.hooks.HookTypes;
import kempodev.distinct.main.Distinct;
import kempodev.distinct.utilities.ColorUtil;
import kempodev.distinct.utilities.RenderUtil;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StringUtils;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
public class ModuleTracers extends BaseModule{
	public float width = 1.5F;
	private ColorUtil colorUtil = new ColorUtil();
	private RenderUtil renderUtil = new RenderUtil();
	//private boolean tracePlayers = true,traceLiving = false;
	public ModuleTracers() {
		super("Tracers", Keyboard.KEY_P, 0xC4C957, null);
		addHookType(HookTypes.RENDER);
		addField("tracePlayers",true);
		addField("traceLiving",false);
		Distinct.getInstance().lol.add("youusing");
		// TODO Auto-generated constructor stub
	}
	@Override
	public void onRender() {
		// Really not the most efficient way. ~kempo
		renderUtil.glOpen();
		for(Object o : Distinct.getInstance().getWorld().loadedEntityList) {
			if(o instanceof EntityPlayer && Boolean.parseBoolean(getValueByField("tracePlayers").toString())) {
				EntityPlayer e =  (EntityPlayer) o;
				if(e != null && e != Distinct.getInstance().getPlayer() && !Distinct.getInstance().getPlayer().getCommandSenderName().equalsIgnoreCase(e.getCommandSenderName()) && e.getHealth() > 0 && !isShop(e) && getClient().getPlayer().getDistanceToEntity(e) <= 256 && !isDummy(e)) {
					GL11.glColor4f(1, Distinct.getInstance().getPlayer().getDistanceToEntity(e) / 64, 0, 1);
					if (Distinct.getInstance().getPlayer().getDistanceToEntity(e) >= 64) {
						GL11.glColor4d(0, 1, 0, 1);
					}
					double var10 = RenderManager.instance.viewerPosX;
					double var12 = RenderManager.instance.viewerPosY;
					double var14 = RenderManager.instance.viewerPosZ;
					double d = e.lastTickPosX + (e.posX - e.lastTickPosX) * (double)getClient().getMinecraft().timer.renderPartialTicks;
					double d1 = e.lastTickPosY + (e.posY - e.lastTickPosY) * (double)getClient().getMinecraft().timer.renderPartialTicks;
					double d2 = e.lastTickPosZ + (e.posZ - e.lastTickPosZ) * (double)getClient().getMinecraft().timer.renderPartialTicks;
					double x = d-var10;
					double y = d1-var12;
					double z = d2-var14;
					//float interpolatedYaw = (float)(e.prevRotationYaw + (e.rotationYaw - e.prevRotationYaw) * (double)getClient().getMinecraft().timer.renderPartialTicks);
					
					if (Distinct.getInstance().getFriendManager().getFriends().containsKey(StringUtils.stripControlCodes(e.getCommandSenderName()))) {
						ColorUtil fcolor = new ColorUtil();
						fcolor.setHex(0x55FFFF);
						GL11.glColor4d(fcolor.getRed(), fcolor.getGreen(), fcolor.getBlue(), 0.6);
					}
					GL11.glLineWidth(width);
					GL11.glBegin(GL11.GL_LINES);
					GL11.glVertex2d(0, 0);
					GL11.glVertex3d(x, y, z);
					GL11.glEnd();
					AxisAlignedBB axbb = new AxisAlignedBB(x - e.width, y, z - e.width, x + e.width, y + e.height + 0.2, z + e.width);
					axbb = axbb.contract(0.2, 0, 0.2);
					if (e.isSneaking()) {
						GL11.glColor4d(1, 0, 0.5, 0.6);
					} else if (Distinct.getInstance().getFriendManager().getFriends().containsKey(StringUtils.stripControlCodes(e.getCommandSenderName()))) {
						ColorUtil fcolor = new ColorUtil();
						fcolor.setHex(0x55FFFF);
						GL11.glColor4d(fcolor.getRed(), fcolor.getGreen(), fcolor.getBlue(), 0.6);
					} else {
						ColorUtil dcolor = new ColorUtil();
						dcolor.setHex(0x00FF00);
						GL11.glColor4d(dcolor.getRed(), dcolor.getGreen(), dcolor.getBlue(), 0.6);
					}
				
					
			
					GL11.glPushMatrix();
					{
					GL11.glTranslated(x, y, z);
			        GL11.glRotatef(e.rotationYaw, 0.0F, (float)y, 0.0F);
			        GL11.glTranslated(-x, -y, -z);
					RenderUtil.drawOutlinedBoundingBox(axbb);
					}
					GL11.glPopMatrix();
					//System.out.println(e.getCommandSenderName() + "   " + getClient().getPlayer().getDistanceToEntity(e));
//					drawAccordingToHeight(e,x,y,z);
				}
			}
			if(o instanceof EntityLivingBase && Boolean.parseBoolean(getValueByField("traceLiving").toString())) {
				EntityLivingBase e = (EntityLivingBase)o;
				if(e != null && !(e instanceof EntityPlayer) && Distinct.getInstance().getPlayer().getDistanceToEntity(e) <= 35 && e.getHealth() > 0 && !isShop(e)) {
					colorUtil.setHex(0xF56DFC);
					double var10 = RenderManager.instance.viewerPosX;
					double var12 = RenderManager.instance.viewerPosY;
					double var14 = RenderManager.instance.viewerPosZ;
					double d = e.lastTickPosX + (e.posX - e.lastTickPosX) * (double)getClient().getMinecraft().timer.renderPartialTicks;
					double d1 = e.lastTickPosY + (e.posY - e.lastTickPosY) * (double)getClient().getMinecraft().timer.renderPartialTicks;
					double d2 = e.lastTickPosZ + (e.posZ - e.lastTickPosZ) * (double)getClient().getMinecraft().timer.renderPartialTicks;
					double x = d-var10;
					double y = d1-var12;
					double z = d2-var14;
					GL11.glColor4d(colorUtil.getRed(),colorUtil.getGreen(),colorUtil.getBlue(), 1);
					GL11.glLineWidth(width);
					GL11.glBegin(GL11.GL_LINES);
					GL11.glVertex2d(0, 0);
					GL11.glVertex3d(x, y, z);
					GL11.glEnd();
					AxisAlignedBB axbb = new AxisAlignedBB(x - e.width, y, z + e.width, x + e.width, y + e.height, z - e.width);
					GL11.glPushMatrix();
					{
					GL11.glTranslated(x, y, z);
			        GL11.glRotatef(e.rotationYaw, 0.0F, (float)y, 0.0F);
			        GL11.glTranslated(-x, -y, -z);
					RenderUtil.drawOutlinedBoundingBox(axbb);
					}
					GL11.glPopMatrix();
//										drawAccordingToHeight(e,x,y,z);
				}
			}
		}
		renderUtil.glClose();
	}
	
	private void drawAccordingToHeight(EntityLivingBase e,double renderX,double renderY,double renderZ) {
		setColorByState(e);
		GL11.glBegin(GL11.GL_LINES);
		GL11.glVertex3d(renderX, renderY, renderZ);
		GL11.glVertex3d(renderX, renderY + (e.getEyeHeight()), renderZ);
		GL11.glEnd();
	}
	
	private void setColorByState(EntityLivingBase e) {
		if(!Distinct.getInstance().getFriendManager().getFriends().containsKey(StringUtils.stripControlCodes(e.getCommandSenderName()))) {
			if(e.isSneaking() || e.hurtTime > 0) {
				colorUtil.setHex(0xE01B5D);
				GL11.glColor3d(colorUtil.getRed(), colorUtil.getGreen(), colorUtil.getBlue());
			}else{
				colorUtil.setHex(0x2FE01B);
				GL11.glColor3d(colorUtil.getRed(), colorUtil.getGreen(), colorUtil.getBlue());
			}
		}else{
			colorUtil.setHex(0x39E3E6);
			GL11.glColor3d(colorUtil.getRed(), colorUtil.getGreen(), colorUtil.getBlue());
		}
	}
	
	private void setColorByDistance(EntityLivingBase e) {
		int color = setColor(Distinct.getInstance().getPlayer().getDistanceToEntity(e));
		if(Distinct.getInstance().getFriendManager().getFriends().containsKey(StringUtils.stripControlCodes(e.getCommandSenderName()))) {
			color = 6;
		}
		double distance = Distinct.getInstance().getPlayer().getDistanceToEntity(e);

		switch (color) {
		/**Red,Orange,Yellow,Green,Friend **/
		case 1:  
			colorUtil.setHex(0xE01B5D);
			GL11.glColor3d(colorUtil.getRed(), colorUtil.getGreen(), colorUtil.getBlue()); break;
		case 2:  
			colorUtil.setHex(0xE0AB1B);
			GL11.glColor3d(colorUtil.getRed(), colorUtil.getGreen(), colorUtil.getBlue()); break;
		case 3:  
			colorUtil.setHex(0xD9E01B);
			GL11.glColor3d(colorUtil.getRed(), colorUtil.getGreen(), colorUtil.getBlue()); break;
		case 4:
			colorUtil.setHex(0xAFE01B);
			GL11.glColor3d(colorUtil.getRed(), colorUtil.getGreen(), colorUtil.getBlue()); break;
		case 5:
			colorUtil.setHex(0x2FE01B);
			GL11.glColor3d(colorUtil.getRed(), colorUtil.getGreen(), colorUtil.getBlue()); break;
		case 6:
			colorUtil.setHex(0x39E3E6);
			GL11.glColor3d(colorUtil.getRed(), colorUtil.getGreen(), colorUtil.getBlue()); break;
		default: 
			colorUtil.setHex(0x2FE01B);
			GL11.glColor3d(colorUtil.getRed(), colorUtil.getGreen(), colorUtil.getBlue()); break;
		}
	}
	
	private int setColor(float distance) {
		if(distance <= 16) {
			return 1;
		}
		if(distance > 16 && distance <= 32) {
			return 2;
		}
		if(distance > 32 && distance <= 48) {
			return 3;
		}
		if(distance > 48 && distance <= 64) {
			return 4;
		}
		if(distance > 64) {
			return 5;
		}
		return 0;
	}
	
	/**
	private float getTransparencyForDistance(double distance) {
		return MathHelper.clamp_float((float)(0.65F + (distance / 255)), 0f, 1.0F);
	}
	 **/
	@Override
	public void onCommand(String i) {
		String args[] = i.split(" ");
		if(args[0].equalsIgnoreCase(this.setCommand("trace"))) {
			if(args[1].equalsIgnoreCase("living")) {
				setField("traceLiving",!Boolean.parseBoolean(getValueByField("traceLiving").toString()));
				getClient().getChat().sendClientMessage("Tracing living entities(excluding players) is now " + (Boolean.parseBoolean(getValueByField("traceLiving").toString()) ? "enabled" : "disabled"));

			}
			if(args[1].equalsIgnoreCase("players")) {
				setField("tracePlayers",!Boolean.parseBoolean(getValueByField("tracePlayers").toString()));
				getClient().getChat().sendClientMessage("Tracing player entities is now " + (Boolean.parseBoolean(getValueByField("tracePlayers").toString()) ? "enabled" : "disabled"));
			}
		}
	}
	private boolean isShop(EntityLivingBase e) {
		for(Object b : getClient().getWorld().field_147482_g) {
			if(b instanceof TileEntityChest) {
				TileEntityChest c = (TileEntityChest)b;
				String name = StringUtils.stripControlCodes(e.getCommandSenderName());
				if((withinRange(e.posX,e.posY,e.posZ,c.field_145851_c,c.field_145848_d,c.field_145849_e) && e.getTotalArmorValue() == 0 && e.isEntityInsideOpaqueBlock()) || name.startsWith("[S]")) {
					return true;
				}
			}
		}
		return false;
	}
	
	private boolean withinRange(double x,double y,double z, int x2,int y2,int z2) {
		return getDistance(x,y,z,x2,y2,z2) <= 1;
	}
	
	public int getDistance(double x,double y, double z,int par1, int par3, int par5)
	{
		double var7 = x - par1;
		double var9 = y - par3;
		double var11 = z - par5;
		return (int)MathHelper.sqrt_double(var7 * var7 + var9 * var9 + var11 * var11);
	}
	/**
	public static void drawOutlinedBoundingBox(AxisAlignedBB axbb) {
		Tessellator var2 = Tessellator.instance;
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
	**/
}
