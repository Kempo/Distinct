package kempodev.distinct.modules.misc.radar;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import kempodev.distinct.main.Distinct;
import kempodev.distinct.modules.misc.Location;
import kempodev.distinct.utilities.ColorUtil;
import kempodev.distinct.utilities.RenderUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.block.BlockGrass;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockNewLeaf;
import net.minecraft.block.BlockOldLeaf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.ColorizerFoliage;
import net.minecraft.world.ColorizerGrass;

import org.lwjgl.opengl.GL11;

import bspkrs.client.util.HUDUtils;
public class TerrainHandler 
{
	private ResourceLocation loc = new ResourceLocation("textures/atlas/blocks.png");
	private int lowerBound;
	private int upperBound;
	private Minecraft mc;
	private Gui g;
	private RenderItem itemRenderer = new RenderItem();
	private ColorUtil color = new ColorUtil();
	private ConcurrentHashMap<Double,Double> posManager = new ConcurrentHashMap<Double,Double>();
	private ArrayList<Integer> colorManager = new ArrayList<Integer>();
	private ArrayList<Integer> renderBlocks = new ArrayList<Integer>();
	/**
	 * 
	 * @param mc
	 * @param g
	 * @param lowerBound
	 * @param upperBound
	 */
	public TerrainHandler(Minecraft mc, Gui g, int lowerBound, int upperBound)
	{
		this.lowerBound = lowerBound;
		this.upperBound = upperBound;
		this.mc = mc;
		this.g = g;
		renderBlocks.add(54);
	}

	private void colorBasedOnMultiplier(int color)
	{
		float f = (float) (color >> 24 & 0xff) / 255F;
		float f1 = (float) (color >> 16 & 0xff) / 255F;
		float f2 = (float) (color >> 8 & 0xff) / 255F;
		float f3 = (float) (color & 0xff) / 255F;
		GL11.glColor4f(f1, f2, f3, 1);
	}
	// horribly written lol, was using the base of JMT's old minimap source which was considered 'godly' back in the day
	public void drawSurface(int centerX, int centerY)
	{

		GL11.glPushMatrix();


		for(int as = lowerBound; as<upperBound; as++)
		{
			int pX = as;
			for(int aa = lowerBound; aa<upperBound; aa++)
			{
				int pZ = aa;
				int bX = (int) (mc.thePlayer.posX + pX); //block posX
				int bZ = (int) (mc.thePlayer.posZ + pZ); //block posZ
				int bY = mc.theWorld.getHeightValue(bX, bZ); //formatted block posY (-1 for compensation)
				Block block = mc.theWorld.getBlock(bX,bY,bZ);//block id
				int bI = Block.getIdFromBlock(block);
				int t = bY;
				if(!renderBlocks.contains(bI)) {
					bY -= 1;
				}
				if(bY != t) {
					block = mc.theWorld.getBlock(bX,bY,bZ);//block id
				}
				if(block != null || Block.getIdFromBlock(block) > 0) {
					double disx = (mc.thePlayer.posX-bX);
					double disz = (mc.thePlayer.posZ-bZ);
					if((disx - 0.5)*(disx - 0.5) + (disz - 0.5)*(disz - 0.5) > (upperBound)*(upperBound))
					{
						continue;
					}
					mc.renderEngine.bindTexture(loc); //binding terrain texture

					if(renderBlocks.contains(bI)) {
						//renderItemImage(block,disx,disz);
						itemRenderer.zLevel = 200.0F;
						GL11.glPushMatrix();
						Item item = Item.getItemFromBlock(block);
						ItemStack e = new ItemStack(item);
						
						GL11.glTranslated(disx, disz, 0.0F);
						GL11.glRotatef(getClient().getPlayer().rotationYaw, 0.0F, 0.0F, 1.0F);
						GL11.glTranslated(-disx, -disz, 0.0F);
						
						GL11.glScalef(0.5F, 0.5F, 0.5F);
						GL11.glDisable(GL11.GL_LIGHTING);
						itemRenderer.renderItemIntoGUI(getClient().getMinecraft().fontRenderer, getClient().getMinecraft().getTextureManager(), e,(int)disx * 2, (int)disz * 2);
						GL11.glEnable(GL11.GL_LIGHTING);
						GL11.glScalef(2F, 2F, 2F);
						GL11.glPopMatrix();
					}else{
						if(!(block instanceof BlockGrass || block instanceof BlockOldLeaf || block instanceof BlockNewLeaf)) {
							int colorHex = block.getMaterial().getMaterialMapColor().colorValue;
							colorBasedOnMultiplier(colorHex);
						}
						if(block instanceof BlockGrass)
						{
							colorBasedOnMultiplier(ColorizerGrass.getGrassColor(MathHelper.clamp_float(mc.theWorld.getWorldChunkManager().getBiomeGenAt(bX, bZ).temperature, 0.0F, 1.0F), MathHelper.clamp_float(mc.theWorld.getWorldChunkManager().getBiomeGenAt(bX, bZ).getFloatRainfall(), 0.0F, 1.0F))); 
						}
						if(block instanceof BlockOldLeaf || block instanceof BlockNewLeaf)
						{
							colorBasedOnMultiplier(ColorizerFoliage.getFoliageColor(MathHelper.clamp_float(mc.theWorld.getWorldChunkManager().getBiomeGenAt(bX, bZ).getFloatRainfall(), 0.0F, 1.0F), MathHelper.clamp_float(mc.theWorld.getWorldChunkManager().getBiomeGenAt(bX, bZ).getFloatRainfall(), 0.0F, 1.0F)));
						}
						IIcon k8 = block.getIcon(0, 0);
						if(k8 != null && !renderBlocks.contains(bI)) {
							GL11.glPushMatrix();
							GL11.glDisable(GL11.GL_LIGHTING);
							g.drawTexturedModelRectFromIcon((int)disx - 1, (int)disz - 1, k8, 1, 1);
							GL11.glEnable(GL11.GL_LIGHTING);
							GL11.glPopMatrix();
						}
					}
				}
			}
		}

		GL11.glPopMatrix();
	}
	private Distinct getClient() {
		return Distinct.getInstance();
	}
	private void renderItemImage(Block b, double disx, double disz) {
		itemRenderer.zLevel = 200.0F;
		GL11.glPushMatrix();
		Item item = Item.getItemFromBlock(b);
		ItemStack e = new ItemStack(item);
		int x = (int) (disx);
		int y = (int) (disz);
		GL11.glTranslated(x, y, 0.0F);
		GL11.glRotatef(getClient().getPlayer().rotationYaw, 0.0F, 0.0F, 1.0F);
		GL11.glTranslated(-x, -y, 0.0F);
		GL11.glScalef(0.5F, 0.5F, 0.5F);
		GL11.glDisable(GL11.GL_LIGHTING);
		itemRenderer.renderItemIntoGUI(getClient().getMinecraft().fontRenderer, getClient().getMinecraft().getTextureManager(), e,x, y);
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glScalef(1F, 1F, 1F);
		GL11.glPopMatrix();
	}
	private void renderBlockPixel() {
		
	}
}
