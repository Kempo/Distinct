package kempodev.distinct.gui.screens;

import java.awt.Font;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import kempodev.distinct.main.Distinct;
import kempodev.distinct.utilities.CFontRenderer;
import kempodev.distinct.utilities.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ChatLine;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.MathHelper;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.GL11;

import com.google.common.collect.Lists;

public class DistinctTTFChat extends Gui
{
	private static final Logger logger = LogManager.getLogger();
	private final Minecraft mc = Distinct.getInstance().getMinecraft();
	private final List field_146248_g = new ArrayList();
	private final List field_146252_h = new ArrayList();
	private final List field_146253_i = new ArrayList();
	private int field_146250_j;
	private boolean field_146251_k;
	private double xOffset, yOffset;
	public CFontRenderer cFont = new CFontRenderer(new Font("Verdana", Font.PLAIN, 17),true);
	public RenderUtil e = new RenderUtil();
	public DistinctTTFChat(Minecraft par1Minecraft)
	{
	}

	public void func_146230_a(int p_146230_1_)
	{
		try{
		if (this.mc.gameSettings.chatVisibility != EntityPlayer.EnumChatVisibility.HIDDEN)
		{
			int var2 = this.func_146232_i();
			boolean var3 = false;
			int var4 = 0;
			int var5 = this.field_146253_i.size();
			float var6 = this.mc.gameSettings.chatOpacity * 0.9F + 0.1F;

			if (var5 > 0)
			{
				if (this.func_146241_e())
				{
					var3 = true;
				}

				float var7 = this.func_146244_h();
				int var8 = MathHelper.ceiling_float_int((float)this.func_146228_f() / var7);
				GL11.glPushMatrix();
				GL11.glTranslatef(2.0F, 20.0F, 0.0F);
				GL11.glScalef(var7, var7, 1.0F);
				int var9;
				int var11;
				int var14 = 0;

				if (Distinct.getInstance().getModuleByName("Chat").enabled) {
					reverseChat();
				}
				
				for (var9 = 0; var9 + this.field_146250_j < this.field_146253_i.size() && var9 < var2; ++var9)
				{
					ChatLine var10 = (ChatLine)this.field_146253_i.get(var9 + this.field_146250_j);

					if (var10 != null)
					{
						var11 = p_146230_1_ - var10.getUpdatedCounter();

						if (var11 < 200 || var3)
						{
							double var12 = (double)var11 / 200.0D;
							var12 = 1.0D - var12;
							var12 *= 10.0D;

							if (var12 < 0.0D)
							{
								var12 = 0.0D;
							}

							if (var12 > 1.0D)
							{
								var12 = 1.0D;
							}

							var12 *= var12;
							var14 = (int)(255.0D * (Distinct.getInstance().getModuleByName("Chat").enabled? 1 : var12));

							if (var3)
							{
								var14 = 255;
							}

							var14 = (int)((float)var14 * var6);
							++var4;

							if (var14 > 3)
							{
								byte var15 = 0;
								int var16 = -var9 * 9;
								//drawRect(var15, var16 - 9, var15 + var8 + 4, var16, var14 / 2 << 24);
								//drawRect(2, (int)this.yOffset - 14, var15 + var8 + 4, 1, var14 / 2 << 24);
								String var17 = var10.func_151461_a().getFormattedText();
								this.xOffset = var15 + var8 + 12;
								this.yOffset = var16;
								GL11.glDisable(GL11.GL_ALPHA_TEST);
							}
						}
					}
				}
				
				if (var14 > 0 && Distinct.getInstance().getModuleByName("Chat").enabled) {
					drawRect(2, (int)this.yOffset - 14, (int)this.xOffset, 1, var14 / 2 << 24);
					//e.drawBorderedRect(2, this.yOffset - 14, this.xOffset, 1, 1.6f, 0x60000000, -2146365167);
					if (this.func_146241_e()) {
						//e.drawBorderedRect(2, this.yOffset - 30, this.xOffset, this.yOffset - 16, 1.6f, 0x60000000, -2146365167);
						//cFont.drawString("\247nChat", 4, this.yOffset - 26.5, 0xFFFFFFFF);
					}
					//e.drawGradientRect(-1, this.yOffset - 10, this.xOffset, this.yOffset + 20, 0x8f000000, var14 / 2 << 24);
				}
	
				for (var9 = 0; var9 + this.field_146250_j < this.field_146253_i.size() && var9 < var2; ++var9)
				{
					ChatLine var10 = (ChatLine)this.field_146253_i.get(var9 + this.field_146250_j);

					if (var10 != null)
					{
						var11 = p_146230_1_ - var10.getUpdatedCounter();

						if (var11 < 200 || var3)
						{
							double var12 = (double)var11 / 200.0D;
							var12 = 1.0D - var12;
							var12 *= 10.0D;

							if (var12 < 0.0D)
							{
								var12 = 0.0D;
							}

							if (var12 > 1.0D)
							{
								var12 = 1.0D;
							}

							var12 *= var12;
							var14 = (int)(255.0D * var12);

							if (var3)
							{
								var14 = 255;
							}

							var14 = (int)((float)var14 * var6);
							++var4;

							if (var14 > 3)
							{
								byte var15 = 0;
								int var16 = -var9 * 9;
								if (!Distinct.getInstance().getModuleByName("Chat").enabled)
									drawRect(var15, var16 - 9, var15 + var8 + 4, var16, var14 / 2 << 24);
								String var17 = var10.func_151461_a().getFormattedText();
								var17 = Distinct.getInstance().getHooks().onChatHook(var17);
								if (Distinct.getInstance().getModuleByName("Chat").enabled) {
									cFont.drawStringWithShadow(var17, var15 + 4, var16 - 10, 16777215 + (var14 << 24));
								} else {
									this.mc.fontRenderer.drawStringWithShadow(var17, var15, var16 - 8, 16777215 + (var14 << 24));
								}
								GL11.glDisable(GL11.GL_ALPHA_TEST);
							}
						}
					}
				}

				if (var3)
				{
					var9 = this.mc.fontRenderer.FONT_HEIGHT;
					GL11.glTranslatef(-3.0F, 0.0F, 0.0F);
					int var18 = var5 * var9 + var5;
					var11 = var4 * var9 + var4;
					int var20 = this.field_146250_j * var11 / var5;
					int var13 = var11 * var11 / var18;

					if (var18 != var11)
					{
						var14 = var20 > 0 ? 170 : 96;
						int var19 = this.field_146251_k ? 13382451 : 3355562;
						drawRect(0, -var20, 2, -var20 - var13, var19 + (var14 << 24));
						drawRect(2, -var20, 1, -var20 - var13, 13421772 + (var14 << 24));
					}
				}

				GL11.glPopMatrix();
			}
		}
		}catch(Exception e) {
			
		}
	}

	public void func_146231_a()
	{
		this.field_146253_i.clear();
		this.field_146252_h.clear();
		this.field_146248_g.clear();
	}

	public void func_146227_a(IChatComponent p_146227_1_)
	{
		this.func_146234_a(p_146227_1_, 0);
	}

	public void func_146234_a(IChatComponent p_146234_1_, int p_146234_2_)
	{
		this.func_146237_a(p_146234_1_, p_146234_2_, this.mc.ingameGUI.getUpdateCounter(), false);
		logger.info("[CHAT] " + p_146234_1_.getUnformattedText());
	}

	private String func_146235_b(String p_146235_1_)
	{
		return Minecraft.getMinecraft().gameSettings.chatColours ? p_146235_1_ : EnumChatFormatting.getTextWithoutFormattingCodes(p_146235_1_);
	}

	private void func_146237_a(IChatComponent p_146237_1_, int p_146237_2_, int p_146237_3_, boolean p_146237_4_)
	{
		if (p_146237_2_ != 0)
		{
			this.func_146242_c(p_146237_2_);
		}

		int var5 = MathHelper.floor_float((float)this.func_146228_f() / this.func_146244_h());
		int var6 = 0;
		ChatComponentText var7 = new ChatComponentText("");
		ArrayList var8 = Lists.newArrayList();
		ArrayList var9 = Lists.newArrayList(p_146237_1_);

		for (int var10 = 0; var10 < var9.size(); ++var10)
		{
			IChatComponent var11 = (IChatComponent)var9.get(var10);
			String var12 = this.func_146235_b(var11.getChatStyle().getFormattingCode() + var11.getUnformattedTextForChat());
			int var13 = Distinct.getInstance().getModuleByName("Chat").enabled? cFont.getStringWidth(var12) + 4 : this.mc.fontRenderer.getStringWidth(var12);
			ChatComponentText var14 = new ChatComponentText(var12);
			var14.setChatStyle(var11.getChatStyle().createShallowCopy());
			boolean var15 = false;

			if (var6 + var13 > var5)
			{
				String var16 = this.mc.fontRenderer.trimStringToWidth(var12, var5 - var6, false);
				String var17 = var16.length() < var12.length() ? var12.substring(var16.length()) : null;

				if (var17 != null && var17.length() > 0)
				{
					int var18 = var16.lastIndexOf(" ");

					if (var18 >= 0 && this.mc.fontRenderer.getStringWidth(var12.substring(0, var18)) > 0)
					{
						var16 = var12.substring(0, var18);
						var17 = var12.substring(var18);
					}

					ChatComponentText var19 = new ChatComponentText(var17);
					var19.setChatStyle(var11.getChatStyle().createShallowCopy());
					var9.add(var10 + 1, var19);
					
				}

				var13 = this.mc.fontRenderer.getStringWidth(var16);
				var14 = new ChatComponentText(var16);
				var14.setChatStyle(var11.getChatStyle().createShallowCopy());
				var15 = true;
			}

			if (var6 + var13 <= var5)
			{
				var6 += var13;
				var7.appendSibling(var14);
			}
			else
			{
				var15 = true;
			}

			if (var15)
			{
				var8.add(var7);
				var6 = 0;
				var7 = new ChatComponentText("");
			}
		}

		var8.add(var7);
		boolean var21 = this.func_146241_e();
		IChatComponent var22;

		for (Iterator var20 = var8.iterator(); var20.hasNext(); this.field_146253_i.add(0, new ChatLine(p_146237_3_, var22, p_146237_2_)))
		{
			var22 = (IChatComponent)var20.next();

			if (var21 && this.field_146250_j > 0)
			{
				this.field_146251_k = true;
				this.func_146229_b(1);
			}
		}

		while (this.field_146253_i.size() > 100)
		{
			this.field_146253_i.remove(this.field_146253_i.size() - 1);
		}

		if (!p_146237_4_)
		{
			this.field_146252_h.add(0, new ChatLine(p_146237_3_, p_146237_1_, p_146237_2_));

			while (this.field_146252_h.size() > 100)
			{
				this.field_146252_h.remove(this.field_146252_h.size() - 1);
			}
		}
	}

	public void func_146245_b()
	{
		this.field_146253_i.clear();
		this.resetScroll();

		for (int var1 = this.field_146252_h.size() - 1; var1 >= 0; --var1)
		{
			ChatLine var2 = (ChatLine)this.field_146252_h.get(var1);
			this.func_146237_a(var2.func_151461_a(), var2.getChatLineID(), var2.getUpdatedCounter(), true);
		}
	}

	public List func_146238_c()
	{
		return this.field_146248_g;
	}

	public void func_146239_a(String p_146239_1_)
	{
		if (this.field_146248_g.isEmpty() || !((String)this.field_146248_g.get(this.field_146248_g.size() - 1)).equals(p_146239_1_))
		{
			this.field_146248_g.add(p_146239_1_);
		}
	}

	public void resetScroll()
	{
		this.field_146250_j = 0;
		this.field_146251_k = false;
	}

	public void func_146229_b(int p_146229_1_)
	{
		this.field_146250_j += p_146229_1_;
		int var2 = this.field_146253_i.size();

		if (this.field_146250_j > var2 - this.func_146232_i())
		{
			this.field_146250_j = var2 - this.func_146232_i();
		}

		if (this.field_146250_j <= 0)
		{
			this.field_146250_j = 0;
			this.field_146251_k = false;
		}
	}

	public IChatComponent func_146236_a(int p_146236_1_, int p_146236_2_)
	{
		if (!this.func_146241_e())
		{
			return null;
		}
		else
		{
			ScaledResolution var3 = new ScaledResolution(this.mc.gameSettings, this.mc.displayWidth, this.mc.displayHeight);
			int var4 = var3.getScaleFactor();
			float var5 = this.func_146244_h();
			int var6 = p_146236_1_ / var4 - 3;
			int var7 = p_146236_2_ / var4 - 27;
			var6 = MathHelper.floor_float((float)var6 / var5);
			var7 = MathHelper.floor_float((float)var7 / var5);

			if (var6 >= 0 && var7 >= 0)
			{
				int var8 = Math.min(this.func_146232_i(), this.field_146253_i.size());

				if (var6 <= MathHelper.floor_float((float)this.func_146228_f() / this.func_146244_h()) && var7 < this.mc.fontRenderer.FONT_HEIGHT * var8 + var8)
				{
					int var9 = var7 / this.mc.fontRenderer.FONT_HEIGHT + this.field_146250_j;

					if (var9 >= 0 && var9 < this.field_146253_i.size())
					{
						ChatLine var10 = (ChatLine)this.field_146253_i.get(var9);
						int var11 = 0;
						Iterator var12 = var10.func_151461_a().iterator();

						while (var12.hasNext())
						{
							IChatComponent var13 = (IChatComponent)var12.next();

							if (var13 instanceof ChatComponentText)
							{
								var11 += this.mc.fontRenderer.getStringWidth(this.func_146235_b(((ChatComponentText)var13).getChatComponentText_TextValue()));

								if (var11 > var6)
								{
									return var13;
								}
							}
						}
					}

					return null;
				}
				else
				{
					return null;
				}
			}
			else
			{
				return null;
			}
		}
	}

	public boolean func_146241_e()
	{
		return this.mc.currentScreen instanceof GuiChat;
	}

	public void func_146242_c(int p_146242_1_)
	{
		Iterator var2 = this.field_146253_i.iterator();
		ChatLine var3;

		do
		{
			if (!var2.hasNext())
			{
				var2 = this.field_146252_h.iterator();

				do
				{
					if (!var2.hasNext())
					{
						return;
					}

					var3 = (ChatLine)var2.next();
				}
				while (var3.getChatLineID() != p_146242_1_);

				var2.remove();
				return;
			}

			var3 = (ChatLine)var2.next();
		}
		while (var3.getChatLineID() != p_146242_1_);

		var2.remove();
	}

	public int func_146228_f()
	{
		return func_146233_a(this.mc.gameSettings.chatWidth + (Distinct.getInstance().getModuleByName("Chat").enabled ? 0.2F : 0.0F));
	}

	public int func_146246_g()
	{
		return func_146243_b(this.func_146241_e() ? this.mc.gameSettings.chatHeightFocused : this.mc.gameSettings.chatHeightUnfocused);
	}

	public float func_146244_h()
	{
		return this.mc.gameSettings.chatScale;
	}

	public static int func_146233_a(float p_146233_0_)
	{
		short var1 = 320;
		byte var2 = 40;
		return MathHelper.floor_float(p_146233_0_ * (float)(var1 - var2) + (float)var2);
	}

	public static int func_146243_b(float p_146243_0_)
	{
		short var1 = 180;
		byte var2 = 20;
		return MathHelper.floor_float(p_146243_0_ * (float)(var1 - var2) + (float)var2);
	}

	public int func_146232_i()
	{
		return this.func_146246_g() / 9;
	}
	
	private void reverseChat() {
	        double scaleFactor = 1;
	        int var4 = mc.gameSettings.guiScale;

	        if (var4 == 0)
	        {
	            var4 = 1000;
	        }

	        while (scaleFactor < var4 && mc.displayWidth / (scaleFactor + 1) >= 320 && mc.displayHeight / (scaleFactor + 1) >= 240)
	        {
	            ++scaleFactor;
	        }

	        double sf = 2;
	        
	        GL11.glScaled(sf / scaleFactor, sf / scaleFactor, sf / scaleFactor);
	}
}
