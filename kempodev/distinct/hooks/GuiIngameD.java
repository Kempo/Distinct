package kempodev.distinct.hooks;

import java.util.ArrayList;

import kempodev.distinct.gui.screens.DistinctTTFChat;
import kempodev.distinct.main.Distinct;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.potion.Potion;

public class GuiIngameD extends GuiIngame{
	private final DistinctTTFChat persistantChatGUI;
	public GuiIngameD(Minecraft par1Minecraft) {
		super(par1Minecraft);
		persistantChatGUI = new DistinctTTFChat(mc);
		// TODO Auto-generated constructor stub
	}
	@Override
	public void renderGameOverlay(float par1, boolean par2, int par3, int par4) {
		super.renderGameOverlay(par1, par2, par3, par4);
		
		if(!Distinct.getInstance().getMinecraft().gameSettings.showDebugInfo) {
			Distinct.getInstance().getHooks().onRenderIngameHook();
		}
		
	}
	private Distinct getClient() {
		return Distinct.getInstance();
	}
}
