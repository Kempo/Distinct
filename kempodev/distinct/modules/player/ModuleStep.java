package kempodev.distinct.modules.player;

import kempodev.distinct.base.BaseModule;
import kempodev.distinct.main.Distinct;

public class ModuleStep extends BaseModule{
	private float stepHeight = 0.5F;
	public ModuleStep() {
		super("Step", -1, -1, "!sh (float)");
		this.enabled = true;
		this.visible = false;
		addHookType("ONUPDATE");
		addField("stepHeight",0.5F);
		// TODO Auto-generated constructor stub
	}
	@Override
	public void onUpdate() {
		Distinct.getInstance().getPlayer().stepHeight = stepHeight;
	}
	@Override
	public void onDisable() {
		Distinct.getInstance().getPlayer().stepHeight = 0.5F;
		removeAllHookTypes();
	}
	@Override
	public void onCommand(String i) {
		String args[] = i.split(" ");
		if(args[0].equalsIgnoreCase(this.setCommand("sh"))) {
			Float b = Float.parseFloat(args[1]);
			setField("stepHeight",b);
			Distinct.getInstance().getChat().sendClientMessage("Step height has been changed to " + getValueByField("stepHeight") + " blocks");
		}
	}

}
