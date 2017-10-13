package kempodev.distinct.modules.player;

import org.lwjgl.input.Keyboard;

import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import kempodev.distinct.base.BaseModule;
import kempodev.distinct.hooks.HookTypes;

public class ModulePhase extends BaseModule{
	private long last, current = -1;	
	public ModulePhase() {
		super("Phase", Keyboard.KEY_G, -1, "!pdelay (long)");
		addHookType(HookTypes.ONUPDATE);
		addField("threshold",500);
		// TODO Auto-generated constructor stub
	}
	 @Override
	 public void onDisable() {
	  getClient().getPlayer().noClip = false;
	  getClient().getPlayer().capabilities.isFlying = false;
	  removeAllHookTypes();
	 }
	 @Override
	 public void onUpdate() {
		 getClient().getPlayer().noClip = true;
		 getClient().getPlayer().capabilities.isFlying = true;
		 getClient().getPlayer().motionX = 0;
		 getClient().getPlayer().motionY = 0;
		 getClient().getPlayer().motionZ = 0;
	 }
	 @Override
	 public boolean onAllowPacketSend(Packet event) {
		 if (event instanceof C03PacketPlayer) {
			 this.current = System.nanoTime() / 1000000L;
			 if (this.current - this.last >= Long.parseLong(getValueByField("threshold").toString())) {
				 this.last = System.nanoTime() / 1000000L;
				 return false;
			 }
		 }
		return true;
	 }
	 @Override
	 public void onCommand(String s) {
		 String args[] = s.split(" ");
		 if(args[0].equalsIgnoreCase(this.setCommand("pdelay"))) {
			 Integer r = Integer.parseInt(args[1]);
			 setField("threshold",r);
			 getClient().getChat().sendClientMessage("Phase delay has been set to " + getValueByField("threshold") + " milliseconds");
		 }
	 }
}
