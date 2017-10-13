package kempodev.distinct.modules.player;

import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C03PacketPlayer;
import kempodev.distinct.base.BaseModule;

public class ModuleAntiAFK extends BaseModule{
	 private long CUR_MS, LAST_MS;
	public ModuleAntiAFK() {
		super("AntiAFK", 0xFF9A17, -1, "!afkc (command) | !afkd (milliseconds)");
		this.setBindable(false);
		addHookType("ONUPDATE");
		addField("command","mahniggaa");
		addField("threshold",10000L);
		// TODO Auto-generated constructor stub
	}
	@Override
	public void onUpdate() {
		this.CUR_MS = System.nanoTime() / 1000000L;
		Long delay = Long.parseLong(getValueByField("threshold").toString());
	    boolean canSend = this.CUR_MS - this.LAST_MS >= delay || this.LAST_MS == -1L;
	    if(canSend) {
	    	getClient().getPlayer().sendChatMessage(getValueByField("command").toString());
	    	this.LAST_MS = System.nanoTime() / 1000000L;
	    }
	}
	@Override
	public void onCommand(String s) {
		String args[] = s.split(" ");
		if(args[0].equalsIgnoreCase(this.setCommand("afkc"))) {
			setField("command",args[1]);
			getClient().getChat().sendClientMessage("AntiAFK command set to " + getValueByField("command").toString());
		}
		if(args[0].equalsIgnoreCase(this.setCommand("afkd"))) {
			setField("threshold",Long.parseLong(args[1]));
			getClient().getChat().sendClientMessage("AntiAFK command set to " + Long.parseLong(getValueByField("threshold").toString()));
		}
		if (args[0].equalsIgnoreCase("!find")) {
			try {

				double xmax = Double.parseDouble(args[1]);
				double xmin = Double.parseDouble(args[2]);
				double yval = 257.0D;
				double zmax = Double.parseDouble(args[3]);
				double zmin = Double.parseDouble(args[4]);

				int startvalZunchanged = Integer.parseInt(args[3]);

				while (xmax >= xmin) {
					while (zmax >= zmin) {
						Minecraft.getMinecraft().thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(xmax, yval - 1.0D, yval, zmax, Minecraft.getMinecraft().thePlayer.onGround));
						zmax -= 256.0D;
					}
					xmax -= 128.0D;
					zmax = startvalZunchanged;
				}
			}
			catch (Exception e)
			{
				return;
			}
		}
	}
}
