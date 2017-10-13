package kempodev.distinct.modules.misc;

import kempodev.distinct.main.Distinct;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;

import org.lwjgl.opengl.GL11;

public class NametagEditor {
	public void renderChosenLabel(Entity e, String name, double par3, double par5, double par7, RenderManager renderManager) {
		if(Distinct.getInstance().getModuleByName("Names").enabled && !(Distinct.getInstance().getMinecraft().currentScreen instanceof GuiInventory)) {
			this.renderCustomLabel(e, name, par3, par5, par7, renderManager);
		}else{
			this.renderDefaultLabel(e, name, par3, par5, par7, renderManager);
		}
	}
	private void renderDefaultLabel(Entity e, String name, double par3, double par5, double par7, RenderManager renderManager) {

		double var10 = e.getDistanceSqToEntity(renderManager.livingPlayer);

        if (var10 <= (double)(64 * 64))
        {
            FontRenderer var12 = Distinct.getInstance().getFontRenderer();
            float var13 = 1.6F;
            float var14 = 0.016666668F * var13;
            GL11.glPushMatrix();
            GL11.glTranslatef((float)par3 + 0.0F, (float)par5 + e.height + 0.5F, (float)par7);
            GL11.glNormal3f(0.0F, 1.0F, 0.0F);
            GL11.glRotatef(-renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
            GL11.glRotatef(renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
            GL11.glScalef(-var14, -var14, var14);
            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glDepthMask(false);
            GL11.glDisable(GL11.GL_DEPTH_TEST);
            GL11.glEnable(GL11.GL_BLEND);
            OpenGlHelper.glBlendFunc(770, 771, 1, 0);
            Tessellator var15 = Tessellator.instance;
            byte var16 = 0;

            if (name.equals("deadmau5"))
            {
                var16 = -10;
            }

            GL11.glDisable(GL11.GL_TEXTURE_2D);
            var15.startDrawingQuads();
            int var17 = var12.getStringWidth(name) / 2;
            var15.setColorRGBA_F(0.0F, 0.0F, 0.0F, 0.25F);
            var15.addVertex((double)(-var17 - 1), (double)(-1 + var16), 0.0D);
            var15.addVertex((double)(-var17 - 1), (double)(8 + var16), 0.0D);
            var15.addVertex((double)(var17 + 1), (double)(8 + var16), 0.0D);
            var15.addVertex((double)(var17 + 1), (double)(-1 + var16), 0.0D);
            var15.draw();
            GL11.glEnable(GL11.GL_TEXTURE_2D);
            var12.drawString(name, -var12.getStringWidth(name) / 2, var16, 553648127);
            GL11.glEnable(GL11.GL_DEPTH_TEST);
            GL11.glDepthMask(true);
            var12.drawString(name, -var12.getStringWidth(name) / 2, var16, -1);
            GL11.glEnable(GL11.GL_LIGHTING);
            GL11.glDisable(GL11.GL_BLEND);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glPopMatrix();
        }
	}
	private void renderCustomLabel(Entity par1EntityLivingBase, String par2Str, double par3, double par5, double par7, RenderManager renderManager) 
	{

		double var10 = par1EntityLivingBase.getDistanceSqToEntity(renderManager.livingPlayer);
		FontRenderer var12 = Distinct.getInstance().getFontRenderer();
		float var13 = Distinct.getInstance().getHooks().onSetNametagSizeHook(par1EntityLivingBase);
		float var14 = 0.016666668F * var13;
		GL11.glPushMatrix();
		GL11.glDisable(GL11.GL_FOG);
		GL11.glTranslatef((float)par3 + 0.0F, (float)par5 + par1EntityLivingBase.height + 0.7F, (float)par7);
		GL11.glNormal3f(0.0F, 1.0F, 0.0F);
		GL11.glRotatef(-renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
		GL11.glRotatef(renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
		GL11.glScalef(-var14, -var14, var14);
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glDepthMask(false);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		Tessellator var15 = Tessellator.instance;
		byte var16 = (byte) (-Distinct.getInstance().getHooks().onSetNametagSizeHook(par1EntityLivingBase) / 2);

		if (par2Str.equals("deadmau5"))
		{
			var16 = -10;
		}

		String b = Distinct.getInstance().getHooks().onSetPlacementHook(par2Str);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		var15.startDrawingQuads();
		int var17 = var12.getStringWidth(b) / 2;
		var15.setColorRGBA_F(0.0F, 0.0F, 0.0F, 0.45F);
		var15.addVertex((double)(-var17 - 1), (double)(-1 + var16), 0.0D);
		var15.addVertex((double)(-var17 - 1), (double)(8 + var16), 0.0D);
		var15.addVertex((double)(var17 + 1), (double)(8 + var16), 0.0D);
		var15.addVertex((double)(var17 + 1), (double)(-1 + var16), 0.0D);
		var15.draw();
		GL11.glEnable(GL11.GL_TEXTURE_2D);

		var12.drawString(b, -var12.getStringWidth(b) / 2, var16, -1);
		GL11.glDepthMask(true);
		var12.drawStringWithShadow(b, -var12.getStringWidth(b) / 2, var16, -1);

		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glEnable(GL11.GL_FOG);
		GL11.glPopMatrix();
	}

}
