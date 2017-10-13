package kempodev.distinct.modules;

import net.minecraft.network.play.client.C03PacketPlayer;
import kempodev.distinct.base.BaseModule;
import kempodev.distinct.main.Distinct;

public class ModuleSpawn extends BaseModule{
	private double hearts = 3;
	private boolean shouldLog = false;
	public ModuleSpawn() {
		super("Spawn", -1, -1, "!stp");
		this.visible = false;
		this.enabled = true;
		addHookType("ONUPDATE");
		// TODO Auto-generated constructor stub
	}
	@Override
	public void onUpdate() {
		double realHearts = hearts / 2;
		if(shouldLog && Distinct.getInstance().getPlayer().getHealth() <= realHearts) {
			sendDisconnect();
			shouldLog = false;
		}
	}
	@Override
	public void onCommand(String i) {
		String args[] = i.split(" ");
		if(args[0].equalsIgnoreCase(this.setCommand("stp"))) {
			sendDisconnect();
		}
		if(args[0].equalsIgnoreCase(this.setCommand("autolog"))) {
			shouldLog = !shouldLog;
			Distinct.getInstance().getChat().sendClientMessage("Auto logging has been " + (shouldLog ? "enabled" : "disabled"));
		}
		if(args[0].equalsIgnoreCase(this.setCommand("health"))) {
			Double e = Double.parseDouble(args[1]);
			hearts = e;
			Distinct.getInstance().getChat().sendClientMessage("Disconnecting at " + (hearts / 2) + " half hearts!");
		}
	}
	private void sendDisconnect() {
		
		C03PacketPlayer.C04PacketPlayerPosition payload = new C03PacketPlayer.C04PacketPlayerPosition();
		payload.field_149479_a = Double.NaN;
		payload.field_149477_b = Double.NaN;
		payload.field_149478_c = Double.NaN;
		payload.field_149475_d = Double.NaN;
		Distinct.getInstance().getPlayer().sendQueue.addToSendQueue(payload);
		
	}
}
