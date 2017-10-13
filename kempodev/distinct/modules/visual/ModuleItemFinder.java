package kempodev.distinct.modules.visual;

import java.util.List;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.util.AxisAlignedBB;
import kempodev.distinct.base.BaseModule;
import kempodev.distinct.hooks.HookTypes;
import kempodev.distinct.main.Distinct;
import kempodev.distinct.utilities.ColorUtil;
import kempodev.distinct.utilities.RenderUtil;

public class ModuleItemFinder extends BaseModule{
	private final ColorUtil colorUtil = new ColorUtil();
	private final RenderUtil renderUtil = new RenderUtil();
	public ModuleItemFinder() {
		super("ItemFinder", -1, -1, null);
		this.addHookType(HookTypes.RENDER);
		
		// TODO Auto-generated constructor stub
	}
	@Override
	public void onRender() {
		colorUtil.setHex(0xFFFFFF);
		renderUtil.glOpen();
		List list = getClient().getWorld().loadedEntityList;
		for(Object o : list) {
			if(o != null && o instanceof EntityItem) {
				EntityItem e = (EntityItem)o;
				double var10 = RenderManager.instance.viewerPosX;
				double var12 = RenderManager.instance.viewerPosY;
				double var14 = RenderManager.instance.viewerPosZ;
				double d = e.lastTickPosX + (e.posX - e.lastTickPosX) * (double)getClient().getMinecraft().timer.renderPartialTicks;
				double d1 = e.lastTickPosY + (e.posY - e.lastTickPosY) * (double)getClient().getMinecraft().timer.renderPartialTicks;
				double d2 = e.lastTickPosZ + (e.posZ - e.lastTickPosZ) * (double)getClient().getMinecraft().timer.renderPartialTicks;
				double x = d-var10;
				double y = d1-var12;
				double z = d2-var14;
				GL11.glDisable(GL11.GL_TEXTURE_2D);
				GL11.glColor4d(colorUtil.getRed(),colorUtil.getGreen(),colorUtil.getBlue(), 1);
				GL11.glLineWidth(1.5F);
				GL11.glBegin(GL11.GL_LINES);
				GL11.glVertex2d(0, 0);
				GL11.glVertex3d(x, y, z);
				GL11.glEnd();
				
				renderItemName(x,y,z,e);
				//AxisAlignedBB axbb = new AxisAlignedBB(x - e.width, y, z + e.width, x + e.width, y + e.height, z - e.width);
				//renderUtil.drawOutlinedBoundingBox(axbb);
			}
		}
		renderUtil.glClose();
	}
	@Override
	public void onPreRenderEntity(Entity var25) {
		if(var25 instanceof EntityItem) {
			GL11.glPushMatrix();
			GL11.glDisable(GL11.GL_DEPTH_TEST);
		}
	}
	@Override
	public void onPostRenderEntity(Entity var25) {
		if(var25 instanceof EntityItem) {
			GL11.glEnable(GL11.GL_DEPTH_TEST);
			GL11.glPopMatrix();
		}
	}
	private void renderItemName(double x,double y,double z,EntityItem e) {
		double var10 = e.getDistanceSqToEntity(getClient().getPlayer());
		FontRenderer var12 = Distinct.getInstance().getFontRenderer();
		float var13 = Distinct.getInstance().getHooks().onSetNametagSizeHook(e);
		float var14 = 0.010666668F * var13;
		GL11.glPushMatrix();
		GL11.glTranslatef((float)x + 0.0F, (float)y + e.height + 0.7F, (float)z);
		GL11.glNormal3f(0.0F, 1.0F, 0.0F);
		GL11.glRotatef(-RenderManager.instance.playerViewY, 0.0F, 1.0F, 0.0F);
		GL11.glRotatef(RenderManager.instance.playerViewX, 1.0F, 0.0F, 0.0F);
		GL11.glScalef(-var14, -var14, var14);
		//GL11.glDisable(GL11.GL_LIGHTING);
		//GL11.glDisable(GL11.GL_DEPTH_TEST);
		//GL11.glEnable(GL11.GL_BLEND);
		//GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		Tessellator var15 = Tessellator.instance;
		byte var16 = (byte) (-Distinct.getInstance().getHooks().onSetNametagSizeHook(e) / 2);

		if (e.getCommandSenderName().equals("deadmau5"))
		{
			var16 = -10;
		}

		//String b = Distinct.getInstance().getHooks().onSetPlacementHook(e.getCommandSenderName());
		String name = e.getCommandSenderName();
		name = name.replaceAll("tile.", "");
		name = name.replaceAll("name.", "");
		name = name.replaceAll(".default", "");
		name = name.replaceAll("item.", "");
		name = name.replaceAll("cloth.", "wool");
		name = name.replaceAll("log.", "log");
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		
		var15.startDrawingQuads();
		int var17 = var12.getStringWidth(name) / 2;
		
		var15.setColorRGBA_F(0.0F, 0.0F, 0.0F, 0.45F);
		var15.addVertex((double)(-var17 - 1), (double)(-1 + var16), 0.0D);
		var15.addVertex((double)(-var17 - 1), (double)(8 + var16), 0.0D);
		var15.addVertex((double)(var17 + 1), (double)(8 + var16), 0.0D);
		var15.addVertex((double)(var17 + 1), (double)(-1 + var16), 0.0D);
		var15.draw();
		
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		var12.drawString(name, -var12.getStringWidth(name) / 2, var16, -1);
		var12.drawStringWithShadow(name, -var12.getStringWidth(name) / 2, var16, -1);

		//GL11.glEnable(GL11.GL_DEPTH_TEST);
		//GL11.glEnable(GL11.GL_LIGHTING);
		//GL11.glDisable(GL11.GL_BLEND);
		//GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glPopMatrix();
	}
}
