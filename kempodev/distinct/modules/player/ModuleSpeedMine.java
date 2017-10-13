package kempodev.distinct.modules.player;

import kempodev.distinct.base.BaseModule;
import kempodev.distinct.main.Distinct;

public class ModuleSpeedMine extends BaseModule{
	public ModuleSpeedMine() {
		super("SpeedMine", -1, 0xA2F2A8, "!ms (float)");
		addHookType("ONUPDATE");
		addField("breakSpeed",1.35);
		// TODO Auto-generated constructor stub
	}
	@Override
	public void onUpdate() {
		Distinct.getInstance().getMinecraft().playerController.blockHitDelay = 0;
	}
	@Override
	public float onStrVsCurrentBlockMultiplier() {
		return Float.parseFloat(getValueByField("breakSpeed").toString());
	}
	@Override
	public void onCommand(String i) {
		String args[] = i.split(" ");
		if(args[0].equalsIgnoreCase(this.setCommand("ms"))) {
			setField("breakSpeed",Float.parseFloat(args[1]));
			Distinct.getInstance().getChat().sendClientMessage("Mining speed multiplier has been set to " + getValueByField("breakSpeed"));
		}
	}
}
