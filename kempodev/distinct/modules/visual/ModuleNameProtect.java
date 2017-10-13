package kempodev.distinct.modules.visual;

import kempodev.distinct.base.BaseModule;
import kempodev.distinct.main.Distinct;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.util.StringUtils;

public class ModuleNameProtect extends BaseModule{

	public ModuleNameProtect() {
		super("Names", -1, -1, null);
		this.visible = false;
		this.enabled = true;
		// TODO Auto-generated constructor stub
	}
	@Override
	public float onSetNametagSize(Entity var1)
	{
		float var2;
		try{
			if(getClient().getModuleByName("Freecam").enabled && getClient().getWorld().getEntityByID(-1) != null && getClient().getWorld().getEntityByID(-1).getCommandSenderName().equalsIgnoreCase(StringUtils.stripControlCodes(getClient().getPlayer().getCommandSenderName()))) {
				EntityLivingBase e = (EntityLivingBase) getClient().getWorld().getEntityByID(-1);
				var2 = e.getDistanceToEntity(var1);
			}else{
				var2 = Distinct.getInstance().getPlayer().getDistanceToEntity(var1);
			}
			float var3 = var2 / 3.0F;

			if (var3 < 3.0F)
			{
				var3 = 3.0F;
			}

			if (var3 > 15.0F)
			{
				var3 = 15.0F;
			}
			return var3;
		}catch(Exception e) {
			return 1.6F;
		}
	}
	@Override
	public String onSetPlacement(String var1)
	{
		return Distinct.getInstance().getFriendManager().getFriends().containsKey(StringUtils.stripControlCodes(var1)) ? "\247b" + (String)Distinct.getInstance().getFriendManager().getFriends().get(StringUtils.stripControlCodes(var1)) : var1;
	}
	@Override
	public String onChatRender(String s) {
		for(Object o : Distinct.getInstance().getFriendManager().getFriends().keySet()) {
			String name = o.toString();
			String alias = Distinct.getInstance().getFriendManager().getFriends().get(name).toString();
			if(s.contains(name)) {
				s = s.replaceAll(name, "\2476" + alias + "\247f");
				//System.out.println(s);
			}
		}

		return s;
	}
	
	/**
	@Override
	public boolean onPacketRecieve(Packet packet) {
		
		if(packet instanceof S02PacketChat && getClient().getMinecraft().ingameGUI != null) {
			S02PacketChat p = (S02PacketChat)packet;
			if(p.field_148919_a.getUnformattedText().contains("has requested to teleport")) {
				for(Object o : getClient().getFriendManager().getFriends().keySet()) {
					String name = o.toString();
					//System.out.println(p.field_148919_a.getUnformattedText() + "        NAME:   " + name);
					if(p.field_148919_a.getUnformattedText().contains(name)) {
						
						getClient().getPlayer().sendChatMessage("/tpaccept");
					}
				}
			}
		}
		
		// TODO Auto-generated method stub
		return false;
	}
	**/
}
