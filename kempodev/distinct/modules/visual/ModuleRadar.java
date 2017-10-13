package kempodev.distinct.modules.visual;

import java.text.DecimalFormat;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.entity.EntityLivingBase;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import kempodev.distinct.base.BaseModule;
import kempodev.distinct.main.Distinct;
import kempodev.distinct.modules.misc.radar.Utilities;
import kempodev.distinct.modules.misc.radar.Radar;
import kempodev.distinct.radar.MapWriter;
import kempodev.distinct.utilities.ColorUtil;
import kempodev.distinct.utilities.RenderUtil;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.Packet;
import net.minecraft.util.Direction;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StringUtils;
public class ModuleRadar extends BaseModule{

	public ModuleRadar() {
		super("Radar", -1, -1, "!rpos (postion) | !rt");
		this.visible = false;
		this.disableOnClose = true;
		addHookType("RENDERUI");
	}
	@Override
	public void onRenderUI() {
		if(MapWriter.instance != null)
		MapWriter.instance.hookUpdateCameraAndRender(getClient().getMinecraft());
	}
	/**
	 * 
	 * @author Kempo, credits to JMTNerdy for original base
	 * TODO:
	 * Make terrain-rendering much more efficient, 20+ fps drops = common, FIX DAT
	 
	private ColorUtil colorUtil = new ColorUtil();
	private RenderUtil renderUtil = new RenderUtil();
	private Radar radar;
	private int offset = 65;
	private boolean rendered = false;
	public ModuleRadar() {
		super("Radar", -1, -1, "!rpos (postion) | !rt");
		this.visible = false;
		this.disableOnClose = true;
		addHookType("RENDERUI");
		addField("posx",200);
		addField("posy",150);
		addField("position","bottomright");
		addField("renderTerrain",true);
		// TODO Auto-generated constructor stub
	}
	@Override
	public void onRenderUI() {
		if(getClient().getMinecraft().inGameHasFocus) {
			ScaledResolution var2 = new ScaledResolution(Distinct.getInstance().getMinecraft().gameSettings, Distinct.getInstance().getMinecraft().displayWidth, Distinct.getInstance().getMinecraft().displayHeight);
			setPositions(getValueByField("position").toString(),var2);
			try{
				radar = new Radar(Integer.parseInt(getValueByField("posx").toString()), Integer.parseInt(getValueByField("posy").toString()), getClient().getMinecraft(),60);
			}catch(Exception e) {
				this.enabled = false;
			}
		}
	}
	private void setPositions(String input,ScaledResolution var2) {
		if(input.contains("bottom")) {
			setField("posy",var2.getScaledHeight() - offset);
		}
		if(input.contains("right")) {
			setField("posx",var2.getScaledWidth() - offset);
		}
		if(input.contains("left")) {
			setField("posx",0 + offset);
		}
		if(input.contains("top")) {
			setField("posy",0 + offset);
		}
		if(input.equalsIgnoreCase("topleft")) {
			renderCoordinates();
		}
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
		GL11.glDisable(GL11.GL_LIGHTING);
		int var25 = MathHelper.floor_double((double)(getClient().getPlayer().rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
		String direction = Direction.directions[var25];
		int y1 = var2.getScaledHeight() - 130;
		int y2 = var2.getScaledHeight() - y1;
		getClient().getFontRenderer().drawStringWithShadow(direction, 30 + (offset / 2), y2, -1);
		getClient().getFontRenderer().drawStringWithShadow(var7, (offset / 2), y2 + 10, -1);
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glPopMatrix();
	}
	@Override
	public void onCommand(String s) {
		String args[] = s.split(" ");
		if(args[0].equalsIgnoreCase(setCommand("rpos"))) {
			setField("position",args[1]);
			getClient().getChat().sendClientMessage("Radar position set to " + getValueByField("position"));
		}
		if(args[0].equalsIgnoreCase(setCommand("rt"))) {
			setField("renderTerrain",!Boolean.parseBoolean(getValueByField("renderTerrain").toString()));
			getClient().getChat().sendClientMessage("Rendering terrain is " + (Boolean.parseBoolean(getValueByField("renderTerrain").toString()) ? "enabled" : "disabled"));
		}
	}
	**/
}
