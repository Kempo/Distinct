package kempodev.distinct.modules.visual;

import java.text.DecimalFormat;
import java.util.ArrayList;

import kempodev.distinct.base.BaseModule;
import kempodev.distinct.main.Distinct;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StringUtils;

import org.lwjgl.opengl.GL11;

public class ModuleList extends BaseModule{
	public ModuleList() {
		super("ArrayList", -1, -1,"");
		this.visible = false;
		this.enabled = true;
		addHookType("RENDERUI");
		// TODO Auto-generated constructor stub
	}
	@Override
	public void onRenderUI() {
		int y = 2;
		ScaledResolution r = new ScaledResolution(getClient().getMinecraft().gameSettings, getClient().getMinecraft().displayWidth, getClient().getMinecraft().displayHeight);
		/**
		boolean canRender = getClient().getModuleByName("Radar") != null ? getClient().getModuleByName("Radar").enabled && getClient().getModuleByName("Radar").getValueByField("position").toString().equalsIgnoreCase("topleft") : true; 
		if(!canRender) {
		getClient().getFontRenderer().drawStringWithShadow("Distinct <" + getClient().getMinecraft().debugFPS + ">", 2, 2, 0xFFFFFF);
		}
		**/
		for(BaseModule m : getClient().getModuleManager().getModules()) {
			if(m.enabled && m.visible) {
				int x = r.getScaledWidth() - getClient().getFontRenderer().getStringWidth(m.getName()) - 2;
				getClient().getFontRenderer().drawStringWithShadow(m.getName(), x, y, m.getColor());
				y += 10;
			}
		}
		/**
		if(!canRender) {
		renderCoordinates();
		}
		**/
	}
	private void renderCoordinates()
	{
		ScaledResolution var2 = new ScaledResolution(Distinct.getInstance().getMinecraft().gameSettings, Distinct.getInstance().getMinecraft().displayWidth, Distinct.getInstance().getMinecraft().displayHeight);
		DecimalFormat var3 = new DecimalFormat("##");
		EntityClientPlayerMP var0 = Distinct.getInstance().getPlayer();
		var3.setDecimalSeparatorAlwaysShown(true);
		var3.setMinimumFractionDigits(1);
		var3.setMaximumFractionDigits(1);
		String var4 = var3.format(var0.posX);
		String var5 = var3.format(var0.posY);
		String var6 = var3.format(var0.posZ);
		String var7 = var4 + ", " + var5 + ", " + var6;
		GL11.glPushMatrix();
		GL11.glScaled(1, 1, 1);
		getClient().getFontRenderer().drawStringWithShadow(var7, 2, 12, -1);
		GL11.glScaled(1, 1, 1);
		GL11.glPopMatrix();
	}
	public double getDistanceToEntity(Entity par1Entity)
    {
        double var2 = (float)(getClient().getPlayer().posX - par1Entity.posX);
        double var3 = (float)(getClient().getPlayer().posY  - par1Entity.posY);
        double var4 = (float)(getClient().getPlayer().posZ  - par1Entity.posZ);
        return MathHelper.sqrt_double(var2 * var2 + var3 * var3 + var4 * var4);
    }
}
